package be.ap.edu.integratedprojectmobile

import android.util.Log
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom
import kotlin.Throws
import kotlin.experimental.and

class Password(password: String) {
    var password: String?
    private fun hashMe(password: String, salt: String): String? {
        var generatedPassword: String? = null
        try {
            // Create MessageDigest instance for MD5
            val md = MessageDigest.getInstance("SHA-256")

            // Add password bytes to digest
            md.update(salt.toByteArray())

            // Get the hash's bytes
            val bytes = md.digest(password.toByteArray())

            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(
                    Integer.toString((bytes[i] and 0xff.toByte()) + 0x100, 16)
                        .substring(1)
                )
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return generatedPassword
    }

    companion object {
        // Always use a SecureRandom generator
        @get:Throws(
            NoSuchAlgorithmException::class,
            NoSuchProviderException::class
        )
        private val salt: String

        // Create array for salt

        // Get a random salt

            // return salt
            private get() {
                // Always use a SecureRandom generator
                val sr = SecureRandom.getInstance("SHA1PRNG")

                // Create array for salt
                val salt = ByteArray(16)

                // Get a random salt
                sr.nextBytes(salt)

                // return salt
                return salt.toString()
            }
    }

    init {
        this.password = hashMe(password, salt)
        Log.d("password in teh hash calss ", this.password!!)
    }
}