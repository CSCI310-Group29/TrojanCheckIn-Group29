package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.UserUseCases
import com.csci310_group29.trojancheckincheckout.domain.usecases.VisitUseCases
import com.csci310_group29.trojancheckincheckout.ui.views.ManagerStudentProfileActivity
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.item_buildingstudentlist.view.*
import javax.inject.Inject

class BuildingStudentListAdapter @Inject constructor(private val userDomain: UserUseCases, private val sList: List<User>): RecyclerView.Adapter<BuildingStudentListAdapter.ViewHolder>() {

    @Inject
    lateinit var visitDomain: VisitUseCases

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView) {
        val studentId = itemView.findViewById<TextView>(R.id.buildingStudentList_SID)
        val lName = itemView.findViewById<TextView>(R.id.buildingStudentList_lastName)
        var fName = itemView.findViewById<TextView>(R.id.buildingStudentList_firstName)
        val studentProfileButton = itemView.findViewById<Button>(R.id.buildingStudentList_viewProfile)
        val kickOutButton = itemView.findViewById<Button>(R.id.buildingStudentListKickOut)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "Building Student List onCreate")
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_buildingstudentlist, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "Building Student List bind")
        val user: User = sList.get(position)

        holder.studentId.text = user.studentId
        holder.lName.text = user.lastName
        holder.fName.text = user.firstName
        holder.studentProfileButton.text = "Profile"
        holder.kickOutButton.text = "Kick Out"

        holder.studentProfileButton.setOnClickListener(View.OnClickListener { v->
            val i = Intent(v.context, ManagerStudentProfileActivity::class.java)
            i.putExtra("studentUID", user.id)

            startActivity(v.context, i, null)
        })

        holder.kickOutButton.setOnClickListener(View.OnClickListener {v ->
            //TODO: connect to UI
        })

        /*val observable = userDomain.observeUserById(user.id)
        observable.subscribe(object: Observer<User> {
            override fun onComplete() { }

            override fun onSubscribe(d: Disposable) { }

            override fun onNext(t: User) {
//                val elem = user
//                if(/* CONDITION FOR USER CHECKED OUT */) {
//                    sList.removeAt(position)
//                    notifyItemRemoved(position)
//                    notifyItemRangeChanged(position, sList.size)
//                }

            }

            override fun onError(e: Throwable) {

            }
        })*/

    }


    override fun getItemCount(): Int {
        return sList.size
    }

}