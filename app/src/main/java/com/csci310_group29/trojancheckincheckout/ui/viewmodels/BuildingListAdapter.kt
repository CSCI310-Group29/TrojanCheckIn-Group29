package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.Building
import com.csci310_group29.trojancheckincheckout.domain.models.MutableBuilding
import com.csci310_group29.trojancheckincheckout.domain.usecases.BuildingUseCases
import com.csci310_group29.trojancheckincheckout.ui.views.BuildingStudentListActivity
import com.csci310_group29.trojancheckincheckout.ui.views.QrCodeActivity
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject


//class BuildingListAdapter(private val bList: List<Building>): RecyclerView.Adapter<BuildingListAdapter.ViewHolder>() {


class BuildingListAdapter @Inject constructor(private val buildingDomain: BuildingUseCases, private val bList: List<MutableBuilding>): RecyclerView.Adapter<BuildingListAdapter.ViewHolder>() {


    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val buildingNameText = itemView.findViewById<TextView>(R.id.buildingName)
        val numOfPeopleText = itemView.findViewById<TextView>(R.id.numOfPeople)
        val numOfCapacityText = itemView.findViewById<TextView>(R.id.numOfCapacity)
        val studentListButton = itemView.findViewById<Button>(R.id.student_list)
        val qrCodeButton = itemView.findViewById<Button>(R.id.qr_code)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_buildinginfo, parent, false)

        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(buildingHolding: ViewHolder, position: Int) {

        val building: MutableBuilding = bList.get(position)


        val bText = buildingHolding.buildingNameText
        bText.setText(building.buildingName)

        val numOfPeo = buildingHolding.numOfPeopleText
        numOfPeo.setText(building.numPeople.toString())

        val numOfCap = buildingHolding.numOfCapacityText
        numOfCap.setText(building.capacity.toString())

        val studentListButton = buildingHolding.studentListButton
        studentListButton.text = "Students"

        val qrCodeButton = buildingHolding.qrCodeButton
        qrCodeButton.text = "Qr Code"

        studentListButton.setOnClickListener(View.OnClickListener { v ->
            val i = Intent(v.context, BuildingStudentListActivity::class.java)
            i.putExtra("buildingName", building.buildingName)
            startActivity(v.context, i, null)
            //Toast.makeText(v.context,"Student List Button clicked: ${building.buildingName}", Toast.LENGTH_SHORT).show()
        })

        qrCodeButton.setOnClickListener(View.OnClickListener {v ->
            val i = Intent(v.context, QrCodeActivity::class.java)
            i.putExtra("buildingName", building.buildingName)
            startActivity(v.context,i,null)
            //Toast.makeText(v.context,"QR Code Button clicked: ${building.buildingName}", Toast.LENGTH_SHORT).show()
        })

        val observable = buildingDomain.observeBuildingById(building.id)
        observable.subscribe(object: Observer<Building> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Building) {
                val elem = building
                elem.numPeople = t.numPeople
                elem.capacity = t.capacity
                notifyItemChanged(position)

            }

            override fun onError(e: Throwable) {

            }
        })

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return bList.size
    }
}