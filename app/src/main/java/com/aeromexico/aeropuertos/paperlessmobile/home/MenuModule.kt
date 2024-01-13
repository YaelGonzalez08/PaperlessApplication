package com.aeromexico.aeropuertos.paperlessmobile.home

data class MenuModule(
    var name: String,
    var image: Int,
    var destination: (MenuModule)-> Unit
)

data class MenuModuleRecientes(
    var name: String,
    var image: Int,
)
