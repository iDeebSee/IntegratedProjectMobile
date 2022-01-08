package be.ap.edu.integratedprojectmobile.exam

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ExamsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)

        val btnNewExam = findViewById<Button>(R.id.btnNewExam)
        val db = Firebase.firestore
        val layout = findViewById<LinearLayout>(R.id.layoutExamsLinear)

        fun createNewTextView(text:String): TextView {
            val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            lparams.setMargins(0, 0, 0, 15)
            val txt = TextView(this)
            txt.text = text
            txt.layoutParams = lparams
            Log.d("id", txt.id.toString())
            txt.setOnClickListener{
                val intent = Intent(this, ExamDashboardActivity::class.java)
                intent.putExtra("examName",txt.text.toString())
                startActivity(intent)
                Log.d("child", txt.text.toString())
            }
            return  txt
        }

        db.collection("exams")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    layout.addView(createNewTextView(document.data["name"].toString()))

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }



        btnNewExam.setOnClickListener {
            val intent = Intent(this, NewExamActivity::class.java)
            startActivity(intent)
        }
    }

}