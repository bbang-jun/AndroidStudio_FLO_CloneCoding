package com.example.flo_BBangJun

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivityMainBinding
import com.example.flo_BBangJun.SongActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var player: Player // 쓰레드 2: 전역변수
    private val handler = Handler(Looper.getMainLooper()) // 쓰레드 7: 메인쓰레드에 메시지를 보낼 것이기 때문에 Looper.getMainLooper()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        val song= Song("라일락", "아이유(IU)", 10, false)
        player=Player(song.playTime, song.isPlaying) // 쓰레드3: 쓰레드2에서의 lateinit으로 인해 선언.객체만 생성하면 쓰레드가 시작되는 것이 아님. 생성자값을 인자로 넣어주기
        player.start() // 쓰레드4: 스레드 시작



//        Log.d("Log test", song.title + song.singer) // 로그캣

        // 버튼 클릭 시 수행할 동작을 지정하는 리스너(함수)
        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
//            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
//            intent.putExtra("music", song.music)
            startActivity(intent) // startActivity 메소드를 통해서 메인액티비티에서 송액티비티로 전환
            }


        binding.mainMiniplayerBtn.setOnClickListener {
            if(player.state==Thread.State.TERMINATED){ // 쓰레드의 재활용을 위함. terminated를 걸지 않으면 무한 재생 불가.
                player=Player(song.playTime, song.isPlaying)
                player.start()
            }
            player.isPlaying = true
            songPlayPause(true)

            val intent=Intent(this, SongActivity::class.java)
            intent.putExtra("play", false)
        }

        binding.mainMiniplayerpauseIB.setOnClickListener {
            player.isPlaying=false
            songPlayPause(false)

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

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
    }
}

