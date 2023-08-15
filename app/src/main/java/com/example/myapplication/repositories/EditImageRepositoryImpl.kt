package com.example.myapplication.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.core.content.FileProvider
import com.example.myapplication.data.ImageFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImage3x3ConvolutionFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageAlphaBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBilateralBlurFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBoxBlurFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorDodgeBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorMatrixFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import kotlin.io.path.fileVisitor

class EditImageRepositoryImpl(private val context: Context) :EditImageRepository{

    override suspend fun getImageFilters(image: Bitmap): List<ImageFilter> {
        val gpuImage=GPUImage(context).apply {
            setImage(image)
        }
        val imageFilter:ArrayList<ImageFilter> = ArrayList()
        //region::FilterPresetStart.....
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name = "Normal",
                    filter=filter,
                    FilterPreview=gpuImage.bitmapWithFilterApplied

                )
            )
        }

        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.1f, 0.2f, 0.0f,
                0.1f, 0.1f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f

            )
        ).also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Retro",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        GPUImageColorMatrixFilter(
            0.9f,
            floatArrayOf(
                0.4f, 0.6f, 0.5f, 0.0f,
                0.0f, 0.4f, 1.0f, 0.0f,
                0.05f, 0.1f, 0.4f, 0.4f,
                1.0f, 1.0f, 1.0f, 1.0f
            )
        ).also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Just",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.25f, 0.0f, 0.2f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.0f, 0.3f, 1.0f, 0.3f,
                0.0f, 0.0f, 0.0f, 1.0f

            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Hume",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.6f,0.4f,0.2f,0.05f,
                0.0f,0.8f,0.3f,0.05f,
                0.3f,0.3f,0.5f,0.08f,
                0.0f,0.0f,0.0f,1.0f

            )
        ).also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Dessert",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )

            )
        }
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f,0.05f,0.0f,0.0f,
                -0.2f,1.1f,-0.2f,0.11f,
                0.2f,0.0f,1.0f,0.0f,
                0.0f,0.0f,0.0f,1.0f


            )
        ).also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Old Times",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )

            )
        }
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
               1.0f,0.0f,0.08f,0.0f,
                0.4f,1.0f,0.0f,0.0f,
                0.0f,0.0f,1.0f,0.1f,
                0.0f,0.0f,0.0f,1.0f

            )
        ).also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Limo",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )

            )
        }
        GPUImageSepiaToneFilter().also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Sepia",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageAlphaBlendFilter().also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Alpha Blend",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageBrightnessFilter().also{
            filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Brightness",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageColorDodgeBlendFilter().also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Dodge",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageSaturationFilter(2.0f).also {filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="wole",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImage3x3ConvolutionFilter().also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Convolution",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageBoxBlurFilter().also {filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Box Blur",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        GPUImageVibranceFilter(1.5f).also {filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Vibrance",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }
        GPUImageBilateralBlurFilter().also { filter->
            gpuImage.setFilter(filter)
            imageFilter.add(
                ImageFilter(
                    name="Bilateral Blur",
                    filter=filter,
                    FilterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }










        return imageFilter
    }
//region end
    override suspend fun prepareImagePreview(imageUri:Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
            val originalBitmap=BitmapFactory.decodeStream(inputStream)
            val width=context.resources.displayMetrics.widthPixels
            val height=((originalBitmap.height*width)/originalBitmap.width)
            return Bitmap.createScaledBitmap(originalBitmap,width,height,false)



        }?:return null

    }


    private fun getInputStreamFromUri(uri:Uri):InputStream?{
        return context.contentResolver.openInputStream(uri)

    }

    override suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri? {
        return try {
            val mediaStorageDirectory= File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Images"
            )
            if (!mediaStorageDirectory.exists()){
                mediaStorageDirectory.mkdirs()
            }
            val fileName="IMG_${System.currentTimeMillis()}.jpg"
            val file=File(mediaStorageDirectory,fileName)
            saveFile(file,filteredBitmap)
            FileProvider.getUriForFile(context,"${context.packageName}.provider",file)

        }catch (exception:Exception){
            null
        }
    }
    private fun saveFile(file:File,bitmap: Bitmap){
        with(FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,this)
            flush()
            close()

        }
    }
}