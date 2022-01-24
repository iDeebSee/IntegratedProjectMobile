package be.ap.edu.integratedprojectmobile.student

import android.content.ContentValues
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentViewExamActivity : AppCompatActivity() {
    lateinit var mcQuestionAswers:Array<String>
    lateinit var openQuestionAswers:Array<String>
    lateinit var codeQuestionAswers:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_view_exam)

        val db = Firebase.firestore


        val layout = findViewById<LinearLayout>(R.id.llstudentViewExam)
        val exam = intent.getStringExtra("exam").toString()
        val student = intent.getStringExtra("student").toString()


        db.collection("studentanswers").document("${exam}-${student}")
            .get()
            .addOnSuccessListener{ result ->

                mcQuestionAswers =
                    result.data?.get("mcQuestionsAnswers").toString().split(",","[", "]").filter{ x:String? -> x != "" }.toTypedArray()
                openQuestionAswers =
                    result.data?.get("openQuestionsAnswers").toString().split(",","[", "]").filter{ x:String? -> x != "" }.toTypedArray()
                codeQuestionAswers =
                    result.data?.get("codeQuestionsAnswers").toString().split(",","[", "]").filter{ x:String? -> x != "" }.toTypedArray()

            }

        fun addTitle(text:String): TextView {
            val lparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            lparams.setMargins(0, 0, 0, 15)

            val txt = TextView(this)
            txt.text = text
            txt.setTextColor(Color.BLACK)
            txt.setTextSize(20f)
            return txt
        }

        var tellerForOpenQuestions = 0
        fun createNewTextView(text:String):TextView {

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
            for (i in 0 .. tellerForOpenQuestions){
                txt.hint = openQuestionAswers[i]
            }
            txt.isEnabled = false
            tellerForOpenQuestions++
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
            txt.isEnabled = false
            layout.addView(txt)

            for(i in 1 .. teller){
                val view = layoutInflater.inflate(R.layout.code_vraag_layout, null)
                val etAnswer = view.findViewById<EditText>(R.id.etAnswer)
                etAnswer.hint = codeQuestionAswers[i-1]
                etAnswer.isEnabled = false
                //if (codeQuestionAswers.contains())
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
                if (mcQuestionAswers.contains(rdbNewRadioButton.text)){
                    rdbNewRadioButton.isChecked = true

                }
                rdbNewRadioButton.isEnabled = false
                radioButtonArray.add(rdbNewRadioButton)
                Log.d("radiobutton", rdbNewRadioButton.text.toString())
            }

            return radioButtonArray
        }



        fun radioButtonGroup(title:TextView, radioButtons: ArrayList<RadioButton>): RadioGroup {

            val radioButtonGroup = RadioGroup(this)
            radioButtonGroup.layoutParams  = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            (radioButtonGroup.layoutParams as LinearLayout.LayoutParams).setMargins(0, 0, 0, 15)
            radioButtonGroup.addView(title)
            for(radio in radioButtons){
                radioButtonGroup.addView(radio)
            }

            Log.d("radiobuttonGroup", radioButtonGroup.toString())
            return radioButtonGroup

        }



        db.collection("exams").document(exam)
            .get()
            .addOnSuccessListener { result ->
                val codeQuestions: ArrayList<String> = ArrayList()
                val mcQuestions: ArrayList<String> = ArrayList()
                val oQuestions: ArrayList<String> = ArrayList()
                val amount: Any? = result.data?.get("radioGroupAmount")//.toString().split(",").toString()
                val theArray = amount.toString().split(",", "[", "]", " ").toTypedArray()
                Log.d("amount", amount.toString())
                var theAmount: String = ""
                val mySize = theArray.size
                Log.d("theArray size", mySize.toString())
                val mcResultaat = result.data?.get("multipleChoice").toString().split(",","[", "]").filter{ x:String? -> x != "" }.toTypedArray()
                val resultaat = result.data?.get("radioGroup").toString().split(",","[", "]").filter{ x:String? -> x != "" }.toTypedArray()
                val titles = result.data?.get("mcTitles").toString().split(",", "[", "]").filter { x:String? -> x != "" }.toTypedArray()
                Log.d("titles size", titles.size.toString())
                var title:String = ""
                //var teller = 0
                for (i in 0 until titles.size){
                    title = titles[i]
                    Log.d("radioGroupSize i:", i.toString())
                }
                for (item in 0 until mySize - 1){
                    if (theArray[item] != "[" && theArray[item] != "]" && theArray[item] != ""){
                        theAmount = theArray[item]
                        Log.d("the amount $item: ", theAmount)
                        for (i in 0 until theAmount.toInt()){

                            mcQuestions.add(mcResultaat[i])
                        }
                        layout.addView(radioButtonGroup(addTitle(title),radioButton(mcQuestions.toTypedArray())))

                    }
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


    }
}