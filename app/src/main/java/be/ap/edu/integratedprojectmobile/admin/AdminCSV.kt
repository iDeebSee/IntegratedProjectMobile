package be.ap.edu.integratedprojectmobile.admin

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import be.ap.edu.integratedprojectmobile.admin.AdminDashboardActivity
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminCSV : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_csv)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        val db = Firebase.firestore
        val btnaddStudentCSV = findViewById<Button>(R.id.btnaddStudentCSV)
        val csvTextField = findViewById<EditText>(R.id.txtCSVInput)


        btnaddStudentCSV.setOnClickListener {
            val csvData = csvTextField!!.text.toString()
            if(csvData.isNotEmpty()) {
                val studentsData = csvData.split(",", " ").toTypedArray()
                for (i in studentsData) {

                    val student = hashMapOf(
                        "latitude" to "",
                        "longitude" to "",
                        "student" to i
                    )

                    db.collection("students").document(i)
                        .set(student)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
                Toast.makeText(this, "Students opgeslagen!", Toast.LENGTH_LONG).show()

            }

            intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        }



    }
}