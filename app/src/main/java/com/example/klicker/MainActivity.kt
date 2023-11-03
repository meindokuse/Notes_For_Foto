package com.example.klicker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.klicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result:ActivityResult ->
            if(result.resultCode== RESULT_OK){
                binding.textView.text = result.data?.getStringExtra("Text")
                binding.button.text = "Познать Дзен еще разок"
                Log.d("MyLog","${binding.textView.text}")
            }

        }
        binding.button.setOnClickListener{

            startForResult.launch(Intent(this,TetsActivity::class.java))
        }
    }



}