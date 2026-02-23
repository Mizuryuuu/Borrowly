package dev.dodo.borrowly.ui.detailstatus

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import dev.dodo.borrowly.R
import dev.dodo.borrowly.common.helper.formattedPhoneNumber
import dev.dodo.borrowly.common.helper.formattedVisualPhoneNumber
import dev.dodo.borrowly.common.helper.toReadableDate
import dev.dodo.borrowly.common.model.Loan
import dev.dodo.borrowly.common.model.LoanDetailUiModel
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.model.User
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.ui.common.component.CustomButton
import dev.dodo.borrowly.ui.common.component.PopupHelper
import dev.dodo.borrowly.ui.common.component.TitleBar
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder
import java.util.Date
import androidx.core.net.toUri

@Composable
fun DetailStatusScreen(
    idLoan: String = "",
    navController: NavController
) {
    val viewModel: DetailStatusViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    DetailStatusContent(
        idLoan = idLoan,
        uiState = uiState,
        onLoanRefresh = viewModel::onLoanRefresh,
        onCancelLoan = viewModel::onCancelLoan,
        onDoneLoan = viewModel::onDoneLoan,
        onConfirmCancel = viewModel::onConfirmReject,
        onDismissDialog = viewModel::dismissRejectDialog,
        navController = navController
    )

}

@SuppressLint("UseKtx")
@Composable
fun DetailStatusContent(
    idLoan: String = "",
    uiState: DetailStatusUiState,
    onLoanRefresh: (String) -> Unit,
    onConfirmCancel: (String) -> Unit,
    onDismissDialog: () -> Unit,
    onCancelLoan: (String) -> Unit,
    onDoneLoan: (String) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val formatted = uiState.loanTransaction?.owner?.phoneNumber?.formattedPhoneNumber()

    LaunchedEffect(idLoan) {
        onLoanRefresh(idLoan)
    }

    val lazyGridState = rememberLazyGridState()
    Scaffold(
        topBar = {
            TitleBar(
                title = "Detail Barang",
                onNavigationActionClick = {
                    navController.popBackStack()
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.textLight)
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                state = lazyGridState,
//                contentPadding = PaddingValues(
//                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
//                    bottom = 28.dp
//                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        val textStatus = uiState.loanTransaction?.loan?.status?.label

                        Text(
                            text = if (textStatus != "Ditolak") "Sedang $textStatus" else textStatus,
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0xFFEDEDED)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tanggal Permintaan",
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = uiState.loanTransaction?.loan?.createdAt?.toReadableDate()
                                    .toString(),
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Detail Barang",
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0xFFEDEDED)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = uiState.loanTransaction?.product?.product?.image,
                                contentDescription = null,
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
                                    text = uiState.loanTransaction?.product?.product?.title ?: "",
                                    color = AppColors.text,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "${uiState.loanTransaction?.product?.product?.total}x Barang",
                                    color = AppColors.secondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W600
                                )
                            }
                        }
                        Text(
                            text = "Deskripsi :",
                            color = AppColors.secondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600
                        )
                        Text(
                            text = uiState.loanTransaction?.product?.product?.description
                                ?: "Tidak ada deskripsi",
                            color = AppColors.secondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            lineHeight = 18.sp
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Detail Pemilik",
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0xFFEDEDED)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Nama Pemilik",
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = uiState.loanTransaction?.owner?.username ?: "",
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Alamat",
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = uiState.loanTransaction?.owner?.address ?: "",
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "No telepon pemilik",
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = uiState.loanTransaction?.owner?.phoneNumber?.formattedVisualPhoneNumber()
                                    ?: "",
                                color = AppColors.secondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0xFFEDEDED)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val message =
                                        "Halo, saya ingin meminjam ${uiState.loanTransaction?.product?.product?.title ?: "barang ini"}."
                                    val encodedMessage = URLEncoder.encode(message, "UTF-8")
                                    val url = "https://wa.me/$formatted?text=$encodedMessage"

                                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                    context.startActivity(intent)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "WhatsApp Pemilik",
                                    color = AppColors.text,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W400,
                                )
                            }
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_no_stick),
                                contentDescription = null,
                                tint = AppColors.text
                            )
                        }
                        Spacer(Modifier.size(30.dp))
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        if (uiState.loanTransaction?.loan?.status == Status.BORROW) {
                            CustomButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                height = 50.dp,
                                color = Color(0xFF05BB01),
                                text = "Selesai",
                                onClick = {
                                    onDoneLoan(uiState.loanTransaction.loan.id)
                                }
                            )
                        }
                        if (uiState.loanTransaction?.loan?.status == Status.PROSES || uiState.loanTransaction?.loan?.status == Status.BORROW) {
                            Spacer(Modifier.size(10.dp))
                            CustomButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                height = 50.dp,
                                text = "Batalkan Peminjaman",
                                onClick = {
                                    uiState.loanTransaction.loan.id.let { onConfirmCancel(it) }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    if (uiState.showCancelDialog) {
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
                    title = "Batalkan Peminjaman",
                    message = "Apakah Anda yakin ingin batalkan peminjaman ini?",
                    buttonText = "Batalkan Peminjaman",
                    color = AppColors.primary,
                    onClick = {
                        uiState.loanTransaction?.loan?.id?.let { realId ->
                            onCancelLoan(realId)
                        }
                        onDismissDialog()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DetailStatusScreenPrev() {
    BorrowlyTheme {
        DetailStatusContent(
            uiState = DetailStatusUiState(
                loanTransaction = LoanDetailUiModel(
                    loan = Loan(
                        id = "",
                        status = Status.REJECT,
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
            ),
            onLoanRefresh = { TODO() },
            navController = rememberNavController(),
            onCancelLoan = { TODO() },
            onConfirmCancel = { TODO() },
            onDismissDialog = { TODO() },
            onDoneLoan = { TODO() }
        )
    }
}