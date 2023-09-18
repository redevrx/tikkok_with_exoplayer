package com.redevrx.tikkok

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.util.SparseArray
import android.view.View
import android.view.WindowManager
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.upstream.DefaultAllocator
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.redevrx.tikkok.databinding.VideoContenLayoutBinding

class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding:VideoContenLayoutBinding
    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0
    private lateinit var videoPlayerView: PlayerView


    init {
       binding = VideoContenLayoutBinding.bind(itemView)
    }

    fun setData(
        data: Uri,
        position: Int,
        mStoredPlayers: SparseArray<ExoPlayer?>,
        mStoredPlayerView: SparseArray<PlayerView>
    )
    {
      //  mStoredPlayerView.append(position,getVideoSource(itemView.context))
        mStoredPlayers.append(position, getVPlayer(itemView.context))
    }

    @SuppressLint("UnsafeOptInUsageError")
     fun getVPlayer(context: Context): ExoPlayer {
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

    @SuppressLint("UnsafeOptInUsageError")
    private fun getVideoSource(mContext: Context): PlayerView {
        val display =(mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val point = Point()
        display.getSize(point)

        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y
        videoPlayerView = PlayerView(mContext)
        videoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        videoPlayerView.useController = false
        videoPlayerView.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
        return videoPlayerView
    }
}


object VideoPlayerConfig {
    //Minimum Video you want to buffer while Playing
    const val MIN_BUFFER_DURATION = 2000

    //Max Video you want to buffer during PlayBack
    const val MAX_BUFFER_DURATION = 5000

    //Min Video you want to buffer before start Playing it
    const val MIN_PLAYBACK_START_BUFFER = 1500

    //Min video You want to buffer when user resumes video
    const val MIN_PLAYBACK_RESUME_BUFFER = 2000
}