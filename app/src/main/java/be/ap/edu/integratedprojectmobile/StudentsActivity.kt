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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class StudentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)
        getLocation()
    }

    var locationNetwork: Location? = null
    var locationGps: Location? = null

    fun getLocation() {

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

        val uid = intent.getStringExtra("snummer") //Firebase.auth.currentUser?.uid
        Toast.makeText(applicationContext, uid.toString(), Toast.LENGTH_LONG).show()
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

            if (hasNetwork) { //hierop focussen; andere simulator nexus 9
                Log.d("hasNetwork", hasNetwork.toString())
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F) { p0 ->
                locationNetwork =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)//p0

                Log.d("locationNetwork", locationNetwork.toString())
                txtLat.text = locationNetwork!!.latitude.toString() + " [Network]"
                txtLon.text = locationNetwork!!.longitude.toString() + " [Network]"

                if (locationNetwork != null) {
                    if (uid != null) {
                        txtLat.text = locationNetwork!!.latitude.toString()
                        txtLon.text = locationNetwork!!.longitude.toString()

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