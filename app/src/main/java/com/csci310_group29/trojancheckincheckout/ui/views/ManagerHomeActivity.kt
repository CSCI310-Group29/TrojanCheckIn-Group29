package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.ManagerHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

private const val TAG = "ManagerHomeActivity"


@AndroidEntryPoint
class ManagerHomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ManagerHomeViewModel

    fun onGetBuildings(view: View) {
        startActivity(Intent(this, BuildingInfoActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var building: ArrayList<Building>
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_home)

    }

    fun onSearch(view: View) {
        startActivity(Intent(this, VisitQueryActivity::class.java))
    }


    fun onChangeCapacities(view: View) {
        startActivity(Intent(this, ManagerUpdateCapacityActivity::class.java))
    }

    fun onViewProfile(view: View) {
        startActivity(Intent(this, ManagerProfileActivity::class.java))
    }

    fun onLogout(view: View)  {
        val observable = viewModel.logout()
        observable.subscribe(object: CompletableObserver {
            override fun onComplete() {
                startActivity(Intent(this@ManagerHomeActivity, AppHomeActivity::class.java))
                finishAffinity()
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                val toast = Toast.makeText(this@ManagerHomeActivity, "Unable to logout. Try again",Toast.LENGTH_SHORT)
                toast.show()
            }
        })

    }


}