package com.aeromexico.aeropuertos.paperlessmobile.home

import android.graphics.drawable.Drawable

data class MenuModule(
    var name: String,
    var image: Int,
    var destination: (MenuModule)-> Unit
)
