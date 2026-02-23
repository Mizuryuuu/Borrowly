package dev.dodo.borrowly.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme

@Composable
fun TitleBar(
    color: Color = AppColors.textLight,
    title: String, onNavigationActionClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            )
            .fillMaxWidth()
            .height(64.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(
                onClick = onNavigationActionClick
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color(0xFF000000),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700
                )
            }
            Spacer(modifier = Modifier.size(40.dp))
        }
    }
}

@Preview
@Composable
private fun TitleBarPreview() {
    BorrowlyTheme {
        Surface {
            TitleBar(
                title = "Pantau Tanaman", onNavigationActionClick = {})
        }
    }
}