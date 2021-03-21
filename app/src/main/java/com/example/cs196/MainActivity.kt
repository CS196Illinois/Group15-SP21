package com.example.cs196

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var EnterCourseName: EditText
    lateinit var AddCourse: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EnterCourseName = findViewById(R.id.EnterCourseName)
        AddCourse = findViewById(R.id.AddCourse)

        AddCourse.setOnClickListener {
            saveCourse()
        }
    }

    private fun saveCourse() {
        val name = EnterCourseName.text.toString().trim()

        if(name.isEmpty()) {
            EnterCourseName.error = "Please enter a name"
            return
        }
         
        val ref = FirebaseDatabase.getInstance().getReference("courses")
        val courseID = ref.push().key.toString()
        val course = Course(courseID, name)

        ref.child(courseID).setValue(course)

    }
}