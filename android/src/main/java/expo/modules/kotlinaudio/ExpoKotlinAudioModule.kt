package expo.modules.kotlinaudio

import android.util.Log
import com.doublesymmetry.kotlinaudio.models.*
import com.doublesymmetry.kotlinaudio.players.QueuedAudioPlayer
import com.google.android.exoplayer2.ui.R
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ExpoKotlinAudioModule : Module() {
    private var player: QueuedAudioPlayer? = null
    private val scope = MainScope()

    override fun definition() = ModuleDefinition {

        Name("ExpoKotlinAudio")

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


        // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
        Function("hello") {
            "Hello world! 👋"
        }

        Function("init") {

        }

        Function("add") {
            appContext.activityProvider!!.currentActivity.runOnUiThread {
                player?.add(
                    DefaultAudioItem(
                        "https://cdn.pixabay.com/download/audio/2022/08/31/audio_419263fc12.mp3?filename=leonell-cassio-the-blackest-bouquet-118766.mp3",
                        MediaType.DEFAULT,
                        "Song 1",
                        "Artist 1",
                        "Album 1",
                        "https://upload.wikimedia.org/wikipedia/en/0/0b/DirtyComputer.png"
                    ),
                )
                player?.play()
            }
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
            player?.event?.onPlayerActionTriggeredExternally?.collect() {
                Timber.d(it.toString())
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
    }

    private fun play() {
        appContext.activityProvider!!.currentActivity.runOnUiThread {
            player?.play()
        }
    }

    private fun pause() {
        appContext.activityProvider!!.currentActivity.runOnUiThread {
            player?.pause()
        }
    }

    private fun stop() {
        appContext.activityProvider!!.currentActivity.runOnUiThread {
            player?.stop()
        }
    }

    private fun next() {
        appContext.activityProvider!!.currentActivity.runOnUiThread {
            player?.next()
        }
    }

    private fun previous() {
        appContext.activityProvider!!.currentActivity.runOnUiThread {
            player?.previous()
        }
    }

    private fun seek() {
        appContext.activityProvider!!.currentActivity.runOnUiThread {
            player?.seek(10000, TimeUnit.MILLISECONDS)
        }
    }


}
