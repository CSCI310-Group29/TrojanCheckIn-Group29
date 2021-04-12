package com.csci310_group29.trojancheckincheckout.ui.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.MutableBuilding
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.csci310_group29.trojancheckincheckout.ui.viewmodels.BuildingStudentListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject


@AndroidEntryPoint
class BuildingStudentListActivity : AppCompatActivity() {

    @Inject
    lateinit var userDomain: UserUseCases

    lateinit var rv: RecyclerView
    var map: HashMap<String, Int> = HashMap<String, Int>()
    lateinit var adapter: BuildingStudentListAdapter
    lateinit var studentList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_student_list)

        rv = findViewById<View>(R.id.buildingStudentList) as RecyclerView

        try {
            val observable = userDomain.observeUsersInBuilding(intent.getStringExtra("buildingName").toString())
            observable.subscribe(object : Observer<List<User>> {
                override fun onNext(t: List<User>) {
                    studentList = initializeList(t)
                    initializeHashmap(t)
                    adapter = BuildingStudentListAdapter(userDomain, studentList)
                    rv.adapter = adapter
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                    throw Exception("Unable to get Student List")
                }

                override fun onComplete() {
                }
            })
        } catch(e: Exception) {
            val toast = Toast.makeText(this, "Unable to get Student List", Toast.LENGTH_SHORT)
            toast.show()
        }

        rv.layoutManager = LinearLayoutManager(this)
    }

    fun initializeHashmap(list: List<User>) {
        var i = 0
        for(b in list) {
            map.put(b.id, i)
            i++
        }
    }

    fun initializeList(list: List<User>): ArrayList<User> {
        val res: ArrayList<User> = ArrayList<User>()
        for(u in list) {
            res.add( User(u.id,
                            u.isStudent,
                            u.email,
                            u.firstName,
                            u.lastName,
                            u.major,
                            u.checkedInBuilding,
                            u.studentId,
                            u.deleted,
                            u.profilePicture))


           /* val observable = userDomain.observeUserById(u.id)
            observable.subscribe(object: Observer<List<User>> {
                override fun onComplete() { }
                override fun onSubscribe(d: Disposable) { }
                override fun onNext(t: User) {
                    //
                }

                override fun onError(e: Throwable) { }
            })*/

        }

        return res
    }

}