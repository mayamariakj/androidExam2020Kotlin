package com.example.noforeignland

import android.database.Cursor
import android.os.Bundle
import android.widget.LinearLayout.VERTICAL
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    lateinit var adapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewMain.layoutManager = LinearLayoutManager(this)

        recyclerViewMain.addItemDecoration(
            DividerItemDecoration(
                recyclerViewMain.context,
                VERTICAL
            )
        )

        runOnUiThread {
            val featureList: ArrayList<Feature> = getFeaturesFromDatabase()

            if(featureList.isEmpty()){
                AlertDialog.Builder(this).setTitle("Error").setMessage(getString(R.string.no_internet)).setPositiveButton("OK", null).show()
            }
            else {
                adapter = MainAdapter(featureList)
                recyclerViewMain.adapter = adapter

                featureSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        adapter.filter.filter(newText)
                        return false
                    }
                })
            }
        }
    }

    private fun getFeaturesFromDatabase(): ArrayList<Feature> {
        val dbHandler = DatabaseHelper(this)

        val features_list = ArrayList<Feature>()
        val db_cursor: Cursor? = dbHandler.getAllFeatures()

        db_cursor!!.moveToFirst()

        while (!db_cursor.isAfterLast) {
            val name: String = db_cursor.getString(db_cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
            val id: Long = db_cursor.getLong(db_cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))
            val icon: String = db_cursor.getString(db_cursor.getColumnIndex(DatabaseHelper.COLUMN_ICON))
            val lat: Double = db_cursor.getDouble(db_cursor.getColumnIndex(DatabaseHelper.COLUMN_LAT))
            val long: Double = db_cursor.getDouble(db_cursor.getColumnIndex(DatabaseHelper.COLUMN_LONG))

            features_list.add(Feature(Feature.Properties(name, icon, id), Feature.Geometry(arrayOf(lat, long))))

            db_cursor.moveToNext()
        }
        return features_list
    }
}
