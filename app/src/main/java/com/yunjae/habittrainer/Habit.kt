package com.yunjae.habittrainer

import android.graphics.Bitmap

data class Habit(val titile: String, val description: String, val image: Bitmap)

fun getSampleHabits(): List<Habit> {
    return listOf(
        //Habit("Go for a walk", "A nice walk in the sun gets you ready for the day ahead", R.drawable.walk),
        //Habit("Drink a glass of water", "A rfreshing glass of water gets you hydrated", R.drawable.water)
    )
}