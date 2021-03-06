package com.example.flo_clonecoding_BBangJun

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clonecoding_BBangJun.databinding.ItemLockerAlbumBinding

class AlbumLockerRVAdapter (): RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>(){
    private val albums = ArrayList<Album>()

    interface  MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var  myItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        myItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumLockerRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumLockerRVAdapter.ViewHolder, position: Int){
        holder.bind(albums[position])
        holder.binding.itemAlbumMoreIv.setOnClickListener{
            removeSong(position)
            myItemClickListener.onRemoveSong(albums[position].id)
        }
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: List<Album>){
        this.albums.clear()
        this.albums.addAll(albums)

        notifyDataSetChanged()
    }

//    ArrayList<Album>

    fun removeSong(position: Int){
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemLockerAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumImgIv.setImageResource(album.coverImg!!)
            binding.itemAlbumTitleTv.text=album.title
            binding.itemAlbumSingerTv.text=album.singer
        }
    }

}