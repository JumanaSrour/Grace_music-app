package iug.jumana.grace.fragments


import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import iug.jumana.grace.R
import iug.jumana.grace.database.DatabaseHelper
import iug.jumana.grace.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.lang.NullPointerException
import java.lang.RuntimeException


/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {
    val DatabaseHelper:DatabaseHelper?=null

//    lateinit var mBtn:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        loadLocate()// call loadLocate

//        val actionBar?= null
//        actionBar!!.title=resources.getString(R.string.app_name)
//        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        nav_view.selectedItemId =R.id.navigation_home
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)

//          mBtn.choose_language.setOnClickListener {
//               showChangeLanguage()
//          }
        root.button2.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.myMainContainer, Signup()).addToBackStack(null).commit()

        }

        root.button3.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.myMainContainer, Forgotpassword()).addToBackStack(null).commit()
        }

        root.button.setOnClickListener {
            if (root.editText.text.isNotEmpty() && root.editText2.text.isNotEmpty()) {
                activity!!.supportFragmentManager.beginTransaction().replace(
                    R.id.myMainContainer,
                    HomeFrag()
                ).commit()}else{
                Snackbar.make(myMainContainer,"No available data to show",Snackbar.LENGTH_SHORT).setAction("Action",null).show()
            }
            try {
                val checkEmailPass: User? = DatabaseHelper(activity!!).verification(root.editText.text.toString(),root.editText2.text.toString())

                if ((checkEmailPass)==null) {
                   Toast.makeText(activity,"No data",Toast.LENGTH_SHORT).show()
                } else {
                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.myMainContainer,HomeFrag()).commit()
                }
            } catch (e:Exception){
               throw RuntimeException()
            }
        }
        return root
    }

    override fun onStop() {
        super.onStop()
        activity!!.finish()
    }
}

//    private fun showChangeLanguage() {
//         val langs= arrayOf("Arabic","German","French","Italian","Russian","Turkish","English")
//         val builder=AlertDialog.Builder(activity)
//        builder.setTitle("|* Choose Langage *|")
//        builder.setCancelable(false)
//        builder.setSingleChoiceItems(langs, -1){ dialogInterface,i->
//            if(i==0){
//                setLocate("ar")
//                recreate(Activity())
//            } else if (i==1){
//                setLocate("de")
//                recreate(Activity())
//            } else if (i==2){
//                setLocate("fr")
//                recreate(Activity())
//            } else if (i==3){
//                setLocate("it")
//                recreate(Activity())
//            } else if (i==4){
//                setLocate("ru")
//                recreate(Activity())
//            } else if (i==5){
//                setLocate("tr")
//                recreate(Activity())
//            } else if (i==6) {
//                setLocate("en")
//                recreate(Activity())
//            }
//            dialogInterface.dismiss()
//
//        }
//        val mDialog=builder.create()
//        mDialog.show()
//    }
//
//    private fun setLocate(Lang: String) {
//        val locale=Locale(Lang)
//        Locale.setDefault(locale)
//        val config=Configuration()
//        config.locale=locale
//        activity!!.resources.updateConfiguration(config, activity!!.resources.displayMetrics)
//
//        val sharedpref=context!!.getSharedPreferences("My Pref", Context.MODE_PRIVATE)
//        val editor=sharedpref.edit()
//        editor.putString("Grace", Lang)
//        editor.apply()
//    }
//    private fun loadLocate(){
//        val sharedprefs=context!!.getSharedPreferences("My Pref", Activity.MODE_PRIVATE)
//        val language=sharedprefs.getString("Grace","")
//        if (language != null) {
//            setLocate(language)
//        }
//    }

//    private val mOnNavigationItemSelectedListener =
//        BottomNavigationView.OnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//
//                R.id.navigation_artists -> {
//                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.myContainer,
//                        Artists()).commit()
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_music -> {
//                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.myContainer,
//                        Music()).commit()
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_profile -> {
//                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.myContainer,
//                        Profile()).commit()
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_settings -> {
//                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.myContainer,
//                        Settings()).commit()
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.navigation_home->{
//                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.myContainer,
//                        HomeFrag()).commit()
//                    return@OnNavigationItemSelectedListener true
//                }
//            }
//            false



