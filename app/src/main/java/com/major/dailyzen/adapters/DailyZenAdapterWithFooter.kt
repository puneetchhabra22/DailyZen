package com.major.dailyzen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.major.dailyzen.R
import com.major.dailyzen.models.DailyZenApiResponseItem
import com.major.dailyzen.util.Constants.Companion.FOOTER_TYPE
import com.major.dailyzen.util.Constants.Companion.READ_TYPE
import com.major.dailyzen.util.Constants.Companion.SEND_TYPE
import com.squareup.picasso.Picasso

class DailyZenAdapterWithFooter(private var recyclerViewItemClickListener : RecyclerViewItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<DailyZenApiResponseItem>() {
        override fun areItemsTheSame(
            oldItem: DailyZenApiResponseItem,
            newItem: DailyZenApiResponseItem
        ): Boolean {
            return oldItem.uniqueId == newItem.uniqueId
        }

        override fun areContentsTheSame(
            oldItem: DailyZenApiResponseItem,
            newItem: DailyZenApiResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    class DZSendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //implement data binding or view binding atleast
        val themeTitle = itemView.findViewById<TextView>(R.id.tVThemeTitle)
        val dzImage = itemView.findViewById<ImageView>(R.id.iVdzImage)
        val btnShare = itemView.findViewById<CardView>(R.id.cardViewShare)
        val btnBookmark = itemView.findViewById<CardView>(R.id.cardViewBookmark)

        fun bind(item : DailyZenApiResponseItem){
            themeTitle.text = item.themeTitle
            Picasso.get().load(item.dzImageUrl).into(dzImage)
        }

    }
    class DZReadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //implement data binding or view binding atleast
        val themeTitle = itemView.findViewById<TextView>(R.id.tVThemeTitle)
        val dzImage = itemView.findViewById<ImageView>(R.id.iVdzImage)
        val btnReadFullPost = itemView.findViewById<CardView>(R.id.cardViewReadFullPost)
        val btnShare = itemView.findViewById<CardView>(R.id.cardViewShare)
        val btnBookmark = itemView.findViewById<CardView>(R.id.cardViewBookmark)

        fun bind(item : DailyZenApiResponseItem){
            themeTitle.text = item.themeTitle
            Picasso.get().load(item.dzImageUrl).placeholder(R.drawable.loading_rain_placeholder).into(dzImage)
            //Using raining image as a placeholder for dzType == Read
        }
    }

    class RVFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(itemView: View){

        }
    }


    override fun getItemViewType(position: Int): Int {
        if(position == itemCount-1){
            return FOOTER_TYPE
        }
        val item = differ.currentList.get(position)
        return checkDZType(item.dzType)
    }

    override fun getItemCount(): Int {
        return (differ.currentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SEND_TYPE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dz_card_item_share_or_send,parent,false)
                DZSendViewHolder(itemView)
            }
            READ_TYPE ->{
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dz_card_item_read,parent,false)
                DZReadViewHolder(itemView)
            }
            FOOTER_TYPE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_footer_item,parent,false)
                RVFooterViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type(Not SEND_TYPE || READ_TYPE")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position == itemCount-1) return

        val item = differ.currentList.get(position)
        if(checkDZType(item.dzType) == SEND_TYPE) {
            (holder as DZSendViewHolder).bind(item)
            holder.btnShare.setOnClickListener {
                recyclerViewItemClickListener.onShareBtnClickListener(item)
            }
            holder.btnBookmark.setOnClickListener {
                recyclerViewItemClickListener.onBookmarkBtnClickListener(it)
            }
        } else if (checkDZType((item.dzType)) == READ_TYPE) {
            (holder as DZReadViewHolder).bind(item)
            holder.btnShare.setOnClickListener {
                recyclerViewItemClickListener.onShareBtnClickListener(item)
            }
            holder.btnBookmark.setOnClickListener {
                recyclerViewItemClickListener.onBookmarkBtnClickListener(it)
            }
            holder.btnReadFullPost.setOnClickListener {
                recyclerViewItemClickListener.onReadFullPostBtnClickListner(item)
            }
        }
        else {
            throw IllegalArgumentException("Invalid view type(Not SEND_TYPE || READ_TYPE)")
        }
    }

    private fun checkDZType(dzType: String) : Int {
        return if(dzType.equals("read")){
            READ_TYPE
        } else {
            SEND_TYPE //ignoring new dzTypes
        }
    }
}