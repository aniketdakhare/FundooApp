package com.example.fundooapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundooapp.util.NotesDisplayType
import java.util.*

class NotesSharedViewModel: ViewModel() {

    private val _reminderTime = MutableLiveData<Calendar>()
    val reminderTime = _reminderTime as LiveData<Calendar>

    private val _notesDisplayType = MutableLiveData<NotesDisplayType>()
    val notesDisplayType = _notesDisplayType as LiveData<NotesDisplayType>

    fun setReminderTime(reminderCalendar: Calendar) {
        Log.e("Notes ShareViewmodel", "setReminderTime: ${reminderCalendar.timeInMillis}", )
        _reminderTime.value = reminderCalendar
    }

    fun setNotesDisplayType(notesDisplayType: NotesDisplayType) {
        _notesDisplayType.value = notesDisplayType
    }
}