package com.example.flo_clonecoding_BBangJun

data class LockerAlbum(
    var title : String ="", // 변수: 노래 제목
    var singer : String = "", // 변수: 가수 이름
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null
)
