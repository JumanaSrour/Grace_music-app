package iug.jumana.grace.fragments


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import iug.jumana.grace.R
import iug.jumana.grace.activities.Countrymusic
import iug.jumana.grace.database.DatabaseHelper
import iug.jumana.grace.model.SongsCountry
import kotlinx.android.synthetic.main.fragment_add_song.*
import kotlinx.android.synthetic.main.fragment_add_song.view.*
import kotlin.system.exitProcess

/**
 * A simple [Fragment] subclass.
 */
class AddSongFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_add_song, container, false)

        root.btnSave.setOnClickListener {
            if (root.nameAdd.text.isEmpty()&&root.lyricsAdd.text.isEmpty()){
                Toast.makeText(activity,"Please enter the required information",Toast.LENGTH_SHORT).show()
                root.nameAdd.requestFocus()
                root.lyricsAdd.requestFocus()
            } else{
              DatabaseHelper(Activity()).addSong(context!!,SongsCountry as SongsCountry)
             }
        }
        root.btnCancel.setOnClickListener {
            clearEdits()
            // i guess there must have been a finish function ikr?
        }


        return root
    }

      fun clearEdits(){
        nameAdd.setText("")
          lyricsAdd.setText("")
      }
}
