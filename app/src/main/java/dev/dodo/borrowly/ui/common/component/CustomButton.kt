package dev.dodo.borrowly.ui.common.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dodo.borrowly.ui.theme.AppColors

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 48.dp,
    radius: Dp = 12.dp,
    weight: FontWeight = FontWeight.W600,
    color: Color = AppColors.primary,
    paddingHorizontal: Dp = 16.dp,
    fontSize: TextUnit = 16.sp,
    @DrawableRes icon: Int? = null,
    iconSize: Dp = 20.dp,
    iconPadding: Dp = 8.dp,
    isEnabled: Boolean = true
) {
    Row(
        modifier = modifier
            .let {
                if (width != null) {
                    it.width(width)
                } else {
                    it
                }
            }
            .height(height)
            .clip(RoundedCornerShape(radius))
            .background(if (isEnabled) color else Color(0xFFB5B5B5))
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .padding(horizontal = paddingHorizontal)
        ,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = Color(0xFFE8F5F2)
            )
            Spacer(Modifier.width(iconPadding))
        }
        Text(
            text = text,
            color = AppColors.textLight,
            fontSize = fontSize,
            fontWeight = weight
        )
    }
}

@Preview
@Composable
private fun CustomButtonPreview() {
    CustomButton(
        text = "Button",
        onClick = {}
    )
}