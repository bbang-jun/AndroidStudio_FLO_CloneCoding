package com.example.flo_BBangJun

import androidx.room.*


@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Insert
    fun likeAlbum(like: Like)

    // off -> on
    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikeAlbum(userId: Int, albumId: Int): Int?

    // on -> off
    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId: Int, albumId: Int)

    // 유저가 좋아요 한 것을 보관함 F에서 가져와야 함
    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId=AT.id WHERE LT.userId=:userId")
    fun getLikedAlbums(userId: Int): List<Album>
}