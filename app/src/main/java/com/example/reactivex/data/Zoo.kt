package com.example.reactivex.data

import android.util.Log
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

object Zoo {

    private val animalSubject = ReplaySubject.create<Animal>()
    private val animalList = mutableListOf<Animal>(
        Animal("Lion", "Trophy", "Fang"),
        Animal("Bear", "Jungle", "Claw"),
        Animal("Snake", "Jungle", "Poison")
    )
    init {
        animalList.forEach {
            animalSubject.onNext(it)
        }
    }
    fun getAnimals() = animalSubject
    fun addNewAnimal(name: String, habitat: String, weapon: String) {
        val animal = Animal(name, habitat, weapon)
        animalSubject.onNext(animal)
    }
}