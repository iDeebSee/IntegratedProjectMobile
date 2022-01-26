package be.ap.edu.integratedprojectmobile.student

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentLoginActivity : AppCompatActivity() {

    override fun onBackPressed() {
        Toast.makeText(
            applicationContext,
            "U mag de app niet sluiten",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.i(
                "U mag de app niet sluiten",
                "U mag de app niet sluiten"
            ) // here you'll have to do something to prevent the button to go to the home screen
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

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
                        //student = document.data["student"].toString()
                        spinnerStudents.adapter = studentAdapter;
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        btnLogin.setOnClickListener {
            var uid = spinnerStudents.selectedItem.toString()
            val intent = Intent(this, StudentsActivity::class.java)
            intent.putExtra("snummer", uid)
            startActivity(intent)
        }

    }
}