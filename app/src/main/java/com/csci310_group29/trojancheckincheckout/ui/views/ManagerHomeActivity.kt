package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.ui.util.EspressoIdlingResource
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_manager_home.*
import kotlinx.android.synthetic.main.activity_student_home.*
import javax.inject.Inject

private const val TAG = "ManagerHomeActivity"


@AndroidEntryPoint
class ManagerHomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ManagerHomeViewModel

    private val disposables = CompositeDisposable()

    fun onGetBuildings(view: View) {
        startActivity(Intent(this, BuildingInfoActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var building: ArrayList<Building>
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_home)

        observeViewModel()
    }

    private fun observeViewModel() {
        val userObserver = Observer<User> { newUser ->
            Log.i(TAG, "in Manager Home")
            NameManager.text = newUser.firstName

        }

        viewModel.currUser.observe(this, userObserver)
    }

    fun onSearch(view: View) {
        startActivity(Intent(this, StudentQueryActivity::class.java))
    }


    fun onChangeCapacities(view: View) {
        startActivity(Intent(this, ManagerUpdateCapacityActivity::class.java))
    }

    fun onViewProfile(view: View) {
        startActivity(Intent(this, ManagerProfileActivity::class.java))
    }

    fun onLogout(view: View)  {
        EspressoIdlingResource.increment()
        Log.d(TAG, "trying to log out")
        val observable = viewModel.logout()
//        val disposable = observable.subscribe(object: CompletableObserver {
//            override fun onComplete() {
//                EspressoIdlingResource.decrement()
//
//                startActivity(Intent(this@ManagerHomeActivity, AppHomeActivity::class.java))
//                finishAffinity()
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onError(e: Throwable) {
//                Log.d(TAG, "error: ${e.localizedMessage}")
//                EspressoIdlingResource.decrement()
//
//                val toast = Toast.makeText(this@ManagerHomeActivity, "Unable to logout. Try again",Toast.LENGTH_SHORT)
//                toast.show()
//            }
//        })
        disposables.add(observable.subscribe({
            EspressoIdlingResource.decrement()
            startActivity(Intent(this@ManagerHomeActivity, AppHomeActivity::class.java))
            finishAffinity()
        }, { e ->
            Log.d(TAG, "error: ${e.localizedMessage}")
                EspressoIdlingResource.decrement()
                val toast = Toast.makeText(this@ManagerHomeActivity, "Unable to logout. Try again",Toast.LENGTH_SHORT)
                toast.show()
        }))

    }

}