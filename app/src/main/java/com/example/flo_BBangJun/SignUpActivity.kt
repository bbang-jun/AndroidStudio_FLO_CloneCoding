package com.example.flo_BBangJun

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity(){

    lateinit var  binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

//    private fun getUser(): User{
//        val email: String = binding
//
//    }
}