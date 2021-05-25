package iug.jumana.grace.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import iug.jumana.grace.model.SongsCountry
import java.lang.Exception

class GraceDatabase:SQLiteOpenHelper {
    companion object{
        fun addSong(context: Context?, songsCountry: SongsCountry.CREATOR) {

        }

        val DB_NAME="FavoriteDatabase"
        val TABLE_NAME="FavoriteTable"
        val COL_ID="songId".toLong()
        val COL_SONG_TITLE="songTitle"
        val COL_SONG_ARTIST="songArtist"
        val COL_SONG_PATH="songPath"
        val COL_DATE="songDate".toLong()
        var _songList=ArrayList<SongsCountry>()


    }
    object Staticated{
        var DB_VERSION=1
        val DB_NAME="FavoriteDatabase"
    }


    constructor(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
    ) : super(context, name, factory, version)
    constructor(
        context: Context?
    ) : super(context, Staticated.DB_NAME,null, Staticated.DB_VERSION)

    override fun onCreate(sqliteDatabase: SQLiteDatabase?) {
        sqliteDatabase?.execSQL("CREATE_TABLE "+ TABLE_NAME+"( "+ COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL_SONG_ARTIST+ " STRING,"+ COL_SONG_TITLE+" STRING,"+ COL_SONG_PATH+
                " STRING);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    fun storeAsFavorite(id:Int?,artist:String?,songTitle:String?,path:String?){
        val db=this.writableDatabase
        var contentValues=ContentValues()
        contentValues.put(COL_ID.toString(),id)
        contentValues.put(COL_SONG_ARTIST,artist)
        contentValues.put(COL_SONG_TITLE,songTitle)
        contentValues.put(COL_SONG_PATH,path)
        db.insert(TABLE_NAME,null,contentValues)
        db.close()
    }

    fun queryDBList():ArrayList<SongsCountry>?{
        try {
            val db=this.readableDatabase
            val query_params="SELECT * FROM "+ TABLE_NAME
            var cSor=db.rawQuery(query_params, null)
            if (cSor.moveToFirst()){
                do {
                    var _id=cSor.getInt(cSor.getColumnIndexOrThrow(COL_ID.toString()))
                    var _artist=cSor.getString(cSor.getColumnIndexOrThrow(COL_SONG_ARTIST))
                    var _title=cSor.getString(cSor.getColumnIndexOrThrow(COL_SONG_TITLE))
                    var _songPath=cSor.getString(cSor.getColumnIndexOrThrow(COL_SONG_PATH))
                    _songList.add(SongsCountry(_id.toLong(),_title,_artist,_songPath,0))
                } while (cSor.moveToNext())
            } else{
                return null
            }
        } catch (e:Exception){
            e.printStackTrace()
        }

       return _songList
    }
    fun checkIfIdExists(_id:Int):Boolean{
        var storeId=-1090
        val db=this.readableDatabase
        val query_params=" SELECT * FROM "+ TABLE_NAME+" WHERE SongId = '$_id'"
        val cSor=db.rawQuery(query_params, null)
        if (cSor.moveToFirst()){
            do {
                storeId=cSor.getInt(cSor.getColumnIndexOrThrow(COL_ID.toString()))
            } while (cSor.moveToNext())
        } else{
            return false
        }
        return storeId!=-1090
    }

    fun deleteFavorite(_id: Int){
        val db=this.writableDatabase
        db.delete(TABLE_NAME, COL_ID.toString()+"="+_id,null)
        db.close()
    }
    fun checkSize():Int{
        var counter=0
        var db=this.readableDatabase
        var query_params=" SELECT * FROM "+ TABLE_NAME
        val cSor=db.rawQuery(query_params,null)
        if (cSor.moveToFirst()){
            do {
                counter=counter+1
            } while (cSor.moveToNext())
        } else{
            return 0
        }
        return counter
    }
     fun addSong(context: Context,songsCountry: SongsCountry){
         val contentValues=ContentValues()
         contentValues.put(COL_SONG_TITLE,songsCountry.songTitle)
         contentValues.put(COL_SONG_PATH,songsCountry.songData)
         val db=this.writableDatabase
         try {
             db.insert(TABLE_NAME,null,contentValues)
             Toast.makeText(context,"Song Added",Toast.LENGTH_SHORT).show()
         } catch (e:Exception){
             Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()

             e.printStackTrace()
         }
         db.close()
     }
    fun deleteSong(songId:Int):Boolean{
        val qry="DELETE FROM $TABLE_NAME WHERE $COL_ID =$songId"
        val db=this.writableDatabase
        var result:Boolean=false
        try {
            val cSor=db.execSQL(qry)
            result=true
        }catch (e:Exception){
            e.printStackTrace()
        }
        db.close()
        return result
    }
    fun updateSong(id:String, songTitle: String?,path: String?):Boolean{
        val db=this.writableDatabase
        var result:Boolean=false
        val contentValues=ContentValues()
        contentValues.put(COL_SONG_TITLE,songTitle)
        contentValues.put(COL_SONG_PATH, songTitle)
        try {
            db.update(TABLE_NAME,contentValues,"$COL_ID = ?", arrayOf(id))
            result=true
        } catch (e:Exception){
            result=false
            e.printStackTrace()
        }
        return true
    }
}