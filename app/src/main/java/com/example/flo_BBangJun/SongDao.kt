package com.example.flo_BBangJun

import androidx.room.*


@Dao // annotation
interface SongDao {
    @Insert // 삽입
    fun insert(song: Song)

    @Update // 갱신
    fun update(song: Song)

    @Delete // 삭제
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable") // 테이블의 모든 값을 가져와서
    fun getSongs(): List<Song> // 리스트에 반환

    @Query("SELECT * FROM SongTable WHERE id = :id") // 특정 id값인 song의 모든 테이블 반환
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id") // 해당 id값을 가진 song의 isLike 값 갱신
    fun updateIsLikeById(isLike :Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>
}