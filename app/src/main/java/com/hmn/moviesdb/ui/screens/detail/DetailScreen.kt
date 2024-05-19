package com.hmn.moviesdb.ui.screens.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.hmn.data.utils.UrlConstants
import com.hmn.moviesdb.R
import com.hmn.moviesdb.core.BaseScreen
import com.hmn.moviesdb.navigation.Routes
import com.hmn.moviesdb.ui.screens.YouTubePlayerActivity
import com.hmn.moviesdb.ui.screens.login.LoginScreenContent
import com.hmn.moviesdb.ui.theme.BlueVariant

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel,
    navController: NavController,
    detailId: Int,
    onBackPress: () -> Unit
) {



    val isInternetAvailable by detailViewModel.isNetwork.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = detailId) {
     detailViewModel.getMovieById(detailId)
        detailViewModel.getVideoInfoWithId(detailId,isInternetAvailable)
    }
    val context = LocalContext.current
    val detailUiState by detailViewModel.detailUiState.collectAsStateWithLifecycle()
    val movieDetail = detailUiState.movie
    val error = detailUiState.error
    val isFavourite = detailUiState.isFavorite


    if (!error.isNullOrEmpty()){
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }


    val videoState by detailViewModel.videoUiState.collectAsStateWithLifecycle()

    val isVideoErrorMessage = videoState.videoErrorMessage
    val videoKey = videoState.videoKey
    val videoLoading = videoState.videoLoading
    val isVideoError = videoState.videoError

    val isFavOccur = detailUiState.isFavErrorOccure
    val isFavErrorMsg = detailUiState.favStateErrorMessage
    var shouldFavToast by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isFavOccur) {
        if (isFavOccur && !isFavErrorMsg.isNullOrEmpty()) {
            shouldFavToast = true
        }
    }
    if (shouldFavToast) {
        Toast.makeText(context, isFavErrorMsg, Toast.LENGTH_SHORT).show()
        shouldFavToast = false
    }

    var tintColor = if (isFavourite) {
        Color.Red
    } else {
        Color.LightGray
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {

        if (movieDetail != null) {
            val posterPath = movieDetail.posterPath ?: ""
            val rating = movieDetail.getRatingBaseOnFiveStar()
            DetailAppBar(
                tittle = movieDetail.title ?: "",
                isShowFavButton = true,
                isFav = isFavourite,
                onBackClick = { onBackPress()},
                onFavClickL = {
                    detailViewModel.onEvent(
                        DetailUiEvent.OnFavourite(
                            movieId = movieDetail.id,
                            isFav = !isFavourite
                        )
                    )
                    tintColor = Color.Red


                },
                tintColor = tintColor

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = "${UrlConstants.IMAGE_BASE_URL}/$posterPath",
                        builder = {
                            placeholder(R.drawable.placeholder_image)
                            crossfade(true)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {
                        detailViewModel.checkNetwork()
                        val hasInternet = detailViewModel.isNetwork.value

                        detailViewModel.getVideoInfoWithId(detailId, isInternetAvailable)

                        if (!hasInternet) {
                            Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        if (videoLoading) {
                            Toast.makeText(context, "Loading Video", Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        if (isVideoErrorMessage != null) {
                            Toast.makeText(context, isVideoErrorMessage, Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        if (videoKey == "") {
                            Toast.makeText(context, "Loading Video", Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        if (videoKey != null) {
                            YouTubePlayerActivity.start(context, videoKey)
                        } else {
                            Toast.makeText(context, "No Video Key Found", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                ){
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movieDetail.title ?: "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            GenreAndRating(
                genre = " Genre: ${movieDetail.genres?.get(0)?.name ?: ""}",
                rating = rating ?: 0f
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = movieDetail.releaseDate ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movieDetail.overview ?: "",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movieDetail.overview ?: "",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Actors:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

        }

    }
}

@Composable
fun GenreAndRating(
    genre: String,
    rating: Float
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.83f),
                text = genre,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            VoteAverageRatingIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.17f),
                percentage = rating
            )
        }
    }
}

@Composable
fun VoteAverageRatingIndicator(
    modifier: Modifier = Modifier,
    percentage: Float,
    number: Int = 10,
    fontSize: TextUnit = 16.sp,
    radius: Dp = 20.dp,
    color: Color = BlueVariant,
    strokeWidth: Dp = 3.dp,
    animationDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animDelay
        ), label = ""
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(
            modifier = Modifier
                .size(radius * 2f)
        ) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = (360 * (currentPercentage.value * 0.1)).toFloat(),
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${(currentPercentage.value * number).toInt()}%",
            color = BlueVariant,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }


}

