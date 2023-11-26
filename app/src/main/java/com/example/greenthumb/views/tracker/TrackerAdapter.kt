package com.example.greenthumb.views.tracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gardening.databinding.TrackeritemlayoutBinding

import com.example.greenthumb.models.LibraryModel
import com.example.greenthumb.models.UploadModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso



class TrackerAdapter(plantTrackerActivity: PlantTrackerActivity) : RecyclerView.Adapter<TrackerAdapter.MyViewHolder>() {

    var list = ArrayList<UploadModel>()

    class MyViewHolder(val itemview: TrackeritemlayoutBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(applicant: LibraryModel) {
            applicant.let {
                itemview.models = applicant
            }
        }
    }

    fun getData(data:ArrayList<UploadModel>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackeritemlayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
          //  bind(list[position])
        }

        try {

            holder.itemview.idName.setText(list[position].description?:"")
            Picasso.get().load(list[position].pantIMG).into(holder.itemview.profileImage)

            holder.itemview.idRemove.setOnClickListener {
                var database = FirebaseDatabase.getInstance().getReference("Plants");
                val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                list[position].removeKey?.let { it1 -> database.child(keys).child(it1).removeValue() }
            }

        }catch (e:java.lang.Exception){

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}