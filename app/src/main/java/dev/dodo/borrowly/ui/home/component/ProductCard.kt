package dev.dodo.borrowly.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.dodo.borrowly.R
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme

@Composable
fun ProductCard(
    modifier: Modifier,
    image: String?,
    title: String?,
    owner: String,
    address: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFFFF))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
                    text = title ?: "",
                    color = AppColors.text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Pemilik: $owner",
                    color = AppColors.text,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(Modifier.height(4.dp))
                Row {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_map_pin),
                        contentDescription = null,
                        tint = Color(0xFFBCBCBC)
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = address,
                        color = Color(0xFFBCBCBC),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun ProductCardPrev() {
    BorrowlyTheme {
        ProductCard(
            modifier = Modifier.fillMaxWidth(),
            image = "",
            title = "Kamera Canon EOS 1100D",
            owner = "Eko Supriato",
            address = "Jln. Pahit yang kusuka No 8",
            onClick = {}
        )
    }
}