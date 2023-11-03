package com.example.klicker

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.klicker.databinding.ActivityEditBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.Manifest

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    private var indexImage = 0
    private var ImageId = 0
    private var imagePass: String? = null
    private lateinit var tempFilePath:String
    private val ImageList = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.vertuha,
        R.drawable.muradov)
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 123



    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val imageUri = data?.data
            if (imageUri != null) {

                // Отобразить выбранное изображение в ImageView
                binding.imageView2.setImageURI(imageUri)
                val selectedImage: Bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))

                // Корректировать ориентаци изображения
                val fixedImage = fixImageOrientation(imageUri, selectedImage)

                // Создайте временный файл для сохранения изображения
                val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
                tempFilePath = tempFile.absolutePath

                // Сохраните изображение во временный файл
                val outputStream = FileOutputStream(tempFile)
                fixedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                imagePass = tempFilePath

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        tempFilePath = " "



    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено, выполните операции с галереей.
                openImagePicker()
            } else {
                // Пользователь отказал в предоставлении разрешения, обработайте это соответственно.
                // Например, показав сообщение пользователю.
                Toast.makeText(this, "Для доступа к галерее необходимо разрешение", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun init() = with(binding){

        bNext.setOnClickListener {
            indexImage++
            if (indexImage>ImageList.size-1) indexImage = 0
            ImageId = ImageList[indexImage]
            imageView2.setImageResource(ImageId)



        }
        bDone.setOnClickListener {
            ImageId = ImageList[indexImage]
            val title = edTitle.text.toString()
            val desc = edDescription.text.toString()
            if(imagePass!=tempFilePath)
            {
            val selectedImage = BitmapFactory.decodeResource(resources, ImageId)
            val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
            val tempFilePath = tempFile.absolutePath
            val outputStream = FileOutputStream(tempFile)
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            imagePass = tempFilePath
            }


            if(title.isNotEmpty()){
                val cont = Content(imagePass,title,desc)
                intent.putExtra("content",cont)
                setResult(RESULT_OK,intent)
                finish()

            }
            else Toast.makeText(this@EditActivity,"Заполение заголовка обязательно", Toast.LENGTH_SHORT ).show()

        }
        bChooseImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@EditActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this@EditActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_REQUEST)}
            else{openImagePicker()}
        }
        floatingActionButton4.setOnClickListener { finish() }


    }
    private fun openImagePicker() {
        val pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = "image/*"
        pickImage.launch(pickImageIntent)
    }


private fun fixImageOrientation(imageUri: Uri, selectedImage: Bitmap): Bitmap {
    val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
    if (inputStream != null) {
        val ei = ExifInterface(inputStream)

        val orientation: Int =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotatedBitmap: Bitmap

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotatedBitmap = rotateImage(selectedImage, 90)
            }

            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotatedBitmap = rotateImage(selectedImage, 180)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotatedBitmap = rotateImage(selectedImage, 270)
            }

            else -> {
                rotatedBitmap = selectedImage
            }
        }

        return rotatedBitmap
    }
    return selectedImage



}

    private fun rotateImage(source: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }



}