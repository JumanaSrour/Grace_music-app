package iug.jumana.grace.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import iug.jumana.grace.R
import iug.jumana.grace.activities.Countrymusic
import iug.jumana.grace.database.DatabaseHelper
import iug.jumana.grace.fragments.Now_PlayingFragment
import iug.jumana.grace.fragments.TrendingFragment
import iug.jumana.grace.model.SongsCountry
import kotlinx.android.synthetic.main.lo_song_update.view.*
import org.w3c.dom.Text

class SongsCountryAdapter(_songDetails: ArrayList<SongsCountry>, _context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var songDetails = _songDetails
    var mContext = _context

    init {
        this.songDetails = _songDetails
        this.mContext = _context
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val songObject = songDetails.get(position)
        holder.itemView.findViewById<TextView>(R.id.trackTitle).text = songObject.songTitle
        holder.itemView.findViewById<TextView>(R.id.trackArtist).text = songObject.artist
        holder.itemView.findViewById<TextView>(R.id.trackArtist).setOnClickListener {
            val songPlayingFragment = Now_PlayingFragment()
            var args = Bundle()
            args.putString("songArtist", songObject.artist)
            args.putString("path", songObject.songData)
            args.putString("songTitle", songObject.songTitle)
            args.putInt("songId", songObject.songId.toInt())
            args.putInt("songPosition", position)
            args.putParcelableArrayList("songData", songDetails)
            songPlayingFragment.arguments = args
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .addToBackStack("Now_PlayingFragment")
                .replace(R.id.contentMain, Now_PlayingFragment()).commit()
        }
        holder.itemView.findViewById<ImageButton>(R.id.delete).setOnClickListener {
            val alert = AlertDialog.Builder(mContext)
            alert.setTitle("Warning")
            alert.setMessage("Are you sure you want to delete $songDetails")
            alert.setPositiveButton("Yes") { _, _ ->
                if (TrendingFragment().dbHandler.deleteSong(songObject.songId.toInt())) {
                    songDetails.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, songDetails.size)
                    Toast.makeText(mContext, "Song: $songDetails Deleted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(mContext, "Failed to delete", Toast.LENGTH_SHORT).show()

                }
            }
            alert.setNegativeButton("No") { _, _ ->
                alert.setIcon(R.drawable.ic_warning_black_24dp)
                alert.show()
            }
        }
        holder.itemView.findViewById<ImageButton>(R.id.edit).setOnClickListener {
            val infalter = LayoutInflater.from(mContext)
            val view = infalter.inflate(R.layout.lo_song_update, null)
            val txtSongName: TextView = view.findViewById(R.id.trackTitle)
            val txtSongLyrics: TextView = view.findViewById(R.id.trackArtist)
            txtSongName.text = songObject.songTitle.toString()
            txtSongLyrics.text = songObject.songData.toString()
            val builder = AlertDialog.Builder(mContext).setTitle("Update song title")
                .setView(view)
                .setPositiveButton("Update") { _, _ ->
                    val isUpdate = DatabaseHelper(Activity()).updateSong(
                        songObject.songId.toString(),
                        songObject.songTitle.toString()
                    )
                    view.trackTitle.text.toString()
                    view.trackArtist.text.toString()
                    if (isUpdate) {
                        songObject.songTitle = view.trackTitle.toString()
                        songObject.songData = view.trackArtist.toString()
                        notifyDataSetChanged()
                        Toast.makeText(mContext, "Updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mContext, "Failed to update", Toast.LENGTH_SHORT).show()
                    }
                }.setNegativeButton("Cancel") { _, _ ->

                }
            val alert = builder.create()
            alert.show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_custom_mainscreen_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if (songDetails.isNullOrEmpty()) {
            return 0
        } else {
            return (songDetails).size
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var trackTitle: TextView? = null
        var trackArtist: TextView? = null
        var contentHolder: RelativeLayout? = null
        var delete: ImageButton? = null

        init {
            trackTitle = view.findViewById<TextView>(R.id.trackTitle)
            trackArtist = view.findViewById<TextView>(R.id.trackArtist)
            contentHolder = view.findViewById<RelativeLayout>(R.id.contentRow)
            delete = view.findViewById(R.id.delete)
        }
    }


}