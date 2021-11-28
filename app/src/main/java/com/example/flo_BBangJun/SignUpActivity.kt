package com.example.flo_BBangJun

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity(), SignUpView {

    lateinit var binding: ActivitySignupBinding

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
    private fun getUser(): User {
        val email: String =
            binding.signupStartidET.text.toString() + "@" + binding.signupEndidET.text.toString()
        val pwd: String = binding.signupPasswordET.text.toString()
        val name: String = binding.signupNameET.text.toString()

        return User(email, pwd, name)
    }

//    // 회원가입
//    private fun signUp(){
//        if(binding.signupStartidET.text.toString().isEmpty() ||  binding.signupEndidET.text.toString().isEmpty()){
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if(binding.signupPasswordET.text.toString() !=  binding.signupConfirmPasswordET.text.toString()){
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//
//        val users = userDB.userDao().getUsers()
//        Log.d("SIGNUPACT", users.toString())
//    }

    private fun signUp() {
        if (binding.signupStartidET.text.toString()
                .isEmpty() || binding.signupEndidET.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupNameET.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupPasswordET.text.toString() != binding.signupConfirmPasswordET.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)
        authService.signUp(getUser())

        Log.d("SIGNUPACT/ASNYC", "Hello, ")
    }


    override fun onSignUpLoading() {
        binding.signUpLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signUpLoadingPb.visibility = View.GONE

        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signUpLoadingPb.visibility = View.GONE

        when (code) {
            2016, 2017 -> {
                binding.signUpEmailErrorTv.visibility = View.VISIBLE
                binding.signUpEmailErrorTv.text = message
            }
        }
    }
}





//authService.signUp(getUser()).enqueue(object : Callback<AuthResponse> {
//    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//        Log.d("SIGNUPACT/API-RESPONSE", response.toString())
//
//        val resp = response.body()!!
//
//        Log.d("SIGNUPACT/API-RESPONSE-FLO", resp.toString())
//
//        when (resp.code) {
//            1000 -> finish()
//            2016, 2017 -> {
//                binding.signUpEmailErrorTv.visibility = View.VISIBLE
//                binding.signUpEmailErrorTv.text = resp.message
//            }
//        }
//    }
//
//    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//        Log.d("SIGNUPACT/API-ERROR", t.toString())
//    }
//
//})