package com.example.flo_BBangJun

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
// 제목, 가수, 사진, 재생시간, 현재 재생시간, isplaying(재생 되고 있는지)
data class Song(
        var title : String ="", // 변수: 노래 제목
        var singer : String = "", // 변수: 가수 이름
        var second : Int = 0, // 변수: 재생시간이 현재 몇 초인지
        var playTime : Int = 0, // 총재생시간 200=3분20초
        var isPlaying : Boolean = false, // 재생되고 있는 지
        var music : String = "", // 변수: 어떤 음악이 재생되고 있었는지
        var coverImg: Int? = null, // 커버이미지
        var isLike: Boolean = false, // 좋아요 버튼 클릭 유무 판단
) {
        @PrimaryKey(autoGenerate = true) var id: Int=0 // autogenerate를 통해 id값을 자동으로 생성
}

