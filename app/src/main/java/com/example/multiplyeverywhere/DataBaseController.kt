
package com.example.multiplyeverywhere

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseController (val context: Context , val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper (context, "appBase", factory, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (id INT PRIMARY KEY, name STRING, level INT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
    fun addUser(user: User){
        val values  = ContentValues()
        values.put("name", user.name)
        values.put("level", user.level)
        val db = this.writableDatabase
        db.insert("users", null, values)
    }
}