package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    val TAG = "SplashScreenActivity"

    @Inject
    lateinit var viewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val observable = viewModel.isLoggedIn()

        observable.subscribe(object: SingleObserver<User> {
            override fun onSuccess(t: User) {
                Log.i(TAG,"user logged in: $t")
                loggedInNext(t)
            }

            override fun onSubscribe(d: Disposable) {
                Log.i(TAG, "start splash")
            }

            override fun onError(e: Throwable) {
                Log.i(TAG, "user not logged in")
                startHome()
            }
        })
    }

    fun startHome() {
        startActivity(Intent(this,AppHomeActivity::class.java))
    }

    fun loggedInNext(user: User) {
        if(user!!.isStudent!!) {
            startActivity(Intent(this, StudentHomeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, ManagerHomeActivity::class.java))
            finish()
        }
    }
}