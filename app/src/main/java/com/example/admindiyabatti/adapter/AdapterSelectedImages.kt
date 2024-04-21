package com.example.admindiyabatti.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.admindiyabatti.databinding.ItemViewImageSelectionBinding

class AdapterSelectedImages(val imageUris:ArrayList<Uri>):RecyclerView.Adapter<AdapterSelectedImages.SelectedImagesViewHolder>() {
    class SelectedImagesViewHolder(val binding: ItemViewImageSelectionBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSelectedImages.SelectedImagesViewHolder {
       return SelectedImagesViewHolder(ItemViewImageSelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(
        holder: AdapterSelectedImages.SelectedImagesViewHolder,
        position: Int
    ) {
       val images=imageUris[position]
        holder.binding.apply {
            ivImage.setImageURI(images)
        }

        holder.binding.closeButton.setOnClickListener {
            if(position<imageUris.size){
                 imageUris.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
       return imageUris.size
    }
}