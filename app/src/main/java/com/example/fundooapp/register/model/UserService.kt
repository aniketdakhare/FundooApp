package com.example.fundooapp.register.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserService : IUserService {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun addUserDetails(userDetails: User) {

        val user: MutableMap<String, Any> = HashMap()

        user["Name"] = userDetails.firstName
        user["lastName"] = userDetails.lastName
        user["emailId"] = userDetails.email

        fireStore.collection("users").document(firebaseAuth.currentUser!!.uid).set(user)
    }

    override fun authenticateUser(user: User, listener: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener{
            addUserDetails(user)
            listener(it.isSuccessful)
        }
    }
}