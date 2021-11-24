package be.ap.edu.integratedprojectmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val toast = Toast.makeText(applicationContext, "Wrong Password!", Toast.LENGTH_LONG);
        val lblLogin = findViewById<TextView>(R.id.lblLogin)

        val password = "0"

        val txtPasswordField = findViewById<TextView>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            if (txtPasswordField.text.toString() == password){
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
            }else{
                //lblLogin.text = "Wrong Password!"
                toast.setGravity(Gravity.TOP , 0, 0);
                toast.show();
            }

        }


    }
}