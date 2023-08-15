package com.example.myapplication.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.ImageFilter
import com.example.myapplication.repositories.EditImageRepository
import com.example.myapplication.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository):ViewModel() {

    //region::imagePreview...

    private val imagePreviewDataState=MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState:LiveData<ImagePreviewDataState>get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri)
    {
        Coroutines.io {
            runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
            if (bitmap!=null){
                emitImagePreviewUiState(bitmap=bitmap)

            }
                else{
                emitImagePreviewUiState(error = "unable to prepare Image Preview")
            }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(
        isLoading: Boolean=false,
        bitmap: Bitmap?=null,
        error: String?=null
    )
    {
        val dataState=ImagePreviewDataState(isLoading,bitmap,error)
        imagePreviewDataState.postValue(dataState)
    }
    data class ImagePreviewDataState(
        val isLoading:Boolean,
        val bitmap: Bitmap?,
        val error:String?


    )
    //endregion...

    //region::loadImageFilter...
    private val imageFiltersDataState=MutableLiveData<ImageFiltersDataState>()
    val imageFilterUiState:LiveData<ImageFiltersDataState> get()=imageFiltersDataState

    fun loadImageFilter(orginalImage: Bitmap){
        Coroutines.io {
            runCatching {
                emitImageFilterUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(orginalImage))

            }.onSuccess { imagefilters->
                emitImageFilterUiState(imageFilters = imagefilters)
            }.onFailure {
                emitImageFilterUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(orginalImage:Bitmap):Bitmap{
        return runCatching {
            val previewWidth=150
            val previewHeight=orginalImage.height * previewWidth/orginalImage.width
            Bitmap.createScaledBitmap(orginalImage,previewWidth,previewHeight,false)

        }.getOrDefault(orginalImage)
    }

    private fun emitImageFilterUiState(
        isLoading: Boolean=false,
        imageFilters: List<ImageFilter>?=null,
        error: String?=null
    ){
        val dataState=ImageFiltersDataState(isLoading,imageFilters,error)
        imageFiltersDataState.postValue(dataState)
    }


    data class ImageFiltersDataState(
        val isLoading: Boolean,
        val imageFilters:List<ImageFilter>?,
        val error: String?
    )








    //endregion

    //region::save image...

    private val saveFilteredImageDataState=MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState:LiveData<SaveFilteredImageDataState>get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap:Bitmap)
    {
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUistate(isLoading = true)
                editImageRepository.saveFilteredImage(filteredBitmap)
            }.onSuccess { savedImageUri->
                emitSaveFilteredImageUistate(uri=savedImageUri)
            }.onFailure {
                emitSaveFilteredImageUistate(error = it.message.toString())
            }
        }
    }


    private fun emitSaveFilteredImageUistate(
        isLoading: Boolean=false,
        uri: Uri?=null,
        error: String?=null
    ){

        val dataState=SaveFilteredImageDataState(isLoading,uri,error)
        saveFilteredImageDataState.postValue(dataState)
    }


    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )


    //endregion..



}