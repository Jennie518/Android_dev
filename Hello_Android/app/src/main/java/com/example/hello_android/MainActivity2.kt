package com.example.hello_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
//       get text view by id
        val textView: TextView = findViewById(R.id.textView)
        val buttonText = intent.getStringExtra("button_text_key") // Use the same key to retrieve the data
        textView.text = buttonText


    }
}