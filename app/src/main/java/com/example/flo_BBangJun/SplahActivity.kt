package com.example.flo_BBangJun

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_BBangJun.databinding.ActivitySplashBinding

class SplahActivity : AppCompatActivity(){

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handler: 워커 스레드에서 뷰렌더링을 사용하지 못하지 못하기 때문에 handler를 통해 메인 스레드에 메세지를 전달
        // Looper: Handler의 인자. 받아온 메세지를 꺼내와서 메인 스레드 or 다른 스레드에 전달하여 처리해주는 역할
        // Looper.getMainLooper(): mainthread에 전달
        Handler(Looper.getMainLooper()).postDelayed({
            val intent= Intent(this, MainActivity::class.java) // splash activity -> mainactivity
            startActivity(intent)
        }, 2000) // 1초 = 1000mils 즉, 2초 후 코드를 실행
    }
}