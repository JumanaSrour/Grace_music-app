package iug.jumana.grace.utils

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import iug.jumana.grace.R
import iug.jumana.grace.activities.MainActivity
import iug.jumana.grace.fragments.Now_PlayingFragment
import java.lang.Exception

class CaptureBroadcast :BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action==Intent.ACTION_NEW_OUTGOING_CALL){
            try {
                MainActivity.Statified.notificationManager?.cancel(2000)
            } catch (e:Exception){
                e.printStackTrace()
            }

            try {
                if (Now_PlayingFragment.Statified.mediaPLayer?.isPlaying as Boolean){
                    Now_PlayingFragment.Statified.mediaPLayer?.pause()
                    Now_PlayingFragment.Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
                }
            } catch (e:Exception){
                e.printStackTrace()
            }
        } else{
            val tm:TelephonyManager=p0?.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            when(tm?.callState){
                TelephonyManager.CALL_STATE_RINGING->{
                    try {
                        MainActivity.Statified.notificationManager?.cancel(2000)
                    } catch (e:Exception){
                        e.printStackTrace()
                    }
                    try {
                        if (Now_PlayingFragment.Statified.mediaPLayer?.isPlaying as Boolean){
                            Now_PlayingFragment.Statified.mediaPLayer?.pause()
                            Now_PlayingFragment.Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
                        }
                    } catch (e:Exception){
                        e.printStackTrace()
                    }
                } else->{

            }
            }
        }
    }
}