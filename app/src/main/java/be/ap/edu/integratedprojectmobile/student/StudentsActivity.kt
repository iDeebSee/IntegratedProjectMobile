package be.ap.edu.integratedprojectmobile.student

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.LocationManager
import android.provider.Settings
import android.location.Location
import android.util.Log
import android.view.KeyEvent
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class StudentsActivity : AppCompatActivity() {

//    val uid = intent.getStringExtra("snummer").toString()
    var getLon: String = ""
    var getLat: String = ""
    val db = Firebase.firestore
    var examsDone:List<String> = ArrayList<String>()
    var tempList = ArrayList<String>()
    private val studentExam = mutableMapOf<String, String>()
    private val studentExamSecond = ArrayList<String>()
    private val studentExamAnswers = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)
        val layout = findViewById<LinearLayout>(R.id.LinearLayoutSA)
        val uid = intent.getStringExtra("snummer").toString()


        getLocation()
        db.collection("studentanswers").get().addOnSuccessListener { result ->
            for (documt in result){
                if (documt.get("student") == uid){
                    studentExamAnswers.add(documt.get("exam").toString())
                }
            }
        }

        db.collection("exams")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (!(studentExamAnswers.contains(document.data["name"].toString()))){
                        layout.addView(createNewTextView(document.data["name"].toString()))
                    }
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }



    fun createNewTextView(text:String): TextView {


        val exam:String = ""
        val uid = intent.getStringExtra("snummer").toString()
        val lparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        lparams.setMargins(0, 0, 0, 15)
        val txt = TextView(this)
        txt.text = text
        txt.textSize = 20f
        txt.layoutParams = lparams
        txt.setTextColor(Color.RED)

        Log.d("id", txt.id.toString())

                    txt.setOnClickListener {
                        val intent = Intent(this, StudentExamActivity::class.java)
                        intent.putExtra("examName", txt.text.toString())
                        intent.putExtra("snummer", uid)
                        intent.putExtra("lon", getLon)
                        intent.putExtra("lat", getLat)
                        startActivity(intent)
                        Log.d("child", txt.text.toString())
                    }

        return  txt
    }

    var locationNetwork: Location? = null
    var locationGps: Location? = null

    fun getLocation() {
        val uid = intent.getStringExtra("snummer").toString()
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        }

        //val uid = intent.getStringExtra("snummer") //Firebase.auth.currentUser?.uid
        //Toast.makeText(applicationContext, uid.toString(), Toast.LENGTH_LONG).show()
        val txtLat = findViewById<TextView>(R.id.txtLat)
        val txtLon = findViewById<TextView>(R.id.txtLon)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps || hasNetwork) {
            if (hasGps) {
                Log.d("hasGPS", hasGps.toString())
                Log.d("uid", uid.toString())

                locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)//p0
                Log.d("locationGPS", locationGps.toString())

                if (locationGps != null) {
                    if (uid != null) {
                        txtLat.text = locationGps!!.latitude.toString()
                        txtLon.text = locationGps!!.longitude.toString()

                        getLon = locationGps!!.longitude.toString()
                        getLat = locationGps!!.latitude.toString()

                        val student = hashMapOf(
                            "longitude" to locationGps!!.longitude,
                            "latitude" to locationGps!!.latitude,
                            "student" to uid
                        )

                        //db.document(uid).update(student as Map<String, Any>)

//                        Firebase.firestore.collection("students")
//                            .add(student)
                        db.collection("students").document(uid).update(student as Map<String, Any>)
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "Document updated"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error updating document", e)
                            }
                    }
                }

                val localGpsLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation

            }

            if (hasNetwork) {
                Log.d("hasNetwork", hasNetwork.toString())
                locationNetwork =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)//p0

                Log.d("locationNetwork", locationNetwork.toString())

                if (locationNetwork != null) {
                    if (uid != null) {
                        txtLat.text = locationNetwork!!.latitude.toString()
                        txtLon.text = locationNetwork!!.longitude.toString()

                        getLon = locationNetwork!!.longitude.toString()
                        getLat = locationNetwork!!.latitude.toString()

                        val student = hashMapOf(
                            "longitude" to locationNetwork!!.longitude,
                            "latitude" to locationNetwork!!.latitude,
                            "student" to uid
                        )

                        //Firebase.firestore.collection("students")
                            //.add(student)
                        db.collection("students").document(uid).update(student as Map<String, Any>)
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "Document updated"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error updating document", e)
                            }
                    }
            }

            val localNetworkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (localNetworkLocation != null)
                locationNetwork = localNetworkLocation
        }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

}