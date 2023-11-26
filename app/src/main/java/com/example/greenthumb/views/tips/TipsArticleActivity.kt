package com.example.greenthumb.views.tips

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gardening.databinding.ActivityTipsArticleBinding
import com.example.greenthumb.common.isInternetConnected

class TipsArticleActivity : AppCompatActivity() {
    lateinit var databinding : ActivityTipsArticleBinding
    private val tipsArticlesViewModel: TipsArticlesViewModel by viewModels()
    lateinit var adapter : TipsArticleAdapter
    private var dialog: ProgressDialog? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = ActivityTipsArticleBinding.inflate(layoutInflater)
        setContentView(databinding.root)

        adapter = TipsArticleAdapter(this@TipsArticleActivity)
        databinding.idPlantLibraryRecycle.layoutManager = LinearLayoutManager(this)
        databinding.idPlantLibraryRecycle.adapter = adapter
        dialog= ProgressDialog(this@TipsArticleActivity)

        if(isInternetConnected(this)){
            tipsArticlesViewModel.getPlantTracker()
        }else{
            Toast.makeText(this@TipsArticleActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }
        // viewmodels
        tipsArticlesViewModel.message.observe(this@TipsArticleActivity, Observer {
            Toast.makeText(this@TipsArticleActivity,""+it, Toast.LENGTH_SHORT).show()
        })

        tipsArticlesViewModel.plantDetails.observe(this@TipsArticleActivity, Observer {
            if(it.isNotEmpty()){
                adapter.getData(it)
            }
        })

        tipsArticlesViewModel.isLoading.observe(this@TipsArticleActivity, Observer {
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