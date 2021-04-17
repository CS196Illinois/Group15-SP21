package com.example.cs196

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*

class DashboardActivity : AppCompatActivity() {

    lateinit var EnterCourseName: EditText
    lateinit var AddCourse: Button
    lateinit var context : Context
    lateinit var alarmManager : AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        EnterCourseName = findViewById(R.id.hello)
        AddCourse = findViewById(R.id.AddCourse)

        AddCourse.setOnClickListener {
            saveCourse()
        }

        context = this
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        create.setOnClickListener {
            val second = set_timer.text.toString().toInt() * 1000
            val intent = Intent(context,Receiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + second, pendingIntent)
            Log.d("DashboardActivity", "Create : " + Date().toString())
        }

        update.setOnClickListener {
            val second = set_timer.text.toString().toInt() * 1000
            val intent = Intent(context,Receiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + second, pendingIntent)
            Log.d("DashboardActivity", "Update : " + Date().toString())
        }

        cancel.setOnClickListener {
            val intent = Intent(context,Receiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            Log.d("DashboardActivity", "Cancel : " + Date().toString())
            alarmManager.cancel(pendingIntent)
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

    class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("DashboardActivity", "Received message")
        }
    }
}