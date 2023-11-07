package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.pager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager


class CustomViewPager : ViewPager {

    //Esta clase sobreescribe el ViewPager nativo de android

    //La bandera isPaginEnabled impide que se navegue de una página (step) a otro
    //cuando se encuentra en false. Este es el comportamiento deseado para los steppers.
    private var isPagingEnabled = false;

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setPagingEnabled(b: Boolean) {
        isPagingEnabled = b
    }
}