package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.savedstate.R
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.ImageFilter
import com.example.myapplication.databinding.ItemContainerFilterBinding
import com.example.myapplication.listeners.ImageFilterListeners


class ImageFiltersAdapters (
    private val imageFilters:List<ImageFilter>,
    private val imageFilterListeners: ImageFilterListeners
):
RecyclerView.Adapter<ImageFiltersAdapters.ImageFilterViewHolder>(){
    private var selectedFilterPosition=0
    private var previousSelectedPosition=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding= ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(FilterPreview)
                binding.imageFilterName.text=name
                binding.root.setOnClickListener {
                    if(position!=selectedFilterPosition)
                    {
                        imageFilterListeners.onFilterSelected(this)
                        previousSelectedPosition=selectedFilterPosition
                        selectedFilterPosition=position
                        with(this@ImageFiltersAdapters)
                        {
                            notifyItemChanged(previousSelectedPosition,Unit)
                            notifyItemChanged(selectedFilterPosition,Unit)
                        }
                    }
                    imageFilterListeners.onFilterSelected(this)
                }
            }
            binding.imageFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.imageFilterName.context,
                    if (selectedFilterPosition==position)
                        com.example.myapplication.R.color.primaryDark
                    else
                        com.example.myapplication.R.color.primaryText




                )
            )
        }
    }

    override fun getItemCount()=imageFilters.size



    inner class ImageFilterViewHolder(val binding:ItemContainerFilterBinding):
            RecyclerView.ViewHolder(binding.root)
}