package dev.dodo.borrowly.ui.status

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import dev.dodo.borrowly.common.helper.toReadableDate
import dev.dodo.borrowly.common.model.Loan
import dev.dodo.borrowly.common.model.LoanDetailUiModel
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.model.User
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.ui.AppDestination
import dev.dodo.borrowly.ui.status.component.StatusCard
import dev.dodo.borrowly.ui.status.component.StatusChip
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel
import java.util.Date

@Composable
fun StatusScreen(
    navController: NavController,
    initialFilter: Status?
) {
    val viewModel: StatusViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialFilter) {
        viewModel.changeSelectedChip(initialFilter ?: Status.ALL)
        viewModel.refreshLoans(initialFilter)
    }

    StatusContent(
        uiState = uiState,
        onChipChange = { newStatus ->
            viewModel.changeSelectedChip(newStatus)
            val filter = if (newStatus == Status.ALL) null else newStatus
            viewModel.refreshLoans(filter)
        },
        onLoanRefresh = { viewModel.refreshLoans(uiState.selectedChip) },
        navController = navController
    )
}

@Composable
fun StatusContent(
    uiState: StatusUiState,
    onChipChange: (Status) -> Unit,
    onLoanRefresh: () -> Unit,
    navController: NavController
) {
    val lazyGridState = rememberLazyGridState()

    AnimatedVisibility(
        visible = uiState.isLoading, enter = fadeIn(), exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Box(
                modifier = Modifier.padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(Status.entries) { item ->
                        StatusChip(
                            label = item.label,
                            onClick = { onChipChange(item) },
                            isSelected = item == uiState.selectedChip,
                        )
                    }
                }
            }
            Spacer(Modifier.size(24.dp))
            AnimatedVisibility(
                visible = !uiState.isLoading, enter = fadeIn(), exit = fadeOut()
            ) {
                if (uiState.isEmpty) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada peminjaman >_<",
                            fontWeight = FontWeight.W400,
                            color = AppColors.secondary,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        state = lazyGridState,
//                contentPadding = PaddingValues(
//                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 28.dp,
//                    bottom = 28.dp
//                ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.productQuery) { item ->
                            StatusCard(
                                image = item.product?.product?.image ?: "",
                                date = item.loan?.createdAt.toReadableDate(),
                                status = item.loan?.status ?: Status.PROSES,
                                title = item.product?.product?.title ?: "",
                                totalItem = item.loan?.total ?: "0",
                                address = item.owner?.address ?: "",
                                onClick = {
                                    navController.navigate(
                                        AppDestination.DetailLoanRequest(
                                            item.loan?.id ?: ""
                                        )
                                    )
                                })
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.size(30.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun StatusScreenPrev() {
    BorrowlyTheme {
        Surface {
            StatusContent(
                uiState = StatusUiState(
                productQuery = listOf(
                    LoanDetailUiModel(
                        loan = Loan(
                            id = "1",
                            status = Status.BORROW,
                            createdAt = Timestamp(Date()),
                        ), product = ProductUiModel(
                            Product(
                                image = "",
                                title = "Kamera Canon EOS 1100D",
                                total = 0,
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut arcu sed risus malesuada aliquam. Vestibulum lacinia viverra arcu et blandit. Pellentesque condimentum risus vitae magna tincidunt elementum. Duis nec nibh id sapien dapibus interdum ac in ex. Sed libero ante, vulputate vel urna sed, iaculis tristique justo. Integer eu sem odio. Cras auctor, mi tempus consectetur feugiat, odio lorem luctus eros, sed pulvinar tortor leo tempor quam. Etiam vehicula mollis justo ut molestie."
                            ),
                            location = "Jln. Pahit yang kusuka No. 8",
                            phoneNumber = "082193849572",
                        ), owner = User(
                            image = "",
                            email = "ekosupriato@example.com",
                            username = "Eko Supriato",
                            phoneNumber = "082193849572",
                            address = "Jln. Pahit yang kusuka No 8",
                            password = "qwerty"
                        ), borrower = User(
                            image = "",
                            email = "bijana@example.com",
                            username = "Bijana Putra",
                            phoneNumber = "087861839394",
                            address = "Jln. Angjay mabar No 66 Blok N2",
                            password = "qwerty"
                        )
                    ), LoanDetailUiModel(
                        loan = Loan(
                            id = "1",
                            status = Status.BORROW,
                            createdAt = Timestamp(Date()),
                        ), product = ProductUiModel(
                            Product(
                                image = "",
                                title = "Kamera Canon EOS 1100D",
                                total = 0,
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut arcu sed risus malesuada aliquam. Vestibulum lacinia viverra arcu et blandit. Pellentesque condimentum risus vitae magna tincidunt elementum. Duis nec nibh id sapien dapibus interdum ac in ex. Sed libero ante, vulputate vel urna sed, iaculis tristique justo. Integer eu sem odio. Cras auctor, mi tempus consectetur feugiat, odio lorem luctus eros, sed pulvinar tortor leo tempor quam. Etiam vehicula mollis justo ut molestie."
                            ),
                            location = "Jln. Pahit yang kusuka No. 8",
                            phoneNumber = "082193849572",
                        ), owner = User(
                            image = "",
                            email = "ekosupriato@example.com",
                            username = "Eko Supriato",
                            phoneNumber = "082193849572",
                            address = "Jln. Pahit yang kusuka No 8",
                            password = "qwerty"
                        ), borrower = User(
                            image = "",
                            email = "bijana@example.com",
                            username = "Bijana Putra",
                            phoneNumber = "087861839394",
                            address = "Jln. Angjay mabar No 66 Blok N2",
                            password = "qwerty"
                        )
                    )
                ), selectedChip = Status.PROSES
            ),
                onChipChange = { TODO() },
                onLoanRefresh = { TODO() },
                navController = rememberNavController()
            )
        }
    }
}