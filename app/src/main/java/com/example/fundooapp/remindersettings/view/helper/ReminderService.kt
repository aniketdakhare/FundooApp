package com.example.fundooapp.remindersettings.view.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.fundooapp.model.Note
import com.example.fundooapp.notification.localnotification.ReminderReceiver
import java.util.*

class ReminderService(val context: Context) {
    fun setReminder(reminderCalendar: Calendar, note: Note) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra("tittle", note.tittle)
        intent.putExtra("text", note.content)
        intent.putExtra("noteId", note.noteId)
        intent.putExtra("userId", note.userId)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderCalendar.timeInMillis, pendingIntent)
    }
}
