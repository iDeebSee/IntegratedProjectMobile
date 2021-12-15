package be.ap.edu.integratedprojectmobile

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExamDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_dashboard)

        val db = Firebase.firestore
        val txtExamTitle = findViewById<TextView>(R.id.txtExamTitle)
        val examName=intent.getStringExtra("examName")
        val layout = findViewById<LinearLayout>(R.id.layoutOefeningen)
        txtExamTitle.text = examName.toString()


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
            txt.hint = text
            return  txt
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

                oQuestions.addAll(listOf(result.data?.get("openQuestions").toString()))

                if (result.data?.get("openQuestions").toString() != ""){
                    for (item in oQuestions){
                        layout.addView(createNewTextView(item))
                    }

                }

                Log.d(TAG, "${result.id} => ${result.data?.get("multipleChoice").toString()}")

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}