
package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.Session
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.StudentProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_manager_student_profile.*
import kotlinx.android.synthetic.main.activity_student_profile.*
import javax.inject.Inject


@AndroidEntryPoint
class StudentProfileActivity : AppCompatActivity() {

    private val SELECT_PHOTO = 1

    @Inject
    lateinit var viewModel: StudentProfileViewModel

    private val TAG = "StudentProfileActivity"
    lateinit var pb: ProgressBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        pb = findViewById(R.id.indeterminateBar)
        loadingStart()
        observeViewModel()

    }

    private fun observeViewModel() {
        val userObserver = Observer<User> { newUser ->
            Log.i(TAG, "in profile view")
            FirstName.text = newUser.firstName
            LastName.text = newUser.lastName
            Major.text = newUser.major
            StudentId.text = newUser.studentId
            SProfilePic.setImageBitmap(toBitmap(newUser.profilePicture))
            if (newUser.checkedInBuilding != null) {
                profileCurrBuilding.text = newUser.checkedInBuilding!!.buildingName
            } else {
                profileCurrBuilding.text = "Not Checked In"
            }
            loadingEnd()

        }

        viewModel.currUser.observe(this, userObserver)
    }

    fun onDelete(view: View) {
        val yn = arrayOf("Yes", "No")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to delete your account?")
        builder.setItems(yn) { dialog, which ->
            when(which) {
                0 -> {
                    val observable = viewModel.deleteAccount()
                    observable.subscribe(
                        object : CompletableObserver {
                            override fun onComplete() {
                                goToStarter();
                            }

                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onError(e: Throwable) {
                                if(viewModel.currUser!!.value!!.checkedInBuilding != null) {
                                    makeToast("Must check out before deleting your account")
                                } else {
                                    makeToast("Unable to delete account")
                                }
                            }
                        }

                    )

                }
                1 -> {
                    dialog!!.cancel()
                }
            }
        }
        builder.show()



    }



    fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun goToStarter() {
        startActivity(Intent(this, AppHomeActivity::class.java))
        finishAffinity()
    }

    fun onViewHistory(view: View) {
        val i = Intent(this, StudentHistoryActivity::class.java)
        i.putExtra("studentUID", Session.uid)
//        startActivity(Intent(this, StudentHistoryActivity::class.java))
        startActivity(i)
    }

    fun onUpdateProfilePic(view: View) {
        Log.i(TAG, "In onUpdateProfilePic")
        val yn = arrayOf("Gallery", "Link")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")

        builder.setItems(yn) { dialog, which ->
            when (which) {
                0 -> {
                    // Gallery
                    Log.i(TAG, "Chose GALLERY")
                    try {
                        val i = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        Log.i(TAG, " image pick activity start")
                        startActivityForResult(i, SELECT_PHOTO)
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                1 -> {
                    // Link
                    Log.i(TAG, "Chose LINK")
                    //initialize variables for handling user input
                    var link: String

                    // Box user will type link into
                    val editTextName1 = EditText(this)

                    val builderLink = AlertDialog.Builder(this)
                    // Set up user input box
                    builderLink.setTitle("Choose Image Link")
                    builderLink.setView(editTextName1)


                    builderLink.setPositiveButton("OK") { dialog, which ->
                        link = editTextName1.getText().toString()
                        Log.i(TAG, "Chose LINK: " + link)

                        try {
                            val observable = viewModel.updateProfilePicWithLink(link)
                            observable.subscribe(object: SingleObserver<User> {
                                override fun onSuccess(t: User) {
                                }

                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onError(e: Throwable) {
                                    makeToast("Invalid link. Unable to update profile picture")
                                }
                            })
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    builderLink.setNegativeButton("Cancel") { dialog, which ->
                        dialog.cancel()
                        Log.i(TAG, "LINK canceled")
                    }

                    builderLink.show()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "image pick activity returned")
        when(requestCode) {
            SELECT_PHOTO -> {
                if (resultCode == RESULT_OK) {
                    val uri = data!!.data!!
                    val stream = applicationContext.contentResolver.openInputStream(data!!.data!!)
                    val bitmap = BitmapFactory.decodeStream(stream)
                    loadingStart()
                    viewModel.updateProfilePic(bitmap)
                } else {
                    Toast.makeText(this, "Unable to update profile picture", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun loadingStart() {
        pb!!.visibility = ProgressBar.VISIBLE
    }

    fun loadingEnd() {
        pb!!.visibility = ProgressBar.INVISIBLE
    }

    private fun toBitmap(bArray: ByteArray?): Bitmap? {
        if(bArray == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArray, 0, bArray.size)
    }

}
