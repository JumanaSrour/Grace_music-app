package iug.jumana.grace.fragments


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat

import iug.jumana.grace.R
import iug.jumana.grace.database.DatabaseHelper
import iug.jumana.grace.model.User
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.person
import kotlinx.android.synthetic.main.fragment_profile.editgmail as editgmail1
import kotlinx.android.synthetic.main.fragment_profile.editname as editname1

/**
 * A simple [Fragment] subclass.
 */
class EditProFragment : Fragment() {
    var imageURI:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment_edit, container, false)


    root.editperson.setOnClickListener {
        val popupmenu = PopupMenu(activity, person)
        popupmenu.menuInflater.inflate(R.menu.popup_menu, popupmenu.menu)
        popupmenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.camera -> {
                    val media = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(media, 808)
                    true
                }
                else -> {
                    checkPermissionForImage()
                    pickImageFromGallery()
                    true
                }

            }
/*
else{
R.id.gallery-> }
*/
        }
        popupmenu.show()

    }


//        root.nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        root.nav_view.selectedItemId = R.id.navigation_profile
    return root
}




private fun checkPermissionForImage() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if ((ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED)
            && (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED)
        ) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            requestPermissions(
                permission,
                808
            ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
            requestPermissions(
                permissionCoarse,
                808
            ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
        } else {
            pickImageFromGallery()
        }
    }
}

private fun pickImageFromGallery() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, 606) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
}
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 606) {
        val bmb = data!!.extras!!.get("data") as Bitmap
        if (person.isClickable) {
            person.setImageBitmap(bmb)
        }

    }
}
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            (R.layout.fragment_edit)


            val db = DatabaseHelper(activity!!)

            person.setOnClickListener {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, 100)
            }


            saveprof.setOnClickListener {

                if (editgmail.text.isNotEmpty() && editname.text.isNotEmpty()) {
                    if (db.insertUser(
                            editgmail.text.toString(),
                            editname.text.toString(),
                            User.COL_NAME,
                            User.COL_ID.toInt(),
                            imageURI
                        )
                    ) {
                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show()
                        editname.text=""
                        editgmail.text=""

                    } else {
                        Toast.makeText(context, "Add Failed", Toast.LENGTH_SHORT).show()

                    }
                }else{
                    Toast.makeText(context, "Fill Fields", Toast.LENGTH_SHORT).show()
                }
            }


        }




}
