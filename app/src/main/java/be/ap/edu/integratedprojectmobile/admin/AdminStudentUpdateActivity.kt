package be.ap.edu.integratedprojectmobile.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminStudentUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_update)

        val db = Firebase.firestore
        val student = intent.getStringExtra("student").toString()
        val txtStudent = findViewById<TextView>(R.id.txtStudent)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        txtStudent.text = student


        btnUpdate.setOnClickListener {
            val studentData = hashMapOf(
                "latitude" to "",
                "longitude" to "",
                "student" to txtStudent.text.toString()
            )
            db.collection("students").document(student).delete()
            db.collection("students").document(txtStudent.text.toString()).set(studentData)
            Toast.makeText(this, "Student geupdatet!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, AdminStudentActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            finish();
            startActivity(intent)
        }

    }
}