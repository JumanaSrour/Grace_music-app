package iug.jumana.grace.fragments


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView

import iug.jumana.grace.R
import iug.jumana.grace.adapter.ArtistInfoAdapter
import iug.jumana.grace.model.ArtistInfo
import kotlinx.android.synthetic.main.fragment_artists.*
import kotlinx.android.synthetic.main.my_layout.view.*

/**
 * A simple [Fragment] subclass.
 */
class Artists : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_artists, container, false)

        val data= mutableListOf<ArtistInfo>()
        data.add(ArtistInfo(1,"Jim","45",R.drawable.person))
        data.add(ArtistInfo(2,"Jim","45",R.drawable.person))
        data.add(ArtistInfo(3,"Jim","45",R.drawable.person))
        data.add(ArtistInfo(4,"Jim","45",R.drawable.person))
        data.add(ArtistInfo(5,"Jim","45",R.drawable.person))
        data.add(ArtistInfo(6,"Jim","45",R.drawable.person))

        rvArtists.layoutManager = LinearLayoutManager(activity)

        val artsAdapter = ArtistInfoAdapter(Activity(),data)
        rvArtists.adapter=artsAdapter

        root.nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        root.nav_view.selectedItemId = R.id.navigation_artists
        return root
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

}
