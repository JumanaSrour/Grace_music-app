package iug.jumana.grace.database

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import iug.jumana.grace.fragments.Login
import iug.jumana.grace.fragments.Signup
import iug.jumana.grace.model.SongsCountry
import iug.jumana.grace.model.User
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import java.lang.Exception

@Suppress("CAST_NEVER_SUCCEEDS")
class DatabaseHelper(activity: Activity) :
    SQLiteOpenHelper(activity, DATABASE_NAME, null, DATABASE_VERSION) {
    private val db: SQLiteDatabase = this.writableDatabase

    companion object {
        val DATABASE_NAME = "University"
        val DATABASE_VERSION = 3
        var result: Boolean = false

    }


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SongsCountry.Staticated.TABLE_CREATE)
        db!!.execSQL(User.TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("Drop table if exists ${SongsCountry.Staticated.TABLE_NAME}")
        db!!.execSQL("Drop table if exists ${User.TABLE_NAME}")
        onCreate(db)
    }

    //==============================================================================================
    //DML

    fun insertSong(id: Int, name: String, artist: String, data: String, date: Double): Boolean {
        val cv = ContentValues()
        cv.put(SongsCountry.Staticated.COL_ID, id)
        cv.put(SongsCountry.Staticated.COL_NAME, name)
        cv.put(SongsCountry.Staticated.COL_ARTIST, artist)
        cv.put(SongsCountry.Staticated.COL_DATA, data)
        cv.put(SongsCountry.Staticated.COL_DATE, date)

        return db.insert(SongsCountry.Staticated.TABLE_NAME, null, cv) > 0
    }

    fun insertUser(email: String, password: String, name: String, id: Int, image: String):Boolean{
        val user = ContentValues()
        user.put(User.COL_EMAIL, email)
        user.put(User.COL_PASSWORD, password)
        user.put(User.COL_NAME, name)
        user.put(User.COL_ID, id)
        user.put(User.COL_IMG, image)

//        return db.insert(User.TABLE_NAME, null, user)> 0
        return db.insert(User.TABLE_NAME, null, user) > 0
    }

    fun verification(email: String, password: String): User? {
        var user: User
        val queryparams =
            "SELECT * FROM ${User.TABLE_NAME} WHERE ${User.COL_EMAIL} = '$email' and ${User.COL_PASSWORD} = '$password'"
        val cursor = db.rawQuery(queryparams, null)!!
        if (cursor.moveToFirst()) {

            user = User(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
            )
            db.close()

        } else {
            return null
        }
        return user
    }


    fun getAllSongs(): ArrayList<SongsCountry> {
        val db = this.readableDatabase
        val data = ArrayList<SongsCountry>()
        val c =
            db.rawQuery(
                "select * from ${SongsCountry.Staticated.TABLE_NAME} order by ${SongsCountry.Staticated.COL_ID} desc",
                null
            )
        c.moveToFirst()
        while (!c.isAfterLast) {
            val songs = SongsCountry(
                c.getInt(0).toLong(),
                c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getInt(4).toLong()
            )
            data.add(songs)
        }
        c.close()
        return data
    }


    fun storeAsFavorite(id: Int?, artist: String?, songTitle: String?, path: String?) {
        var contentValues = ContentValues()
        contentValues.put(SongsCountry.Staticated.COL_ID.toString(), id)
        contentValues.put(SongsCountry.Staticated.COL_NAME, songTitle)
        contentValues.put(SongsCountry.Staticated.COL_ARTIST, artist)
        contentValues.put(SongsCountry.Staticated.COL_DATA, path)
        db.insert(SongsCountry.Staticated.TABLE_NAME, null, contentValues)
        db.close()
    }

    fun queryDBList(): ArrayList<SongsCountry>? {
        var _songList = ArrayList<SongsCountry>()

        try {
            val db = this.readableDatabase
            val query_params = "SELECT * FROM " + SongsCountry.Staticated.TABLE_NAME
            var cSor = db.rawQuery(query_params, null)
            if (cSor.moveToFirst()) {
                do {
                    var _id =
                        cSor.getInt(cSor.getColumnIndexOrThrow(SongsCountry.Staticated.COL_ID.toString()))
                    var _artist =
                        cSor.getString(cSor.getColumnIndexOrThrow(SongsCountry.Staticated.COL_ARTIST))
                    var _title =
                        cSor.getString(cSor.getColumnIndexOrThrow(SongsCountry.Staticated.COL_NAME))
                    var _songPath =
                        cSor.getString(cSor.getColumnIndexOrThrow(SongsCountry.Staticated.COL_DATA))
                    _songList.add(SongsCountry(_id.toLong(), _title, _artist, _songPath, 0))
                } while (cSor.moveToNext())
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return _songList
    }

    fun checkIfIdExists(_id: Int): Boolean {
        var storeId: Int
        val db = this.readableDatabase
        val query_params =
            " SELECT * FROM " + SongsCountry.Staticated.TABLE_NAME + " WHERE SongId = '$_id'"
        val cSor = db.rawQuery(query_params, null)
        if (cSor.moveToFirst()) {
            do {
                storeId =
                    cSor.getInt(cSor.getColumnIndexOrThrow(SongsCountry.Staticated.COL_ID.toString()))
            } while (cSor.moveToNext())
        } else {
            return false
        }
        return storeId != -1090
    }

    fun deleteFavorite(_id: Int) {
        db.delete(
            SongsCountry.Staticated.TABLE_NAME,
            SongsCountry.Staticated.COL_ID.toString() + "=" + _id,
            null
        )
        db.close()
    }

    fun checkSize(): Int {
        var counter = 0
        var db = this.readableDatabase
        var query_params = " SELECT * FROM " + SongsCountry.Staticated.TABLE_NAME
        val cSor = db.rawQuery(query_params, null)
        if (cSor.moveToFirst()) {
            do {
                counter = counter + 1
            } while (cSor.moveToNext())
        } else {
            return 0
        }
        return counter
    }

    fun addSong(context: Context, songsCountry: SongsCountry) {
        val contentValues = ContentValues()
        contentValues.put(SongsCountry.Staticated.COL_NAME, songsCountry.songTitle)
        contentValues.put(SongsCountry.Staticated.COL_DATA, songsCountry.songData)
        try {
            db.insert(SongsCountry.Staticated.TABLE_NAME, null, contentValues)
            Toast.makeText(context, "Song Added", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

            e.printStackTrace()
        }
        db.close()
    }

    fun deleteSong(songId: Int): Boolean {
        val qry =
            "DELETE FROM ${SongsCountry.Staticated.TABLE_NAME} WHERE ${SongsCountry.Staticated.COL_ID} =$songId"
        var result: Boolean = false
        try {
            db.execSQL(qry)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        db.close()
        return result
    }

    fun updateSong(id: String, songTitle: String?): Boolean {
        val contentValues = ContentValues()
        contentValues.put(SongsCountry.Staticated.COL_NAME, songTitle)
        contentValues.put(SongsCountry.Staticated.COL_DATA, songTitle)
        try {

            db.update(
                SongsCountry.Staticated.TABLE_NAME,
                contentValues,
                "${SongsCountry.Staticated.COL_ID} = ?",
                arrayOf(id)
            )
            result = true
        } catch (e: Exception) {
            result = false
            e.printStackTrace()
        }
        return true
    }

}

