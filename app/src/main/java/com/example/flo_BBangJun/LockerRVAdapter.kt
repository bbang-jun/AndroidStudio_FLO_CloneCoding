package com.example.flo_BBangJun

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_BBangJun.databinding.ItemLockerBinding

class LockerRVAdapter(private val lockerAlbumList: ArrayList<LockerAlbum>) : RecyclerView.Adapter<LockerRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LockerRVAdapter.ViewHolder {
        val binding: ItemLockerBinding = ItemLockerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(lockerAlbumList[position])
    }

    override fun getItemCount(): Int = lockerAlbumList.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemLockerBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(lockeralbum: LockerAlbum){
            binding.itemLockerTitleTV.text = lockeralbum.title
            binding.itemLockerSingerTV.text = lockeralbum.singer
            binding.itemLockerCoverImgIV.setImageResource(lockeralbum.coverImg!!)
        }
    }
}