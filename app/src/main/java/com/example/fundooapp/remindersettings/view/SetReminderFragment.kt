 package com.example.fundooapp.remindersettings.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundooapp.R
import com.example.fundooapp.databinding.FragmentSetReminderBinding
import com.example.fundooapp.model.Note
import com.example.fundooapp.remindersettings.view.helper.ReminderService
import com.example.fundooapp.remindersettings.viewmodel.SetReminderViewModel
import com.example.fundooapp.viewmodel.NotesSharedViewModel
import java.util.*


 class SetReminderFragment(private val note: Note) : DialogFragment() {

    private lateinit var binding: FragmentSetReminderBinding
    private lateinit var notesSharedViewModel: NotesSharedViewModel
    private lateinit var setReminderViewModel: SetReminderViewModel
    private lateinit var reminderCalendar: Calendar

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(1000, 750)    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_set_reminder, container, false)
        notesSharedViewModel = ViewModelProvider(requireActivity())[NotesSharedViewModel::class.java]
        setReminderViewModel = ViewModelProvider(this)[SetReminderViewModel::class.java]
        reminderCalendar = Calendar.getInstance()
        binding.setReminderViewModel = setReminderViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reminderDialogeCloseIcon.setOnClickListener{ dismiss() }
        handleDateOption()
        handleTimeOption()
        setReminder()
    }

     private fun setReminder() {
         binding.setReminderIcon.setOnClickListener {
             ReminderService(requireContext()).setReminder(reminderCalendar, note)
             notesSharedViewModel.setReminderTime(reminderCalendar)
             dismiss()
         }
     }

     private fun handleTimeOption() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.selectTime.text = DateFormat.format("hh:mm a", calendar)
        reminderCalendar.set(Calendar.HOUR_OF_DAY, hour)
        reminderCalendar.set(Calendar.MINUTE, minute)

        binding.selectTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { timePicker: TimePicker, hour: Int, minute: Int ->
                    val time = Calendar.getInstance()
                    time.set(Calendar.HOUR_OF_DAY, hour)
                    reminderCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    time.set(Calendar.MINUTE, minute)
                    reminderCalendar.set(Calendar.MINUTE, minute)

                    binding.selectTime.text = DateFormat.format("hh:mm a", time)
                },
                hour,
                minute,
                DateFormat.is24HourFormat(requireContext())
            )
            timePickerDialog.show()
        }
    }

    private fun handleDateOption() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.selectDate.text = DateFormat.format("MMM d, yyyy", calendar)
        reminderCalendar.set(Calendar.YEAR, year)
        reminderCalendar.set(Calendar.MONTH, month)
        reminderCalendar.set(Calendar.DATE, day)

        binding.selectDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, month, dayOfMonth ->
                    val datePickerCalender = Calendar.getInstance()
                    datePickerCalender.set(Calendar.YEAR, year)
                    datePickerCalender.set(Calendar.MONTH, month)
                    datePickerCalender.set(Calendar.DATE, dayOfMonth)
                    reminderCalendar.set(Calendar.YEAR, year)
                    reminderCalendar.set(Calendar.MONTH, month)
                    reminderCalendar.set(Calendar.DATE, dayOfMonth)

                    binding.selectDate.text = DateFormat.format("MMM d, yyyy", datePickerCalender)
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }
}