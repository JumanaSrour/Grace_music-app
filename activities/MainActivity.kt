package iug.jumana.grace.activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iug.jumana.grace.R
import iug.jumana.grace.fragments.Login
import iug.jumana.grace.fragments.Now_PlayingFragment
import iug.jumana.grace.fragments.Signup
import java.lang.Exception
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {

           object Statified{
               var notificationManager:NotificationManager?=null
           }
           var trackNotificationBuilder:Notification?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerPref=getSharedPreferences("registerPref", Context.MODE_PRIVATE)
        val isUser=registerPref.getBoolean("isUser",false)
        if (isUser){
            supportFragmentManager.beginTransaction().replace(R.id.myMainContainer, Login()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.myMainContainer, Signup()).commit()
        }

        val intent=Intent(this,MainActivity::class.java)
        val pIntent=PendingIntent.getActivity(this,System.currentTimeMillis().toInt(),intent, 0)
        trackNotificationBuilder=Notification.Builder(this)
            .setContentTitle("A track is playing in background")
            .setSmallIcon(R.drawable.logofrags)
            .setContentIntent(pIntent)
            .setOngoing(true)
            .setAutoCancel(true)
            .build()
        Statified.notificationManager=getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager

    }

    }



  