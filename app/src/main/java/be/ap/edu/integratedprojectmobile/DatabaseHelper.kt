package be.ap.edu.integratedprojectmobile

import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import be.ap.edu.integratedprojectmobile.DatabaseHelper
import kotlin.Throws
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "ADMINPASS", null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, $PW_COL TEXT )"
        sqLiteDatabase.execSQL(createTable)
        val firstPass = Password("admin")
        sqLiteDatabase.execSQL("INSERT INTO $TABLE_NAME ($PW_COL) VALUES ('" + firstPass.password+ "')")
        val result = sqLiteDatabase.execSQL("SELECT $PW_COL FROM $TABLE_NAME")
        Log.d("database check", result.toString())
        firstPass.password?.let { Log.d("---- first password ----", it) }
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(sqLiteDatabase)
    }

    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class)
    fun addAdminPass(password: String?): Boolean {
        val pass = password?.let { Password(it) }
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PW_COL, pass.toString())
        Log.d(TAG, "---- adding AdminPass: " + pass + " to " + TABLE_NAME)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class)
    fun updateAdminPass(newPass: String?, oldPass: String?) {
        val newPassword = newPass?.let { Password(it) }
        val oldPassword = oldPass?.let { Password(it) }
        val db = this.writableDatabase
        val query = "UPDATE " + TABLE_NAME + " SET " + PW_COL +
                " = '" + newPassword + "' WHERE " + PW_COL + " = '" + oldPassword + "'"
        Log.d(TAG, "updateName: query: $query")
        Log.d(TAG, "updateName: Setting name to $newPassword")
        db.execSQL(query)
    }//result = data.getString(0);

    //Log.d(TAG, "getAdminPass: query: " + query);
    //data.getString(0);
//    @get:Throws(
//        NoSuchProviderException::class,
//        NoSuchAlgorithmException::class
//    )
//    val adminPass: Cursor
//        get() {
//            val db = this.readableDatabase
//            val query = "SELECT " + PW_COL + " FROM " + TABLE_NAME + " LIMIT 1;"
//            var data = db.rawQuery(query, null)
//            var result = data.getString(0)
//
//            //Log.d(TAG, "getAdminPass: query: " + query);
//            Log.d(TAG, "the result: $result")
//            return data
//        }

    companion object {
        private const val TAG = "DatabaseHelper"
        private const val TABLE_NAME = "Admin"
        private const val ID_COL = "ID"
        private const val PW_COL = "Password"
    }
}