

package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModel: LoginViewModel

    private var pb: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pb = findViewById(R.id.indeterminateBar)
        loadingEnd()

    }

    public fun onLogin(view: View) {
        try {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: java.lang.Exception) {
        }

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        var dis: Disposable? = null
        try {
            loginViewModel.log(" activity called viewModel");

            //Log.i(TAG,"User returned is: " + user.firstName + " " + user.lastName + " " + user.major + " " + user.isStudent)
            val observable = loginViewModel.login(email, password)
            observable.subscribe(object : SingleObserver<User> {

                override fun onSuccess(t: User) {
                    Log.i(TAG, "success in activity")
                    //Log.i(TAG, Session.user!!.firstName + Session.user!!.lastName)
                    loadingEnd()
                    loginNextActivity(t)
                    dis!!.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    Log.i(TAG, "started subscribe in activity")
                    dis = d
                    loadingStart()
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, e.localizedMessage)
                    loadingEnd()
                    makeToast("email or password incorrect")
                    dis!!.dispose()
                }
            })



        } catch (e: Exception) {
            //Log.i(TAG, "exception returned to onLogin in LoginActivity " + e.localizedMessage)
            val toast = Toast.makeText(
                this,
                "Unable to Login: " + e.localizedMessage,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    fun loadingStart() {
        pb!!.visibility = ProgressBar.VISIBLE
    }

    fun loadingEnd() {
        pb!!.visibility = ProgressBar.INVISIBLE
    }


    fun loginNextActivity(user: User) {

        if(user != null && user!!.isStudent!!) {
            startActivity(Intent(this, StudentHomeActivity::class.java))
            finish()
        } else if ( user != null) {
            startActivity(Intent(this, ManagerHomeActivity::class.java))
            finish()
        } else {
            throw Exception("User is null");
        }
    }

    fun makeToast(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.show()
    }


}

