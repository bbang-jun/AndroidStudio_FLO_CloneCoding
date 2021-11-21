package com.example.flo_BBangJun

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0, // album의 pk는 임의로 지정해주기 위해 autogenerate 사용x
    var title : String? ="", // 변수: 노래 제목
    var singer : String? = "", // 변수: 가수 이름
    var coverImg: Int? = null,
)