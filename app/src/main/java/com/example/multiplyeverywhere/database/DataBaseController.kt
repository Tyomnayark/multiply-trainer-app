
package com.example.multiplyeverywhere.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.multiplyeverywhere.User

class DataBaseController (val context: Context , val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper (context, "usersdb", factory, 1){
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
    fun doesDatabaseExist(context: Context, dbName: String): Boolean {
        val dbFile = context.getDatabasePath(dbName)
        return dbFile.exists()
    }

    @SuppressLint("Range")
    fun getUserByName(userName: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("id", "name", "level"),
            "name = ?",
            arrayOf(userName.toString()),
            null, null, null, null
        )

        var user: User? = null

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val level = cursor.getInt(cursor.getColumnIndex("level"))
                user = User( name, level)
            }
            cursor.close()
        }
        return user
    }
    fun getUserNames(): ArrayList<String> {
        val userNames = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("name"),
            null,
            null,
            null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex("name"))
                userNames.add(name)
            }
        }

        return userNames
    }
    fun deleteUserByName(userName: String): Boolean {
        val db = this.writableDatabase

        val whereClause = "name = ?"
        val whereArgs = arrayOf(userName)

        val deletedRows = db.delete("users", whereClause, whereArgs)

        return deletedRows > 0
    }
}