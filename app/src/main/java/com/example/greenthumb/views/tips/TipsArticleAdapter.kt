package com.example.greenthumb.views.tips

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gardening.databinding.TipsItemlayoutBinding
import com.example.greenthumb.models.TipsResponseModel
import com.squareup.picasso.Picasso



class TipsArticleAdapter(val plantsLibraryActivity: TipsArticleActivity) : RecyclerView.Adapter<TipsArticleAdapter.MyViewHolder>() {

    var list = ArrayList<TipsResponseModel>()

    class MyViewHolder(val itemview: TipsItemlayoutBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(applicant: TipsResponseModel) {
            applicant.let {
                itemview.models = applicant
            }
        }
    }

    fun getData(data:ArrayList<TipsResponseModel>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TipsItemlayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            bind(list[position])
        }

        try {
            holder.itemview.idName.setText(list[position].title?:"")
            Picasso.get().load(list[position].urlToImage).into(holder.itemview.profileImage)
        }catch (e:java.lang.Exception){

        }

        holder.itemview.idTitpsLink.setOnClickListener {
            if(!list[position].url.isNullOrEmpty()){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(list[position].url?:"")
                plantsLibraryActivity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}