package com.major.dailyzen.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.major.dailyzen.R
import com.major.dailyzen.DailyZenApplication
import com.major.dailyzen.adapters.DailyZenAdapterWithFooter
import com.major.dailyzen.adapters.RecyclerViewItemClickListener
import com.major.dailyzen.databinding.ActivityDailyZenBinding
import com.major.dailyzen.models.DailyZenApiResponseItem
import com.major.dailyzen.ui.viewmodels.DailyZenViewModel
import com.major.dailyzen.ui.viewmodels.DailyZenViewModelFactory
import com.major.dailyzen.util.DateUtil

class DailyZenActivity : AppCompatActivity(){
    lateinit var binding: ActivityDailyZenBinding

    lateinit var dailyZenViewModel: DailyZenViewModel
    lateinit var btnPrev : ImageButton
    lateinit var btnNext : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_daily_zen)

        val rVDailyZen = binding.rVDailyZen
        val tVDate = binding.textViewDay
        btnPrev = binding.imageButtonLeft
        btnNext = binding.imageButtonRight

        val dailyZenRepository = (application as DailyZenApplication).dailyZenRepository
        dailyZenViewModel = ViewModelProvider(this,DailyZenViewModelFactory(dailyZenRepository)).get(DailyZenViewModel::class.java)


        //observing dateActive and update dateText and buttons on top
        dailyZenViewModel.dateActive.observe(this, Observer { newDate ->
            val sevenDaysAgoDate = DateUtil.getSevenDaysAgoDate()
            val yesterdayDate = DateUtil.getYesterday()

            if(newDate == dailyZenViewModel.dateToady){
                tVDate.text = "Today"
                removeNextButton()
                showPrevButton()
            } else if(newDate == DateUtil.formatDateToString(sevenDaysAgoDate)){
                tVDate.text = DateUtil.formatDateToDisplay(sevenDaysAgoDate)
                removePrevButton()
                showNextButton()
            } else if(newDate == DateUtil.formatDateToString(yesterdayDate)){
                tVDate.text = "Yesterday"
                showNextButton()
                showPrevButton()
            } else {
                tVDate.text = DateUtil.formatDateToDisplay(DateUtil.parseStringToDate(newDate))
                showPrevButton()
                showNextButton()
            }
        })

        rVDailyZen.layoutManager = LinearLayoutManager(this)
        val adapter = DailyZenAdapterWithFooter(recyclerViewItemClickListener)

        rVDailyZen.adapter = adapter

        dailyZenViewModel.dailyZen.observe(this, Observer {dailyZenApiResponse ->
            adapter.differ.submitList(dailyZenApiResponse)
        })

        btnPrev.setOnClickListener {
            dailyZenRepository.emptyListData()
            val currDateString = dailyZenViewModel.dateActive.value
            val prevDate = DateUtil.getPreviousDate(DateUtil.parseStringToDate(currDateString!!))

            dailyZenViewModel.dateActive.value = DateUtil.formatDateToString(prevDate)
        }

        btnNext.setOnClickListener {
            dailyZenRepository.emptyListData()
            val currDateString = dailyZenViewModel.dateActive.value
            val nextDate = DateUtil.getNextDate(DateUtil.parseStringToDate(currDateString!!))

            dailyZenViewModel.dateActive.value = DateUtil.formatDateToString(nextDate)

        }
    }

    //handling click listeners of recycler view items
    val recyclerViewItemClickListener = object : RecyclerViewItemClickListener{
        override fun onShareBtnClickListener(dailyZenApiResponseItem: DailyZenApiResponseItem) {
            showShareBottomSheet(dailyZenApiResponseItem)
        }

        override fun onBookmarkBtnClickListener(view: View) {
            Snackbar.make(view,"Daily Zen has been saved",Snackbar.LENGTH_SHORT).show()
        }

        override fun onReadFullPostBtnClickListner(dailyZenApiResponseItem: DailyZenApiResponseItem) {
            val readPostIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                addCategory(Intent.CATEGORY_BROWSABLE)
                data = Uri.parse(dailyZenApiResponseItem.articleUrl)
            }
            try {
                startActivity(readPostIntent)
            } catch (e : ActivityNotFoundException){
                Toast.makeText(this@DailyZenActivity,"Unable to open link! \nSomething went wrong.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showShareBottomSheet(dailyZenApiResponseItem: DailyZenApiResponseItem){
        val bottomSheetFargment = BottomSheetFragment()
        val bundle = Bundle()
        bundle.putParcelable("dzItem",dailyZenApiResponseItem)
        bottomSheetFargment.arguments = bundle
        bottomSheetFargment.show(supportFragmentManager,bottomSheetFargment.tag)
    }

    private fun removeNextButton(){
        btnNext.apply {
            isClickable = false
            isEnabled = false
            isVisible = false
        }
    }
    private fun showNextButton(){
        btnNext.apply {
            isClickable = true
            isEnabled = true
            isVisible = true
        }
    }
    private fun removePrevButton(){
        btnPrev.apply {
            isClickable = false
            isEnabled = false
            isVisible = false
        }
    }
    private fun showPrevButton(){
        btnPrev.apply {
            isClickable = true
            isEnabled = true
            isVisible = true
        }
    }

}