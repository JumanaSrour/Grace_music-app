package iug.jumana.grace.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import iug.jumana.grace.R
import iug.jumana.grace.fragments.Now_PlayingFragment
import iug.jumana.grace.model.SongsCountry

class FavoriteAdapter(_songDetails:ArrayList<SongsCountry>, _context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var songDetails = _songDetails
    var mContext = _context

    init {
        this.songDetails = _songDetails
        this.mContext = _context
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val songObject = songDetails?.get(position)
        holder.itemView.findViewById<TextView>(R.id.trackTitle).text = songObject?.songTitle
        holder.itemView.findViewById<TextView>(R.id.trackArtist).text = songObject?.artist
        holder.itemView.findViewById<TextView>(R.id.trackArtist).setOnClickListener {
            val songPlayingFragment = Now_PlayingFragment()
            var args = Bundle()
            args.putString("songArtist", songObject?.artist)
            args.putString("path", songObject?.songData)
            args.putString("songTitle", songObject?.songTitle)
            args.putInt("songId", songObject?.songId?.toInt())
            args.putInt("songPosition", position)
            args.putParcelableArrayList("songData", songDetails)
            songPlayingFragment.arguments = args
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction().addToBackStack("Now_PlayingFragmentFavorite").replace(
                R.id.contentMain,
                Now_PlayingFragment()
            ).commit()
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.row_custom_mainscreen_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if (songDetails == null) {
            return 0
        } else {
            return (songDetails as ArrayList<SongsCountry>).size
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var trackTitle: TextView? = null
        var trackArtist: TextView? = null
        var contentHolder: RelativeLayout? = null

        init {
            trackTitle = view.findViewById<TextView>(R.id.trackTitle)
            trackArtist = view.findViewById<TextView>(R.id.trackArtist)
            contentHolder = view.findViewById<RelativeLayout>(R.id.contentRow)
        }
    }



}