package com.silverorange.videoplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val loadingState = viewModel.loading.collectAsState()

    Column(Modifier.fillMaxSize()) {
        ToolBar()

        if (loadingState.value) {
            CircularLoader(Modifier.fillMaxSize())
        } else {
            TopVideoPlayer(viewModel = viewModel)
        }
    }
}


@Composable
fun ToolBar() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            text = "Video Player",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun CircularLoader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        CircularProgressIndicator(
            modifier = Modifier
                .weight(1f)
                .size(40.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun TopVideoPlayer(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
) {
    val context = LocalContext.current

    //used to manage video's state during changes in activity lifecycle
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f),
        factory = {
            StyledPlayerView(context).apply {
                player = viewModel.player
            }
        },
        update = {
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_CREATE -> {
                    it.onPause()
                    it.player?.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }
                Lifecycle.Event.ON_STOP -> {
                    it.onPause()
                    it.player?.pause()
                }
                else -> Unit
            }
        },
    )
}


@Preview
@Composable
fun PreviewHomeScreen() {

}