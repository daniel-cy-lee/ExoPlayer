package com.daniel.exoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.rtmp.RtmpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView
import com.daniel.exoplayer.ui.theme.ExoPlayerTheme

const val EXAMPLE_VIDEO_URI = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
const val EXAMPLE_RTSP_URI = "rtsp://admin:admin@192.168.1.115:1935"
const val EXAMPLE_RTSP_URI2 = "rtsp://192.168.1.213:8554/stream1"
const val EXAMPLE_RTSP_URI3 = "rtsp://172.24.10.11:8554/stream1"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    ExoPlayerView()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    //val context = LocalContext.current
    //val exoPlayer = ExoPlayer.Builder(context).build()


}

/**
 * Composable function that displays an ExoPlayer to play a video using Jetpack Compose.
 *
 * @OptIn annotation to UnstableApi is used to indicate that the API is still experimental and may
 * undergo changes in the future.
 *
 * @see EXAMPLE_VIDEO_URI Replace with the actual URI of the video to be played.
 */
@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView() {
    // Get the current context
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()

    // Create a MediaSource
    val mediaSource = remember(EXAMPLE_RTSP_URI3) {
        MediaItem.fromUri(EXAMPLE_RTSP_URI3)
    }
    val rtmpDataSourceFactory = RtmpDataSource.Factory()
    val mediaSourceRtsp: MediaSource =
        RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(EXAMPLE_RTSP_URI3))

    val url = EXAMPLE_RTSP_URI3


    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSourceRtsp) {
        exoPlayer.setMediaSource(mediaSourceRtsp)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExoPlayerTheme {
        Greeting("Android")
    }
}