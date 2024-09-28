package com.example.cronometro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.MessageFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var reset: MaterialButton
    private lateinit var start: MaterialButton
    private lateinit var stop: MaterialButton

    private var seconds: Int = 0
    private var minutes: Int = 0
    private var milliseconds: Long = 0
    private var startTime: Long = 0
    private var timeBuff: Long = 0
    private var updateTime: Long = 0

    private lateinit var handler: Handler

    private val runnable = object : Runnable {
        override fun run() {
            milliseconds = SystemClock.uptimeMillis() - startTime
            updateTime = timeBuff + milliseconds
            seconds = (updateTime / 1000).toInt()
            minutes = seconds / 60
            seconds %= 60
            val millis = (updateTime % 1000).toInt()

            textView.text = MessageFormat.format(
                "{0}:{1}:{2}",
                minutes,
                String.format(Locale.getDefault(), "%02d", seconds),
                String.format(Locale.getDefault(), "%02d", millis)
            )
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textview)
        reset = findViewById(R.id.reset)
        start = findViewById(R.id.start)
        stop = findViewById(R.id.stop)

        handler = Handler(Looper.getMainLooper())

        start.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(runnable, 0)
            reset.isEnabled = false
            stop.isEnabled = true
            start.isEnabled = false
        }

        stop.setOnClickListener {
            timeBuff += milliseconds
            handler.removeCallbacks(runnable)
            reset.isEnabled = true
            stop.isEnabled = false
            start.isEnabled = true
        }

        reset.setOnClickListener {
            milliseconds = 0L
            startTime = 0L
            timeBuff = 0L
            updateTime = 0L
            seconds = 0
            minutes = 0
            textView.text = "00:00:00"
        }

        textView.text = "00:00:00"
    }
}
