package iug.jumana.grace.fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

import iug.jumana.grace.R
import iug.jumana.grace.adapter.FavoriteAdapter
import iug.jumana.grace.database.DatabaseHelper
import iug.jumana.grace.model.SongsCountry
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_now__playing.view.*
import kotlinx.android.synthetic.main.fragment_trending.view.*
import kotlinx.android.synthetic.main.my_layout.view.*
import kotlinx.android.synthetic.main.my_layout.view.nav_view
import kotlinx.android.synthetic.main.row_custom_mainscreen_adapter.*
import java.lang.Exception
import java.util.zip.DataFormatException

/**
 * A simple [Fragment] subclass.
 */
class Favorite : Fragment() {
      var myActivity:Activity?=null
    var noFavorites:TextView?=null
    var nowPlayingBottomBar:RelativeLayout?=null
    var playPauseButton:ImageButton?=null
    var songTitle:TextView?=null
    var recyclerView:RecyclerView?=null
    var trackPosition:Int=0
    var favoriteContent:DatabaseHelper?=null
    var refreshList:ArrayList<SongsCountry>?=null
            var getListFromDatabase:ArrayList<SongsCountry>?=null
    object Statified{
        var mediaPlayer:MediaPlayer?=null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_favorite, container, false)
        activity!!.title="Favorite"
        root.nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        root.nav_view.selectedItemId = R.id.navigation_music

         noFavorites=root?.noFavorites
        nowPlayingBottomBar=root.hiddenBarFavScreen
        songTitle=root.findViewById(R.id.songTitleFav)
        playPauseButton=root.findViewById(R.id.playPauseButton)
        recyclerView=root.favoriteRecycler
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity=context as Activity
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        myActivity=activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteContent= DatabaseHelper(myActivity!!)
        displayFavoritesBySearching()
        BottomBarSetup()


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item=menu?.findItem(R.id.actionsort)
        item?.isVisible=false
    }
    fun getSongsFromPhone():ArrayList<SongsCountry>{
        var arrayList=ArrayList<SongsCountry>()
        var contentResolver=myActivity?.contentResolver
        var songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor=contentResolver?.query(songUri,null,null,null,null)
        if (songCursor!=null&&songCursor.moveToFirst()){
            val songId=songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle= songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist=songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData=songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex=songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songCursor.moveToNext()){
                var currentId=songCursor.getLong(songId)
                var currentTitle=songCursor.getString(songTitle)
                var currentArtist=songCursor.getString(songArtist)
                var currentData=songCursor.getString(songData)
                var currentIndex=songCursor.getLong(dateIndex)

                arrayList.add(SongsCountry(currentId,currentTitle,currentArtist,currentData,currentIndex))

            }
        }
        return arrayList
    }
    val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                iug.jumana.grace.R.id.navigation_settings -> {
                    childFragmentManager.beginTransaction().replace(
                        iug.jumana.grace.R.id.myCont,
                        Settings()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }
                iug.jumana.grace.R.id.navigation_music -> {
                    activity!!.supportFragmentManager.beginTransaction().replace(
                        iug.jumana.grace.R.id.myCont,
                        Favorite()
                    ).addToBackStack(null).commit()
                    return@OnNavigationItemSelectedListener true
                }
                iug.jumana.grace.R.id.navigation_home -> {
                    childFragmentManager.beginTransaction().replace(
                        iug.jumana.grace.R.id.myCont,
                        HomeFrag()
                    ).commit()
                    return@OnNavigationItemSelectedListener true
                }

                iug.jumana.grace.R.id.navigation_profile -> {
                    activity!!.supportFragmentManager.beginTransaction().replace(
                        iug.jumana.grace.R.id.myCont,
                        Profile()
                    ).addToBackStack(null).commit()
                    return@OnNavigationItemSelectedListener true
                }
                iug.jumana.grace.R.id.navigation_artists -> {
                    activity!!.supportFragmentManager.beginTransaction().replace(
                        iug.jumana.grace.R.id.myCont,
                        Artists()
                    ).addToBackStack(null).commit()
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }
    fun BottomBarSetup(){
        try {
            bottomBarClickHandler()
            songTitle?.setText(Now_PlayingFragment.Statified.currentSongHelper?.songTitle)
            Now_PlayingFragment.Statified.mediaPLayer?.setOnCompletionListener {
                songTitle?.setText(Now_PlayingFragment.Statified.currentSongHelper?.songTitle)
                Now_PlayingFragment.Staticated.onSongComplete()
            }
            if (Now_PlayingFragment.Statified.mediaPLayer?.isPlaying as Boolean){
                nowPlayingBottomBar?.visibility=View.VISIBLE
            } else{
                nowPlayingBottomBar?.visibility=View.INVISIBLE

            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun bottomBarClickHandler(){
        nowPlayingBottomBar?.setOnClickListener {
            Statified.mediaPlayer=Now_PlayingFragment.Statified.mediaPLayer
            val songPlayingFragment = Now_PlayingFragment()
            var args = Bundle()
            args.putString("songArtist", Now_PlayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("path", Now_PlayingFragment.Statified.currentSongHelper?.songPath)
            args.putString("songTitle", Now_PlayingFragment.Statified.currentSongHelper?.songTitle)
            args.putInt("songId", Now_PlayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition", Now_PlayingFragment.Statified.currentSongHelper?.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData", Now_PlayingFragment.Statified.fetchSongs)
            args.putString("FavBottomBar","success")
            songPlayingFragment.arguments = args
            fragmentManager!!.beginTransaction().replace(R.id.detailsFrag,Now_PlayingFragment()).addToBackStack("Now_PlayingFragment").commit()
        }
        playPauseButton?.setOnClickListener {
            if (Now_PlayingFragment.Statified.mediaPLayer?.pause() as Boolean){
                Now_PlayingFragment.Statified.mediaPLayer?.pause()
                trackPosition=Now_PlayingFragment.Statified.mediaPLayer?.currentPosition as Int
                Now_PlayingFragment.Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
            } else{
                Now_PlayingFragment.Statified.mediaPLayer?.seekTo(trackPosition)
                Now_PlayingFragment.Statified.mediaPLayer?.start()
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        }
    }
    fun displayFavoritesBySearching(){
        if (favoriteContent?.checkSize()as Int >0){
          refreshList= ArrayList<SongsCountry>()
            getListFromDatabase=favoriteContent?.queryDBList()
            var fetchListFromDevice=getSongsFromPhone()
            if (fetchListFromDevice!=null){
                for (i in 0..fetchListFromDevice?.size-1){
                    for (j in 0..getListFromDatabase?.size as Int -1 ){
                        if ((getListFromDatabase?.get(j)?.songId)===(fetchListFromDevice?.get(i)?.songId)){
                            refreshList?.add((getListFromDatabase as ArrayList<SongsCountry>)[j])
                        }
                    }
                }
            } else{

            }
            if (refreshList==null){
                recyclerView?.visibility=View.INVISIBLE
                noFavorites?.visibility=View.VISIBLE
            } else{
                var favoriteAdapter=FavoriteAdapter(refreshList as ArrayList<SongsCountry>,myActivity as Context)
                val mLayoutManager=LinearLayoutManager(activity)
                recyclerView?.layoutManager=mLayoutManager
                recyclerView?.itemAnimator=DefaultItemAnimator()
                recyclerView?.adapter=favoriteAdapter
                recyclerView?.setHasFixedSize(true)
            }
        } else{
            recyclerView?.visibility=View.INVISIBLE
            noFavorites?.visibility=View.VISIBLE
        }

    }
}
