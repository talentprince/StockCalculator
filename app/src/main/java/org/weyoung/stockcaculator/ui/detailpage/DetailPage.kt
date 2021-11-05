package org.weyoung.stockcaculator.ui.detailpage

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke.Companion.DefaultMiter
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun DetailPage(modifier: Modifier = Modifier, navController: NavHostController, code: String) {
    val viewModel: DetailViewModel = hiltViewModel()
    val favoriteState = viewModel.favoriteState.collectAsState(initial = emptyList())
    val records = viewModel.stockRecords(code).collectAsState(initial = emptyList())
    val trendColor = MaterialTheme.colors.primary
    val coordinateColor = MaterialTheme.colors.onBackground
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 30f
        color = coordinateColor.value.hashCode()
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    }
    val favoriteColor =
        if (favoriteState.value.contains(code)) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "走势")
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            elevation = 12.dp,
            actions = {
                IconButton(onClick = { viewModel.favorite(code) }) {
                    Icon(Icons.Filled.Favorite, "Favorite", tint = favoriteColor)
                }
            }
        )
    }) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            Text(
                modifier = modifier.padding(8.dp),
                text = "每日竞价",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h5
            )
            Card(modifier = modifier.padding(8.dp)) {
                Canvas(
                    modifier = modifier
                        .height(300.dp)
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    val totalRecords = records.value.size
                    val lineDistance = size.width / (totalRecords + 1)
                    val cHeight = size.height
                    var currentLineDistance = 0f + lineDistance
                    records.value.forEachIndexed { index, stockItem ->
                        val currentY = calculateYCoordinate(
                            currentTransactionRate = stockItem.bidding.toDouble(),
                            canvasHeight = cHeight
                        )
                        if (totalRecords >= index + 2) {
                            drawLine(
                                start = Offset(
                                    x = currentLineDistance,
                                    y = currentY
                                ),
                                end = Offset(
                                    x = currentLineDistance + lineDistance,
                                    y = calculateYCoordinate(
                                        currentTransactionRate = records.value[index + 1].bidding.toDouble(),
                                        canvasHeight = cHeight
                                    )
                                ),
                                color = trendColor,
                                strokeWidth = DefaultMiter
                            )
                        }
                        drawCircle(
                            center =
                            Offset(
                                x = currentLineDistance,
                                y = currentY
                            ), color = trendColor, radius = 10.0f
                        )
                        drawIntoCanvas {
                            it.nativeCanvas.drawText(
                                stockItem.bidding,
                                currentLineDistance,
                                currentY,
                                textPaint
                            )
                        }
                        currentLineDistance += lineDistance
                    }
                }
            }
        }

    }
}

private fun calculateYCoordinate(
    currentTransactionRate: Double,
    canvasHeight: Float
): Float {
    val maxAndCurrentValueDifference = 10 - currentTransactionRate.toFloat()
    val relativePercentageOfScreen = (canvasHeight / 20)
    return maxAndCurrentValueDifference * relativePercentageOfScreen
}