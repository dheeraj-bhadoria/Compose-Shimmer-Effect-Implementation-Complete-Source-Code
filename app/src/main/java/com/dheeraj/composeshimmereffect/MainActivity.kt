package com.dheeraj.composeshimmereffect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.dheeraj.composeshimmereffect.ui.theme.ComposeShimmerEffectTheme
import kotlinx.coroutines.delay

import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.core.graphics.withSave

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeShimmerEffectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CountryListScreen()
                }
            }
        }
    }
}

@Composable
fun CountryListScreen() {
    var showCountryList by remember { mutableStateOf(false) }
    var showShimmerEffect by remember { mutableStateOf(false) }

    Surface(color = Color.DarkGray) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    showCountryList = true
                    showShimmerEffect = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show Country")
            }

            if (showCountryList) {
                if (showShimmerEffect) {
                    LaunchedEffect(true) {
                        delay(2000)
                        showShimmerEffect = false
                    }
                    ShimmerEffect(modifier = Modifier.fillMaxSize())
                } else {
                    CountryList()
                }
            }
        }
    }
}

@Composable
fun ShimmerEffect(modifier: Modifier) {
    val transition = rememberInfiniteTransition()
    val animatedValue by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    Column(modifier) {
        repeat(5) { index ->
            ShimmerItem(modifier = Modifier.fillMaxWidth(), animatedValue = animatedValue)
        }
    }
}

@Composable
fun ShimmerItem(modifier: Modifier, animatedValue: Float) {
    Box(modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas {canvas ->
                // Set up the paint with linear gradient from transparent to white
                val paint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
                val width = size.width
                val height = size.height
                val colors = intArrayOf(
                    Color.Transparent.toArgb(),
                    Color.White.toArgb(),
                    Color.Transparent.toArgb()
                )
                val positions = floatArrayOf(
                    0f,
                    animatedValue,
                    1f
                )
                val shader = android.graphics.LinearGradient(
                    0f, 0f, width, height,
                    colors, positions,
                    android.graphics.Shader.TileMode.CLAMP
                )
                paint.shader = shader

                // Draw the shimmer effect with a rectangle
                canvas.nativeCanvas.withSave {
                    val rect = android.graphics.RectF(0f, 0f, width, height)
                    drawRoundRect(rect, 8f, 8f, paint)
                }
            }
        }
    }
}

@Composable
fun CountryList() {
    val countryList = listOf("Country 1", "Country 2", "Country 3", "Country 4", "Country 5")

    Column {
        countryList.forEach { country ->
            Text(
                text = country,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeShimmerEffectTheme {
        CountryListScreen()
    }
}