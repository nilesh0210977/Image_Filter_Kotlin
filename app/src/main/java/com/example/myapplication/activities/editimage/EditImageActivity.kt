package com.example.myapplication.activities.editimage


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.activities.filteredimage.FilteredImageActivity
import com.example.myapplication.activities.main.MainActivity
import com.example.myapplication.adapters.ImageFiltersAdapters
import com.example.myapplication.data.ImageFilter
import com.example.myapplication.databinding.ActivityEditImageBinding
import com.example.myapplication.listeners.ImageFilterListeners
import com.example.myapplication.utilities.displayToast
import com.example.myapplication.utilities.show
import com.example.myapplication.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(),ImageFilterListeners {

    companion object{
        const val KEY_FILTERED_IMAGE_URI="filterImageUri"

    }

    private lateinit var binding: ActivityEditImageBinding
    private val viewModel:EditImageViewModel by viewModel()
    private lateinit var gpuImage:GPUImage

    //imageBitmap
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap=MutableLiveData<Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()

    }
    private fun setupObservers(){
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                //for first time
                originalBitmap=bitmap
                filteredBitmap.value=bitmap

                with(originalBitmap){
                    gpuImage.setImage(this)

                    binding.imagePreview.show()
                    viewModel.loadImageFilter(this)
                }

            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
        viewModel.imageFilterUiState.observe(this) {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFilterProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapters(imageFilters,this).also { adapter->
                    binding.filtersRecyclerView.adapter=adapter
                }
            }?:kotlin.run {
                imageFiltersDataState.error?.let { error->
                    displayToast(error)
                }
            }


        }
        filteredBitmap.observe(this) {bitmap->
            binding.imagePreview.setImageBitmap(bitmap)

        }
        viewModel.saveFilteredImageUiState.observe(this){
            val saveFilteredImageDataState=it?:return@observe
            if (saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility=View.GONE
                binding.savingProgressBar.visibility=View.VISIBLE

            }else{
                binding.savingProgressBar.visibility=View.GONE
                binding.imageSave.visibility=View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let {savedImageUri->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also {filteredImageIntent->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI,savedImageUri)
                    startActivity(filteredImageIntent)

                }
            }?:kotlin.run{
                saveFilteredImageDataState.error?.let { error->
                    displayToast(error)
                }
            }
        }
    }

    private fun prepareImagePreview(){
        gpuImage= GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->

            viewModel.prepareImagePreview(imageUri)
        }

    }



    private fun setListeners()
    {
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
        binding.imageSave.setOnClickListener{
            filteredBitmap.value?.let {bitmap ->
            viewModel.saveFilteredImage(bitmap)
                

            }
        }



        binding.imagePreview.setOnLongClickListener{
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener{
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }


    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value=bitmapWithFilterApplied
            }
        }

    }
}