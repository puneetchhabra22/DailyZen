package com.major.dailyzen.ui

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.major.dailyzen.R
import com.major.dailyzen.databinding.BottomSheetDialogBinding
import com.major.dailyzen.models.DailyZenApiResponseItem
import com.major.dailyzen.ui.viewmodels.BottomSheetFragmentViewModel
import com.major.dailyzen.ui.viewmodels.BottomSheetViewModelFactory
import com.squareup.picasso.Picasso


class BottomSheetFragment() :
    BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetDialogBinding
    lateinit var bottomSheetFragmentViewModel: BottomSheetFragmentViewModel
    lateinit var btnShareCopy: Button
    lateinit var btnShareWhatsApp: CardView
    lateinit var btnShareInstagram: CardView
    lateinit var btnShareFacebook: CardView
    lateinit var btnShareDownload: CardView
    lateinit var btnShareMore: CardView
    lateinit var btnClose: ImageButton
    lateinit var dailyZenApiResponseItem: DailyZenApiResponseItem
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dailyZenApiResponseItem = arguments?.getParcelable("dzItem")!!

        bottomSheetFragmentViewModel =
            ViewModelProvider(
                this,
                BottomSheetViewModelFactory(dailyZenApiResponseItem, requireContext())
            ).get(
                BottomSheetFragmentViewModel::class.java
            )

        binding = BottomSheetDialogBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set EXPANDED state for bottom sheet dialog
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        //access bottom Sheet Views
        val imageView: ImageView = binding.imageView
        val tVDZText: TextView = binding.tVDZText
        btnShareCopy = binding.btnCopy
        btnShareWhatsApp = binding.cardViewWhatsApp
        btnShareInstagram = binding.cardViewInstagram
        btnShareFacebook = binding.cardViewFacebook
        btnShareDownload = binding.cardViewDownload
        btnShareMore = binding.cardViewMore
        btnClose = binding.imageButtonClose


//        DailyZenApiResponseItem properties
        val primaryCTAtext = bottomSheetFragmentViewModel.primaryCTAtext
        val articleUrl = bottomSheetFragmentViewModel.articleUrl
        val shareText = bottomSheetFragmentViewModel.shareText
        val text = bottomSheetFragmentViewModel.text
        val author = bottomSheetFragmentViewModel.author
        val dzImageUrl = bottomSheetFragmentViewModel.dzImageUrl


        //setup bottom sheet views
        Picasso.get().load(dzImageUrl).into(imageView)

        var cpyText = text

        if (text.isEmpty()) {
            cpyText = articleUrl //if text if empty (sometimes in case of dzType == read), let user copy url
        } else {
            cpyText = "$text \n$author" //else copy text and author
        }
        tVDZText.text = cpyText

        val imageURI = bottomSheetFragmentViewModel.getImageURI(imageView.drawable)

        btnShareCopy.setOnClickListener {
            val clipboardManager = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            val clipdata = ClipData.newPlainText("Daily Zen", cpyText)
            clipboardManager.setPrimaryClip(clipdata)
            btnShareCopy.text = "Copied"
            btnShareCopy.setTextColor(Color.WHITE)
            btnShareCopy.setBackgroundResource(R.drawable.copy_btn_pressed_bg)
        }

        btnShareWhatsApp.setOnClickListener {
            val shareDZIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageURI)
                putExtra(Intent.EXTRA_TEXT, shareText.toString())
                `package` = "com.whatsapp"
            }
            try {
                startActivity(shareDZIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this.context,
                    "Whatsapp not found on this device!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnShareInstagram.setOnClickListener {
            val shareDZIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageURI)
                putExtra(Intent.EXTRA_TEXT, shareText.toString())
                `package` = "com.instagram.android"
            }
            try {
                startActivity(shareDZIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this.context,
                    "Instagram not found on this device!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnShareFacebook.setOnClickListener {
            val shareDZIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageURI)
                putExtra(Intent.EXTRA_TEXT, shareText.toString())
                `package` = "com.facebook.katana"
            }
            try {
                startActivity(shareDZIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this.context,
                    "Facebook not found on this device!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnShareMore.setOnClickListener {
            val shareDZIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageURI)
                putExtra(Intent.EXTRA_TEXT, shareText.toString())
            }
            if (shareDZIntent.resolveActivity(activity?.packageManager!!) != null) {
                startActivity(Intent.createChooser(shareDZIntent, primaryCTAtext))
            }
        }
        btnClose.setOnClickListener {
            this.dismiss()
        }
    }


}