package be.ap.edu.integratedprojectmobile.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import be.ap.edu.integratedprojectmobile.database.DatabaseHelper
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_change_password)
        val dbh: DatabaseHelper = DatabaseHelper(applicationContext)
        val db = Firebase.firestore
        val oldPassword = findViewById<TextView>(R.id.txtOldPassword)
        val newPassword = findViewById<TextView>(R.id.txtNewPassword)
        val btnSubmit = findViewById<Button>(R.id.btnChangePassword)
        var password = ""
        var passUpdate = db.collection("admin").document()


        db.collection("admin")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TAG1", "${document.id} => ${document.data}")
                    password = document.data.get("password").toString()
                    Log.d("password in db ", password);
                    passUpdate = db.collection("admin").document(document.id)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG2", "Error getting documents.", exception)
            }


        btnSubmit.setOnClickListener {
            //val oldPass = Password(oldPassword.text as String?)
            //val newPass = Password(newPassword.text as String?)
            Log.d("old pass on onclick", oldPassword.text.toString())
            Log.d("database pass on onclick", password)

            if (oldPassword.text.toString() == password){
                passUpdate.update("password", newPassword.text.toString())
                val intent = Intent(this, AdminLoginActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext, "Wrong password!", Toast.LENGTH_LONG).show()
            }
            //dbh.updateAdminPass(newPass.toString(), oldPass.toString())

        }
    }
}