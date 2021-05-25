package iug.jumana.grace.fragments
// here you need to solve the problem of bottom navigation menu appearance

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

import iug.jumana.grace.activities.*
import kotlinx.android.synthetic.main.my_layout.*
import kotlinx.android.synthetic.main.my_layout.view.*
import java.lang.Exception
import java.util.*
import iug.jumana.grace.R


/**
 * A simple [Fragment] subclass.
 */
class HomeFrag : Fragment(),View.OnClickListener {
    private val REQUEST_CODE_SPEECH_INPUT=100

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when(p0.id){
                 iug.jumana.grace.R.id.electro->Intent(activity, Electromusic::class.java)
                iug.jumana.grace.R.id.electronicdance-> Intent(activity, Electronicdance::class.java)
                iug.jumana.grace.R.id.country-> Intent(activity, Countrymusic::class.java)
                iug.jumana.grace.R.id.dubstep-> Intent(activity, Dubstepmusic::class.java)
                iug.jumana.grace.R.id.indiepop-> Intent(activity, Indiepop::class.java)
            }
        }
     }


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(iug.jumana.grace.R.layout.my_layout, container, false)


        root.electro.setOnClickListener{
             val i=Intent(activity, Electromusic::class.java)
             startActivity(i)
         }
          root.electronicdance.setOnClickListener{
              val i=Intent(activity, Electronicdance::class.java)
              startActivity(i)
          }
        root.indiepop.setOnClickListener{
            val i=Intent(activity, Indiepop::class.java)
            startActivity(i)
        }
        root.country.setOnClickListener{
            val i=Intent(activity, Countrymusic::class.java)
            startActivity(i)
        }
        root.dubstep.setOnClickListener{
            val i=Intent(activity, Dubstepmusic::class.java)
            startActivity(i)
        }
        root.voiceBtn.setOnClickListener {
            speak()
        }


        root.nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        root.nav_view.selectedItemId = R.id.navigation_home


        return root
    }

    private fun speak() {
        val mIntent= Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak what you need")

        try {
            // if there's no error show speaking dialog
            startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
        }
        catch (e:Exception){
            // if there's an error get error message and show in snackbar
            val dia=AlertDialog.Builder(activity)
            dia.setTitle("Search result")
            dia.setIcon(iug.jumana.grace.R.drawable.ic_mic_black_24dp)
            dia.setMessage("Sorry, I can't help you with that")
            dia.setCancelable(false)
            dia.setPositiveButton("OK"){dialogInterface, i ->
                     val snackbar = Snackbar.make(myCont, "Maybe later!",
                        Snackbar.LENGTH_LONG)
                    snackbar.show()

            }
            dia.setNegativeButton("Try again"){dialogInterface,i->
                speak()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE_SPEECH_INPUT->{
                if (resultCode==Activity.RESULT_OK&& null!=data){
                    // get text from result
                    val result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // set the text to textview
                }
            }
        }
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
