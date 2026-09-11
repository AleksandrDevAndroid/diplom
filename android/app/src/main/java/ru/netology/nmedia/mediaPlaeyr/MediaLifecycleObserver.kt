package ru.netology.nmedia.mediaPlayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MediaLifecycleObserver(
    private val context: Context
) : LifecycleEventObserver {

    private var mediaPlayer: MediaPlayer? = null
    private var currentUrl: String? = null

    fun play(url: String?) {
        if (currentUrl == url && mediaPlayer != null) {
            mediaPlayer?.let {
                if (it.isPlaying) it.pause() else it.start()
            }
            return
        }

        stop()
        currentUrl = url

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context, Uri.parse(url))
                setOnPreparedListener { start() }
                setOnCompletionListener { currentUrl = null }
                setOnErrorListener { _, what, extra ->
                    Log.e("MediaObserver", "Error: what=$what, extra=$extra")
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("MediaObserver", "Error", e)
        }
    }

    fun stop() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) it.stop()
            } catch (e: Exception) { }
            it.release()
        }
        mediaPlayer = null
        currentUrl = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> mediaPlayer?.pause()
            Lifecycle.Event.ON_STOP -> stop()
            Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}