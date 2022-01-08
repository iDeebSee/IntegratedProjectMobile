package be.ap.edu.integratedprojectmobile.exam

import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import be.ap.edu.integratedprojectmobile.R
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
        val btnSave = findViewById<Button>(R.id.btnSaveOplossing)
        val openVragenAntwoord : ArrayList<String> = ArrayList()
        val openVragenVeld : ArrayList<TextView> = ArrayList()
        val mcVragenAntwoord : ArrayList<String> = ArrayList()
        val mcVragenVeld : ArrayList<RadioButton> = ArrayList()
        val codeVragenAntwoord : ArrayList<String> = ArrayList()
        val codeVragenVeld : ArrayList<TextView> = ArrayList()

        fun addTitle(text:String):TextView{
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

        var textViewTeller = 0
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
                codeVragenVeld.add(etAnswer)
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
                mcVragenVeld.add(rdbNewRadioButton)
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

        db.collection("exams").document(examName.toString())
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
                            //Log.d("$i", theAmount)
                            mcQuestions.add(mcResultaat[i])
                            //teller++
                        }
                        //Log.d("teller", teller.toString())
                        //Log.d("title", resultaat[teller])
                        //Log.d("radioGroupSize", radioGroupSize.toString())
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

                Log.d(TAG, "${result.id} => ${result.data?.get("multipleChoice").toString()}")

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


        btnSave.setOnClickListener {
            for(item:TextView in openVragenVeld){

                openVragenAntwoord.add(item.text.toString())
                Log.d("btn save openQ text", item.text.toString())
            }

            for(item:RadioButton in mcVragenVeld){
                if (item.isChecked()){
                    mcVragenAntwoord.add(item.text.toString())
                }else{
                    item.isChecked = false
                }

            }
            for (item:TextView in codeVragenVeld){
                codeVragenAntwoord.add(item.text.toString())
                Log.d("code questions",item.text.toString())
            }

            val examAnswers = hashMapOf(
                "openQuestionsAnswers" to openVragenAntwoord.toString().split(",", "[", "]").filter{ x:String? -> x != "" },
                "mcQuestionsAnswers" to mcVragenAntwoord.toString().split(",", "[", "]").filter{ x:String? -> x != "" },
                "codeQuestionsAnswers" to codeVragenAntwoord.toString().split(",", "[", "]").filter{ x:String? -> x != "" }

            )
            db.collection("solutions")
                .document(txtExamTitle.text.toString())
                .set(examAnswers)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
                    Toast.makeText(applicationContext, "Examen is opgeslagen!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            openVragenAntwoord.clear()
            mcVragenAntwoord.clear()
            codeVragenAntwoord.clear()

        }
    }
}