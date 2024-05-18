package com.hmn.moviesdb.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.hmn.data.model.resp.MovieVo
import com.hmn.data.utils.UrlConstants
import com.hmn.moviesdb.R
import com.hmn.moviesdb.ui.theme.BlueVariant
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun PopularMovies(
    modifier: Modifier = Modifier,
    popularMovies: List<MovieVo>,
    onMovieClick: (Int, MovieVo) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(start = 24.dp)
    ) {
        items(count = popularMovies.size) { index ->
            val movieId = popularMovies[index].id
            Card(modifier = modifier.padding(4.dp)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    val imagePath = if (popularMovies[index].backdropPath.isNullOrBlank()) {
                        popularMovies[index].posterPath ?: ""
                    } else {
                        popularMovies[index].backdropPath ?: ""
                    }
                    Image(
                        painter = rememberImagePainter(
                            data = "${UrlConstants.IMAGE_BASE_URL}/$imagePath",
                            builder = {
                                placeholder(R.drawable.placeholder_image)
                                crossfade(true)
                            }
                        ),
                        modifier = Modifier
                            .size(width = 200.dp, height = 260.dp)
                            .clickable {
                                onMovieClick(
                                    popularMovies[index].id ?: 0,
                                    popularMovies[index]
                                )
                            },
                        contentScale = ContentScale.Crop,
                        contentDescription = "Movie Banner"
                    )
                }

            }

        }
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun TopRatedMovies(
    topRatedMovies:List<MovieVo>,
    onDetailScreen: (Int) -> Unit,
    onSeatScreen:()->Unit,
) {
    HorizontalPager(
        count = topRatedMovies.size,
        contentPadding = PaddingValues(start = 48.dp, end = 48.dp)
    ) { page ->

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    lerp(
                        start = ScaleFactor(1f, 0.85f),
                        stop = ScaleFactor(1f, 1f),
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale.scaleX
                        scaleY = scale.scaleY
                    }
                }
                .clickable {
                    onDetailScreen(topRatedMovies[page].id)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.BottomCenter

            ) {
                Image(
                    painter = rememberImagePainter(
                        data = "${UrlConstants.IMAGE_BASE_URL}/${topRatedMovies[page].backdropPath}",
                        builder = {
                            placeholder(R.drawable.placeholder_image)
                            crossfade(true)
                        }
                    ),
                    contentDescription = "Movie Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.85f)
                        .height(200.dp)
                )
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                            val translation = pageOffset.coerceIn(0f, 1f)

                            translationY = translation * 200
                        }
                        .fillMaxWidth(fraction = 0.85f)
                        .wrapContentHeight()
                        .background(
                            BlueVariant
                        )
                        .padding(vertical = 16.dp)
                        .clickable {
                            onSeatScreen()
                        }
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Buy Ticket", style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = topRatedMovies[page].title ?: "",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun Categories(onClick:()->Unit) {
    val categories = listOf(
        "Animation",
        "Horror",
        "Action",
        "Comedy",
        "Romance",
        "Sci-fi",
        "History",
        "Adventure",
    )
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier.horizontalScroll(scrollState)
    ) {
        repeat(categories.size) { index ->
            Surface(
                /// order matters
                modifier = Modifier
                    .padding(
                        start = if (index == 0) 24.dp else 0.dp,
                        end = 12.dp,
                    )
                    .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onClick() }
                    .padding(12.dp)
            ) {
                Text(text = categories[index], style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}


@Composable
fun Banner(
    nowPlaying: List<MovieVo>,
    onClick: (Int) -> Unit
) {
    val currentIndex = remember {
        mutableIntStateOf(0)
    }

    if (nowPlaying.isNotEmpty()) {
        val takeFirstThree = nowPlaying.take(3)

        LaunchedEffect(key1 = Unit) {
            while (true) {
                delay(3000)
                currentIndex.intValue = (currentIndex.intValue + 1) % takeFirstThree.size
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),

            ) {


            ImageSliderItem(imageUrl = nowPlaying[currentIndex.intValue].backdropPath ?: "") {
                onClick(nowPlaying[currentIndex.intValue].id ?: 0)
            }
            Row(
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 12.dp, start = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                nowPlaying.forEachIndexed { index, i ->
                    Indicator(active = index == currentIndex.intValue)
                    if (index < nowPlaying.size - 1) {
                        Spacer(modifier = Modifier.width(5.dp))
                    }

                }
            }

        }
    }


}

@Composable
fun ImageSliderItem(modifier: Modifier = Modifier, imageUrl: String, onClick: () -> Unit) {
    Card(modifier = modifier.padding(4.dp)) {
        Image(
            painter = rememberImagePainter(
                data = "${UrlConstants.IMAGE_BASE_URL}/$imageUrl",
                builder = {
                    placeholder(R.drawable.placeholder_image)
                    crossfade(true)
                }
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    onClick()
                },
            contentScale = ContentScale.FillWidth,
            contentDescription = "Banner"
        )
    }


}

@Composable
fun Indicator(active: Boolean) {
    val color = if (active) Color.Red else Color.White
    val size = if (active) 20.dp else 10.dp
    Box(
        modifier = Modifier
            .height(8.dp)
            .width(size)
            .clip(CircleShape)
            .background(color)

    )
}