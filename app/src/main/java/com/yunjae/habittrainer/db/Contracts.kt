package com.yunjae.habittrainer.db

import android.provider.BaseColumns

val DATABASE_NAME = "habittrainer.db"
val DATABASE_VERSION = 10

object HabitEntry: BaseColumns {
    val TABLE_NAME = "habit"
    val _ID        = "id"
    val TTILE_COL = "title"
    val DESCR_COL = "description"
    val IMAGE_COL = "image"
}