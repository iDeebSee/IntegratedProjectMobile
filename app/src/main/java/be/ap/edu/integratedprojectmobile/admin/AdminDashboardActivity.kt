package be.ap.edu.integratedprojectmobile.admin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import be.ap.edu.integratedprojectmobile.exam.ExamsActivity
import be.ap.edu.integratedprojectmobile.R
import be.ap.edu.integratedprojectmobile.student.StudentDashboardActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val examAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item)
        val studentAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item)
        val spinnerExams = findViewById<Spinner>(R.id.spnrExams)
        val spinnerStudents = findViewById<Spinner>(R.id.spnrStudents)
        val db = Firebase.firestore
        val btnExams = findViewById<Button>(R.id.btnExams)
        val btnStudents = findViewById<Button>(R.id.btnStudents)
        val btnChangePass = findViewById<Button>(R.id.btnPassword)
        val btnCSV = findViewById<Button>(R.id.btnCSVImport)

        db.collection("exams")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    if (spinnerExams != null) {
                        examAdapter.add(document.data["name"].toString());
                        spinnerExams.adapter = examAdapter;
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        db.collection("students")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data["student"]}")
                    if (spinnerStudents != null) {
                        studentAdapter.add(document.data["student"].toString());//.values.toString()
                        spinnerStudents.adapter = studentAdapter;
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        btnExams.setOnClickListener {
            val examsIntent = Intent(this, ExamsActivity::class.java)
            startActivity(examsIntent)
        }

        btnStudents.setOnClickListener {
            val studentsIntent = Intent(this, StudentDashboardActivity::class.java)
            startActivity(studentsIntent)
        }

        btnChangePass.setOnClickListener {
            val changePassIntent = Intent(this, AdminChangePasswordActivity::class.java)
            startActivity(changePassIntent)
        }

        btnCSV.setOnClickListener {
            val CSVIntent = Intent(this, AdminCSV::class.java)
            startActivity(CSVIntent)
        }


    }
}