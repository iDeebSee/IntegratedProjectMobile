package be.ap.edu.integratedprojectmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Toast.makeText(applicationContext, "firebase connection success", Toast.LENGTH_LONG).show();


        val btnAdmin = findViewById<Button>(R.id.btnAdmin)
        btnAdmin.setOnClickListener {
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
        }

        val btnStudent = findViewById<Button>(R.id.btnStudent)
        btnStudent.setOnClickListener {
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
        }

    }
}