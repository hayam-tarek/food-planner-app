package com.example.foodplanner.viewModel

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.db.MealDao
import com.example.foodplanner.model.Meal
import com.example.foodplanner.model.toMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale


class CloudViewModelFactory(private val dao: MealDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CloudViewModel::class.java)) {
            return CloudViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CloudViewModel(private val dao: MealDao) : ViewModel() {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _warningMessage = MutableLiveData<String>()
    val warningMessage: LiveData<String> get() = _warningMessage

    private val _infoMessage = MutableLiveData<String>()
    val infoMessage: LiveData<String> get() = _infoMessage

    private val _lastBackupTimestamp = MutableLiveData<String?>()
    val lastBackupTimestamp: LiveData<String?> get() = _lastBackupTimestamp

    private var _uid: String = ""

    init {
        _uid = auth.currentUser?.uid ?: ""
        fetchLastBackupTimestamp()
    }

    private fun fetchLastBackupTimestamp() {
        if (_uid.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection("users")
                    .document(_uid)
                    .collection("favorites")
                    .document("current")
                    .get()
                    .await()

                if (snapshot.exists()) {
                    val timestamp = snapshot.getString("lastBackupTimestamp")
                    withContext(Dispatchers.Main) {
                        _lastBackupTimestamp.postValue(timestamp)
                    }
                }
            } catch (e: Exception) {
                Log.e("CloudViewModel", "fetchLastBackupTimestamp: ${e.message}")
            }
        }
    }

    fun backupFavoritesToFirestore() {
        val currentUser = auth.currentUser
        if (currentUser == null || _uid.isEmpty()) {
            _errorMessage.postValue("Please login to backup favorites")
            return
        }

        if (currentUser.uid != _uid) {
            _errorMessage.postValue("User authentication mismatch")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _infoMessage.postValue("Loading backup...")
                }
                val favoritesList = dao.getAll(_uid)
                if (favoritesList.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        _warningMessage.postValue("No favorites to backup")
                    }
                    return@launch
                }

                val favoritesMap = favoritesList.map { meal -> meal.toMap() }

                val timestamp =
                    SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault()).format(Date())

                firestore.collection("users")
                    .document(_uid)
                    .collection("favorites")
                    .document("current")
                    .set(
                        mapOf(
                            "lastBackupTime" to timestamp,
                            "meals" to favoritesMap
                        )
                    )
                    .await()

                withContext(Dispatchers.Main) {
                    _successMessage.postValue("Favorites backed up successfully")
                    _lastBackupTimestamp.postValue(timestamp)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.postValue("Error backing up favorites: ${e.message}")
                }
                Log.e("CloudViewModel", "backupFavoritesToFirestore: ${e.message}")
            }
        }
    }

    fun restoreFavoritesFromFirestore() {
        val currentUser = auth.currentUser
        if (currentUser == null || _uid.isEmpty()) {
            _errorMessage.postValue("Please login to restore favorites")
            return
        }

        if (currentUser.uid != _uid) {
            _errorMessage.postValue("User authentication mismatch")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    _infoMessage.postValue("Loading restore...")
                }

                val snapshot = firestore.collection("users")
                    .document(_uid)
                    .collection("favorites")
                    .document("current")
                    .get()
                    .await()

                if (!snapshot.exists()) {
                    withContext(Dispatchers.Main) {
                        _warningMessage.postValue("No backup found")
                    }
                    return@launch
                }

                val favoritesData = snapshot.data?.get("meals") as? List<Map<String, Any?>>
                if (favoritesData.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        _warningMessage.postValue("No favorites found in this backup")
                    }
                    return@launch
                }

                val restoredFavorites = favoritesData.map { mealMap ->
                    Meal.fromMap(mealMap).copy(uid = _uid)
                }

                dao.deleteAll(_uid)
                dao.insertAll(restoredFavorites)

                withContext(Dispatchers.Main) {
                    _successMessage.postValue("Favorites restored successfully")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.postValue("Error restoring favorites: ${e.message}")
                }
                Log.e("CloudViewModel", "restoreFavoritesFromFirestore: ${e.message}")
            }
        }
    }
}