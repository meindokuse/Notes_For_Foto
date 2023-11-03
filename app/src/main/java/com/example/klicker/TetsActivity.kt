package com.example.klicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.klicker.databinding.ActivityTetsBinding

class TetsActivity : AppCompatActivity() {
    lateinit var dataBase:MeyDataBaseHelper
    lateinit var binding: ActivityTetsBinding
    private lateinit var  adapter :MainAdapter
    private var editLauncher: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = MeyDataBaseHelper(this)
        adapter = MainAdapter(object : ItemClickListener {
            override fun onItemClick(position: Int) {
                if (adapter.deleteMode) {
                    // Режим удаления включен, удалите элемент
                    Log.d("MyLog","${adapter.contentList[position]}")
                    dataBase.deleteContent(adapter.contentList[position])
                    adapter.removeItem(position)

                } else {
                    // Режим удаления выключен, переходите к другой активности
                    val content = adapter.contentList[position]
                    Log.d("MyLog","${content.resId}")
                    val intent = Intent(this@TetsActivity, ContentInfoActivity::class.java)
                    intent.putExtra("content", content)
                    startActivity(intent)
                }
            }
        })
        adapter.addListContent(dataBase.getAllContent())

        init()

        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                @Suppress("DEPRECATION")
                adapter.addContent(it.data?.getSerializableExtra("content") as Content)
                @Suppress("DEPRECATION")
                dataBase.addContent(it.data?.getSerializableExtra("content") as Content)

            }
        }


    }
    private fun init() = with(binding){
        rcView.layoutManager = GridLayoutManager(this@TetsActivity,2)
        rcView.adapter = adapter
        floatingActionButton2.setOnClickListener {
            editLauncher?.launch(Intent(this@TetsActivity,EditActivity::class.java))

        }
        val customToolbar = findViewById<Toolbar>(R.id.custom_toolbar)
        customToolbar.inflateMenu(R.menu.rv_menu)
        val deleteButton = customToolbar.menu.findItem(R.id.delete)
        deleteButton.setOnMenuItemClickListener { item ->
            // Обработка нажатия на кнопку удаления
            if (adapter.contentList.size > 0) {
                Toast.makeText(this@TetsActivity, "Нажмите на элемент, который хотите удалить", Toast.LENGTH_SHORT).show()
                adapter.enableDeleteMode()
            } else {
                Toast.makeText(this@TetsActivity, "Нет элементов для удаления", Toast.LENGTH_SHORT).show()
            }
            true
        }


    }




    override fun onResume() {
        super.onResume()
        intent.putExtra("Text","Норм контент да?")
        setResult(RESULT_OK,intent)
    }

}