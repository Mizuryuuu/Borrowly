package dev.dodo.borrowly.ui.detailproduct

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import dev.dodo.borrowly.R
import dev.dodo.borrowly.common.helper.formattedPhoneNumber
import dev.dodo.borrowly.common.helper.formattedVisualPhoneNumber
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.model.User
import dev.dodo.borrowly.ui.common.component.CustomButton
import dev.dodo.borrowly.ui.common.component.TitleBar
import dev.dodo.borrowly.ui.detailproduct.component.PopupTotal
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder

@Composable
fun DetailProductScreen(
    idProduct: String = "",
    navController: NavController
) {
    val viewModel: DetailProductViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    DetailProductContent(
        idProduct = idProduct,
        uiState = uiState,
        onLoanProduct = viewModel::loanProduct,
        onProductRefresh = viewModel::onProductRefresh,
        incrementTotal = viewModel::incrementTotal,
        decrementTotal = viewModel::decrementTotal,
        showPopup = viewModel::showPopup,
        hidePopup = viewModel::hidePopup,
        navController = navController
    )
}

@Composable
fun DetailProductContent(
    idProduct: String = "",
    uiState: DetailProductUiState,
    onLoanProduct: (() -> Unit) -> Unit,
    onProductRefresh: (String) -> Unit,
    showPopup: () -> Unit,
    hidePopup: () -> Unit,
    incrementTotal: () -> Unit,
    decrementTotal: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val formatted = uiState.product?.phoneNumber?.formattedPhoneNumber()
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

        LaunchedEffect(idProduct) {
            if (idProduct.isNotEmpty()) {
                onProductRefresh(idProduct)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.textLight)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppColors.background)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "",
                            contentDescription = null,
                            modifier = Modifier
                                .width(290.dp)
                                .height(250.dp),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.img_placeholder),
                            error = painterResource(R.drawable.img_placeholder)
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = uiState.product?.product?.title ?: "",
                            color = AppColors.text,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W600
                        )
                        Text(
                            text = "Pemilik : ${uiState.product?.username}",
                            color = AppColors.secondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            text = "Jumlah: ${uiState.product?.product?.total}",
                            color = AppColors.secondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Alamat",
                                color = AppColors.secondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = uiState.product?.location ?: "",
                                color = AppColors.secondary,
                                fontSize = 14.sp,
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
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                text = uiState.product?.phoneNumber?.formattedVisualPhoneNumber()
                                    ?: "-",
                                color = AppColors.secondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                        Text(
                            text = "Deskripsi :",
                            color = AppColors.secondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600
                        )
                        Text(
                            text = uiState.product?.product?.description ?: "",
                            color = AppColors.secondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            lineHeight = 18.sp
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.size(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        CustomButton(
                            modifier = Modifier
                                .weight(1f),
                            height = 50.dp,
                            text = "Ajukan Pinjaman",
                            onClick = {
                                showPopup()
                            }
                        )
                        Spacer(Modifier.size(10.dp))
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    val message = "Halo, apakah ${uiState.product?.product?.title ?: "Barang"} ini tersedia?"
                                    val encodedMessage = URLEncoder.encode(message, "UTF-8")
                                    val url = "https://wa.me/$formatted?text=$encodedMessage"

                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                                .background(Color(0xFF05BB01)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_whatsapp),
                                contentDescription = null,
                                tint = AppColors.textLight
                            )
                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.size(30.dp))
                }
            }
        }
    }
    if (uiState.showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { hidePopup() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clickable(enabled = false, onClick = {})
            ) {
                PopupTotal(
                    uiState = uiState.total,
                    decrement = decrementTotal,
                    increment = incrementTotal,
                    onClick = {
                        onLoanProduct{
                            navController.popBackStack()
                        }
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun DetailProductScreenPrev() {
    BorrowlyTheme {
        DetailProductContent(
            uiState = DetailProductUiState(
                product = ProductUiModel(
                    Product(
                        image = "",
                        title = "Kamera Canon EOS 1100D",
                        total = 1,
                        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut arcu sed risus malesuada aliquam. Vestibulum lacinia viverra arcu et blandit. Pellentesque condimentum risus vitae magna tincidunt elementum. Duis nec nibh id sapien dapibus interdum ac in ex. Sed libero ante, vulputate vel urna sed, iaculis tristique justo. Integer eu sem odio. Cras auctor, mi tempus consectetur feugiat, odio lorem luctus eros, sed pulvinar tortor leo tempor quam. Etiam vehicula mollis justo ut molestie."
                    ),
                    username = "Eko Supriato",
                    location = "Jln. Pahit yang kusuka No. 8",
                    phoneNumber = "082193849572",
                ),
            ),
            onLoanProduct = { TODO() },
            onProductRefresh = { TODO() },
            incrementTotal = { },
            decrementTotal = { },
            showPopup = { },
            hidePopup = { },
            navController = rememberNavController()
        )
    }
}