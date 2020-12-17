package com.example.fundooapp.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.Exception

class NotesService(private val dbHelper: DBHelper) : INotesService {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    init {
        initService()
    }

    private fun initService() {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
    }

    override fun addNotes(notes: Note, listener: (Note?, Exception?) -> Unit) {
        firebaseAuth.currentUser?.let { user ->
            val documentReference = fireStore.collection("users").document(user.uid)
                .collection("Notes").document()
            notes.noteId = documentReference.id
            notes.userId = user.uid

            Log.e(TAG, "addNotes: ${documentReference.id}")

            documentReference.set(notes).addOnSuccessListener {
                dbHelper.addNote(notes) {
                    listener(notes, null)
                }
            }.addOnFailureListener{
                listener(null, it)
            }
        }
    }

    override fun updateNotes(notes: Note, listener: (Boolean?, Exception?) -> Unit) {
        val note: MutableMap<String, Any> = HashMap()
        note[TITTLE] = notes.tittle
        note[CONTENT] = notes.content
        fireStore.collection("users").document(notes.userId)
            .collection("Notes").document(notes.noteId).update(note)
            .addOnSuccessListener {
                dbHelper.updateNote(notes) {
                    listener(it, null)
                }
                }.addOnFailureListener{
                listener(null, it)
            }
    }

    override fun deleteNotes(note: Note, listener: (Boolean?, Exception?) -> Unit) {
        fireStore.collection("users").document(note.userId)
            .collection("Notes").document(note.noteId).delete()
            .addOnSuccessListener {
                dbHelper.deleteNote(note) {listener(it, null)}
            }.addOnFailureListener{
                listener(null, it)
            }
    }

    override fun fetchNotes(listener: (List<Note>, Exception?) -> Unit) {
        Log.e(TAG, "fetchNotes: ")
        firebaseAuth.currentUser?.let {
            dbHelper.fetchNotes(it.uid) { notes ->
                listener(notes, null)
            }
        }
    }

    private fun fetchNotesFromFireStore(listener: (List<Note>) -> Unit) {
        firebaseAuth.currentUser?.let { it ->
            fireStore.collection("users").document(it.uid).collection("Notes").get()
                .addOnSuccessListener { querySnapshot ->
                    val notes: MutableList<Note> = mutableListOf()
                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            notes.add(
                                Note(
                                    document.getString(TITTLE).toString(),
                                    document.getString(CONTENT).toString(),
                                    document.getString("noteId").toString(),
                                    document.getString("userId").toString()
                                )
                            )
                        }
                    }
                    Log.e(TAG, "fetchNotesFromFireStore: Success ${notes.size}")
                    listener(notes)
                }.addOnFailureListener { e ->
                    listener(emptyList())
                    Log.e(TAG, "fetchNotesFromFireStore: Fail")
                    e.printStackTrace()
                }
        }
    }

    override fun updateLocalDB(listener: (Boolean?, Exception?) -> Unit) {
        dbHelper.clearNotes()
        fetchNotesFromFireStore { notesFromFireStore ->
            for (note in notesFromFireStore) {
                dbHelper.addNote(note) {}
            }
            listener(true, null)
        }
    }

    companion object {
        private const val TITTLE = "tittle"
        private const val CONTENT = "content"
        private const val TAG = "NotesService"
    }
}
