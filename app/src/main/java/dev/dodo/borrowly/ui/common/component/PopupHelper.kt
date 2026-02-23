package dev.dodo.borrowly.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme

@Composable
fun PopupHelper(
    title: String,
    message: String,
    buttonText: String,
    onClick: () -> Unit,
    color: Color = Color(0xFFFF383C),
) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.textLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp, vertical = 42.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = title,
                color = AppColors.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
            Text(
                text = message,
                color = AppColors.secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
            )
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth(),
                height = 45.dp,
                color = color,
                text = buttonText,
                onClick = {
                    onClick()
                }
            )
        }
    }
}

@Preview
@Composable
private fun PopupHelperPrev() {
    BorrowlyTheme {
        PopupHelper(
            title = "Logout",
            message = "Yakin untuk keluar dari akun?",
            buttonText = "Logout",
            color = Color(0xFFFF383C),
            onClick = {  }
        )
    }
}