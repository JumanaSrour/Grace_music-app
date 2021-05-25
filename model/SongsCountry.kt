package iug.jumana.grace.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
data class SongsCountry(var songId:Long, var songTitle:String?, var artist:String?, var songData:String?, var dateAdded:Long):Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    object Staticated{
        val COL_ID = "id"
        val COL_NAME = "title"
        val COL_ARTIST = "artist"
        val COL_DATA="data"
        val COL_DATE="date"

        val TABLE_NAME = "SongsCountry"
        val TABLE_CREATE = "create table $TABLE_NAME ($COL_ID integer primary key autoincrement," +
                "$COL_NAME text not null, $COL_ARTIST text not null, $COL_DATA text" +
                " not null, $COL_DATE Double)"
    }
    object Statified{
        var nameComparator:Comparator<SongsCountry> = java.util.Comparator { song1, song2 ->
            val songOne=song1.songTitle!!.toUpperCase()
            val songTwo=song2.songTitle!!.toUpperCase()
            songOne.compareTo(songTwo)
        }
        var dateComparator:Comparator<SongsCountry> = java.util.Comparator { song1, song2 ->
            val songOne=song1.dateAdded.toDouble()
            val songTwo=song2.dateAdded.toDouble()
            songOne.compareTo(songTwo)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(songId)
        parcel.writeString(songTitle)
        parcel.writeString(artist)
        parcel.writeString(songData)
        parcel.writeLong(dateAdded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongsCountry> {
        override fun createFromParcel(parcel: Parcel): SongsCountry {
            return SongsCountry(parcel)
        }

        override fun newArray(size: Int): Array<SongsCountry?> {
            return arrayOfNulls(size)
        }
    }

}