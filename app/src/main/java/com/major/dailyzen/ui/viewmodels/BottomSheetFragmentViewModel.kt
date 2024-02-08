package com.major.dailyzen.ui.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.major.dailyzen.R
import com.major.dailyzen.models.DailyZenApiResponseItem

class BottomSheetFragmentViewModel(dailyZenApiResponseItem: DailyZenApiResponseItem, val context: Context) : ViewModel(){

//    DailyZenApiResponseItem properties
    val primaryCTAtext = dailyZenApiResponseItem.primaryCTAText
    val articleUrl = dailyZenApiResponseItem.articleUrl
    val sharePrefix = dailyZenApiResponseItem.sharePrefix
    val text = dailyZenApiResponseItem.text
    val author = dailyZenApiResponseItem.author
    val dzImageUrl = dailyZenApiResponseItem.dzImageUrl
    val dzUniqueId = dailyZenApiResponseItem.uniqueId


    //check if articleURL is there or not, if yes append with sharePrefix
    private val shareTextBuilder: StringBuilder = StringBuilder(sharePrefix).apply {
        if(articleUrl.isNotEmpty()) {
            this.append("\n$articleUrl")
        }
    }

    val shareText = shareTextBuilder.toString() //to be accessed by fragment


    fun getImageURI(drawable: Drawable): Uri{
        //To share images via intent we store that in MediaStore
        val bitmap = (drawable as BitmapDrawable).bitmap
        val bitmapPath = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            dzUniqueId,
            null
        )
        val imageURI = Uri.parse(bitmapPath)
        return imageURI
    }

}