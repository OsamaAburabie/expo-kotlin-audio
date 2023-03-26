package expo.modules.kotlinaudio

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.doublesymmetry.kotlinaudio.models.*
import com.doublesymmetry.kotlinaudio.players.QueuedAudioPlayer
import com.google.android.exoplayer2.ui.R
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

const val ON_STATE_CHANGE = "onStateChange"
const val ON_TRACK_CHANGE = "onTrackChange"
const val ON_TRACK_INDEX_CHANGE = "onTrackIndexChange"
const val ON_QUEUE_CHANGE = "onQueueChange"
const val ON_PLAYING_CHANGE = "onPlayingChange"

internal class Track : Record {
    @Field
    var title: String? = null

    @Field
    var artist: String? = null

    @Field
    var uri: String = ""

    @Field
    var artwork: String? = null

//    @Field
//    var duration: String? = null
}

class ExpoKotlinAudioModule : Module() {
    private var player: QueuedAudioPlayer? = null
    private val scope = MainScope()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun definition() = ModuleDefinition {

        Name("ExpoKotlinAudio")
        Events(
            ON_STATE_CHANGE,
            ON_TRACK_CHANGE,
            ON_QUEUE_CHANGE,
            ON_PLAYING_CHANGE,
            ON_TRACK_INDEX_CHANGE
        )


        OnCreate {
            player = QueuedAudioPlayer(
                context, playerConfig = PlayerConfig(
                    interceptPlayerActionsTriggeredExternally = true,
                    handleAudioBecomingNoisy = true,
                    handleAudioFocus = true
                )
            )

            setupNotification()
            observeEvents()
        }

        OnDestroy {
            player?.destroy()
        }


        Function("init") {

        }

        Function("add") { tracks: List<Track> ->
            add(tracks)
        }

        Function("play") {
            play()
        }

        Function("pause") {
            pause()
        }

        Function("stop") {
            stop()
        }

        Function("next") {
            next()
        }

        Function("previous") {
            previous()
        }

        Function("seek") {
            seek()
        }

        Function("reset") {
            reset()
        }

        Function("skipTo") { index: Int ->
            skipTo(index)
        }

        Function("getDuration") {
            return@Function getDuration()
        }

        Function("getPosition") {
            return@Function getPosition()
        }

        Function("getQueue") {
            return@Function getQueue()
        }

        Function("getCurrentIndex") {
            return@Function getCurrentIndex()
        }

        Function("getCurrentTrack") {
            return@Function getCurrentTrack()
        }

        Function("getPlaying") {
            return@Function getPlaying()
        }

        Function("getState") {
            return@Function getState().toString()
        }

    }

    private val context
        get() = requireNotNull(appContext.reactContext)

    private fun setupNotification() {
        val notificationConfig = NotificationConfig(
            listOf(
                NotificationButton.PLAY_PAUSE(),
                NotificationButton.NEXT(isCompact = true),
                NotificationButton.PREVIOUS(isCompact = true),
                NotificationButton.BACKWARD(isCompact = true),
                NotificationButton.FORWARD(
                    isCompact = true,
                    icon = R.drawable.exo_icon_circular_play
                ),
                NotificationButton.SEEK_TO
            ), accentColor = null, smallIcon = null, pendingIntent = null
        )
        player?.notificationManager?.createNotification(notificationConfig)
    }

    private fun observeEvents() {
        scope.launch {
            player?.event?.stateChange?.collect {
                Log.d("StateChange", it.toString())
                sendStateChangeEvent(it)
            }
        }

        scope.launch {
            player?.event?.onPlayerActionTriggeredExternally?.collect() {
                Log.d("NotificationAction", it.toString())
                when (it) {
                    MediaSessionCallback.PLAY -> play()
                    MediaSessionCallback.PAUSE -> pause()
                    MediaSessionCallback.NEXT -> next()
                    MediaSessionCallback.PREVIOUS -> previous()
                    MediaSessionCallback.STOP -> stop()
                    is MediaSessionCallback.SEEK -> player?.seek(
                        it.positionMs,
                        TimeUnit.MILLISECONDS
                    )
                    else -> Timber.d("Event not handled")
                }
            }
        }


        scope.launch {
            player?.event?.audioItemTransition?.collect() {
                sendTrackChangeEvent()
                sendQueueChangeEvent()
            }
        }
    }

    // Actions
    private fun play() {
        runOnUiThread {
            player?.play()
        }
    }

    private fun pause() {
        runOnUiThread {
            player?.pause()
        }
    }

    private fun stop() {
        runOnUiThread {
            player?.stop()
        }
    }

    private fun next() {
        runOnUiThread {
            player?.next()
        }
    }

    private fun previous() {
        runOnUiThread {
            player?.previous()
        }
    }

    private fun seek() {
        runOnUiThread {
            player?.seek(10000, TimeUnit.MILLISECONDS)
        }
    }

    private fun reset() {
        runOnUiThread {
            player?.stop()
            player?.clear()
        }
    }

    private fun add(tracks: List<Track>) {
        runOnUiThread {
            val mediaItems = tracks.map {
                DefaultAudioItem(
                    it.uri,
                    MediaType.DEFAULT,
                    it.title,
                    it.artist,
                    it.title,
                    it.artwork
                )
            }

            player?.add(mediaItems)
        }
    }

    private fun skipTo(index: Int) {
        runOnUiThread {
            player?.jumpToItem(index, true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDuration(): Long {
        val future = CompletableFuture<Long>()
        runOnUiThread {
            val duration = player?.duration ?: 0
            future.complete(duration)
        }
        return future.get()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getPosition(): Long {
        val future = CompletableFuture<Long>()
        runOnUiThread {
            val position = player?.position ?: 0
            future.complete(position)
        }
        return future.get()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getPlaying(): Boolean {
        val future = CompletableFuture<Boolean>()
        runOnUiThread {
            val playing = player?.isPlaying ?: false
            future.complete(playing)
        }

        return future.get()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getQueue(): List<Map<String, Any?>> {
        val future = CompletableFuture<List<Map<String, Any?>>>()
        runOnUiThread {
            val queue = player?.items?.map {
                mapOf(
                    "title" to it.title,
                    "artist" to it.artist,
                    "artwork" to it.artwork,
                    "duration" to  it.duration
                )
            } ?: emptyList()
            future.complete(queue)
        }
        return future.get()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentIndex(): Int {
        val future = CompletableFuture<Int>()
        runOnUiThread {
            val index = player?.currentIndex ?: 0
            future.complete(index)
        }
        return future.get()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentTrack(): Map<String, Any?>?{
        val future = CompletableFuture<Map<String, Any?>?>()
        runOnUiThread {
            val trackMap = if (player?.currentItem?.title != null) {
                mapOf(
                    "title" to player?.currentItem?.title,
                    "artist" to player?.currentItem?.artist,
                    "artwork" to player?.currentItem?.artwork,
                    "duration" to  player?.currentItem?.duration
                )
            } else {
                null
            }
            future.complete(trackMap)
        }
        return future.get()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getState(): AudioPlayerState {
        val future = CompletableFuture<AudioPlayerState>()
        runOnUiThread {
            val state = player?.playerState ?: AudioPlayerState.IDLE
            future.complete(state)
        }
        return future.get()
    }


    // Events
    private fun sendStateChangeEvent(state: AudioPlayerState) {
        runOnUiThread {
            sendEvent(ON_STATE_CHANGE, mapOf("state" to state))
        }
    }

    private fun sendTrackChangeEvent() {
        runOnUiThread {
            val trackMap = if (player?.currentItem?.title != null) {
                mapOf(
                    "title" to player?.currentItem?.title,
                    "artist" to player?.currentItem?.artist,
                    "artwork" to player?.currentItem?.artwork,
                    "duration" to player?.currentItem?.duration
                )
            } else {
                null
            }

            sendEvent(
                ON_TRACK_CHANGE,
                mapOf(
                    "track" to trackMap,
                )
            )

            Log.d("TrackIndex", player?.currentIndex.toString())

            sendEvent(
                ON_TRACK_INDEX_CHANGE,
                mapOf(
                    "index" to player?.currentIndex,
                )
            )
        }
    }

    private fun sendQueueChangeEvent() {
        runOnUiThread {
            player?.items?.let { items ->
                val tracks = items.map {
                    mapOf(
                        "title" to it.title,
                        "artist" to it.artist,
                        "artwork" to it.artwork,
                        "duration" to it.duration
                    )
                }

                sendEvent(
                    ON_QUEUE_CHANGE,
                    mapOf(
                        "queue" to tracks
                    )
                )
            }
        }
    }


    // Helpers
    private fun runOnUiThread(action: () -> Unit) {
        appContext.activityProvider?.currentActivity.let {
            it?.runOnUiThread(action)
        }
    }

}
