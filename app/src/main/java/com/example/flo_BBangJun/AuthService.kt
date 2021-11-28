package com.example.flo_BBangJun

import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.security.auth.callback.Callback

class AuthService {
    private lateinit var loginView: LoginView
    private lateinit var signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    //원래
    fun signUp(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signUpView.onSignUpLoading()

        authService.signUp(user).enqueue(object : retrofit2.Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUPACT/API-RESPONSE", response.toString())

                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("SIGNUPACT/API-RESPONFLO", response.toString())

                    when (resp.code) {
                        1000 -> signUpView.onSignUpSuccess()
                        else -> signUpView.onSignUpFailure(resp.code, resp.message)

                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t:Throwable){
                Log.d("SIGNUPACT/API-ERROR", t.toString())

                signUpView.onSignUpFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        loginView.onLoginLoading()


        authService.login(user).enqueue(object : retrofit2.Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGINACT/API-RESPONSE", response.toString())

                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!

                    Log.d("LOGINACT/API-RESPONFLO", response.toString())

                    when (resp.code) {
                        1000 -> loginView.onLoginSuccess(resp.result!!)
                        else -> loginView.onLoginFailure(resp.code, resp.message)
                    }
                }
            }


            override fun onFailure(call: Call<AuthResponse>, t:Throwable){
                Log.d("LOGINACT/API-ERROR", t.toString())

                loginView.onLoginFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    private fun getRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202")
            .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit
    }

//    fun login(user: User){
//        val authService = getRetrofit(). create(AuthRetrofitInterface::class.java)
//
//        loginView.onLoginLoading()
//
//        authService.login(user).enquene(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>){
//                Log.d("LOGINACT/API-RESPONSE", response.toString())
//
//                if(response.isSuccessful && response.code()==200){
//                    val resp=response.body()!!
//                    Log.d("LOGINACT/API-RESPONSE-FLO", resp.toString())
//
//                    when(resp.code){
//                        1000->loginView.onLoginSuccess(resp.result!!)
//                        else->loginView.onLoginFailure(resp.code, resp.message)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable){
//                Log.d("LOGINACT/API-ERROR", t.toString())
//                loginView.onLoginFailure(400, t.message.toString())
//            }
//        })
//    }


}