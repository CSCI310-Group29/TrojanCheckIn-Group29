
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Visit

class VisitHistoryAdapter(private val vHistory: List<Visit>): RecyclerView.Adapter<VisitHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val buildingText = itemView.findViewById<TextView>(R.id.historyBuilding)
        val checkInText = itemView.findViewById<TextView>(R.id.historyCheckIn)
        val checkOutText = itemView.findViewById<TextView>(R.id.historyCheckOut)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_visit, parent, false)

        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return vHistory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visit: Visit = vHistory[position]

        val bText = holder.buildingText
        bText.text = visit.building!!.buildingName

        val checkIn = holder.checkInText
        checkIn.text = visit.checkIn.toString()

        val checkOut = holder.checkOutText
        checkOut.text = visit.checkOut.toString()

    }
}
