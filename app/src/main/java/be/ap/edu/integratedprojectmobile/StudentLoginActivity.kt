package be.ap.edu.integratedprojectmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class StudentLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)

        val txtSnummer = findViewById<TextView>(R.id.txtSnummer)
        val btnLogin = findViewById<Button>(R.id.btnStudentLogin)
        btnLogin.setOnClickListener {
            val intent = Intent(this, StudentsActivity::class.java)
            intent.putExtra("snummer", txtSnummer.text.toString())
            startActivity(intent)
        }

    }
}