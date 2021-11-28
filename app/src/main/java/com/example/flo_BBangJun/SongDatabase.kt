package com.example.flo_BBangJun

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 6)
abstract class SongDatabase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao // 추상클래스
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
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build() // main thread
                }
            }
            return instance // 인스턴스 반환
        }
    }
}