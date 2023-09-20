
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
            "CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, points INTEGER, image TEXT, level INTEGER, complexity INTEGER)"
        db!!.execSQL(query)

        val scoreRecordTableQuery =
            "CREATE TABLE score_records (id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, date TEXT, earned_points INTEGER, fail_game INTEGER, ordinary_game INTEGER, perfect_game INTEGER,  FOREIGN KEY (user_name) REFERENCES users(name))"
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
        values.put("complexity", user.complexity)
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
            arrayOf("id", "name", "points", "image", "level", "complexity"),
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
                val complexity = cursor.getInt(cursor.getColumnIndex("complexity"))
                user = User(name, points, image, level, complexity)
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

        deleteScoreRecordsIfExists(userName)

        val deletedUserRows = db.delete("users", whereClause, whereArgs)

        return deletedUserRows > 0
    }

    fun deleteScoreRecordsIfExists(userName: String) {
        val db = this.writableDatabase

        val checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='score_records'"
        val tableCursor = db.rawQuery(checkTableQuery, null)

        if (tableCursor != null) {
            if (tableCursor.moveToFirst()) {

                val whereClause = "user_name = ?"
                val whereArgs = arrayOf(userName)
                db.delete("score_records", whereClause, whereArgs)
            }
            tableCursor.close()
        }
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
    fun updateUserComplexity(userName: String, newComplexity: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("complexity", newComplexity)

        val whereClause = "name = ?"
        val whereArgs = arrayOf(userName)

        val updatedRows = db.update("users", values, whereClause, whereArgs)

        return updatedRows > 0
    }

    @SuppressLint("Range")
    fun addScoreRecord(
        userName: String,
        date: String,
        earnedPoints: Int,
        failGame: Int,
        ordinaryGame: Int,
        perfectGame: Int
    ) {
        val db = this.writableDatabase

        val checkQuery =
            "SELECT id, earned_points, fail_game, ordinary_game, perfect_game FROM score_records WHERE user_name = ? AND date = ?"
        val checkCursor = db.rawQuery(checkQuery, arrayOf(userName, date))
        checkCursor?.use {
            if (it.moveToFirst()) {

                val existingId = it.getLong(it.getColumnIndex("id"))
                val currentEarnedPoints = it.getInt(it.getColumnIndex("earned_points"))
                val currentFailGame = it.getInt(it.getColumnIndex("fail_game"))
                val currentOrdinaryGame = it.getInt(it.getColumnIndex("ordinary_game"))
                val currentPerfectGame = it.getInt(it.getColumnIndex("perfect_game"))

                val updatedEarnedPoints = currentEarnedPoints + earnedPoints
                val updatedFailGame = currentFailGame + failGame
                val updatedOrdinaryGame = currentOrdinaryGame + ordinaryGame
                val updatedPerfectGame = currentPerfectGame + perfectGame

                val updateQuery =
                    "UPDATE score_records SET earned_points = ?, fail_game = ?, ordinary_game = ?, perfect_game = ? WHERE id = ?"
                db.execSQL(
                    updateQuery,
                    arrayOf(updatedEarnedPoints, updatedFailGame, updatedOrdinaryGame, updatedPerfectGame, existingId)
                )
            } else {

                val values = ContentValues()
                values.put("user_name", userName)
                values.put("date", date)
                values.put("earned_points", earnedPoints)
                values.put("fail_game", failGame)
                values.put("ordinary_game", ordinaryGame)
                values.put("perfect_game", perfectGame)
                db.insert("score_records", null, values)
            }
        }

        val countQuery = "SELECT COUNT(*) FROM score_records WHERE user_name = ?"
        val countCursor = db.rawQuery(countQuery, arrayOf(userName))
        countCursor?.use {
            if (it.moveToFirst()) {
                val count = it.getInt(0)
                if (count > 7) {
                    val deleteQuery =
                        "DELETE FROM score_records WHERE id = (SELECT id FROM score_records WHERE user_name = ? ORDER BY date ASC LIMIT 1)"
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
                val failGame = it.getInt(it.getColumnIndex("fail_game"))
                val ordinaryGame = it.getInt(it.getColumnIndex("ordinary_game"))
                val perfectGame = it.getInt(it.getColumnIndex("perfect_game"))
                scoreRecords.add(
                    ScoreRecord(
                        id,
                        userName,
                        date,
                        earnedPoints,
                        failGame,
                        ordinaryGame,
                        perfectGame
                    )
                )
            }
        }
        return scoreRecords
    }

}
