package com.example.reactivex.data

import android.util.Log

object Zoo {
    var animalList = mutableListOf<Animal>(
        Animal("Lion", "Trophy", "Fang"),
        Animal("Bear", "Jungle", "Claw"),
        Animal("Snake", "Jungle", "Poison")
    )

    fun getAnimals() = animalList
    fun addNewAnimal(name: String, habitat: String, weapon: String){
        animalList.add(Animal(name, habitat, weapon))
        Log.d("Tracking", "New animal created")
    }
}