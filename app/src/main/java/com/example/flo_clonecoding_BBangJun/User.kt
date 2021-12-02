package com.example.flo_clonecoding_BBangJun

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(

//    var name: String,
    var email: String, // email
    var password: String, // 비밀번호
    var name: String,
){
    @PrimaryKey(autoGenerate = true) // primary key로 id값을 자동으로 생성
    var id: Int = 0
}