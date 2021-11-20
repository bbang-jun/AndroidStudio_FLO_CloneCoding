package com.example.flo_BBangJun

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Song::class, User::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao // 추상클래스
    abstract fun userDao(): UserDao // 추상클래스

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries().build() // main thread
                }
            }
            return instance // 인스턴스 반환
        }
    }
}