package com.example.fundooapp.fundoofirebaseauth.util

import android.util.Log
import com.example.fundooapp.model.User
import com.example.fundooapp.model.UserService
import com.example.fundooapp.util.AuthResponseDetails
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection


class HttpsHelper {

    fun loginUserWithEmailAndPassword(email: String, password: String): AuthResponseDetails {
        val url =
            URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$FIREBASE_KEY")
        val conn = url.openConnection() as HttpsURLConnection

        val str = "email=$email&password=$password&returnSecureToken=true"
        val postData = str.toByteArray(StandardCharsets.UTF_8)
        conn.instanceFollowRedirects = false;
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", postData.size.toString());
        conn.useCaches = false;

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setRequestProperty("Accept", "*/*")
        conn.readTimeout = 15000
        conn.connectTimeout = 10000
        conn.requestMethod = "POST"
        conn.doInput = true


        DataOutputStream(conn.outputStream).use { wr -> wr.write(postData) }
        conn.connect()

        val stream = readStream(conn.inputStream)

        Log.e(TAG, "loginUserWithEmailAndPassword: $stream")
        return AuthResponseDetails(
            registeredStatus = stream?.get("registered") as Boolean,
            idToken = stream["idToken"] as String, localId = stream["localId"] as String
        )
    }

    fun fetchDataFromFireStore(localId: String, idToken: String): User {
        val url =
            URL("https://firestore.googleapis.com/v1/projects/fundooapp-c6f1d/databases/(default)/documents/users/$localId?")
        val conn = url.openConnection() as HttpsURLConnection

        conn.setRequestProperty("Accept", "*/*")
        conn.setRequestProperty("Authorization", "Bearer $idToken")
        conn.setRequestProperty("charset", "utf-8");
        conn.readTimeout = 15000
        conn.connectTimeout = 10000
        conn.requestMethod = "GET"
        conn.doInput = true

        conn.connect()

        val stream = readStream(conn.inputStream)

        val name = ((stream?.get("fields") as MutableMap<*, *>)["name"] as MutableMap<String, String>)["stringValue"]
        val email = ((stream["fields"] as MutableMap<*, *>)["emailId"] as MutableMap<String, String>)["stringValue"]
        val imageUrl = ((stream["fields"] as MutableMap<*, *>)["profileImageUrl"] as MutableMap<String, String>)["stringValue"]
        Log.e(TAG, "fetchDataFromFireStore: name: $name :: email: $email :: imageUrl: $imageUrl ")
        return User(
            fullName = name.toString(),
            email = email.toString(),
            imageUrl = imageUrl.toString()
        )
    }

    private fun readStream(inputStream: InputStream?): MutableMap<*, *>? {

        val response = StringBuilder()
        BufferedReader(
            InputStreamReader(inputStream, "utf-8")
        ).use { br ->
            var responseLine: String?
            while (br.readLine().also { responseLine = it } != null) {
                response.append(responseLine!!.trim())
            }
        }
        Log.e(TAG, "readStream: ")

        return Gson().fromJson(response.toString(), MutableMap::class.java)
    }

    companion object {
        private const val TAG = "HttpsHelper"
        private const val FIREBASE_KEY = "AIzaSyDDwtZQb9PRjSNICPIEm4RTDuAt5hWf9Uk"
    }
}