package dev.dodo.borrowly.ui.myproduct

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.ui.AppDestination
import dev.dodo.borrowly.ui.common.component.CustomButton
import dev.dodo.borrowly.ui.common.component.PopupHelper
import dev.dodo.borrowly.ui.common.component.TitleBar
import dev.dodo.borrowly.ui.myproduct.component.ActionCard
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyProductScreen(
    navController: NavController
) {
    val viewModel: MyProductViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    MyProductContent(
        uiState = uiState,
        onDeleteClick = viewModel::onConfirmDelete,
        onRefreshMyProduct = viewModel::refreshMyProduct,
        confirmDelete = viewModel::deleteProduct,
        dismissDeleteDialog = viewModel::dismissDeleteDialog,
        navController = navController
    )
}

@Composable
fun MyProductContent(
    uiState: MyProductUiState,
    onDeleteClick: (String) -> Unit,
    confirmDelete: () -> Unit = {},
    dismissDeleteDialog: () -> Unit = {},
    onRefreshMyProduct: () -> Unit = {},
    navController: NavController,
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        onRefreshMyProduct()
    }

    Scaffold(
        topBar = {
            TitleBar(
                title = "Barang Saya",
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                state = lazyGridState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomButton(
                            text = "Tambah",
                            width = 150.dp,
                            height = 50.dp,
                            onClick = {
                                navController.navigate(
                                    AppDestination.AddEditProduct(
                                        idProduct = ""
                                    )
                                )
                            }
                        )
                    }
                }
                if (uiState.product.isEmpty() && !uiState.isLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada barang yang tersimpan.",
                                color = AppColors.secondary,
                                fontWeight = FontWeight.W400,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    items(uiState.product) { item ->
                        ActionCard(
                            image = item.product?.image,
                            title = item.product?.title,
                            total = item.product?.total.toString(),
                            address = item.location,
                            idProduct = item.product?.id,
                            isEditClick = {
                                navController.navigate(
                                    AppDestination.AddEditProduct(
                                        idProduct = item.product?.id
                                    )
                                )
                            },
                            isDeleteClick = {
                                item.product?.id?.let { onDeleteClick(it) }
                            },
                        )
                    }
                }
            }
        }
    }

    if (uiState.showDeleteDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable {
                    dismissDeleteDialog()
                },
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.clickable(enabled = false, onClick = {})) {
                PopupHelper(
                    title = "Hapus Barang",
                    message = "Apakah Anda yakin ingin menghapus barang ini?",
                    buttonText = "Hapus",
                    onClick = {
                        confirmDelete()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyProductScreenPrev() {
    BorrowlyTheme {
        MyProductContent(
            uiState = MyProductUiState(
                product = listOf(
                    ProductUiModel(
                        Product(
                            image = "",
                            title = "Kamera Canon EOS 1100D",
                            total = 1,
                            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut arcu sed risus malesuada aliquam. Vestibulum lacinia viverra arcu et blandit. Pellentesque condimentum risus vitae magna tincidunt elementum. Duis nec nibh id sapien dapibus interdum ac in ex. Sed libero ante, vulputate vel urna sed, iaculis tristique justo. Integer eu sem odio. Cras auctor, mi tempus consectetur feugiat, odio lorem luctus eros, sed pulvinar tortor leo tempor quam. Etiam vehicula mollis justo ut molestie."
                        ),
                        location = "Jln. Pahit yang kusuka No. 8",
                        phoneNumber = "082193849572",
                    ),
                    ProductUiModel(
                        Product(
                            image = "",
                            title = "Kamera Canon EOS 1100D",
                            total = 0,
                            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ut arcu sed risus malesuada aliquam. Vestibulum lacinia viverra arcu et blandit. Pellentesque condimentum risus vitae magna tincidunt elementum. Duis nec nibh id sapien dapibus interdum ac in ex. Sed libero ante, vulputate vel urna sed, iaculis tristique justo. Integer eu sem odio. Cras auctor, mi tempus consectetur feugiat, odio lorem luctus eros, sed pulvinar tortor leo tempor quam. Etiam vehicula mollis justo ut molestie."
                        ),
                        location = "Jln. Pahit yang kusuka No. 8",
                        phoneNumber = "082193849572",
                    )
                )
            ),
            onDeleteClick = { TODO() },
            navController = rememberNavController()
        )
    }
}