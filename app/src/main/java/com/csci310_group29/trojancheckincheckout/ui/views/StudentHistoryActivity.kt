
package com.csci310_group29.trojancheckincheckout.ui.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.query.VisitQuery
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.Session
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.VisitHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class StudentHistoryActivity : AppCompatActivity() {

    val TAG = "StudentHistoryActivity"

    @Inject
    lateinit var visitDomain: VisitUseCases

    var pb: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_history)

        val rv = findViewById<View>(R.id.visitHistory) as RecyclerView
        pb = findViewById<ProgressBar>(R.id.indeterminateBarHistory)

        Log.i(TAG, "in student history")
        Log.i(TAG, Session.uid)

        val visits: List<Visit>
        try {
            //val uQuery = UserQuery(Session.user!!.firstName,Session.user!!.lastName, Session.user!!.major,
                //Session.user!!.studentId, (Session.user!!.checkedInBuilding == null), Session.user!!.isStudent)

            val vQuery = VisitQuery(null,null,null,null)
            Log.i(TAG, Session.uid)
            Log.i(TAG, "RECEIVED student ID: " + intent.getStringExtra("studentUID").toString())
            val observable = visitDomain.getUserVisitHistory(intent.getStringExtra("studentUID").toString(), vQuery)
            observable.subscribe(object : SingleObserver<List<Visit>> {
                override fun onSuccess(t: List<Visit>) {
                    Log.i(TAG, "success: ${Session.uid}")
                    Log.i(TAG,"success size: ${t.size}")
                    val visits = t;
                    val adapter =
                        VisitHistoryAdapter(
                            visits
                        )
                    rv.adapter = adapter
                    loadingEnd()
                }

                override fun onSubscribe(d: Disposable) {
                    loadingStart()
                    Log.i(TAG, "subscribed visit history")

                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, Session.uid)
                    throw Exception("unable to get visit history")
                }
            })
        } catch(e: Exception) {
            val toast = Toast.makeText(this, "unable to get visit history", Toast.LENGTH_SHORT)
            toast.show()
        }

        rv.layoutManager = LinearLayoutManager(this)


    }

    fun loadingStart() {
        pb!!.visibility = ProgressBar.VISIBLE
    }

    fun loadingEnd() {
        pb!!.visibility = ProgressBar.INVISIBLE
    }

}
