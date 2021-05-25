package iug.jumana.grace.activities

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iug.jumana.grace.R
import iug.jumana.grace.adapter.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_countrymusic.*

class Electronicdance : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_electronicdance)
        setSupportActionBar(toolbar)

        tabs.addTab(tabs.newTab().setText("Trending"))
        tabs.addTab(tabs.newTab().setText("Recently Added"))


        val sectionsPagerAdapter = MyPagerAdapter( this,supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        var trackNotificationBuilder: Notification?=null


    }
}
