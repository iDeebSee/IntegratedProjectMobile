package be.ap.edu.integratedprojectmobile


import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.children
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.RadioButton




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
        val btnSaveExam = findViewById<Button>(R.id.btnSaveExam)
        val db = Firebase.firestore
        var radioButtonsToDB = ArrayList<String>()
        var openVragenToDB = ArrayList<String>()
        var txtTitle = findViewById<TextView>(R.id.txtExamName)
        var radiobuttonGroupToDB  = ArrayList<String>()
        var radiobuttonGroupAmount  = ArrayList<Int>()


        var questionsList: MutableList<String> = ArrayList()

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



        fun radioButtonGroup(radioButtons: ArrayList<RadioButton>): RadioGroup {

            val radioButtonGroup = RadioGroup(this)
            radioButtonGroup.layoutParams  = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
            )
            (radioButtonGroup.layoutParams as LayoutParams).setMargins(0, 0, 0, 15)
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

            Log.d("radiobuttonGroup", radioButtonGroup.toString())
            return radioButtonGroup

        }


        btnAdd.setOnClickListener {

            if (rdbMultiChoice.isChecked){

                val questions = txtVragen.text.split("-").toTypedArray()

                    layout.addView(radioButtonGroup(radioButton(questions)))
            }

            if (rdbOpenVraag.isChecked){
                layout.addView(createNewTextView(txtVragen.text.toString()))
            }
        }

        btnSaveExam.setOnClickListener {

            val exam = hashMapOf(
                "name" to txtTitle.text.toString(),
                "multipleChoice" to radioButtonsToDB.toString().split(",", "[", "]").filter{ x:String? -> x != "" },
                "openQuestions" to openVragenToDB.toString(),
                "radioGroup" to radiobuttonGroupToDB.toString(),
                "radioGroupAmount" to radiobuttonGroupAmount.toString().split(",", "[", "]").filter{ x:String? -> x != "" },

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

        }
    }
}