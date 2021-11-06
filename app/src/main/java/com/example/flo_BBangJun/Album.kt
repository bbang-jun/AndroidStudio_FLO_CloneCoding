package com.example.flo_BBangJun

data class Album(
    var title : String ="", // 변수: 노래 제목
    var singer : String = "", // 변수: 가수 이름
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null
)
// step3: Album Data Class 생성