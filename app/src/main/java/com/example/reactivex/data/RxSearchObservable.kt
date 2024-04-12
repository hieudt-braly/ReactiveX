package com.example.reactivex.data

import android.database.Observable
import android.widget.SearchView
import io.reactivex.rxjava3.subjects.PublishSubject

object RxSearchObservable {
    fun fromView(searchView: android.widget.SearchView): PublishSubject<String> {
        val subject: PublishSubject<String> = PublishSubject.create()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                subject.onNext(newText)
                return true
            }
        })

        return subject
    }
}