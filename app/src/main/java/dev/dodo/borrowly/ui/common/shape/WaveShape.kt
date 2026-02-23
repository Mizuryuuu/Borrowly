package dev.dodo.borrowly.ui.common.shape

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import dev.dodo.borrowly.R

class WaveShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height * 0.9f)

            cubicTo(
                width * 0.55f, height * 0.8f,
                width * 0.55f, height * 1.15f,
                width, height * 0.9f
            )

            lineTo(width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Preview
@Composable
private fun WaveShapePrev() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.80f)
            .clip(WaveShape())
    ) {
        Image(
            painter = painterResource(R.drawable.wave_pattern),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}