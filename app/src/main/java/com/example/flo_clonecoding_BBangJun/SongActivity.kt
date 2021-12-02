package com.example.flo_clonecoding_BBangJun

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.flo_clonecoding_BBangJun.databinding.ActivitySongBinding
import com.example.flo_clonecoding_BBangJun.databinding.ToastLikeonBinding
import java.util.*
import java.util.concurrent.TimeUnit

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding

    //dlrj
    // 미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null
    lateinit var timer: Timer

    private var songs = ArrayList<Song>()
    private var nowPos = 0 // 현재 포지션을 0으로 초기화
    private lateinit var songDB: SongDatabase // 전역변수로 songDB선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
    }

    override fun onPause() {
        super.onPause()


        mediaPlayer?.pause() // 미디어플레이어 중지 (-38.0)
        songs[nowPos].second = (songs[nowPos].playTime * binding.songPlayerSB.progress) / 1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        // sharedpreference로 현재 보여지고 있는 song의 id 저장
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    // 앱이 종료될 때 리소스 해제
    override fun onDestroy() {
        super.onDestroy()

        // 미디어플레이어 중첩 방지
        timer.interrupt() // 스레드를 해제함
        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해제
        mediaPlayer = null // 미디어플레이어 해제
    }

    private fun initPlayList(){ // songs에 song리스트 추가
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initSong() { // sharedprefence의 id와 songlist를 비교해서 nowpos 찾음
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){ // id(autogenereated로 생성된 id) = songId(shared로부터 받아온 id값)
                return i
            }
        }
        return 0
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        binding.songSongtitleTV.text = song.title
        binding.songSongsingerTV.text = song.singer
        binding.songStarttimeTV.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndtimeTV.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIUIV.setImageResource(song.coverImg!!)
        binding.songPlayerSB.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

        if (song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)

        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        mediaPlayer = MediaPlayer.create(this, music)
    }

    private fun initClickListener() {
        binding.songNuguIB.setOnClickListener {
            finish()
        }

        binding.songPlaybuttonIV.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPausebuttonIV.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPreviousbuttonIB.setOnClickListener {
            moveSong(-1)
        }

        binding.songNextbuttonIB.setOnClickListener {
            moveSong(+1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun moveSong(direct: Int){ // 노래 이동

        if (nowPos + direct < 0){

            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt() // 새로운 노래 재생을 위해 쓰레드에 인터럽
        startTimer()

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()어 해제

        setPlayer(songs[nowPos])
    }

    // 좋아요 버튼
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id) // songs의 isLike가 갱신될 때 db에도 저장

        if (isLike){ // true
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            Toastlike.createToast(context=this, "좋아요 한 곡이 취소되었습니다.")?.show()
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            Toastlike.createToast(context=this, "좋아요 한 곡에 담겼습니다.")?.show()
        }
    }

    object Toastlike {

        fun createToast(context: Context, message: String): Toast? {
            val inflater = LayoutInflater.from(context)
            val binding: ToastLikeonBinding =
                DataBindingUtil.inflate(inflater, R.layout.toast_likeon, null, false)

            binding.tvSample.text = message

            return Toast(context).apply {
                setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 200.toPx())
                duration = Toast.LENGTH_LONG
                view = binding.root
            }
        }

        private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    }


    private fun setPlayerStatus(isPlaying: Boolean) {
        timer.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if (isPlaying) {
            binding.songPlaybuttonIV.visibility = View.GONE
            binding.songPausebuttonIV.visibility = View.VISIBLE

            mediaPlayer?.start()
        } else {
            binding.songPlaybuttonIV.visibility = View.VISIBLE
            binding.songPausebuttonIV.visibility = View.GONE

            mediaPlayer?.pause()
        }
    }


    inner class Timer(private val playTime: Int = 0, var isPlaying: Boolean = false) : Thread() {
        private var second: Long = 0

        @SuppressLint("SetTextI18n")
        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(1000)
                        second++

                        runOnUiThread {
                            binding.songPlayerSB.progress =
                                (second * 1000 / playTime).toInt()
                            binding.songStarttimeTV.text = String.format(
                                "%02d:%02d",
                                TimeUnit.SECONDS.toMinutes(second),
                                second % 60
                            )
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("SONG", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
}
//class SongActivity : AppCompatActivity() { // 콜론을 이용해서 앱콤팻 상속 받기 (안드로이드에서 액티비티 기능을 사용 가능하게 하는 클래스)
//    lateinit var binding: ActivitySongBinding
//    // 뷰 바인딩을 하게 되면 xml과 연결되어 있는 이름이 반전된 바인딩 클래스를 만들어 줌. ex) 송액티비티->액티비티송바인딩
//    private lateinit var player:Player // 쓰레드 2: 전역변수
//    private val song: Song = Song() // 데이터 렌더링 1 (Song.kt클래스를 전역변수로 받아옴)
//    private val handler = Handler(Looper.getMainLooper()) // 쓰레드 7: 메인쓰레드에 메시지를 보낼 것이기 때문에 Looper.getMainLooper()
//    // 미디어 플레이어 1 (?: null값이 들어올 수 있음)
//    private var mediaPlayer: MediaPlayer? = null
//    // Gson(중간다리역할)-라이브러리이기 때문에 라이브러리 추가
//    private var gson: Gson = Gson()
//    lateinit var timer: Timer
//
//    override fun onCreate(savedInstanceState: Bundle?) { // 액티비티가 처음 생성될 때 처음으로 실행되는 함수 onCreate
//        super.onCreate(savedInstanceState)
//        binding = ActivitySongBinding.inflate(layoutInflater)  // 바인딩 초기화
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        ) // 상태바 제거
//        setContentView(binding.root) // xml에 있는 요소들을 가져와서 쓰게 해주는 함수 root는 activity_song의 최상단 뷰
//
//        initSong()
//
//        player=Player(song.playTime, song.isPlaying) // 쓰레드3: 쓰레드2에서의 lateinit으로 인해 선언.객체만 생성하면 쓰레드가 시작되는 것이 아님. 생성자값을 인자로 넣어주기
//        player.start() // 쓰레드4: 스레드 시작
//
//
////        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying") && intent.hasExtra("music")){
////
////            binding.songSongtitleTV.text = intent.getStringExtra("title")
////            binding.songSongsingerTV.text = intent.getStringExtra("singer")
////            binding.songStarttimeTV.text=intent.getIntExtra("second", 0)
////        }
//
//        binding.songNuguIB.setOnClickListener { // binding.뷰 아이디(홈프래그먼트로 돌아가기)
//            val intent = Intent(this, MainActivity::class.java)
//            finish()
//        }
//
//        binding.songRepeatOffIB.setOnClickListener { // 반복재생off -> 반복재생on1
//            binding.songRepeatOffIB.visibility = View.GONE
//            binding.songRepeatOn1IB.visibility = View.VISIBLE
//        }
//
//        binding.songRepeatOn1IB.setOnClickListener { // 반복재생on1 -> 반복재생on2
//            binding.songRepeatOn1IB.visibility = View.GONE
//            binding.songRepeatOn2IB.visibility = View.VISIBLE
//        }
//
//        binding.songRepeatOn2IB.setOnClickListener { // 반복재생on2 -> 반복재생off
//            binding.songRepeatOn2IB.visibility = View.GONE
//            binding.songRepeatOffIB.visibility = View.VISIBLE
//        }
//
//        binding.songPlaybuttonIV.setOnClickListener { // 클릭했을 때 재생 중인 상태로 전환
//            if(player.state==Thread.State.TERMINATED){
//                player=Player(song.playTime, song.isPlaying)
//                player.start()
//            }
//            player.isPlaying=true
//            songPlayPause(true)
//            song.isPlaying = true // 재생이되니까.
//            mediaPlayer?.start()
//        }
//
//        binding.songPausebuttonIV.setOnClickListener { // 클릭했을 때 정지 상태로 전환
//            player.isPlaying=false
//            songPlayPause(false)
//            song.isPlaying = false // 멈춘것이므로.
//            mediaPlayer?.pause()
//        }
//
//        binding.songRandomplayoffIB.setOnClickListener {  // randomplayoff -> randomplayon
//            binding.songRandomplayoffIB.visibility = View.GONE
//            binding.songRandomplayOnIB.visibility = View.VISIBLE
//        }
//
//        binding.songRandomplayOnIB.setOnClickListener {  // randomplayon -> randomplayoff
//            binding.songRandomplayOnIB.visibility = View.GONE
//            binding.songRandomplayoffIB.visibility = View.VISIBLE
//        }
//    }
//
//    private fun initSong(){ // 데이터 렌더링 2 : 전역변수에 받아온 값들을 넣고
//        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying") && intent.hasExtra("music")){
//            song.title = intent.getStringExtra("title")!!
//            song.singer = intent.getStringExtra("singer")!!
//            song.second = intent.getIntExtra("second", 0)
//            song.playTime = intent.getIntExtra("playTime", 0)
//            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
//            song.music = intent.getStringExtra("music")!! // !!를 붙이지 않으면 오류
//            val music = resources.getIdentifier(song.music, "raw", this.packageName) // 리소스를 반환해주어야 mp3 사용 가능
//
//            binding.songStarttimeTV.text=String.format("%02d:%02d", song.second/60, song.second%60)
//            binding.songEndtimeTV.text=String.format("%02d:%02d", song.playTime/60, song.playTime%60) // 데이터 렌더링 3: 각각의 뷰에 렌더링
//            binding.songSongtitleTV.text=song.title // 송액티비티의 제목에 렌더링
//            binding.songSongsingerTV.text=song.singer // 송액티비티의 가수에 렌더링
//            songPlayPause(song.isPlaying)
//            mediaPlayer = MediaPlayer.create(this, music) // 받아온(반환한) 리소스를 mediaplayer에 전달해주어서 올려야함. lilac이 mediaplayer에 연동이 되었음.
//        }
//    }
//
//    fun songPlayPause(isplaying : Boolean){ // 클릭했을 때 재생-정지 전환해주는 함수
//        if(isplaying){
//            binding.songPlaybuttonIV.visibility = View.GONE
//            binding.songPausebuttonIV.visibility = View.VISIBLE
//        }
//        else{
//            binding.songPlaybuttonIV.visibility = View.VISIBLE
//            binding.songPausebuttonIV.visibility = View.GONE
//        }
//    }
//
//    // 쓰레드 1: inner: 코틀린에서는 내부에 클래스가 있어도 서로 다른 클래스가 됨. 위 클래스의 binding을 사용하기 위해 inner를 통해 내부 클래스로 만들어줌 따라서 위의 변수들을 사용할 수 있음
//    inner class Player(private val playTime:Int, var isPlaying: Boolean) : Thread(){ // 스레드를 사용하기 위해 스레드 상속 (스레드의 역할을 할 수 있는 클래스) playtime은 바뀌지 않기 때문에 val
//        private var second = 0 // 시간이 지나가는 타이머를 만들어야 하기 때문에 시간 정보가 필요. 이 클래스 내부에서만 사용을 위해 private
//
//        override fun run() { // 쓰레드5: run 함수 내부의 코드는 player.start()와 동시에 실행 (run 내부의 함수가 종료되면 쓰레드 종료)
//            try{
//                while(true){
//                    if (second >= playTime) {
//                        handler.post{
//                            second=0
//                            binding.songPlayerSB.progress=0
//                            binding.songPausebuttonIV.visibility=View.GONE
//                            binding.songPlaybuttonIV.visibility=View.VISIBLE
//                        }
//                        break
//                    }
//
//                    if (isPlaying){ // 재생 중일때만 내부의 코드 동작
//                        sleep(1000) // 1초마다(sleep은 스레드 내부에서만 사용하는 함수)
//                        second++ // 변수 second에 1씩 더하기
//                        handler.post{ // 쓰레드8: 메인쓰레드에 뷰렌더링을 대신 해달라고 하는 역할 (handler.post 대신 runOnUiThread 사용해도 동일)
//                            // 쓰레드 8: progress 바 진행
//                            binding.songPlayerSB.progress=second*1000/playTime
////                            songtime=second*1000/playTime
//                            // %02d: 변수가 한 자리이면 0을 앞에 추가 ex) 1분 = 01분
//                            // 워크스레드에서 뷰렌더링 작업을 하지 못하기 때문에 이 코드만 쓰면 오류가 남(중단됨). 1. 핸들러 사용 2. 앱콤팻액티비티에서 제공하는 함수인 runonui 스레드
//                            binding.songStarttimeTV.text=String.format("%02d:%02d", second/60, second%60) // 쓰레드 6: second++을 한다고 해서 시간이 1씩 더해지는게 보여지지 않기 때문
//                        }
//                    }
//                }
//            }catch(e: InterruptedException){
//                Log.d("interrupt", "쓰레드가 종료되었습니다.")
//            }
//        }
//    }
//
//    override fun onPause() { // 생명주기에 의한 opPause 상태
//        super.onPause()
//        mediaPlayer?.pause() // 미디어플레이어 중지
//        player.isPlaying = false // 스레드 중지
//        song.isPlaying = false // 노래 중지 상태
//        song.second = (binding.songPlayerSB.progress*song.playTime)/1000
//        songPlayPause(false) // 정지 상태의 이미지로 전환
//
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val editor = sharedPreferences.edit() // sharedpreferences 조작할 때 사용
//        //Gson
//        val json = gson.toJson(song) // song 객체를 Json으로 변환. 변환된 json을 sharedpreferences에 저장을 위해 editor 사용
//        editor.putString("song", json)
//        editor.apply() // editor에서 apply() 적용을 해주어야 함
//    }
//
//
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        player.interrupt()
//        mediaPlayer?.release() // mediaPlayer가 가지고 있던 리소스(라일락) 해제
//        mediaPlayer = null // mediaPlayer 해제
//    }
//}