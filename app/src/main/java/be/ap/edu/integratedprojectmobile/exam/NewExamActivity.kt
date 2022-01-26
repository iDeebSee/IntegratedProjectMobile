package be.ap.edu.integratedprojectmobile.exam


import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.RadioButton
import androidx.core.view.isVisible
import be.ap.edu.integratedprojectmobile.R
import be.ap.edu.integratedprojectmobile.database.FirebaseDatabaseHelper


class NewExamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_exam)

        val btnAdd = findViewById<Button>(R.id.btnAddNewExam)
        val rdbMultiChoice = findViewById<RadioButton>(R.id.rdbMultipleChoice)
        val rdbOpenVraag = findViewById<RadioButton>(R.id.rdbOpenVraag)
        val rdbCodeVraag = findViewById<RadioButton>(R.id.rdbCode)
        val layout = findViewById<LinearLayout>(R.id.layoutNewExam)

        val amount = intent.getStringExtra("amount")
        val txtVragen = findViewById<TextView>(R.id.txtVragen)
        val vraagTitel = findViewById<TextView>(R.id.txtVraagTitel)
        vraagTitel.isEnabled = false
        val btnSaveExam = findViewById<Button>(R.id.btnSaveExam)
        val db = Firebase.firestore
        val radioButtonsToDB = ArrayList<String>()
        val openVragenToDB = ArrayList<String>()
        val titlesToDB = ArrayList<String>()
        val txtTitle = findViewById<TextView>(R.id.txtExamName)
        val radiobuttonGroupToDB  = ArrayList<String>()
        val codeVragenToDB = ArrayList<String>()
        val radiobuttonGroupAmount  = ArrayList<Int>()


        var questionsList: MutableList<String> = ArrayList()


        fun addTitle(text:String):TextView{
            val lparams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f)
            lparams.setMargins(0, 0, 0, 15)

            val txt = TextView(this)
            txt.text = text
            txt.setTextColor(Color.BLACK)
            txt.setTextSize(20f)
            titlesToDB.add(txt.text.toString())
            return txt
        }

        fun createNewTextView(text:String):TextView {
            val lparams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f)
            lparams.setMargins(0, 0, 0, 15)
            val txt = EditText(this)
//            val textView = TextView(this)
//            textView.text = text
            txt.isSingleLine = false
            txt.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            txt.isSingleLine = false
            txt.layoutParams = lparams
            txt.hint = text
            openVragenToDB.add(txt.hint.toString())
            return  txt
        }


        fun createCodeVraag(text:String){
            val lparams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f)
            lparams.setMargins(0, 0, 0, 15)
            val txt = TextView(this)
            txt.isSingleLine = false
            txt.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            txt.isSingleLine = false
            txt.layoutParams = lparams
            var teller = 0
            val cleansedText = text.split("_")
            var finishedText = ""

            for (item in cleansedText){
                teller++
                finishedText += "$item ($teller)____ "

            }
            txt.text = finishedText
            layout.addView(txt)

            for(i in 1 .. teller-1){
                val view = layoutInflater.inflate(R.layout.code_vraag_layout, null)
                val etAnswer = view.findViewById<EditText>(R.id.etAnswer)
                etAnswer.hint = i.toString()
                layout.addView(view)
            }



            Log.d("cleansed text", cleansedText.toString())
            codeVragenToDB.add(text)
        }

        fun radioButton(text: Array<String>): ArrayList<RadioButton> {
            val radioButtonArray: ArrayList<RadioButton> = ArrayList()
            for (rdb in text) {
                val rdbNewRadioButton = RadioButton(this)
                rdbNewRadioButton.layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                rdbNewRadioButton.text = rdb
                radioButtonArray.add(rdbNewRadioButton)
                radioButtonsToDB.add(rdbNewRadioButton.text.toString())
                Log.d("radiobutton", rdbNewRadioButton.text.toString())
            }


            return radioButtonArray
        }


        fun radioButtonGroup(title:TextView, radioButtons: ArrayList<RadioButton>): RadioGroup {

            val radioButtonGroup = RadioGroup(this)
            radioButtonGroup.layoutParams  = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
            )
            (radioButtonGroup.layoutParams as LayoutParams).setMargins(0, 0, 0, 15)

            radioButtonGroup.addView(title)
            for(radio in radioButtons){
                radioButtonGroup.addView(radio)
            }
            val count: Int = radioButtonGroup.childCount
            val listOfRadioButtons = ArrayList<RadioButton>()
            for (i in 0 until count) {
                val o: View = radioButtonGroup.getChildAt(i)
                if (o is RadioButton) {
                    listOfRadioButtons.add(o as RadioButton)
                }
            }
            radiobuttonGroupAmount.add(listOfRadioButtons.size)
            Log.d(TAG, "you have " + listOfRadioButtons.size.toString() + " radio buttons")
            for (rdb in listOfRadioButtons){
                radiobuttonGroupToDB.add(rdb.text.toString())
            }
            //radiobuttonGroupToDB.add(title.text.toString())

            Log.d("radiobuttonGroup", radioButtonGroup.toString())
            return radioButtonGroup

        }

        rdbMultiChoice.setOnClickListener {
            vraagTitel.isEnabled = true
        }
        rdbCodeVraag.setOnClickListener {
            vraagTitel.isEnabled = false
        }
        rdbOpenVraag.setOnClickListener {
            vraagTitel.isEnabled = false
        }

        btnAdd.setOnClickListener {

            if (rdbMultiChoice.isChecked){

                val questions = txtVragen.text.split("-").toTypedArray()
                //val titleArray = questions.toString().split(";").toTypedArray()


                //val questionAndTitle = questions.toString().split(";", "-").toTypedArray()

                layout.addView(radioButtonGroup(addTitle(vraagTitel.text.toString()),radioButton(questions)))
            }
            if (rdbOpenVraag.isChecked){

                layout.addView(createNewTextView(txtVragen.text.toString()))
            }
            if(rdbCodeVraag.isChecked){

                createCodeVraag(txtVragen.text.toString())
            }
        }

        btnSaveExam.setOnClickListener {

            val exam = hashMapOf(
                "name" to txtTitle.text.toString(),
                "multipleChoice" to radioButtonsToDB.toString().split(",", "[", "]").filter{ x:String? -> x != "" },
                "mcTitles" to titlesToDB.toString(),
                "openQuestions" to openVragenToDB.toString(),
                "radioGroup" to radiobuttonGroupToDB.toString(),
                "radioGroupAmount" to radiobuttonGroupAmount.toString().split(",", "[", "]").filter{ x:String? -> x != "" },
                "codeVragen" to codeVragenToDB.toString()
            )

 //Add a new document with a generated ID
            db.collection("exams")
                .document(txtTitle.text.toString())
                .set(exam)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
                    Toast.makeText(applicationContext, "Examen is opgeslagen!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

                val intent = Intent(this, ExamDashboardActivity::class.java)
                intent.putExtra("examName",txtTitle.text.toString())
                startActivity(intent)

        }
    }
}