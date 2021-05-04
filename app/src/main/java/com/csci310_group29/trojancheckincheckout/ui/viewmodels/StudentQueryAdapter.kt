
package com.csci310_group29.trojancheckincheckout.ui.viewmodels

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.csci310_group29.trojancheckincheckout.R
import com.csci310_group29.trojancheckincheckout.domain.models.User
import com.csci310_group29.trojancheckincheckout.domain.models.Visit
import com.csci310_group29.trojancheckincheckout.ui.views.ManagerStudentProfileActivity

val TAG = "StudentQueryAdapter"
class StudentQueryAdapter(private val vHistory: List<User>): RecyclerView.Adapter<StudentQueryAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
        val studentFirst = itemView.findViewById<TextView>(R.id.queryFirstName)
        val studentLast = itemView.findViewById<TextView>(R.id.queryLastName)
//        val studentId = itemView.findViewById<TextView>(R.id.queryStudentId)
        val pfp = itemView.findViewById<ImageView>(R.id.queryPFP)
        val profileButton = itemView.findViewById<Button>(R.id.queryStudentProfile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "oncreate viewholder")
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_query_user, parent, false)

        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return vHistory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "bind viewholder")
        val user: User = vHistory[position]
        Log.i(TAG, user.toString())

        val sFirst = holder.studentFirst
        sFirst.text = user.firstName

        val sLast = holder.studentLast
        sLast.text = user.lastName

//        val sId = holder.studentId
//        sId.text = user.studentId

        val sPFP = holder.pfp
        if(user.profilePicture != null) sPFP.setImageBitmap(toBitmap(user.profilePicture))

        val pButton = holder.profileButton

        pButton.setOnClickListener(View.OnClickListener  { v ->
            val i = Intent(v.context, ManagerStudentProfileActivity::class.java)
            i.putExtra("studentUID", user.id);
            startActivity(v.context,i,null)
        })

    }

    private fun toBitmap(bArray: ByteArray?): Bitmap? {
        if(bArray == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArray, 0, bArray.size)
    }
}
