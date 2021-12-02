package com.example.flo_clonecoding_BBangJun

interface SignUpView {

    fun onSignUpLoading()
    fun onSignUpSuccess()
    fun onSignUpFailure(code: Int, message: String)
}