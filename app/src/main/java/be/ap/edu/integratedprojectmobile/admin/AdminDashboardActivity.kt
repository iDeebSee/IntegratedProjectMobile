package be.ap.edu.integratedprojectmobile.admin

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import be.ap.edu.integratedprojectmobile.exam.ExamsActivity
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AdminDashboardActivity : AppCompatActivity() {

    enum class ButtonType {
        GEO, VIEW
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val examAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item)
        val studentAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item)
        //val spinnerExams = findViewById<Spinner>(R.id.spnrExams)
        val spinnerStudents = findViewById<Spinner>(R.id.spnrStudents)
        val db = Firebase.firestore
        val btnExams = findViewById<Button>(R.id.btnExams)
        val btnStudents = findViewById<Button>(R.id.btnStudents)
        val btnChangePass = findViewById<Button>(R.id.btnPassword)
        val btnCSV = findViewById<Button>(R.id.btnCSVImport)
        val tableLayoutStudent = findViewById<TableLayout>(R.id.tableLayoutStudent)
        //val tableLayoutExam = findViewById<TableLayout>(R.id.tableLayoutExams)
        val studentAnswers: ArrayList<String> = ArrayList()
        var student:String = ""
        var lon:String = ""
        var lat:String = ""


        db.collection("students")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data["student"]}")
                    if (spinnerStudents != null) {
                        studentAdapter.add(document.data["student"].toString());//.values.toString()
                        spinnerStudents.adapter = studentAdapter;
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


        fun addButton(buttonType: AdminDashboardActivity.ButtonType, exam:String, student:String): Button{
            val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            lparams.setMargins(15, 0, 0, 15)

            val btn = Button(this)
            btn.text = buttonType.name
            if (buttonType == ButtonType.GEO){
                btn.setOnClickListener {
                    val intent = Intent(this, AdminStudentGeoActivity::class.java)
                    intent.putExtra("lon", lon)
                    intent.putExtra("lat", lat)
                    startActivity(intent)
                }
            }
            else if (buttonType == AdminDashboardActivity.ButtonType.VIEW){
                btn.setOnClickListener {
                    val intent = Intent(this, AdminStudentViewExamActivity::class.java)
                    intent.putExtra("student", student)
                    intent.putExtra("exam", exam)
                    startActivity(intent)
                }
                //db.collection("students").document(student).update()
            }
            //btn.setTextColor(color)
            return btn
        }

        fun addText(text:String):TextView{
            val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            lparams.setMargins(0, 0, 15, 15)
            val txt = TextView(this)
            txt.text = text
            txt.setTextColor(Color.BLACK)
            txt.setTextSize(18f)
            //titlesToDB.add(txt.text.toString())
            return txt
        }

        spinnerStudents.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                tableLayoutStudent.removeAllViews()
                val item = parent.getItemAtPosition(position)
                student = item.toString()
                db.collection("studentanswers").whereEqualTo("student", item.toString())
                    .get().addOnSuccessListener { result ->
                        for (o in result){
                            lon = o.get("lon").toString()
                            lat = o.get("lat").toString()
                            val tableRow:TableRow = TableRow(applicationContext)
                            Log.d("items in result", o.get("codeQuestionsAnswers").toString())
                            tableRow.addView(addText(o.get("exam").toString()))
                            tableRow.addView(addButton(ButtonType.VIEW, o.get("exam").toString(), student))
                            tableRow.addView(addButton(ButtonType.GEO, o.get("exam").toString(), student))
                            tableLayoutStudent.addView(tableRow)
                        }
                    }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        btnExams.setOnClickListener {
            val examsIntent = Intent(this, ExamsActivity::class.java)
            startActivity(examsIntent)
        }

        btnStudents.setOnClickListener {
            val studentsIntent = Intent(this, AdminStudentActivity::class.java)
            startActivity(studentsIntent)
        }

        btnChangePass.setOnClickListener {
            val changePassIntent = Intent(this, AdminChangePasswordActivity::class.java)
            startActivity(changePassIntent)
        }

        btnCSV.setOnClickListener {
            val CSVIntent = Intent(this, AdminCSV::class.java)
            startActivity(CSVIntent)
        }


    }
}