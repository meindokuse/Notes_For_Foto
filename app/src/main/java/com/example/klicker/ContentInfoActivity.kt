package com.example.klicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.klicker.databinding.ActivityContentInfoBinding

@Suppress("DEPRECATION")
class ContentInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentInfoBinding
    private lateinit var  adapter :MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = MainAdapter(object : ItemClickListener {
            override fun onItemClick(position: Int) {}
        })

        val content = intent.getSerializableExtra("content") as Content
        binding.TitleText.text = content.opiska
        binding.DescText.text = content.opiskaDesc
        Glide.with(this)
            .load(content.resId)
            .into(binding.imageView)

    }

}