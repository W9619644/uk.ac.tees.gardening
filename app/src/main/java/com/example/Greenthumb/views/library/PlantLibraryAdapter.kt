package com.example.greenthumb.views.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gardening.databinding.LibraryItemlayoutBinding
import com.example.greenthumb.models.LibraryModel
import com.squareup.picasso.Picasso


class PlantLibraryAdapter(plantsLibraryActivity: PlantsLibraryActivity) : RecyclerView.Adapter<PlantLibraryAdapter.MyViewHolder>() {

    var list = ArrayList<LibraryModel>()

    class MyViewHolder(val itemview: LibraryItemlayoutBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(applicant: LibraryModel) {
            applicant.let {
                itemview.models = applicant
            }
        }
    }

    fun getData(data:ArrayList<LibraryModel>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LibraryItemlayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            bind(list[position])
        }

        try {

            holder.itemview.idName.setText(list[position].common_name?:"")
            holder.itemview.idYear.setText(""+list[position].year)
            holder.itemview.idRank.setText(list[position].rank?:"")
            Picasso.get().load(list[position].image_url).into(holder.itemview.profileImage)
        }catch (e:java.lang.Exception){

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}