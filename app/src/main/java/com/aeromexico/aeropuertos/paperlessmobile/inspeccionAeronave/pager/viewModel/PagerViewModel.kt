package com.aeromexico.aeropuertos.paperlessmobile.inspeccionAeronave.pager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PagerViewModel: ViewModel() {
    val currentStep = MutableLiveData<Int>(0);
    val cantidad_steps = MutableLiveData<Int>(3);

    fun siguienteStep(){
        if(this.currentStep.value!! < this.cantidad_steps.value!!){
            this.setStep(this.currentStep.value!! + 1);
        }
    }

    fun anteriorStep(){
        if(this.currentStep.value!! > 0){
            this.setStep(this.currentStep.value!! - 1);
        }
    }

    fun setStep(step: Int){
        this.currentStep.value = step;
    }

    //Reiniciar VM
    fun reiniciarVM(){
        currentStep.value = 0
    }
}