package dev.dodo.borrowly.ui.detailproduct.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dodo.borrowly.R
import dev.dodo.borrowly.ui.common.component.CustomButton
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme

@Composable
fun PopupTotal(
    uiState: String,
    decrement: () -> Unit,
    increment: () -> Unit,
    onClick: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Jumlah Barang",
                color = AppColors.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFF383C)
                        )
                        .clickable{
                            decrement()
                        }
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_mines),
                        contentDescription = null,
                        tint = Color(0xFFFF383C)
                    )
                }
                Text(
                    text = uiState,
                    color = AppColors.text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = AppColors.primary
                        )
                        .clickable{
                            increment()
                        }
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = null,
                        tint = AppColors.primary
                    )
                }
            }
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth(),
                height = 45.dp,
                color = AppColors.primary,
                text = "Pinjam",
                onClick = {
                    onClick()
                }
            )
        }
    }
}

@Preview
@Composable
private fun PopupTotalPrev() {
    BorrowlyTheme {
        PopupTotal(
            uiState = "1",
            decrement = {  },
            increment = {  },
            onClick = {  }
        )
    }
}