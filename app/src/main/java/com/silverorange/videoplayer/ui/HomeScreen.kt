package com.silverorange.videoplayer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.silverorange.videoplayer.R

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
            BottomDetailsSection(viewModel = viewModel)
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
    val isPlaying = viewModel.isPlaying.collectAsState()
    val showControls = viewModel.showControls.collectAsState()
    val hasNextVideoItem = viewModel.hasNextItem.collectAsState()
    val hasPreviousVideoItem = viewModel.hasPreviousItem.collectAsState()

    Box(
        modifier = modifier
            .wrapContentHeight()
            .border(1.dp, Color.Black, RectangleShape)
    ) {
        VideoPlayer(
            viewModel = viewModel,
            isPlaying = isPlaying.value,
            updateShowControls = { viewModel.updateShowControls() }
        )

        VideoPlayerControls(
            showControls = showControls.value,
            isPlaying = isPlaying.value,
            hasPreviousVideoItem = hasPreviousVideoItem.value,
            hasNextVideoItem = hasNextVideoItem.value,
            onPreviousVideo = {
                viewModel.onPreviousVideo()
            },
            onPlayPauseVideo = {
                viewModel.onPlayPauseVideo()
            },
            onNextVideo = {
                viewModel.onNextVideo()
            }
        )
    }
}

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
    isPlaying: Boolean,
    updateShowControls: () -> Unit
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
            .clickable { updateShowControls() }
            .fillMaxWidth()
            .aspectRatio(16 / 9f),
        factory = {
            StyledPlayerView(context).apply {
                player = viewModel.player
                useController = false
            }
        },
        update = {
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_CREATE -> {
                    it.onPause()
                    it.player?.pause() //keep player paused on startup
                }
                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }
                Lifecycle.Event.ON_STOP -> {
                    it.onPause()
                    it.player?.pause()
                    viewModel.updateIsPlaying(isPlaying.not()) //when user moves out of app we need to manually update the play/pause icon
                }
                else -> Unit
            }
        },
    )
}


@Composable
fun VideoPlayerControls(
    modifier: Modifier = Modifier,
    showControls: Boolean,
    isPlaying: Boolean,
    hasPreviousVideoItem: Boolean,
    hasNextVideoItem: Boolean,
    onPreviousVideo: () -> Unit,
    onPlayPauseVideo: () -> Unit,
    onNextVideo: () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = showControls,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = Modifier
                    .background(
                        color = Color(0xFFDFDFDF),
                        shape = RoundedCornerShape(50)
                    )
                    .border(1.dp, Color(0xFF8D8C8C), RoundedCornerShape(50)),
                onClick = { onPreviousVideo() },
                enabled = hasPreviousVideoItem
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = "previous"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            IconButton(modifier = Modifier
                .size(80.dp)
                .background(
                    color = Color(0xFFDFDFDF),
                    shape = RoundedCornerShape(50)
                )
                .border(1.dp, Color(0xFF8D8C8C), RoundedCornerShape(50)),
                onClick = { onPlayPauseVideo() }) {
                Icon(
                    painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                    contentDescription = "play/pause"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            IconButton(
                modifier = Modifier
                    .background(
                        color = Color(0xFFDFDFDF),
                        shape = RoundedCornerShape(50)
                    )
                    .border(1.dp, Color(0xFF8D8C8C), RoundedCornerShape(50)),
                onClick = { onNextVideo() },
                enabled = hasNextVideoItem
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "next"
                )
            }
        }
    }
}

@Composable
fun BottomDetailsSection(modifier: Modifier = Modifier, viewModel: HomeScreenViewModel) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(13.dp)
    ) {
        Text(
            text = "${viewModel.player.currentMediaItem?.mediaMetadata?.title}",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${viewModel.player.currentMediaItem?.mediaMetadata?.artist}",
            fontSize = 14.sp,
            color = Color(0xFF6B6A6A)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "${viewModel.player.currentMediaItem?.mediaMetadata?.description}",
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {

}