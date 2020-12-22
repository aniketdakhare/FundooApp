package com.example.fundooapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class NotesSharedViewModel: ViewModel() {

    private val _reminderTime = MutableLiveData<Calendar>()
    val reminderTime = _reminderTime as LiveData<Calendar>

    fun setReminderTime(reminderCalendar: Calendar) {
        Log.e("Notes ShareViewmodel", "setReminderTime: ${reminderCalendar.timeInMillis}", )
        _reminderTime.value = reminderCalendar
    }
}