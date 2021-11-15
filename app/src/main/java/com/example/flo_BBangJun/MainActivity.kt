package com.example.flo_BBangJun

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivityMainBinding
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var song: Song


//    // 미디어 플레이어
//    private var mediaPlayer: MediaPlayer? = null
//
//    // Gson
//    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        inputDummySongs()

        binding.mainPlayerLayout.setOnClickListener {
            Log.d("nowSongId", song.id.toString())
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

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

    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)

//        mediaPlayer = MediaPlayer.create(this, music)

        if (song.isPlaying) {
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility = View.GONE
        } else {
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
        }
    }

    //ROOM_DB
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_lilac",
                R.drawable.img_album_exp,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter (Hotter Remix)",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_lilac",
                R.drawable.img_album_exp,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter (Sweeter Remix)",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_lilac",
                R.drawable.img_album_exp,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_lilac",
                R.drawable.img_album_exp3,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level (IMLAY Remix)",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_lilac",
                R.drawable.img_album_exp3,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "소우주 (Mikrokosmos)",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Make It Right",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_lilac",
                R.drawable.img_album_exp5,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "궁금해",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_lilac",
                R.drawable.img_album_exp5,
                false
            )
        )


        val _songs = songDB.songDao().getSongs()
        Log.d("DB DATA", _songs.toString())

    }
}