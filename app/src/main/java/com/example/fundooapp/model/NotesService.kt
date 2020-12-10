package com.example.fundooapp.model

import android.util.Log
import com.example.fundooapp.util.NotesOperation.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class NotesService : INotesService {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    init {
        initService()
    }

    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
    }


    override fun notesDbOperation(notes: Note, listener: (Boolean) -> Unit) {
        val note: MutableMap<String, Any> = HashMap()
        note[TITTLE] = notes.tittle
        note[CONTENT] = notes.content
        Log.e(notes.noteId, "saveNotesToFirebase: ")
        when (notes.operation) {
            ADD -> fireStore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .collection("Notes").document().set(note).addOnCompleteListener{listener(it.isSuccessful)}
            UPDATE -> fireStore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .collection("Notes").document(notes.noteId).update(note).addOnCompleteListener{listener(it.isSuccessful)}
            DELETE -> fireStore.collection("users").document(firebaseAuth.currentUser!!.uid)
                .collection("Notes").document(notes.noteId).delete().addOnCompleteListener{listener(it.isSuccessful)}
        }
    }

    override fun fetchNotesFromFireBase(listener: (List<Note>) -> Unit) {
        firebaseAuth.currentUser?.let {
            fireStore.collection("users").document(it.uid).collection("Notes")
                .addSnapshotListener{ querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
                    val notes: MutableList<Note> = mutableListOf()
                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            notes.add(Note(document.getString(TITTLE).toString(), document.getString(CONTENT).toString(), document.id, userId = it.uid))
                        }
                    }
                    listener(notes)
            }
        }
    }

    companion object {
        private const val TITTLE = "tittle"
        private const val CONTENT = "content"
    }
}
