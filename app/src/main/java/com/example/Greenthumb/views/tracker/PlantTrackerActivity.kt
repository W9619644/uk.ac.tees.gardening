package com.example.greenthumb.views.tracker

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gardening.databinding.ActivityPlantTrackerBinding
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.common.navigateActivity

class PlantTrackerActivity : AppCompatActivity() {

    lateinit var binding : ActivityPlantTrackerBinding
    lateinit var adapter: TrackerAdapter
    lateinit var dialog :ProgressDialog

    private val trackerViewModel:TrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idUpload.setOnClickListener{
            navigateActivity(this@PlantTrackerActivity,UploadPlantActivity::class.java)
        }

        adapter = TrackerAdapter(this@PlantTrackerActivity)
        binding.idTrackerList.layoutManager = LinearLayoutManager(this)
        binding.idTrackerList.adapter = adapter
        dialog= ProgressDialog(this@PlantTrackerActivity)


        trackerViewModel.getPlantList.observe(this@PlantTrackerActivity, Observer {
            if(it.isNotEmpty()){
                binding.idNotdata.visibility = View.GONE
                binding.idTrackerList.visibility = View.VISIBLE
                adapter.getData(it)
            }else{
                binding.idNotdata.visibility = View.VISIBLE
                binding.idTrackerList.visibility = View.GONE
            }
        })

        trackerViewModel.isLoading.observe(this@PlantTrackerActivity , Observer {
            if(it){
                dialog.setMessage("Loading....")
                dialog?.show()
            }
            else{
                if(dialog?.isShowing == true){
                    dialog.cancel()
                }
            }
        })


    }

    override fun onResume() {
        super.onResume()

        if(isInternetConnected(this)){
            trackerViewModel.getTrackerList()
        }else{
            Toast.makeText(this@PlantTrackerActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }


    }
}