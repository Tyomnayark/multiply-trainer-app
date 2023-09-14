
package com.example.multiplyeverywhere.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.multiplyeverywhere.ScoreRecord
import com.example.multiplyeverywhere.User

class DataBaseController(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "usersdb", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, points INTEGER, image TEXT, level INTEGER)"
        db!!.execSQL(query)

        val scoreRecordTableQuery =
            "CREATE TABLE score_records (id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, date TEXT, earned_points INTEGER, FOREIGN KEY (user_name) REFERENCES users(name))"
        db?.execSQL(scoreRecordTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("name", user.name)
        values.put("points", user.points)
        values.put("image", user.profileImage)
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
            arrayOf("id", "name", "points", "image", "level"),
            "name = ?",
            arrayOf(userName),
            null, null, null, null
        )

        var user: User? = null

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val points = cursor.getInt(cursor.getColumnIndex("points"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                val level = cursor.getInt(cursor.getColumnIndex("level"))
                user = User(name, points, image, level)
            }
            cursor.close()
        }

        return user
    }

    @SuppressLint("Range")
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

    fun updateUserPoints(userName: String, newPoints: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("points", newPoints)

        val whereClause = "name = ?"
        val whereArgs = arrayOf(userName)

        val updatedRows = db.update("users", values, whereClause, whereArgs)

        return updatedRows > 0
    }

    fun updateUserProfileImage(userName: String, newImage: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("image", newImage)

        val whereClause = "name = ?"
        val whereArgs = arrayOf(userName)

        val updatedRows = db.update("users", values, whereClause, whereArgs)

        return updatedRows > 0
    }

    fun updateUserLevel(userName: String, newLevel: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("level", newLevel)

        val whereClause = "name = ?"
        val whereArgs = arrayOf(userName)

        val updatedRows = db.update("users", values, whereClause, whereArgs)

        return updatedRows > 0
    }

    @SuppressLint("Range")
    fun addScoreRecord(userName: String, date: String, earnedPoints: Int) {
        val db = this.writableDatabase

        val checkQuery = "SELECT id, earned_points FROM score_records WHERE user_name = ? AND date = ?"
        val checkCursor = db.rawQuery(checkQuery, arrayOf(userName, date))
        checkCursor?.use {
            if (it.moveToFirst()) {

                val existingId = it.getLong(it.getColumnIndex("id"))
                val currentEarnedPoints = it.getInt(it.getColumnIndex("earned_points"))

                val updatedEarnedPoints = currentEarnedPoints + earnedPoints

                val updateQuery = "UPDATE score_records SET earned_points = ? WHERE id = ?"
                db.execSQL(updateQuery, arrayOf(updatedEarnedPoints, existingId))
            } else {

                val values = ContentValues()
                values.put("user_name", userName)
                values.put("date", date)
                values.put("earned_points", earnedPoints)
                db.insert("score_records", null, values)
            }
        }

        val countQuery = "SELECT COUNT(*) FROM score_records WHERE user_name = ?"
        val countCursor = db.rawQuery(countQuery, arrayOf(userName))
        countCursor?.use {
            if (it.moveToFirst()) {
                val count = it.getInt(0)
                if (count > 7) {
                    val deleteQuery = "DELETE FROM score_records WHERE id = (SELECT id FROM score_records WHERE user_name = ? ORDER BY date ASC LIMIT 1)"
                    db.execSQL(deleteQuery, arrayOf(userName))

                }
            }
        }
    }

    @SuppressLint("Range")
    fun getScoreRecordsForUser(userName: String): List<ScoreRecord> {
        val scoreRecords = mutableListOf<ScoreRecord>()
        val db = this.readableDatabase

        val query = "SELECT * FROM score_records WHERE user_name = ?"
        val cursor = db.rawQuery(query, arrayOf(userName))

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex("id"))
                val date = it.getString(it.getColumnIndex("date"))
                val earnedPoints = it.getInt(it.getColumnIndex("earned_points"))
                scoreRecords.add(ScoreRecord(id, userName, date, earnedPoints))
            }
        }
        return scoreRecords
    }
}
