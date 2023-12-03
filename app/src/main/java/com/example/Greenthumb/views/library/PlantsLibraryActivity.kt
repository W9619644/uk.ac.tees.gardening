package com.example.greenthumb.views.library

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gardening.databinding.ActivityPlantsLibraryBinding
import com.example.greenthumb.common.isInternetConnected

class PlantsLibraryActivity : AppCompatActivity() {

    lateinit var databinding : ActivityPlantsLibraryBinding
    private val plantLibraryViewModel: PlantLibraryViewModel by viewModels()
    lateinit var adapter : PlantLibraryAdapter
    private var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = ActivityPlantsLibraryBinding.inflate(layoutInflater)
        setContentView(databinding.root)
        adapter = PlantLibraryAdapter(this@PlantsLibraryActivity)
        databinding.idPlantLibraryRecycle.layoutManager = LinearLayoutManager(this)
        databinding.idPlantLibraryRecycle.adapter = adapter
        dialog= ProgressDialog(this@PlantsLibraryActivity)

        if(isInternetConnected(this)){
           plantLibraryViewModel.getPlantLibrary()
        }else{
            Toast.makeText(this@PlantsLibraryActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }


        // viewmodels
        plantLibraryViewModel.message.observe(this@PlantsLibraryActivity, Observer {
            Toast.makeText(this@PlantsLibraryActivity,""+it,Toast.LENGTH_SHORT).show()
        })

        plantLibraryViewModel.plantDetails.observe(this@PlantsLibraryActivity, Observer {
            if(it.isNotEmpty()){
                adapter.getData(it)
            }
        })

        plantLibraryViewModel.isLoading.observe(this@PlantsLibraryActivity, Observer {
            if(it){
                dialog?.setMessage("Please wait....")
                dialog?.show()
            }else{
                if(dialog?.isShowing ==true){
                    dialog?.cancel()
                }
            }
        })



    }
}