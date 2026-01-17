package com.example.movieapp.ui.screens

import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movieapp.data.local.MovieEntity
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Dynamic Colors
    val defaultColor = MaterialTheme.colorScheme.background
    var dominantColor by remember { mutableStateOf(defaultColor) }
    val defaultPrimary = MaterialTheme.colorScheme.primary
    var vibrantColor by remember { mutableStateOf(defaultPrimary) }

    val animatedBgColor by animateColorAsState(
        targetValue = dominantColor,
        animationSpec = tween(durationMillis = 1000),
        label = "bgColorAnimation"
    )

    val animatedAccentColor by animateColorAsState(
        targetValue = vibrantColor,
        animationSpec = tween(durationMillis = 1000),
        label = "accentColorAnimation"
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            // Overlay TopBar
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            animatedBgColor.copy(alpha=0.8f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = "Error: ${uiState.error}",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                uiState.movie?.let { movie ->
                    // Parallax Background Image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://image.tmdb.org/t/p/original${movie.posterPath}")
                            .crossfade(true)
                            .allowHardware(false)
                            .listener(onSuccess = { _, result ->
                                val bitmap = (result.drawable as BitmapDrawable).bitmap
                                Palette.from(bitmap).generate { palette ->
                                    palette?.dominantSwatch?.rgb?.let { colorValue ->
                                        dominantColor = Color(colorValue)
                                    }
                                    palette?.vibrantSwatch?.rgb?.let { colorValue ->
                                        vibrantColor = Color(colorValue)
                                    }
                                }
                            })
                            .build(),
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .graphicsLayer {
                                alpha = 1f - (scrollState.value / 1000f).coerceIn(0f, 1f)
                                translationY = scrollState.value * 0.5f
                            }
                    )
                    
                    // Gradient Overlay for text readability
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(padding)
                    ) {
                        Spacer(modifier = Modifier.height(350.dp)) // Push content down

                        // Content Card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                            MaterialTheme.colorScheme.background
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            // Title & Rating
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                RatingCircle(voteAverage = movie.voteAverage, accentColor = animatedAccentColor)
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = movie.releaseDate ?: "Unknown Release Date",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Button(
                                    onClick = { 
                                         com.example.movieapp.util.NotificationHelper.showPlayNotification(context, movie.title)
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = animatedAccentColor),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Play Now", fontWeight = FontWeight.Bold)
                                }
                                
                                IconButton(
                                    onClick = { viewModel.toggleFavourite() },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                ) {
                                    Icon(
                                        imageVector = if (movie.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Like",
                                        tint = if (movie.isFavourite) Color.Red else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                
                                IconButton(
                                    onClick = { viewModel.toggleWatchlist() },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                ) {
                                    Icon(
                                        imageVector = if (movie.isWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Watchlist",
                                        tint = if (movie.isWatchlist) animatedAccentColor else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Overview
                            Text(
                                text = "Overview",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Recommendations Section
                            if (uiState.recommendations.isNotEmpty()) {
                                RecommendationsSection(recommendations = uiState.recommendations)
                            }
                            
                             Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingCircle(voteAverage: Double, accentColor: Color) {
    val targetProgress = (voteAverage / 10.0).toFloat()
    val progress = remember { Animatable(0f) }
    
    LaunchedEffect(targetProgress) {
        progress.animateTo(
            targetValue = targetProgress,
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        )
    }

    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(60.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            strokeWidth = 4.dp
        )
        CircularProgressIndicator(
            progress = { progress.value },
            modifier = Modifier.size(60.dp),
            color = accentColor,
            strokeWidth = 4.dp
        )
        Text(
            text = "${(progress.value * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

enum class ViewType { GRID, LIST, STACK }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendationsSection(recommendations: List<MovieEntity>) {
    var viewType by remember { mutableStateOf(ViewType.LIST) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "More Like This",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Row {
                IconButton(onClick = { viewType = ViewType.LIST }) {
                    Icon(
                        Icons.Default.List, 
                        contentDescription = "List View",
                        tint = if(viewType == ViewType.LIST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { viewType = ViewType.GRID }) {
                    Icon(
                        Icons.Default.Apps, 
                        contentDescription = "Grid View",
                        tint = if(viewType == ViewType.GRID) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { viewType = ViewType.STACK }) {
                    Icon(
                        Icons.Default.ViewCarousel, 
                        contentDescription = "Stack View",
                        tint = if(viewType == ViewType.STACK) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + expandVertically()
        ) {
            when (viewType) {
                ViewType.LIST -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(recommendations) { movie ->
                            RecommendationCard(movie = movie, width = 140.dp, height = 220.dp)
                        }
                    }
                }
                ViewType.GRID -> {
                    // LazyVerticalGrid inside a Scrollable Column is problematic (nested scroll).
                    // Since we are already in a vertical scroll, we should use a fixed height or custom layout.
                    // Or easier: FlowRow? No, user wants Grid structure.
                    // We can simulate grid with Column + Row or disable scrolling of the grid and calculate height.
                    // For simplicity and performance inside a scrollable detail page, horizontal grid (LazyHorizontalGrid) or just sticking to LazyRow is better,
                    // BUT "Grid" usually implies vertical scanning.
                    // Let's implement a "Vertical Grid" behavior by just listing them in Rows of 2 or 3 manually to avoid nested scrolling issues.
                    // OR: Use a Fixed Height LazyVerticalGrid.
                    
                    val chunkSize = 3
                    val chunks = recommendations.chunked(chunkSize)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        chunks.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { movie ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        RecommendationCard(movie = movie, width = null, height = 180.dp)
                                    }
                                }
                                // Fill empty spaces
                                repeat(chunkSize - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                ViewType.STACK -> {
                    val pagerState = rememberPagerState(pageCount = { recommendations.size })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.height(300.dp),
                        contentPadding = PaddingValues(horizontal = 64.dp),
                        pageSpacing = 16.dp
                    ) { page ->
                        val movie = recommendations[page]
                        Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        val pageOffset = (
                                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                        )

                                        // Simple scale effect
                                        val scale = androidx.compose.ui.util.lerp(
                                            start = 0.85f,
                                            stop = 1f,
                                            fraction = 1f - kotlin.math.abs(pageOffset.coerceIn(-1f, 1f))
                                        )
                                        scaleX = scale
                                        scaleY = scale
                                        alpha = androidx.compose.ui.util.lerp(
                                            start = 0.5f,
                                            stop = 1f,
                                            fraction = 1f - kotlin.math.abs(pageOffset.coerceIn(-1f, 1f))
                                        )
                                    },
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                             AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = movie.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(movie: MovieEntity, width: androidx.compose.ui.unit.Dp?, height: androidx.compose.ui.unit.Dp) {
    Card(
        modifier = Modifier
            .then(if (width != null) Modifier.width(width) else Modifier.fillMaxWidth())
            .height(height)
            .clickable { /* TODO: Navigate to detail */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w200${movie.posterPath}")
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )
            Text(
                text = movie.title,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}
