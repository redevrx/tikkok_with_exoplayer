package com.redevrx.tikkok

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.forEachIndexed
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory.AdsLoaderProvider
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.source.ads.AdsLoader
import androidx.media3.exoplayer.upstream.DefaultAllocator
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.log


class VideoAdapter(private val context:Context):RecyclerView.Adapter<VideoHolder>() {

    private var player:ExoPlayer? = null

    private var items:MutableList<Uri> = mutableListOf()

    private var current:Int = 0

    private val mStoredPlayers: SparseArray<ExoPlayer?> = SparseArray<ExoPlayer?>()
    private val mStoredPlayerView: SparseArray<PlayerView> = SparseArray<PlayerView>()

    private var exoPlayer:ExoPlayer? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(items:MutableList<Uri>){
        this.items = items

        ///
        initVideo(0)
        mStoredPlayers[0]?.play()

        ///
        initVideo(1)
        notifyDataSetChanged()
    }

    fun loadMore(items:MutableList<Uri>){
        val oldSize = items.size
        this.items.addAll(items)

       notifyDataSetChanged()
    }

    fun nextVideo(position: Int){
        /**
         * stop video
         */
        if(position - 1 >= 0){
            mStoredPlayers[position - 1]?.pause()
        }

        if(position - 2 >= 0){
            mStoredPlayers[position - 2]?.release()
            mStoredPlayers[position - 2] = null
        }

        mStoredPlayers[position]!!.play()

        /**
         * preload next video
         */
       if(position + 1 < items.size){
           initVideo(position + 1)
       }

    }

    fun previousVideo(position: Int){
        if(position + 1 < items.size){
            /**
             * stop video
             */
            mStoredPlayers[position+1]?.pause()
        }

      if(position + 2 < items.size){
          ///release
          mStoredPlayers[position+2]?.release()
          mStoredPlayers[position+2] = null
      }
        mStoredPlayers[position]!!.play()

        /**
         * preload next video
         */
        if(position - 1 >= 0){
            initVideo(position-1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_conten_layout,parent,false)
       return VideoHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("UnsafeOptInUsageError")
    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        val url = items[position]

//        holder.setData(
//            data = url,
//            position = position,
//            mStoredPlayers = mStoredPlayers,
//            mStoredPlayerView = mStoredPlayerView
//        )
        mStoredPlayerView.append(position,holder.binding.playerView)
        mStoredPlayers[position]?.also {
            holder.binding.playerView.player = it
            holder.binding.playerView.hideController()
            it.addListener(object :Player.Listener{
                override fun onVideoSizeChanged(videoSize: VideoSize) {
                    if(videoSize.width > videoSize.height){
                        holder.binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    }else {
                        holder.binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                        it.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                    }
                }
            })
        }



//        exoPlayer = holder.getVPlayer(context)
//        mStoredPlayerView.append(position,holder.binding.playerView)
    }


    override fun onViewDetachedFromWindow(holder: VideoHolder) {
        super.onViewDetachedFromWindow(holder)

//        val adapterPosition = holder.absoluteAdapterPosition
//
//        mStoredPlayers.get(adapterPosition)?.stop()
//        mStoredPlayers.get(adapterPosition)?.clearMediaItems()
//        mStoredPlayers.get(adapterPosition)?.release()
//        mStoredPlayers.append(adapterPosition,null)
//       // holder.binding.containerView.removeViewAt(0)
//        holder.binding.playerView.player?.release()
        println("onViewDetachedFromWindow :")


    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onViewAttachedToWindow(holder: VideoHolder) {
        super.onViewAttachedToWindow(holder)

        println("onViewAttachedToWindow")

//        val adapterPosition = holder.absoluteAdapterPosition
//        val mediaitem = items[adapterPosition]
//        val dataSourceFactory: DataSource.Factory =
//            DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName.toString()))
//        val mediaSource:MediaSource = ProgressiveMediaSource
//            .Factory(dataSourceFactory)
//            .createMediaSource(MediaItem.fromUri(mediaitem))
////get the existing instances of exoplayer and exoplayer view
//        val playerView = mStoredPlayerView.get(adapterPosition)
//
//        var player: ExoPlayer?
//        if(mStoredPlayers.get(adapterPosition) == null){
//            //new instance player
//            println("old player is null")
//            player = holder.getVPlayer(context)
//            mStoredPlayers[adapterPosition] = player
//        }else {
//             player = mStoredPlayers.get(adapterPosition)
//        }
//
//        player?.setMediaSource(mediaSource, true)
//        player?.prepare()
//        player?.playWhenReady = true
//        if(playerView != null){
//            playerView.player = player
//        }
        //add player view again to parent
        //holder.binding.containerView.addView(playerView)

    }

    @SuppressLint("UnsafeOptInUsageError")
    private  fun initVideo(position: Int) {
        val mediaitem = items[position]
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName.toString()))
        val mediaSource:MediaSource = ProgressiveMediaSource
            .Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(mediaitem))
//get the existing instances of exoplayer and exoplayer view
//        val playerView = mStoredPlayerView.get(position)

        var player: ExoPlayer = getVPlayer(context)
        //new instance player
        mStoredPlayers.append(position,player)

        player?.setMediaSource(mediaSource, true)
        player?.prepare()
        player.repeatMode = Player.REPEAT_MODE_ALL
        mStoredPlayerView[position]?.player = mStoredPlayers[position]
    }

    @SuppressLint("UnsafeOptInUsageError")
   private fun getVPlayer(context: Context): ExoPlayer {
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(
                VideoPlayerConfig.MIN_BUFFER_DURATION,
                VideoPlayerConfig.MAX_BUFFER_DURATION,
                VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER
            )
            .setTargetBufferBytes(-1)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        // Create a progressive media source pointing to a stream uri.
        return ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build()
    }
}
