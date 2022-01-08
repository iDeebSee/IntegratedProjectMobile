package be.ap.edu.integratedprojectmobile.student

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)
        val db = Firebase.firestore
        var student:String = ""
        val studentAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item)
        val spinnerStudents = findViewById<Spinner>(R.id.spnrStudentsLogin)
        val btnLogin = findViewById<Button>(R.id.btnStudentLogin)
        db.collection("students")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data["student"]}")
                    if (spinnerStudents != null) {
                        studentAdapter.add(document.data["student"].toString());//.values.toString()
                        student = document.data["student"].toString()
                        spinnerStudents.adapter = studentAdapter;
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        btnLogin.setOnClickListener {
            val intent = Intent(this, StudentsActivity::class.java)
            intent.putExtra("snummer", student)
            startActivity(intent)
        }

    }
}