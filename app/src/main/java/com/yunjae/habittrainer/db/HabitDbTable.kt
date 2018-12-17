package com.yunjae.habittrainer.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.yunjae.habittrainer.Habit
import com.yunjae.habittrainer.db.HabitEntry.DESCR_COL
import com.yunjae.habittrainer.db.HabitEntry.IMAGE_COL
import com.yunjae.habittrainer.db.HabitEntry.TABLE_NAME
import com.yunjae.habittrainer.db.HabitEntry.TTILE_COL
import com.yunjae.habittrainer.db.HabitEntry._ID
import java.io.ByteArrayOutputStream


class HabitDbTable(context: Context) {

    private val TAG = HabitDbTable::class.java.simpleName

    private val dbHelper = HabitTrainerDb(context)

    fun store(habit: Habit): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(TTILE_COL, habit.titile)
            put(DESCR_COL, habit.description)
            put(IMAGE_COL, toByteArray(habit.image))
        }



        val id = db.transaction{
            insert(TABLE_NAME, null, values)
        }

        /*db.beginTransaction()
        val id = try {
            val returnValue = db.insert(TABLE_NAME, null, values)
            returnValue
        } finally {
            db.endTransaction()
        }
        db.close()*/
        Log.d(TAG, "Stored new habit to the DB $habit")
        return id
    }

    fun readAllHabit(): List<Habit> {
        val columns = arrayOf(_ID, TTILE_COL, DESCR_COL,
            IMAGE_COL)
        val order = "$_ID desc"

        val db = dbHelper.readableDatabase

        val cursor = db.doQuery(TABLE_NAME, columns, orderBy = order)

        val habits = parseHabitFrom(cursor)
        return habits
    }

    private fun parseHabitFrom(cursor: Cursor): MutableList<Habit> {
        val habits = mutableListOf<Habit>()

        while (cursor.moveToNext()) {
            val title = cursor.getString(TTILE_COL)
            val desc = cursor.getString(DESCR_COL)
            val bitmap = cursor.getBlob(IMAGE_COL)

            habits.add(Habit(title, desc, bitmap))
        }
        cursor.close()
        return habits
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}

private fun Cursor.getString(columnName: String) = getString(getColumnIndex(columnName))

private fun Cursor.getBlob(columnName: String): Bitmap {
    val bytes = getBlob(getColumnIndex(columnName))
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

private fun SQLiteDatabase.doQuery(table: String, columns: Array<String>, selection: String?= null,
                                          selectionArgs: Array<String>? = null, groupBy: String? = null,
                                          having: String? = null, orderBy: String? = null): Cursor {
    return query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
}

private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T): T {
    beginTransaction()
    val result = try {
        val returnValue = this.function()
        setTransactionSuccessful()
        returnValue
    } finally {
        endTransaction()
    }
    close()
    return result
}