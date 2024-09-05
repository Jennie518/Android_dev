package com.example.hello_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1:Button = findViewById(R.id.button)
        button1.setOnClickListener{
//            get button text
            val buttonText = button1.text
            val intent = Intent(this,MainActivity2::class.java)
            intent.putExtra("button_text_key", buttonText)
            startActivity(intent)
        }


        val button2:Button = findViewById(R.id.button2)
        button2.setOnClickListener{
            val buttonText = button2.text
            val intent = Intent(this,MainActivity2::class.java)
            intent.putExtra("button_text_key", buttonText)
        }

        val button3:Button = findViewById(R.id.button3)
        button3.setOnClickListener{
            val buttonText = button3.text
            val intent = Intent(this,MainActivity2::class.java)
            intent.putExtra("button_text_key", buttonText)
        }

        val button4:Button = findViewById(R.id.button4)
        button4.setOnClickListener{
            val buttonText = button4.text
            val intent = Intent(this,MainActivity2::class.java)
            intent.putExtra("button_text_key", buttonText)
        }

        val button5:Button = findViewById(R.id.button5)
        button5.setOnClickListener{
            val buttonText = button5.text
            val intent = Intent(this,MainActivity2::class.java)
            intent.putExtra("button_text_key", buttonText)
        }

    }
}