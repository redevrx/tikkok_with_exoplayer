package com.redevrx.tikkok

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.redevrx.tikkok.databinding.FragmentVideoBinding
import kotlinx.coroutines.runBlocking


class VideoFragment: Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: VideoAdapter
    private lateinit var items:MutableList<Uri>
    private var currentFocus = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVideoBinding.inflate(layoutInflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycleView()
        initItems()
        initView()

        binding!!.rvVideo.apply {
            registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                 //   Toast.makeText(requireContext(),"$position",Toast.LENGTH_LONG).show()

                    if(position > currentFocus){
                        ///next video
                        this@VideoFragment.adapter.nextVideo(position)
                    }else{
                        ///back video
                        this@VideoFragment.adapter.previousVideo(position)
                    }
                    currentFocus = position

                    if(position == items.size - 1){
                        ///load more data
                        loadMore()
                    }
                }
            })
        }
    }

    private fun loadMore(){
        val urls = mutableListOf(
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"),
            )

       runBlocking {
          // adapter.loadMore(urls)
       }
    }

    private fun initRecycleView() = with(binding!!){
        adapter = VideoAdapter(requireActivity().applicationContext)
        rvVideo.adapter = adapter
        rvVideo.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    private fun initItems() {
        items = mutableListOf(
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"),)
        adapter.setItem(items)
    }

    private fun initView() {

    }
}