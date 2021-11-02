package com.example.flo_BBangJun

// 제목, 가수, 사진, 재생시간, 현재 재생시간, isplaying(재생 되고 있는지)
data class Song(
        var title : String ="", // 변수: 노래 제목
        var singer : String = "", // 변수: 가수 이름
//        var second : Int = 0, // 변수: 재생시간이 현재 몇 초인지
        var playTime : Int = 0, // 총재생시간 200=3분20초
        var isPlaying : Boolean = false, // 재생되고 있는 지
//        var music : String = "" // 변수: 어떤 음악이 재생되고 있었는지
)

