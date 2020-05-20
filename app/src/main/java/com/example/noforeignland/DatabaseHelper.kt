package com.example.noforeignland

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory? = null) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME  + " TEXT, " +
                COLUMN_ICON + " TEXT, " +
                COLUMN_LAT + " DOUBLE, " +
                COLUMN_LONG + " DOUBLE " + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addManyFeatures(featureList: List<Feature>){
        val values = ContentValues()
        val db = this.writableDatabase

        try {
            db.beginTransaction()


            featureList.forEach{
                values.put(COLUMN_ID, it.properties.id)
                values.put(COLUMN_NAME, it.properties.name.removePrefix(" "))
                values.put(COLUMN_ICON, it.properties.icon)
                values.put(COLUMN_LAT,it.geometry.coordinates[1])
                values.put(COLUMN_LONG,it.geometry.coordinates[0])
                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
                values.clear()
            }
            db.setTransactionSuccessful()
        } catch (ex: Exception){
            Log.w("Exception:", ex)
        } finally {
            db.endTransaction()
        }
    }

    fun getAllFeatures(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_NAME ASC", null)
    }

    fun databaseExist() : Boolean{
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val result: Boolean = !cursor.isAfterLast
        cursor.close()

        return  result
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "NFL_db.db"
        val TABLE_NAME = "name"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "username"
        val COLUMN_ICON = "icon"
        val COLUMN_LAT = "latitude"
        val COLUMN_LONG = "longitude"
    }
}































