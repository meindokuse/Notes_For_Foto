package com.example.klicker

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.klicker.databinding.ContentItemBinding
class MainAdapter(private val itemClickListener: ItemClickListener):RecyclerView.Adapter<MainAdapter.ContentHolder>() {
    val contentList = ArrayList<Content>()
    var deleteMode:Boolean=false
    fun enableDeleteMode(){
        deleteMode = true
        notifyDataSetChanged()
    }
    fun removeItem(position: Int) {
        contentList.removeAt(position)
        notifyDataSetChanged()
        deleteMode = false
    }

    class ContentHolder(item:View):RecyclerView.ViewHolder(item) {

        val binding = ContentItemBinding.bind(item)
        fun bind(cont: Content) = with(binding){
            Log.d("MyLog","bind")
//            val imagePath = cont.resId
//            val bitmap = BitmapFactory.decodeFile(imagePath)
//            im.setImageBitmap(bitmap)
            Glide.with(itemView)
                .load(cont.resId)
                .into(im)
            TextV.text = cont.opiska

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        Log.d("MyLog","onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_item,parent,false)
        return ContentHolder(view)
    }

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        Log.d("MyLog","onBindViewHolder")
        holder.bind(contentList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        Log.d("MyLog","getItemCount")
        return contentList.size
    }
    fun addContent(cont: Content){
        Log.d("MyLog","addContent")
        contentList.add(cont)
        notifyDataSetChanged()
    }
    fun addListContent(cont:List<Content>){
        contentList.addAll(cont)
        notifyDataSetChanged()

    }



}


