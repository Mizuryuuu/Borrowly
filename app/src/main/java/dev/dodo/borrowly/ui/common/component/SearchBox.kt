package dev.dodo.borrowly.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dodo.borrowly.R
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import dev.dodo.borrowly.ui.theme.plus_jakarta_sans

@Composable
fun SearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    containerColor: Color = Color(0xFFF4F5F7),
    isShadowEnabled: Boolean = false,
    onSearch: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = TextStyle(
            color = AppColors.text,
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            fontFamily = plus_jakarta_sans
        ),
        singleLine = true,
        cursorBrush = SolidColor(AppColors.primary),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch?.invoke()
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .let {
                        if (isShadowEnabled) {
                            it.shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color.Black.copy(alpha = 0.4f),
                                spotColor = Color.Black.copy(alpha = 0.4f)
                            )
                        } else {
                            it
                        }
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF98A0AA)
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF98A0AA),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
                if (value.isNotEmpty() && onClear != null) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "Clear Search",
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(45f)
                            .clickable { onClear() }, // Panggil fungsi reset di sini
                        tint = AppColors.secondary
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun SearchBoxPreview() {
    BorrowlyTheme {
        SearchBox(
            value = "d",
            onValueChange = {},
            onClear = {
                println("Clear")
            },
            placeholder = "Cari tanaman..."
        )
    }
}