package com.example.foodplanner.features.home.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodplanner.R
import com.example.foodplanner.model.Category
import com.example.foodplanner.utils.CategoryListener

class CategoriesAdapter(
    val context: Context,
    var data: List<Category>,
    val listener: CategoryListener
) : RecyclerView.
Adapter<CategoriesAdapter.CategoriesHolder>() {
    class CategoriesHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val categoryName: TextView = row.findViewById(R.id.categoryName)
        val categoryImage: ImageView = row.findViewById(R.id.categoryImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        val layout =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_category_item, parent, false)
        val categoriesHolder = CategoriesHolder(layout)
        return categoriesHolder
    }


    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        holder.categoryName.text = data[position].strCategory
        Glide.with(context).load(data[position].strCategoryThumb).transform(RoundedCorners(25))
            .into(holder.categoryImage)
        holder.row.setOnClickListener {
            listener.onCategoryClicked(data[position].strCategory)
        }
    }

}