package com.csci310_group29.trojancheckincheckout.ui.views

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.entities.BuildingEntity


//class BuildingListAdapter(private val bList: List<Building>): RecyclerView.Adapter<BuildingListAdapter.ViewHolder>() {

class BuildingListAdapter(private val bList: List<BuildingEntity>): RecyclerView.Adapter<BuildingListAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val buildingNameText = itemView.findViewById<TextView>(R.id.buildingName)
        val numOfPeopleText = itemView.findViewById<TextView>(R.id.numOfPeople)
        val numOfCapacityText = itemView.findViewById<TextView>(R.id.numOfCapacity)
        val qrCodeButton = itemView.findViewById<Button>(R.id.qr_code)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.activity_building_list, parent, false)

        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(buildingHolding: BuildingListAdapter.ViewHolder, position: Int) {

        val building: BuildingEntity = bList.get(position)

        val bText = buildingHolding.buildingNameText
        bText.setText(building.buildingName)

        val numOfPeo = buildingHolding.numOfPeopleText
        numOfPeo.setText(building.numPeople.toString())

        val numOfCap = buildingHolding.numOfCapacityText
        numOfCap.setText(building.capacity.toString())

        val button = buildingHolding.qrCodeButton
        button.text = "Qr Code"

        button.setOnClickListener(View.OnClickListener {v ->
            val i = Intent(v.context, QrCodeActivity::class.java)
            i.putExtra("buildingName", building.buildingName)
            startActivity(v.context,i,null)
            //Toast.makeText(v.context,"Button clicked: ${building.buildingName}", Toast.LENGTH_SHORT).show()
        })

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return bList.size
    }
}