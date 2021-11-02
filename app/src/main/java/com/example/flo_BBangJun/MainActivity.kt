package com.example.flo_BBangJun

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivityMainBinding
import com.example.flo_BBangJun.SongActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var player: Player // 쓰레드 2: 전역변수
    private val handler = Handler(Looper.getMainLooper()) // 쓰레드 7: 메인쓰레드에 메시지를 보낼 것이기 때문에 Looper.getMainLooper()
    private var gson: Gson = Gson() // Gson
    private var song: Song = Song()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        val song= Song("라일락", "아이유(IU)", 0, 214, false, "lilac")
        player=Player(song.playTime, song.isPlaying) // 쓰레드3: 쓰레드2에서의 lateinit으로 인해 선언.객체만 생성하면 쓰레드가 시작되는 것이 아님. 생성자값을 인자로 넣어주기
        player.start() // 쓰레드4: 스레드 시작


        // 버튼 클릭 시 수행할 동작을 지정하는 리스너(함수)
        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            startActivity(intent) // startActivity 메소드를 통해서 메인액티비티에서 송액티비티로 전환
            }
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        binding.mainMiniplayerBtn.setOnClickListener {
            if(player.state==Thread.State.TERMINATED){ // 쓰레드의 재활용을 위함. (무한재생)
                player=Player(song.playTime, song.isPlaying)
                player.start()
            }
            player.isPlaying = true
            song.isPlaying = true
            songPlayPause(true)
            mediaPlayer?.start()
            val intent=Intent(this, SongActivity::class.java)
            intent.putExtra("play", false)
        }

        binding.mainMiniplayerpauseIB.setOnClickListener {
            player.isPlaying=false
            song.isPlaying = false
            songPlayPause(false)
            mediaPlayer?.pause()
            val intent=Intent(this, SongActivity::class.java)
            intent.putExtra("play", false)
        }


        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    fun songPlayPause(isplaying : Boolean){ // 클릭했을 때 재생-정지 전환해주는 함수
        if(isplaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainMiniplayerpauseIB.visibility = View.VISIBLE
        }
        else{
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainMiniplayerpauseIB.visibility = View.GONE
        }
    }

    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayertitleTV.text = song.title
        binding.mainMiniplayersingerTV.text = song.singer
        binding.songPlayerSB.progress=(song.second*1000/song.playTime)

        if(song.isPlaying){
            binding.mainMiniplayerpauseIB.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
        } else{
            binding.mainMiniplayerpauseIB.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }
    }

    inner class Player(private val playTime:Int, var isPlaying: Boolean): Thread(){
        private var second=0
        override fun run(){
            try {
                while (true) {
                    if (second >= playTime) {
                        handler.post{
                            second=0
                            binding.songPlayerSB.progress=0
                            binding.mainMiniplayerpauseIB.visibility=View.GONE
                            binding.mainMiniplayerBtn.visibility=View.VISIBLE
                        }
                        break
                    }

                    if (isPlaying) {
                        sleep(1000)
                        second++
                        handler.post {
                            binding.songPlayerSB.progress = second * 1000 / playTime
                        }
                    }
                }
            }catch(e:InterruptedException){
                Log.d("intterupt", "쓰레드 종료!")
            }
        }
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    override fun onPause() { // 생명주기에 의한 opPause 상태
        super.onPause()
        mediaPlayer?.pause() // 미디어플레이어 중지
        player.isPlaying = false // 스레드 중지
        song.isPlaying = false // 노래 중지 상태
        song.second = (binding.songPlayerSB.progress*song.playTime)/1000
    songPlayPause(false) // 정지 상태의 이미지로 전환
}

override fun onDestroy() {
    super.onDestroy()
    player.interrupt()
    mediaPlayer?.release() // mediaPlayer가 가지고 있던 리소스(라일락) 해제
    mediaPlayer = null // mediaPlayer 해제
}



    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) // mode_private: 이 앱에서만 sharedpreferences에 접근 가능
        // 값을 단순히 가져오는 것이기 때문에 sharedpreferences를 조작하지 않기에 editor가 필요 없음
        val jsonSong = sharedPreferences.getString("song", null) // songactivity에서의 editor에서 song으로 가져왔기 때문
        song = if(jsonSong == null){
            Song("라일락", "아이유(IU)", 0, 214, false, "lilac")
        } else{
            gson.fromJson(jsonSong, Song::class.java)
        }
        setMiniPlayer(song)
    }
}