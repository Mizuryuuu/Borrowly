package dev.dodo.borrowly.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.dodo.borrowly.R
import dev.dodo.borrowly.ui.theme.BorrowlyTheme

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE8ECF4),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color(0xFFFFFFFF))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF292D32)
        )
    }
}

@Preview
@Composable
private fun BackButtonPreview() {
    BorrowlyTheme {
        BackButton(
            onClick = {}
        )
    }
}