package com.example.reactivex

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reactivex.data.Animal
import com.example.reactivex.data.Zoo
import com.example.reactivex.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val observable = getObservableZoo();
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserverZoo())
    }

    private fun getObservableZoo(): Observable<Animal> {
        return Observable.create { emitter ->
            binding.btnChange.setOnClickListener {
                if (binding.etName.text.isNotEmpty() && binding.etHabitat.text.isNotEmpty() && binding.etWeapon.text.isNotEmpty()) {
                    Zoo.addNewAnimal(
                        binding.etName.text.toString(),
                        binding.etHabitat.text.toString(),
                        binding.etWeapon.text.toString()
                    )
                    emitter.onNext(Zoo.getAnimals().last())
                }
            }
            for (animal in Zoo.getAnimals()) {
                emitter.onNext(animal)
            }

            Log.d("tracking", "onObservableSubcribe - ${Thread.currentThread().name}")
        }
    }

    private fun getObserverZoo(): Observer<Animal> {
        return object : Observer<Animal> {
            override fun onSubscribe(d: Disposable) {
                Log.d("tracking", "onSubcribe - ${Thread.currentThread().name}")
            }

            override fun onError(e: Throwable) {
                Log.d("tracking", "onError")
            }

            override fun onComplete() {
                Log.d("tracking", "onCompleted")
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onNext(t: Animal) {
                Log.d(
                    "tracking",
                    t.name + " live in " + t.habitat + " and use " + t.weapon + " to hunt - ${Thread.currentThread().name}"
                )
                val textView = TextView(this@MainActivity)
                textView.textSize = 24F
                textView.text = t.name
                textView.setPadding(16, 16, 16, 16)
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                val layoutParams = LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(16, 16, 16, 16)
                textView.layoutParams = layoutParams
                textView.background = resources.getDrawable(R.drawable.bg_animal, null)
                binding.zooContainer.addView(textView)
            }
        }
    }
}