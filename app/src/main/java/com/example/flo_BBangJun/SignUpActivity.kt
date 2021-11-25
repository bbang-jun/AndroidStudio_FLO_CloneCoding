package com.example.flo_BBangJun

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity(){

    lateinit var  binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupConfirmSigninBT.setOnClickListener {
            signUp()
            finish()
        }
    }

    // 사용자가 입력한 값을 가져오는 함수
    private fun getUser(): User{
        val email: String = binding.signupStartidET.text.toString() + "@" + binding.signupEndidET.text.toString()
        val pwd: String = binding.signupPasswordET.text.toString()
//        val name: String = binding.signupNameET.text.toString()

        return User(email, pwd) // ,name
    }

    // 회원가입
    private fun signUp(){
        if(binding.signupStartidET.text.toString().isEmpty() ||  binding.signupEndidET.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signupPasswordET.text.toString() !=  binding.signupConfirmPasswordET.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val users = userDB.userDao().getUsers()
        Log.d("SIGNUPACT", users.toString())
    }
}