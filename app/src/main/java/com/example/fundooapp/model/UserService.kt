package com.example.fundooapp.model

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.fundooapp.util.AuthResponseDetails
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONObject


class UserService : IUserService {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var mStorageRef: StorageReference

    init {
        initService()
    }

    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        mStorageRef = FirebaseStorage.getInstance().reference
    }

    override fun authenticateUser(email: String, password: String, listener: (AuthResponseDetails) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            listener(AuthResponseDetails( registeredStatus = it.isSuccessful))
        }
    }

    override fun registerUser(user: User, listener: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) addUserDetailsToFireStore(user, listener)
            }
    }

    override fun resetPassword(emailId: String, listener: (Boolean) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(emailId).addOnCompleteListener {
            listener(it.isSuccessful)
        }
    }

    override fun facebookLogin(token: AccessToken, listener: (User, Boolean) -> Unit) {
        val credentials = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                val user: FirebaseUser? = firebaseAuth.currentUser
                fetchFacebookProfile { url ->
                    val userDetails = User(
                        fullName = user?.displayName.toString(),
                        email = user?.email.toString(),
                        imageUrl = url
                    )
                    addUserDetailsToFireStore(userDetails) {}
                    listener(userDetails, true)
                }
            } else listener(User(), false)
        }
    }

    override fun logoutUser() {
        LoginManager.getInstance().logOut()
        firebaseAuth.signOut()
    }

    private fun addUserDetailsToFireStore(userDetails: User, listener: (Boolean) -> Unit) {
        val user: MutableMap<String, Any> = HashMap()

        when (userDetails.fullName) {
            "" -> user[NAME] = "${userDetails.firstName} ${userDetails.lastName}"
            else -> user[NAME] = userDetails.fullName
        }

        user[EMAIL] = userDetails.email
        user[PROFILE_IMAGE_URL] = userDetails.imageUrl

        fireStore.collection(USER_COLLECTION).document(firebaseAuth.currentUser!!.uid).set(user)
            .addOnCompleteListener { listener(it.isSuccessful) }
    }

    override fun getUserDetails(localId: String, idToken: String, listener: (User) -> Unit) {
        firebaseAuth.currentUser?.let {
            Log.e(TAG, "getUserDetails: 95", )
            fireStore.collection(USER_COLLECTION).document(it.uid)
                .addSnapshotListener { value: DocumentSnapshot?, _: FirebaseFirestoreException? ->
                    Log.e(TAG, "getUserDetails: ${value?.getString(NAME).toString()}", )
                    listener(
                        User(
                            fullName = value?.getString(NAME).toString(),
                            email = value?.getString(EMAIL).toString(),
                            imageUrl = value?.getString(PROFILE_IMAGE_URL).toString()
                        )
                    )
                }
        }
    }

    override fun uploadImageToFirebase(uri: Uri) {
        val fileRef =
            mStorageRef.child("$USER_COLLECTION/${firebaseAuth.currentUser?.uid}/profile.jpg")
        fileRef.putFile(uri)
            .addOnCompleteListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    fireStore.collection(USER_COLLECTION).document(firebaseAuth.currentUser!!.uid)
                        .update(
                            PROFILE_IMAGE_URL, uri.toString()
                        )
                }
            }
    }

    override fun getLoginStatus(listener: (Boolean) -> Unit) {
        firebaseAuth.addAuthStateListener {
            listener(it.currentUser != null)
        }
    }

    private fun fetchFacebookProfile(listener: (String) -> Unit) {
        val request =
            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { _: JSONObject, graphResponse: GraphResponse ->
                listener(getImageUrl(graphResponse).toString())
            }
        val parameters = Bundle()
        parameters.putString("fields", "picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getImageUrl(response: GraphResponse): String? {
        var url: String? = null
        try {
            url = response.jsonObject
                .getJSONObject("picture")
                .getJSONObject("data")
                .getString("url")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return url
    }

    companion object {
        private const val NAME = "name"
        private const val EMAIL = "emailId"
        private const val PROFILE_IMAGE_URL = "profileImageUrl"
        private const val TAG = "UserService"
        private const val USER_COLLECTION = "users"
    }
}