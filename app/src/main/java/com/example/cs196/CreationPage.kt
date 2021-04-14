package com.example.cs196

import android.R
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.creation_page.*
import kotlinx.android.synthetic.main.example_item.view.*


class CreationPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.cs196.R.layout.creation_page)
    }

    fun storeItem(view: View) {
        //Adding variables.
        var edit_text_description = edit_text_description.text.toString()
        var edit_text_title = edit_text_title.text.toString()
        var edit_text_date = edit_text_date.text.toString()

        //Inserting data.
        Log.i("Add course ended", edit_text_date)
        val intent = Intent()
        intent.putExtra("Title", edit_text_title)
        intent.putExtra("Date", edit_text_date)
        intent.putExtra("Description", edit_text_description)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}