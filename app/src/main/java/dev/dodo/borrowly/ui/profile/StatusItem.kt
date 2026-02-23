package dev.dodo.borrowly.ui.profile

import androidx.annotation.DrawableRes
import dev.dodo.borrowly.R
import dev.dodo.borrowly.common.type.Status

enum class StatusItem(
    val title: Status,
    @DrawableRes val icon: Int,
) {
    PROSES(Status.PROSES, R.drawable.ic_bag),
    FINISH(Status.FINISH, R.drawable.ic_calendar_tick),
    BORROW(Status.BORROW, R.drawable.ic_truck),
    REJECT(Status.REJECT, R.drawable.ic_forbidden),

}