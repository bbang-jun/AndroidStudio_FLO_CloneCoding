package com.example.flo_BBangJun

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_BBangJun.databinding.ItemAlbumBinding

// 어댑터 및 뷰홀더 구현 step4-1 데이터를 바인딩 해주려면 매개변수로 데이터를 받아와야함.
class AlbumRVAdapter(private val albumList: ArrayList<Album>) :
    RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemclick(album: Album) // onItemclick 함수를 통해 외부와 소통
        fun onRemoveAlbum(position: Int)
    }

    // 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    // 리스너 객체를 전달받는 함수
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    // 뷰홀더 구현 step4-3 뷰홀더를 생성해줘야 할 때 호출되는 함수(아이템 뷰 객체를 만들어서 뷰홀더에 던져줌.)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        // 4-4 아이템뷰 객체 생성
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        // 4-5 뷰홀더에 반환(던져줌)
        return ViewHolder(binding)
    }

    // 4-6뷰홀더에 데이터를 바인딩 해주어야 할 때마다 호출되는 함수
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        // recyclerview에서는 index가 position
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemclick(albumList[position]) }
    }

    fun addItems(albums: ArrayList<Album>) {
        albumList.clear()
        albumList.addAll(albums)
        notifyDataSetChanged()
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItems() {
        albumList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    // 4-7데이터 세트 크기를 알려주는 함수(recyclerview가 마지막이 언제인지를 알게 해줌)
    override fun getItemCount(): Int = albumList.size

    // 뷰홀더 구현 step4-2 inner class로 뷰홀더 만들기 (아이템 뷰 객체들을 재활용하기 위함)
    // 뷰홀더(매개변수로 item view 객체, 뷰홀더를 상속 받아야 함.)
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){ // 4-6
            binding.itemAlbumTitleTV.text=album.title
            binding.itemAlbumSingerTV.text=album.singer
            binding.itemAlbumCoverImgIV.setImageResource(album.coverImg!!)
        }
    }
}