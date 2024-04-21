package com.example.admindiyabatti.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.denzcoskun.imageslider.models.SlideModel
import com.example.admindiyabatti.utils.FilteringProducts
import com.example.admindiyabatti.databinding.ItemViewProductsBinding
import com.example.admindiyabatti.models.Product

class AdapterProduct(val onEditBtnClicked: (Product, String) -> Unit) :
    RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(), Filterable {
    class ProductViewHolder(val binding: ItemViewProductsBinding) : ViewHolder(binding.root) {

    }


    val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemViewProductsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {

            val imageList = ArrayList<SlideModel>()
            val productImages = product.productImagesUris
//            for(i in 0 until productImages?.size!!){
//                imageList.add(SlideModel(product.productImagesUris!![i].toString()))
//            }
//
//            ivImageSlider.setImageList(imageList)
            if (!productImages.isNullOrEmpty()) {
                for (uri in productImages) {
                    imageList.add(SlideModel(uri.toString()))
                }
                ivImageSlider.setImageList(imageList)
            }
            tvProductTitle.text = product.productTitle
            val quantity = product.productQuantity.toString() + " " + product.productUnit
            productQuantity.text = quantity
            tvProductPrice.text = "₹" + product.productPrice

        }

        holder.itemView.setOnClickListener {
            onEditBtnClicked(product,product.adminUid.toString())
        }

    }

    val filter: FilteringProducts? = null
    var originalList = ArrayList<Product>()
    override fun getFilter(): Filter {

        if (filter == null) return FilteringProducts(this, originalList)
        return filter

    }
}