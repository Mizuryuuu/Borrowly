package dev.dodo.borrowly.ui.loanrequest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import dev.dodo.borrowly.ui.common.component.PopupHelper
import dev.dodo.borrowly.ui.common.component.TitleBar
import dev.dodo.borrowly.ui.loanrequest.component.LoanCard
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel
import java.util.Date

@Composable
fun LoanRequestScreen(
    navController: NavController
) {
    val viewModel: LoanRequestViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LoanRequestContent(
        navController = navController,
        uiState = uiState,
        onRefresh = viewModel::onRefreshLoans,
        onAccept = viewModel::onAccept,
        onConfirmReject = viewModel::onConfirmReject,
        onDismissDialog = viewModel::dismissRejectDialog,
        onReject = viewModel::onReject
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanRequestContent(
    uiState: LoanRequestUiState,
    onRefresh: () -> Unit,
    onAccept: (String) -> Unit,
    onReject: () -> Unit,
    onDismissDialog: () -> Unit = {},
    onConfirmReject: (String) -> Unit,
    navController: NavController
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        onRefresh()
    }

    Scaffold(
        topBar = {
            TitleBar(
                title = "Permintaan Peminjaman",
                onNavigationActionClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.textLight)
                .padding(innerPadding)
        ) {
            if (uiState.isEmpty && !uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada permintaan peminjaman.",
                            color = AppColors.secondary,
                            fontWeight = FontWeight.W400,
                            fontSize = 16.sp
                        )
                    }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    state = lazyGridState,
                    contentPadding = PaddingValues(
                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                        bottom = 28.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val filteredList =
                        uiState.loanRequest.filter { it.loan?.status == Status.PROSES }
                    items(filteredList, key = { it.loan?.id ?: it.hashCode() }) { item ->
                        LoanCard(
                            idLoan = item.loan?.id ?: "",
                            image = item.product?.product?.image ?: "",
                            title = item.product?.product?.title ?: "",
                            borrower = item.borrower?.username ?: "",
                            number = item.borrower?.phoneNumber ?: "",
                            date = item.loan?.createdAt?.toReadableDate(),
                            isAccept = { item.loan?.id?.let { p1 -> onAccept(p1) } },
                            isReject = { item.loan?.id?.let { p1 -> onConfirmReject(p1) } }
                        )
                    }
                }
            }
        }
    }
    if (uiState.showRejectDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable {
                    onDismissDialog()
                },
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.clickable(enabled = false, onClick = {})) {
                PopupHelper(
                    title = "Tolak Peminjaman",
                    message = "Apakah Anda yakin ingin tolak peminjaman ini?",
                    buttonText = "Tolak",
                    onClick = {
                        onReject()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoanRequestScreenPrev() {
    BorrowlyTheme {
        LoanRequestContent(
            navController = rememberNavController(),
            uiState = LoanRequestUiState(
                loanRequest = listOf(
                    LoanDetailUiModel(
                        loan = Loan(
                            id = "",
                            status = Status.BORROW,
                            createdAt = Timestamp(Date()),
                        ),
                        product = ProductUiModel(
                            Product(
                                image = "",
                                title = "Kamera Canon EOS 1100D",
                                total = 0,
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut arcu sed risus malesuada aliquam. Vestibulum lacinia viverra arcu et blandit. Pellentesque condimentum risus vitae magna tincidunt elementum. Duis nec nibh id sapien dapibus interdum ac in ex. Sed libero ante, vulputate vel urna sed, iaculis tristique justo. Integer eu sem odio. Cras auctor, mi tempus consectetur feugiat, odio lorem luctus eros, sed pulvinar tortor leo tempor quam. Etiam vehicula mollis justo ut molestie."
                            ),
                            location = "Jln. Pahit yang kusuka No. 8",
                            phoneNumber = "082193849572",
                        ),
                        owner = User(
                            image = "",
                            email = "ekosupriato@example.com",
                            username = "Eko Supriato",
                            phoneNumber = "082193849572",
                            address = "Jln. Pahit yang kusuka No 8",
                            password = "qwerty"
                        ),
                        borrower = User(
                            image = "",
                            email = "bijana@example.com",
                            username = "Bijana Putra",
                            phoneNumber = "087861839394",
                            address = "Jln. Angjay mabar No 66 Blok N2",
                            password = "qwerty"
                        )
                    ),
                    LoanDetailUiModel(
                        loan = Loan(
                            status = Status.BORROW,
                            createdAt = Timestamp(Date()),
                        ),
                        product = ProductUiModel(
                            Product(
                                image = "",
                                title = "Kamera Canon EOS 1100D",
                                total = 1,
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut arcu sed risus malesuada aliquam. Vestibulum lacinia viverra arcu et blandit. Pellentesque condimentum risus vitae magna tincidunt elementum. Duis nec nibh id sapien dapibus interdum ac in ex. Sed libero ante, vulputate vel urna sed, iaculis tristique justo. Integer eu sem odio. Cras auctor, mi tempus consectetur feugiat, odio lorem luctus eros, sed pulvinar tortor leo tempor quam. Etiam vehicula mollis justo ut molestie."
                            ),
                            location = "Jln. Pahit yang kusuka No. 8",
                            phoneNumber = "082193849572",
                        ),
                        owner = User(
                            image = "",
                            email = "ekosupriato@example.com",
                            username = "Eko Supriato",
                            phoneNumber = "082193849572",
                            address = "Jln. Pahit yang kusuka No 8",
                            password = "qwerty"
                        ),
                        borrower = User(
                            image = "",
                            email = "bijana@example.com",
                            username = "Bijana Putra",
                            phoneNumber = "087861839394",
                            address = "Jln. Angjay mabar No 66 Blok N2",
                            password = "qwerty"
                        )
                    )
                )
            ),
            onAccept = { TODO() },
            onRefresh = { TODO() },
            onDismissDialog = { TODO() },
            onConfirmReject = { TODO() },
            onReject = { TODO() }
        )
    }
}