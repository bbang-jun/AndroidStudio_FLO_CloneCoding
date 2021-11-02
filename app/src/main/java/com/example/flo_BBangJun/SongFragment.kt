package com.example.flo_BBangJun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo_BBangJun.databinding.FragmentSongBinding

class SongFragment : Fragment() {

    lateinit var binding : FragmentSongBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSongBinding.inflate(inflater, container,false)

                binding.albumToggleoffIB.setOnClickListener {
            binding.albumToggleoffIB.visibility = View.GONE
            binding.albumToggleonIB.visibility = View.VISIBLE
        }

        binding.albumToggleonIB.setOnClickListener {
            binding.albumToggleonIB.visibility = View.GONE
            binding.albumToggleoffIB.visibility = View.VISIBLE
        }

        binding.albumSonglilaclayoutLO.setOnClickListener {
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}