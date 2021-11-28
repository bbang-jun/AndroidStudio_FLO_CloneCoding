package com.example.flo_BBangJun

interface SignUpView {

    fun onSignUpLoading()
    fun onSignUpSuccess()
    fun onSignUpFailure(code: Int, message: String)
}