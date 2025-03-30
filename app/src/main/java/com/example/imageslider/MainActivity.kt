package com.example.imageslider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.imageslider.ui.theme.ImageSliderTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageSliderTheme {
                ImageSlider(
                    images = listOf(
                        "https://c0.wallpaperflare.com/preview/803/128/11/k%C4%B1zkulesi-istanbul-bosphorus-manzara.jpg",
                        "https://wallpaperboat.com/wp-content/uploads/2019/07/cartoon-for-mobile-01.jpg",
                        "https://w0.peakpx.com/wallpaper/306/117/HD-wallpaper-kiz-kulesi-ay-black-communism-dark-deniz-gece-manzara-mont-saint-siyah.jpg",
                        "https://i.pinimg.com/564x/c5/d2/64/c5d26442f7ed03f5dd8289b5ccf08385.jpg",
                        "https://images.saymedia-content.com/.image/ar_1:1,c_fill,cs_srgb,fl_progressive,q_auto:eco,w_1200/MTk2MDk5MzU4ODM0MzA0NDcw/dogs-that-dont-shed-fur.jpg"
                    )
                )
            }
        }
    }
}

@Composable
fun ImageSlider(images: List<String>) {

    val pagerState = rememberPagerState(
        pageCount = {
            images.size
        },
        initialPage = 0
    )
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragCount ->
                    coroutineScope.launch {
                        if (dragCount > 0) {
                            pagerState.animateScrollToPage(
                                (pagerState.currentPage - 1).coerceAtLeast(0)
                            )
                        } else {
                            pagerState.animateScrollToPage(
                                (pagerState.currentPage + 1).coerceAtLeast(images.size - 1)
                            )
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 60.dp),
            pageSpacing = 2.dp,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scale = 1f - (0.2f * abs(pageOffset))
            var isLoading by remember { mutableStateOf(true) }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            ) {
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onSuccess = { isLoading = false },
                    onError = { isLoading = true },
                    modifier = Modifier
                        .size(450.dp)
                        .border(
                            width = 0.5.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clip(RoundedCornerShape(24.dp))
                )
                if (isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}