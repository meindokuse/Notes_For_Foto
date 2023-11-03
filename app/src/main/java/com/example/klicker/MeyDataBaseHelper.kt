package com.example.klicker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MeyDataBaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Zametki.db"
        private const val TABLE_NAME = "content"

        private const val KEY_ID = "id"
        private const val KEY_FOTO = "res_id"
        private const val KEY_TITLE = "opiska"
        private const val KEY_DESCRIPTION = "opiska_desc"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_FOTO TEXT, $KEY_TITLE TEXT, $KEY_DESCRIPTION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
        fun addContent(content: Content){
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(KEY_FOTO, content.resId)
                put(KEY_TITLE, content.opiska)
                put(KEY_DESCRIPTION,content.opiskaDesc)
            }
            db.insert(TABLE_NAME,null,values)
            db.close()
        }
        fun getAllContent():List<Content>{
            val contentList = ArrayList<Content>()
            val db = this.readableDatabase
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val cursor = db.rawQuery(selectQuery,null)
            if(cursor.moveToFirst()){
                do{
                    val content = Content(
                        cursor.getString(cursor.getColumnIndex(KEY_FOTO)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))
                    )
                    contentList.add(content)
                }while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return contentList

    }
    fun deleteContent(content: Content){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"$KEY_FOTO=?", arrayOf(content.resId))
        val deletedRows = db.delete(TABLE_NAME,"$KEY_FOTO=?", arrayOf(content.resId))
        db.close()
        Log.d("MyLog", "Deleted $deletedRows rows with resId: ${content.resId}")
    }


}