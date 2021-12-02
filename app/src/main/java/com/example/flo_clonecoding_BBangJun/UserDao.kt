package com.example.flo_clonecoding_BBangJun

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    // 유저의 정보를 넣을 수 있는 insert 함수
    @Insert
    fun insert(user: User)

    // 저장된 모든 유저의 정보를 가져올 수 있도록 하는 쿼리문
    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User>

    // 유저 한 명의 정보만 가져올 수 있도록 하는 쿼리문
    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): User?

//    @Query("SELECT * FROM UserTable WHERE id=:userId")
//    fun getUsers(userId: Int): User
}