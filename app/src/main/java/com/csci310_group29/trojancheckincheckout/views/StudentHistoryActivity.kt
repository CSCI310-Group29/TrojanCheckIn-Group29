
package com.csci310_group29.trojancheckincheckout.views
/*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.data.fake.VisitFakeDataSource
import com.csci310_group29.trojancheckincheckout.data.models.Visit
import com.csci310_group29.trojancheckincheckout.viewmodels.Session
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class StudentHistoryActivity : AppCompatActivity() {
    val visitRepo = VisitFakeDataSource()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_history)

        val rv = findViewById<View>(R.id.visitHistory) as RecyclerView

        val visits: List<Visit>
        try {
            val observable = visitRepo.queryVisits(Session.user!!, Visit(null, null, null, null))
            observable.subscribe(object : SingleObserver<List<Visit>> {
                override fun onSuccess(t: List<Visit>) {
                    val visits = t;
                    val adapter = VisitHistoryAdapter(visits)
                    rv.adapter = adapter
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    throw Exception("unable to get visit history")
                }
            })
        } catch(e: Exception) {
            val toast = Toast.makeText(this, "unable to get visit history", Toast.LENGTH_SHORT)
            toast.show()
        }

        rv.layoutManager = LinearLayoutManager(this)


    }

}*/
