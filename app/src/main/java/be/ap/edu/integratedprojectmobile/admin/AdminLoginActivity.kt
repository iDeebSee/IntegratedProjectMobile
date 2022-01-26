package be.ap.edu.integratedprojectmobile.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import be.ap.edu.integratedprojectmobile.database.DatabaseHelper
import be.ap.edu.integratedprojectmobile.Password
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        val dbh: DatabaseHelper = DatabaseHelper(applicationContext)
//        val localDb = dbh.readableDatabase
 //       val adminPassFromDb = localDb.rawQuery("SELECT Password FROM Admin LIMIT 1", null)
        var adminPass: String = ""
        var password = ""
        val db = Firebase.firestore

//        if (adminPassFromDb.moveToNext()){
//            //Toast.makeText(applicationContext, adminPassFromDb.getString(0), Toast.LENGTH_LONG).show()
//            Log.d("adminPassFromDb 0 ", adminPassFromDb.getString(0))
//            adminPass = adminPassFromDb.getString(0).toString()
//        }
        val toast = Toast.makeText(applicationContext, "Verkeerd wachtwoord!", Toast.LENGTH_LONG);
        val lblLogin = findViewById<TextView>(R.id.lblLogin)

        db.collection("admin")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TAG1", "${document.id} => ${document.data}")
                    password = document.data.get("password").toString()
                    Log.d("password in db ", password);
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG2", "Error getting documents.", exception)
            }

//        var password: String = ""
//        if (dbh.adminPass.toString() == ""){
//            val hashedPass: Password = Password("admin")
//            dbh.addAdminPass("admin")
//            Log.d("password: hashed: ", hashedPass.toString())
//
//        }else{
//            password = dbh.adminPass.toString()
//            Log.d("else pass in db ", dbh.adminPass.getString(0).toString())
//        }
        //dbh.addAdminPass(password);

        val txtPasswordField = findViewById<TextView>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val hashedInput = Password(txtPasswordField.text.toString())
            val input = txtPasswordField.text.toString()
            hashedInput.password?.let { it1 -> Log.d("hashedInput ", it1) }
            //Log.d("the password ", Password(adminPass).toString())
            if (input == password){
                Log.d("hashed string: ", hashedInput.toString())
                Log.d("input pass: ", adminPass)
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
            }else{
                //lblLogin.text = "Wrong Password!"
                //toast.setGravity(Gravity.TOP , 0, 0);
                hashedInput.password?.let { it1 -> Log.d("hashed string: ", it1) }
                Log.d("input pass: ", adminPass)
                toast.show();
            }
        }
    }
}