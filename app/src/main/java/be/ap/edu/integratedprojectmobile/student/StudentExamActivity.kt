package be.ap.edu.integratedprojectmobile.student

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentExamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_exam)

        val uid = intent.getStringExtra("snummer")
        val lon = intent.getStringExtra("lon")
        val lat = intent.getStringExtra("lat")

        val db = Firebase.firestore
        val txtExamTitle = findViewById<TextView>(R.id.tvStudentExamTitle)
        val examName=intent.getStringExtra("examName")
        val layout = findViewById<LinearLayout>(R.id.LinearLayoutOefningen)
        txtExamTitle.text = examName.toString()
        val btnSubmit = findViewById<Button>(R.id.btnStudentExamSubmit)
        val openVragenAntwoord : ArrayList<String> = ArrayList()
        val openVragenVeld : ArrayList<TextView> = ArrayList()

        var textViewTeller = 0
        fun createNewTextView(text:String): TextView {
            val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            lparams.setMargins(0, 0, 0, 15)
            val txt = EditText(this)
            txt.isSingleLine = false
            txt.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            txt.isSingleLine = false
            txt.layoutParams = lparams
            txt.hint = text
            Log.d("on click listener", openVragenAntwoord.toString())
            openVragenVeld.add(txt)
            return  txt
        }


        fun createCodeVraag(text:String){
            val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
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

            for(i in 1 .. teller){
                val view = layoutInflater.inflate(R.layout.code_vraag_layout, null)
                val etAnswer = view.findViewById<EditText>(R.id.etAnswer)
                etAnswer.hint = i.toString()
                layout.addView(view)
            }
        }


        fun radioButton(text: Array<String>): ArrayList<RadioButton> {
            val radioButtonArray: ArrayList<RadioButton> = ArrayList()
            for (rdb in text) {
                val rdbNewRadioButton = RadioButton(this)
                rdbNewRadioButton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                rdbNewRadioButton.text = rdb
                radioButtonArray.add(rdbNewRadioButton)
                Log.d("radiobutton", rdbNewRadioButton.text.toString())
            }

            return radioButtonArray
        }



        fun radioButtonGroup(radioButtons: ArrayList<RadioButton>): RadioGroup {

            val radioButtonGroup = RadioGroup(this)
            radioButtonGroup.layoutParams  = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            (radioButtonGroup.layoutParams as LinearLayout.LayoutParams).setMargins(0, 0, 0, 15)
            for(radio in radioButtons){
                radioButtonGroup.addView(radio)
            }

            Log.d("radiobuttonGroup", radioButtonGroup.toString())
            return radioButtonGroup

        }

        db.collection("exams").document(examName.toString())
            .get()
            .addOnSuccessListener { result ->
                val codeQuestions: ArrayList<String> = ArrayList()
                val mcQuestions: ArrayList<String> = ArrayList()
                val oQuestions: ArrayList<String> = ArrayList()
                Log.d("radioGroupAmount",result.data?.get("radioGroupAmount").toString())
                val amount: Any? = result.data?.get("radioGroupAmount")//.toString().split(",").toString()
                val theArray = amount.toString().split(",", "[", "]", " ").toTypedArray()
                Log.d("amount", amount.toString())
                var theAmount: String = ""
                val mySize = theArray.size
                val resultaat = result.data?.get("multipleChoice").toString().split(",","[", "]").filter{ x:String? -> x != "" }.toTypedArray()
                for (item in 0 until mySize - 1){
                    if (theArray[item] != "[" && theArray[item] != "]" && theArray[item] != ""){
                        theAmount = theArray[item]
                        Log.d("the amount $item: ", theAmount)
                        for (i in 0 until theAmount.toInt()){
                            Log.d("$i", theAmount)
                            mcQuestions.add(resultaat[i])
                        }
                    }
                    layout.addView(radioButtonGroup(radioButton(mcQuestions.toTypedArray())))
                    mcQuestions.clear()

                }

                val openQuestions = result.data?.get("openQuestions")?.toString()
                    ?.split(",", "[", "]")?.filter{ x:String? -> x != "" }?.toTypedArray()
                if (openQuestions != null) {
                    for (item in openQuestions){
                        layout.addView(createNewTextView(item))
                    }
                }
                //oQuestions.addAll(listOf(result.data?.get("openQuestions").toString().split(",", "[", "]", " ").toString()))
                codeQuestions.addAll(listOf(result.data?.get("codeVragen").toString()))

                val codeQuestionsToString = result.data?.get("codeVragen").toString()
                if (result.data?.get("codeVragen").toString() != "" && result.data?.get("codeVragen").toString() != "[]"){
                    createCodeVraag(codeQuestionsToString)
                }

                Log.d(ContentValues.TAG, "${result.id} => ${result.data?.get("multipleChoice").toString()}")

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


        btnSubmit.setOnClickListener {
            for(item: TextView in openVragenVeld){
                openVragenAntwoord.add(item.text.toString())
                Log.d("btn save openQ text", item.text.toString())
            }

            val examAnswers = hashMapOf(
                "openQuestionsAnswers" to openVragenAntwoord.toString().split(",", "[", "]").filter{ x:String? -> x != "" },
                "lon" to lon,
                "lat" to lat,
                "student" to uid
            )
            db.collection("studentanswers")
                .document(txtExamTitle.text.toString()+"-"+uid)
                .set(examAnswers)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference}")
                    Toast.makeText(applicationContext, "Examen is opgeslagen!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

        }
    }

}