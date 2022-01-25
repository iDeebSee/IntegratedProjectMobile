package be.ap.edu.integratedprojectmobile.exam

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ExamsActivity : AppCompatActivity() {

    enum class ButtonType {
        DELETE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)

        val btnNewExam = findViewById<Button>(R.id.btnNewExam)
        val db = Firebase.firestore
        //val layout = findViewById<LinearLayout>(R.id.layoutExamsLinear)
        val tlayout = findViewById<TableLayout>(R.id.layoutTableLayout)

        fun createNewTextView(text:String): TextView {
//            val lparams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1.0f
//            )
//            lparams.setMargins(0, 0, 0, 15)
            val txt = TextView(this)
            txt.text = text
            //txt.layoutParams = lparams
            Log.d("id", txt.id.toString())
            txt.setOnClickListener{
                val intent = Intent(this, ExamDashboardActivity::class.java)
                intent.putExtra("examName",txt.text.toString())
                startActivity(intent)
                Log.d("child", txt.text.toString())
            }
            return  txt
        }

        fun addButton(buttonType: ExamsActivity.ButtonType, exam:String): Button{
            val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            lparams.setMargins(15, 0, 0, 15)

            val btn = Button(this)
            btn.text = buttonType.name
            if (buttonType == ExamsActivity.ButtonType.DELETE){
                btn.setOnClickListener {
                    db.collection("exams").document(exam).delete().addOnSuccessListener {
                        val intent = Intent(this, ExamsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        startActivity(intent)
                    }
                }
            }
            //btn.setTextColor(color)
            return btn
        }

        db.collection("exams")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val tableRow:TableRow = TableRow(this)
                    //layout.addView(createNewTextView(document.data["name"].toString()))
                    tableRow.addView(createNewTextView(document.data["name"].toString()))
                    tableRow.addView(addButton(ButtonType.DELETE, document.data["name"].toString()))
                    tlayout.addView(tableRow)

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