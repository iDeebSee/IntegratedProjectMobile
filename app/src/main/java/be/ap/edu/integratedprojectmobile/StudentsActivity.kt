package be.ap.edu.integratedprojectmobile

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class StudentsActivity : AppCompatActivity() {

    var getLon: String = ""
    var getLat: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)
        val layout = findViewById<LinearLayout>(R.id.LinearLayoutSA)
        val db = Firebase.firestore
        //val uid = intent.getStringExtra("snummer")

        getLocation()

        db.collection("exams")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    layout.addView(createNewTextView(document.data["name"].toString()))

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun createNewTextView(text:String): TextView {
        val uid = intent.getStringExtra("snummer")
        val lparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        lparams.setMargins(0, 0, 0, 15)
        val txt = TextView(this)
        txt.text = text
        txt.layoutParams = lparams
        Log.d("id", txt.id.toString())
        txt.setOnClickListener{
            val intent = Intent(this, StudentExamActivity::class.java)
            intent.putExtra("examName",txt.text.toString())
            intent.putExtra("snummer",uid)
            intent.putExtra("lon",getLon)
            intent.putExtra("lat",getLat)
            startActivity(intent)
            Log.d("child", txt.text.toString())
        }
        return  txt
    }

    var locationNetwork: Location? = null
    var locationGps: Location? = null

    fun getLocation() {
        val uid = intent.getStringExtra("snummer")
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

                        Firebase.firestore.collection("students")
                            .add(student)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot added with ID: ${documentReference.id}"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
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

                        Firebase.firestore.collection("students")
                            .add(student)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot added with ID: ${documentReference.id}"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
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