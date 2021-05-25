package iug.jumana.grace.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import iug.jumana.grace.R
import iug.jumana.grace.model.ArtistInfo
import kotlinx.android.synthetic.main.artist_item.view.*
import kotlinx.android.synthetic.main.my_layout.*

class ArtistInfoAdapter(var activity: Context, var data: MutableList<ArtistInfo>) :
    RecyclerView.Adapter<ArtistInfoAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val person=itemView.person
        val name = itemView.name
        val songs = itemView.songs
        val follow=itemView.follow

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.artist_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.name.text = data[position].name
        holder.songs.text = data[position].songs
        holder.person.setImageResource(data[position].image)

        holder.follow.setOnClickListener {
            val snackbar = Snackbar.make(it, "Maybe later!",
                Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }
}