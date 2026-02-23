package dev.dodo.borrowly.ui.status.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import dev.dodo.borrowly.R
import dev.dodo.borrowly.common.helper.toReadableDate
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme

@Composable
fun StatusCard(
    image: String,
    date: String,
    status: Status,
    title: String,
    totalItem: String,
    address: String,
    onClick: () -> Unit
) {

    val statusColor = when (status) {
        Status.PROSES -> {
            Color(0xFFFFCC00)
        }
        Status.FINISH -> {
            Color(0xFF05BB01)
        }
        Status.REJECT -> {
            Color(0xFFFF383C)
        }
        Status.BORROW -> {
            AppColors.primary
        }
        else -> {
            AppColors.primary
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable{
                onClick()
            }
            .background(AppColors.textLight)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date,
                    color = Color(0xFFBCBCBC),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .background(Color.Transparent)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(20.dp),
                            color = statusColor
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = status.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600,
                        color = statusColor
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFEDEDED)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = title,
                    modifier = Modifier
                        .width(85.dp)
                        .height(70.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.img_placeholder),
                    error = painterResource(R.drawable.img_placeholder)
                )
                Spacer(Modifier.size(10.dp))
                Column {
                    Text(
                        text = title,
                        color = AppColors.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${totalItem}x Barang",
                        color = AppColors.secondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFEDEDED)
            )
            Row {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_map_pin),
                    contentDescription = null,
                    tint = AppColors.text
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = address,
                    color = AppColors.text,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }

}

@Preview
@Composable
private fun StatusCardPrev() {
    BorrowlyTheme {
        StatusCard(
            image = "",
            date = "12 February 2026",
            status = Status.PROSES,
            title = "Kamera Canon EOS 1100D",
            totalItem = "1",
            address = "Jln. Pahit yang kusuka No 8",
            onClick = {}
        )
    }
}