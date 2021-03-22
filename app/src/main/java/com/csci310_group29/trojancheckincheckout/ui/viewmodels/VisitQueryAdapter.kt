
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Visit

val TAG = "VisitQueryAdapter"
class VisitQueryAdapter(private val vHistory: List<Visit>): RecyclerView.Adapter<VisitQueryAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val studentText = itemView.findViewById<TextView>(R.id.querySID)
        val buildingText = itemView.findViewById<TextView>(R.id.queryBuilding)
        val checkInText = itemView.findViewById<TextView>(R.id.queryCheckIn)
        val checkOutText = itemView.findViewById<TextView>(R.id.queryCheckOut)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "oncreate viewholder")
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_queryvisit, parent, false)

        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return vHistory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "bind viewholder")
        val visit: Visit = vHistory[position]
        Log.i(TAG, visit.toString())

        val sText = holder.studentText
        sText.text = visit.user!!.studentId

        val bText = holder.buildingText
        bText.text = visit.building!!.buildingName

        val checkIn = holder.checkInText
        checkIn.text = visit.checkIn.toString()

        val checkOut = holder.checkOutText
        checkOut.text = visit.checkOut.toString()

    }
}
