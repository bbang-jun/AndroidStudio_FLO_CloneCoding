package com.example.flo_BBangJun

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() { // 콜론을 이용해서 앱콤팻 상속 받기 (안드로이드에서 액티비티 기능을 사용 가능하게 하는 클래스)
    lateinit var binding: ActivitySongBinding
    // 뷰 바인딩을 하게 되면 xml과 연결되어 있는 이름이 반전된 바인딩 클래스를 만들어 줌. ex) 송액티비티->액티비티송바인딩
    private lateinit var player:Player // 쓰레드 2: 전역변수
    private val song: Song = Song() // 데이터 렌더링 1 (Song.kt클래스를 전역변수로 받아옴)
    private val handler = Handler(Looper.getMainLooper()) // 쓰레드 7: 메인쓰레드에 메시지를 보낼 것이기 때문에 Looper.getMainLooper()


    // 미디어 플레이어 1 (?: null값이 들어올 수 있음)
//    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) { // 액티비티가 처음 생성될 때 처음으로 실행되는 함수 onCreate
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)  // 바인딩 초기화
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) // 상태바 제거
        setContentView(binding.root) // xml에 있는 요소들을 가져와서 쓰게 해주는 함수 root는 activity_song의 최상단 뷰

        initSong()

        player=Player(song.playTime, song.isPlaying) // 쓰레드3: 쓰레드2에서의 lateinit으로 인해 선언.객체만 생성하면 쓰레드가 시작되는 것이 아님. 생성자값을 인자로 넣어주기
        player.start() // 쓰레드4: 스레드 시작


        if(intent.hasExtra("title") && intent.hasExtra("singer")){

            binding.songSongtitleTV.text = intent.getStringExtra("title")
            binding.songSongsingerTV.text = intent.getStringExtra("singer")
        }

        binding.songNuguIB.setOnClickListener { // binding.뷰 아이디(홈프래그먼트로 돌아가기)
            finish()
        }

        binding.songRepeatOffIB.setOnClickListener { // 반복재생off -> 반복재생on1
            binding.songRepeatOffIB.visibility = View.GONE
            binding.songRepeatOn1IB.visibility = View.VISIBLE
        }

        binding.songRepeatOn1IB.setOnClickListener { // 반복재생on1 -> 반복재생on2
            binding.songRepeatOn1IB.visibility = View.GONE
            binding.songRepeatOn2IB.visibility = View.VISIBLE
        }

        binding.songRepeatOn2IB.setOnClickListener { // 반복재생on2 -> 반복재생off
            binding.songRepeatOn2IB.visibility = View.GONE
            binding.songRepeatOffIB.visibility = View.VISIBLE
        }

        binding.songPlaybuttonIV.setOnClickListener { // 클릭했을 때 재생 중인 상태로 전환
            if(player.state==Thread.State.TERMINATED){
                player=Player(song.playTime, song.isPlaying)
                player.start()
            }
            player.isPlaying=true
            songPlayPause(true)
//            song.isPlaying = true // 재생이되니까.
//            mediaPlayer?.start()
        }

        binding.songPausebuttonIV.setOnClickListener { // 클릭했을 때 정지 상태로 전환
            player.isPlaying=false
            songPlayPause(false)
//            song.isPlaying = false // 멈춘거니까.
//            mediaPlayer?.pause()
        }

        binding.songRandomplayoffIB.setOnClickListener {  // randomplayoff -> randomplayon
            binding.songRandomplayoffIB.visibility = View.GONE
            binding.songRandomplayOnIB.visibility = View.VISIBLE
        }

        binding.songRandomplayOnIB.setOnClickListener {  // randomplayon -> randomplayoff
            binding.songRandomplayOnIB.visibility = View.GONE
            binding.songRandomplayoffIB.visibility = View.VISIBLE
        }
    }

    private fun initSong(){ // 데이터 렌더링 2 : 전역변수에 받아온 값들을 넣고
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")){
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
//            song.second = intent.getIntExtra("second", 0)
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
//            song.music = intent.getStringExtra("music")!! // !!를 붙이지 않으면 오류
//            val music = resources.getIdentifier(song.music, "raw", this.packageName) // 리소스를 반환해주어야 mp3 사용 가능


            binding.songEndtimeTV.text=String.format("%02d:%02d", song.playTime/60, song.playTime%60) // 데이터 렌더링 3: 각각의 뷰에 렌더링
            binding.songSongtitleTV.text=song.title // 송액티비티의 제목에 렌더링
            binding.songSongsingerTV.text=song.singer // 송액티비티의 가수에 렌더링
            songPlayPause(song.isPlaying)
//            mediaPlayer = MediaPlayer.create(this, music) // 받아온(반환한) 리소스를 mediaplayer에 전달해주어서 올려야함. lilac이 mediaplayer에 연동이 되었음.
        }
    }

    fun songPlayPause(isplaying : Boolean){ // 클릭했을 때 재생-정지 전환해주는 함수
        if(isplaying){
            binding.songPlaybuttonIV.visibility = View.GONE
            binding.songPausebuttonIV.visibility = View.VISIBLE
        }
        else{
            binding.songPlaybuttonIV.visibility = View.VISIBLE
            binding.songPausebuttonIV.visibility = View.GONE
        }
    }

    // 쓰레드 1: inner: 코틀린에서는 내부에 클래스가 있어도 서로 다른 클래스가 됨. 위 클래스의 binding을 사용하기 위해 inner를 통해 내부 클래스로 만들어줌 따라서 위의 변수들을 사용할 수 있음
    inner class Player(private val playTime:Int, var isPlaying: Boolean) : Thread(){ // 스레드를 사용하기 위해 스레드 상속 (스레드의 역할을 할 수 있는 클래스) playtime은 바뀌지 않기 때문에 val
        private var second = 0 // 시간이 지나가는 타이머를 만들어야 하기 때문에 시간 정보가 필요. 이 클래스 내부에서만 사용을 위해 private
//        private var songtime =0

        override fun run() { // 쓰레드5: run 함수 내부의 코드는 player.start()와 동시에 실행 (run 내부의 함수가 종료되면 쓰레드 종료)
            try{
                while(true){
                    if (second >= playTime) {
                        handler.post{
                            second=0
                            binding.songPlayerSB.progress=0
                            binding.songPausebuttonIV.visibility=View.GONE
                            binding.songPlaybuttonIV.visibility=View.VISIBLE
                            binding.songStarttimeTV.text=String.format("%02d:%02d", 0, 0)
                        }
                        break
                    }

                    if (isPlaying){ // 재생 중일때만 내부의 코드 동작
                        sleep(1000) // 1초마다(sleep은 스레드 내부에서만 사용하는 함수)
                        second++ // 변수 second에 1씩 더하기
                        handler.post{ // 쓰레드8: 메인쓰레드에 뷰렌더링을 대신 해달라고 하는 역할 (handler.post 대신 runOnUiThread 사용해도 동일)
                            // 쓰레드 8: progress 바 진행
                            binding.songPlayerSB.progress=second*1000/playTime
//                            songtime=second*1000/playTime
                            // %02d: 변수가 한 자리이면 0을 앞에 추가 ex) 1분 = 01분
                            // 워크스레드에서 뷰렌더링 작업을 하지 못하기 때문에 이 코드만 쓰면 오류가 남(중단됨). 1. 핸들러 사용 2. 앱콤팻액티비티에서 제공하는 함수인 runonui 스레드
                            binding.songStarttimeTV.text=String.format("%02d:%02d", second/60, second%60) // 쓰레드 6: second++을 한다고 해서 시간이 1씩 더해지는게 보여지지 않기 때문
                        }
                    }
                }
            }catch(e: InterruptedException){
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }

//    override fun onPause() {
//        super.onPause()
//        mediaPlayer?.pause()
//        player.isPlaying = false
//        song.isPlaying = false
//        song.second = (binding.songPlayerSB.progress*song.playTime)/1000
//        songPlayPause(false)
//    }




    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
    }
}