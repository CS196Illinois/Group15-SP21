package com.example.cs196

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

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
    }

    fun insertItem(view: View) {
        val index = exampleList.size
        val newItem = ExampleItem(
            R.drawable.ic_baseline_emoji_emotions_24,
            "Task",
            "787878",
            "Futuimus et sum."
        )
        exampleList.add(index, newItem)
        adapter.notifyItemInserted(index)
    }

    public fun addItem(item: ExampleItem, int: Int) {
        exampleList.add(int, item)
        adapter.notifyItemInserted(int)
    }

    public fun replaceItem(item: ExampleItem, int: Int) {
        exampleList[int] = item
        adapter.notifyItemInserted(int)
    }


    fun removeItem(view: View) {
        val index = Random.nextInt(8)
        exampleList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"Task $position clicked", Toast.LENGTH_SHORT).show()
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
                val returnDate = data!!.getStringExtra("Date")
                val returnDescription = data!!.getStringExtra("Description")

                // New item.
                val dooblyItem = ExampleItem(
                        R.drawable.ic_baseline_emoji_emotions_24,
                        returnTitle,
                        returnDate,
                        returnDescription
                )

                // Adds new task.
                replaceItem(dooblyItem, spot)
            }
        }
    }



    private fun generateDummyList(size: Int): ArrayList<ExampleItem> {
        val list = ArrayList<ExampleItem>()
        for (i in 0 until size) {
            val drawable = when (i % 1) {
                else -> R.drawable.ic_baseline_emoji_emotions_24
            }
            val item = ExampleItem(drawable, "Task $i", "454545", "Ibsum et macilum de epistulae.")
            list += item
        }
        return list
    }
}