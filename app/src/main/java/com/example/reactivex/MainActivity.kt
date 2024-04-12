package com.example.reactivex

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
    var initialX = 0.0F
    var initialY = 0.0F
    private var isDragging = false

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

        binding.btnChange.setOnClickListener {
            if (binding.etName.text.isNotEmpty() && binding.etHabitat.text.isNotEmpty()
                && binding.etWeapon.text.isNotEmpty()
            ) {
                Zoo.addNewAnimal(
                    binding.etName.text.toString(),
                    binding.etHabitat.text.toString(),
                    binding.etWeapon.text.toString()
                )
            }
        }

        Zoo.getAnimals()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {

            }
            .subscribe({

            }, {

            }, {

            })
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

            @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
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
                textView.setOnTouchListener { v, event ->

                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = event.rawX
                            initialY = event.rawY
                            // Handle initial touch (optional)
                            isDragging = false
                            true // Claim to handle touch events
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val deltaX = event.rawX - initialX
                            val deltaY = event.rawY - initialY
                            v.x += deltaX
                            v.y += deltaY
                            initialX = event.rawX
                            initialY = event.rawY
                            isDragging = true
                            true
                        }

                        MotionEvent.ACTION_UP -> {
                            if (!isDragging) {
                                // Handle click here if not dragging
                                v.performClick() // Or your custom click logic
                            }
                            isDragging = false // Reset flag on touch up

                            false // Don't consume ACTION_UP (optional)
                        }

                        else -> false
                    }

                }



                textView.setOnClickListener {
                    Toast.makeText(this@MainActivity, textView.text, Toast.LENGTH_SHORT).show()

                }
                layoutParams.setMargins(16, 16, 16, 16)
                textView.layoutParams = layoutParams
                textView.background = resources.getDrawable(R.drawable.bg_animal, null)
                binding.zooContainer.addView(textView)

            }
        }
    }
}