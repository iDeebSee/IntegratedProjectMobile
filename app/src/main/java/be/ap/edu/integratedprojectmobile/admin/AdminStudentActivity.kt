package be.ap.edu.integratedprojectmobile.admin

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.LinearLayout.LayoutParams

class AdminStudentActivity : AppCompatActivity() {

    enum class ButtonType {
        DELETE, UPDATE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)


        val db = Firebase.firestore
        var studentsList: ArrayList<String> = ArrayList()
        var studentsListSize:Int = 0

        val tlayout = findViewById<LinearLayout>(R.id.tlayout)


        fun addText(text:String):TextView{
            val lparams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f)
            lparams.setMargins(0, 0, 15, 15)
            val txt = TextView(this)
            txt.text = text
            txt.setTextColor(Color.BLACK)
            txt.setTextSize(18f)
            //titlesToDB.add(txt.text.toString())
            return txt
        }

        fun addButton(buttonType: ButtonType, student:String): Button{
            val lparams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f)
            lparams.setMargins(15, 0, 0, 15)

            val btn = Button(this)
            btn.text = buttonType.name
            if (buttonType == ButtonType.DELETE){
                btn.setOnClickListener {
                    db.collection("students").document(student)
                        .delete().addOnSuccessListener {
                            studentsList.remove(student)
                            val intent = Intent(this, AdminStudentActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent)
                            Log.d(TAG, "DocumentSnapshot successfully deleted!")

                    }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                }
            }
            else if (buttonType == ButtonType.UPDATE){
                btn.setOnClickListener {
                    val intent = Intent(this, AdminStudentUpdateActivity::class.java)
                    intent.putExtra("student", student)
                    startActivity(intent)
                }
                //db.collection("students").document(student).update()
            }
            //btn.setTextColor(color)
            return btn
        }


            db.collection("students").get().addOnSuccessListener{ result ->
                for (document in result){
                    val tableRow:TableRow = TableRow(this)
                    studentsList.add(document.data["student"].toString())
                    studentsListSize++
                    tableRow.addView(addText(document.data["student"].toString()))
                    tableRow.addView(addButton(ButtonType.DELETE, document.data["student"].toString()))
                    tableRow.addView(addButton(ButtonType.UPDATE, document.data["student"].toString()))
                    tlayout.addView(tableRow)
                    Log.d("student list", document.data["student"].toString())
                }

            }
    }
}