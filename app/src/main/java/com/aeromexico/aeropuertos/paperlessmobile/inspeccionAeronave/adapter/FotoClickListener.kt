package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.adapter



interface FotoClickListener {
    fun onView(foto: String);

    fun onDelete(foto: String);
}