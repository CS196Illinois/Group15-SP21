package com.example.cs196

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt


class MainActivity : AppCompatActivity(), ExampleAdapter.OnItemClickListener {
    private val exampleList = generateDummyList(10)
    private val adapter = ExampleAdapter(exampleList, this)
    private var spot = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val list: SharedPreferences = this.getSharedPreferences("Recycler List", 0)

        val mIth = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.RIGHT
                ) {
                    override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: ViewHolder, target: ViewHolder
                    ): Boolean {
                        val fromPos = viewHolder.adapterPosition
                        val toPos = target.adapterPosition
                        // move item in `fromPos` to `toPos` in adapter.
                        return true // true if moved, false otherwise
                    }

                    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                        // remove from adapter
                        exampleList.removeAt(viewHolder.adapterPosition)

                        // Notify adapter
                        adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                }).attachToRecyclerView(recycler_view)

    }

    fun insertItem(view: View) {
        val index = exampleList.size
        //examplelist.size
        val intent = Intent(this, CreationPage::class.java).apply {
            putExtra("Task $index", index)
        }
        spot = index
        startActivityForResult(intent, 1)
        adapter.notifyItemInserted(index)
    }

    public fun addItem(item: ExampleItem, int: Int) {
        exampleList.add(int, item)
        adapter.notifyItemInserted(int)

        //Save list
        //saveArrayList(exampleList, "Key")
    }

    public fun replaceItem(item: ExampleItem, int: Int) {
        exampleList[int] = item
        adapter.notifyItemInserted(int)

        //Save list
        //saveArrayList(exampleList, "Key")
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Task $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = exampleList[position]

        val intent = Intent(this, CreationPage::class.java).apply {
            putExtra("Task $position", position)
        }
        spot = position
        startActivityForResult(intent, 0)
    }


    // This method is called when the second activity finishes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that it is the SecondActivity with an OK result
        Log.i("ClickTest", "Click got through")
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("ClickTest", "Click REALLY got through")

                // Get String data from Intent
                val returnTitle = data!!.getStringExtra("Title")
                val returnDateString = data!!.getStringExtra("Date")
                val returnDescription = data!!.getStringExtra("Description")
                val returnDate = Date()
                returnDate.setTime(data!!.getLongExtra("date", -1))
                val day: Int = returnDate.day
                val month: Int = returnDate.month
                val year: Int = returnDate.year - 100
                val dateString = month.toString() + "/" + day.toString() + "/" + year.toString()

                // New item.
                val dooblyItem = ExampleItem(
                        R.drawable.ic_baseline_emoji_emotions_24,
                        returnTitle,
                        dateString,
                        returnDescription,
                        returnDate

                        )

                // Adds new task.
                replaceItem(dooblyItem, spot)
            }
        }

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("AddButtonTest", "Button Got Through")

                // Get String data from Intent
                val returnTitle = data!!.getStringExtra("Title")
                val returnDescription = data!!.getStringExtra("Description")
                val returnDate = Date()
                returnDate.setTime(data!!.getLongExtra("date", -1))
                val day: Int = returnDate.day
                val month: Int = returnDate.month
                val year: Int = returnDate.year - 100
                val dateString = month.toString() + "/" + day.toString() + "/" + year.toString()
                val date = year.toString() + month.toString() + day.toString()
                // New item.
                val scrooblyItem = ExampleItem(
                        R.drawable.ic_baseline_emoji_emotions_24,
                        returnTitle,
                        dateString,
                        returnDescription,
                        returnDate
                )

                /**part of backend to add google calendar events
                 */
                //val date2 : Long = 1620178182000
                val intent = Intent(Intent.ACTION_INSERT)
                intent.setData(CalendarContract.Events.CONTENT_URI)
                intent.putExtra(CalendarContract.Events.TITLE, returnTitle)
                intent.putExtra(CalendarContract.Events.DESCRIPTION, returnDescription)
                intent.putExtra(CalendarContract.Events.ALL_DAY, true)
                intent.putExtra(CalendarContract.Events.DTSTART, Calendar.getInstance().timeInMillis + 60*60*1000)
                startActivity(intent)

                // Adds new task.
                addItem(scrooblyItem, exampleList.size)

                /**part of backend to send the data to firebase
                 */
                val ref = FirebaseDatabase.getInstance().getReference("tasks")
                val taskID = ref.push().key.toString()
                //val t = ExampleItem(0, returnTitle, dateString, returnDescription, returnDate)
                val t = ExampleItem2(returnTitle, dateString, returnDescription)

                ref.child(taskID).setValue(t)

            }
        }
    }

//    fun saveArrayList(list: java.util.ArrayList<ExampleItem>?, key: String?) {
//        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        val editor: SharedPreferences.Editor = prefs.edit()
//        val gson = Gson()
//        val json: String = gson.toJson(list)
//        editor.putString(key, json)
//        editor.apply()
//        Log.i("Array saved", json)
//    }

//    fun getArrayList(key: String): java.util.ArrayList<ExampleItem?>? {
//        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        val gson = Gson()
//        val json: String = prefs.getString(key, null)
//        val type: Type = object : TypeToken<java.util.ArrayList<String?>?>() {}.getType()
//        return gson.fromJson(json, type)
//    }

    private fun generateDummyList(size: Int): ArrayList<ExampleItem> {
        val list = ArrayList<ExampleItem>()
        val date = Date()
        for (i in 0 until size) {
            val drawable = when (i % 5) {
                0 -> R.drawable.ic_happy
                1 -> R.drawable.ic_smile
                else -> R.drawable.ic_baseline_emoji_emotions_24
            }
            val item = ExampleItem(drawable, "Task $i", "454545", "Ibsum et macilum de epistulae.", date)
            list += item
        }
        return list
    }
}