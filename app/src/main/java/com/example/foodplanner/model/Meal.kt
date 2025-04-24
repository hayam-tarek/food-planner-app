package com.example.foodplanner.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

fun Meal.getIngredientsList(): List<String?> {
    return listOf(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
        strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
    ).filter { !it.isNullOrEmpty() }
}

fun Meal.getMeasuresList(): List<String?> {
    return listOf(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
        strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
    ).filter { !it.isNullOrEmpty() }
}

@Entity(tableName = "meals")
data class Meal(
    val uid: String,
    val dateModified: String?,
    @PrimaryKey
    val idMeal: String,
    val strArea: String?,
    val strCategory: String?,
    val strCreativeCommonsConfirmed: String?,
    val strDrinkAlternate: String?,
    val strImageSource: String?,
    val strIngredient1: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strIngredient16: String?,
    val strIngredient17: String?,
    val strIngredient18: String?,
    val strIngredient19: String?,
    val strIngredient2: String?,
    val strIngredient20: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strInstructions: String?,
    val strMeal: String?,
    val strMealThumb: String?,
    val strMeasure1: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strMeasure16: String?,
    val strMeasure17: String?,
    val strMeasure18: String?,
    val strMeasure19: String?,
    val strMeasure2: String?,
    val strMeasure20: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strSource: String?,
    val strTags: String?,
    val strYoutube: String?,
) {
    @Ignore
    var isFavorite: Boolean = false

    companion object {
        fun fromMap(map: Map<String, Any?>): Meal {
            return Meal(
                uid = map["uid"] as String,
                dateModified = map["dateModified"] as String?,
                idMeal = map["idMeal"] as String,
                strArea = map["strArea"] as String?,
                strCategory = map["strCategory"] as String?,
                strCreativeCommonsConfirmed = map["strCreativeCommonsConfirmed"] as String?,
                strDrinkAlternate = map["strDrinkAlternate"] as String?,
                strImageSource = map["strImageSource"] as String?,
                strIngredient1 = map["strIngredient1"] as String?,
                strIngredient2 = map["strIngredient2"] as String?,
                strIngredient3 = map["strIngredient3"] as String?,
                strIngredient4 = map["strIngredient4"] as String?,
                strIngredient5 = map["strIngredient5"] as String?,
                strIngredient6 = map["strIngredient6"] as String?,
                strIngredient7 = map["strIngredient7"] as String?,
                strIngredient8 = map["strIngredient8"] as String?,
                strIngredient9 = map["strIngredient9"] as String?,
                strIngredient10 = map["strIngredient10"] as String?,
                strIngredient11 = map["strIngredient11"] as String?,
                strIngredient12 = map["strIngredient12"] as String?,
                strIngredient13 = map["strIngredient13"] as String?,
                strIngredient14 = map["strIngredient14"] as String?,
                strIngredient15 = map["strIngredient15"] as String?,
                strIngredient16 = map["strIngredient16"] as String?,
                strIngredient17 = map["strIngredient17"] as String?,
                strIngredient18 = map["strIngredient18"] as String?,
                strIngredient19 = map["strIngredient19"] as String?,
                strIngredient20 = map["strIngredient20"] as String?,
                strInstructions = map["strInstructions"] as String?,
                strMeal = map["strMeal"] as String?,
                strMealThumb = map["strMealThumb"] as String?,
                strMeasure1 = map["strMeasure1"] as String?,
                strMeasure2 = map["strMeasure2"] as String?,
                strMeasure3 = map["strMeasure3"] as String?,
                strMeasure4 = map["strMeasure4"] as String?,
                strMeasure5 = map["strMeasure5"] as String?,
                strMeasure6 = map["strMeasure6"] as String?,
                strMeasure7 = map["strMeasure7"] as String?,
                strMeasure8 = map["strMeasure8"] as String?,
                strMeasure9 = map["strMeasure9"] as String?,
                strMeasure10 = map["strMeasure10"] as String?,
                strMeasure11 = map["strMeasure11"] as String?,
                strMeasure12 = map["strMeasure12"] as String?,
                strMeasure13 = map["strMeasure13"] as String?,
                strMeasure14 = map["strMeasure14"] as String?,
                strMeasure15 = map["strMeasure15"] as String?,
                strMeasure16 = map["strMeasure16"] as String?,
                strMeasure17 = map["strMeasure17"] as String?,
                strMeasure18 = map["strMeasure18"] as String?,
                strMeasure19 = map["strMeasure19"] as String?,
                strMeasure20 = map["strMeasure20"] as String?,
                strSource = map["strSource"] as String?,
                strTags = map["strTags"] as String?,
                strYoutube = map["strYoutube"] as String?
            ).apply {
                isFavorite = map["isFavorite"] as Boolean? ?: false
            }
        }
    }
}

fun Meal.toMap(): Map<String, Any?> {
    return mapOf(
        "uid" to uid,
        "dateModified" to dateModified,
        "idMeal" to idMeal,
        "strArea" to strArea,
        "strCategory" to strCategory,
        "strCreativeCommonsConfirmed" to strCreativeCommonsConfirmed,
        "strDrinkAlternate" to strDrinkAlternate,
        "strImageSource" to strImageSource,
        "strIngredient1" to strIngredient1,
        "strIngredient2" to strIngredient2,
        "strIngredient3" to strIngredient3,
        "strIngredient4" to strIngredient4,
        "strIngredient5" to strIngredient5,
        "strIngredient6" to strIngredient6,
        "strIngredient7" to strIngredient7,
        "strIngredient8" to strIngredient8,
        "strIngredient9" to strIngredient9,
        "strIngredient10" to strIngredient10,
        "strIngredient11" to strIngredient11,
        "strIngredient12" to strIngredient12,
        "strIngredient13" to strIngredient13,
        "strIngredient14" to strIngredient14,
        "strIngredient15" to strIngredient15,
        "strIngredient16" to strIngredient16,
        "strIngredient17" to strIngredient17,
        "strIngredient18" to strIngredient18,
        "strIngredient19" to strIngredient19,
        "strIngredient20" to strIngredient20,
        "strInstructions" to strInstructions,
        "strMeal" to strMeal,
        "strMealThumb" to strMealThumb,
        "strMeasure1" to strMeasure1,
        "strMeasure2" to strMeasure2,
        "strMeasure3" to strMeasure3,
        "strMeasure4" to strMeasure4,
        "strMeasure5" to strMeasure5,
        "strMeasure6" to strMeasure6,
        "strMeasure7" to strMeasure7,
        "strMeasure8" to strMeasure8,
        "strMeasure9" to strMeasure9,
        "strMeasure10" to strMeasure10,
        "strMeasure11" to strMeasure11,
        "strMeasure12" to strMeasure12,
        "strMeasure13" to strMeasure13,
        "strMeasure14" to strMeasure14,
        "strMeasure15" to strMeasure15,
        "strMeasure16" to strMeasure16,
        "strMeasure17" to strMeasure17,
        "strMeasure18" to strMeasure18,
        "strMeasure19" to strMeasure19,
        "strMeasure20" to strMeasure20,
        "strSource" to strSource,
        "strTags" to strTags,
        "strYoutube" to strYoutube,
        "isFavorite" to isFavorite
    )
}


