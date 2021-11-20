package com.example.flo_BBangJun

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(){
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTV.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}