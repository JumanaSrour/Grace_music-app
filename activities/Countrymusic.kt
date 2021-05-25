package iug.jumana.grace.activities

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import iug.jumana.grace.R
import iug.jumana.grace.adapter.MyPagerAdapter
import iug.jumana.grace.fragments.TrendingFragment
import kotlinx.android.synthetic.main.activity_countrymusic.*
import kotlinx.android.synthetic.main.activity_countrymusic.toolbar

class Countrymusic : AppCompatActivity() {
    object Statified{
        var notificationManager:NotificationManager?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countrymusic)

        setSupportActionBar(toolbar)

        tabs.addTab(tabs.newTab().setText("Trending"))
        tabs.addTab(tabs.newTab().setText("Recently Added"))


        val sectionsPagerAdapter = MyPagerAdapter( this,supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        var trackNotificationBuilder:Notification?=null



    }


}
