package dev.dodo.borrowly.ui.status.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.ui.theme.AppColors

@Composable
fun StatusChip(
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) AppColors.primary else AppColors.textLight)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = AppColors.primary
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = if(isSelected) AppColors.background else AppColors.primary
        )
    }
}

@Preview
@Composable
private fun SelectedStatusChipPrev() {
    StatusChip(
        label = Status.ALL.label,
        onClick = {  },
        isSelected = true
    )
}

@Preview
@Composable
private fun UnselectedStatusChipPrev() {
    StatusChip(
        label = Status.FINISH.label,
        onClick = {  },
        isSelected = false
    )
}