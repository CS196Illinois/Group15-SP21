package com.example.cs196

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.creation_page.*
import kotlinx.android.synthetic.main.example_item.view.*
import java.util.*


class CreationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.cs196.R.layout.creation_page)
    }

    fun getDateFromDatePicker(datePicker: DatePicker): Date? {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.getTime()
    }

    fun storeItem(view: View) {
        //Adding variables.
        var edit_text_description = edit_text_description.text.toString()
        var edit_text_title = edit_text_title.text.toString()
        var edit_text_date = edit_date.toString()
        var edit_date_full = getDateFromDatePicker(edit_date)


        //Inserting data.
        Log.i("Add course ended", edit_text_date)
        val intent = Intent()
        intent.putExtra("Title", edit_text_title)
        intent.putExtra("Date_String", edit_text_date)
        intent.putExtra("Description", edit_text_description)
        intent.putExtra("date", edit_date_full!!.getTime());
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}