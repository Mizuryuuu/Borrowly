package dev.dodo.borrowly.ui.loanrequest.component

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.dodo.borrowly.R
import dev.dodo.borrowly.ui.common.component.CustomButton
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme

@Composable
fun LoanCard(
    idLoan: String,
    image: String,
    title: String,
    borrower: String,
    number: String,
    date: String?,
    isAccept: (String) -> Unit,
    isReject: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true,
                ambientColor = Color.Black.copy(alpha = 0.4f),
                spotColor = Color.Black.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.textLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                Column {
                    Text(
                        text = title,
                        color = AppColors.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Peminjam: $borrower",
                        color = AppColors.text,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W600
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "No Hp: $number",
                        color = Color(0xFFBCBCBC),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W400
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFEDEDED)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    CustomButton(
                        width = 80.dp,
                        height = 30.dp,
                        text = "Tolak",
                        fontSize = 12.sp,
                        weight = FontWeight.W400,
                        radius = 8.dp,
                        color = Color(0xFFFF383C),
                        onClick = {
                            isReject(idLoan)
                        }
                    )
                    Spacer(Modifier.size(10.dp))
                    CustomButton(
                        width = 80.dp,
                        height = 30.dp,
                        text = "Terima",
                        fontSize = 12.sp,
                        weight = FontWeight.W400,
                        radius = 8.dp,
                        color = Color(0xFF049601),
                        onClick = {
                            isAccept(idLoan)
                        }
                    )
                }

                date?.let {
                    Text(
                        text = it,
                        color = AppColors.secondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoanCardPrev() {
    BorrowlyTheme {
        LoanCard(
            image = "",
            title = "Kamera Canon EOS 1100D",
            borrower = "Bijana Putra",
            number = "087861839394",
            date = "12 Febuari 2026",
            idLoan = "",
            isAccept = { TODO() },
            isReject = { TODO() }
        )
    }
}