package be.ap.edu.integratedprojectmobile.database

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import be.ap.edu.integratedprojectmobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class FirebaseDatabaseHelper{

    val db = Firebase.firestore

}