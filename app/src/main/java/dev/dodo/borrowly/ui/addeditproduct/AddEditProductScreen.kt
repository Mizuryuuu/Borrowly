package dev.dodo.borrowly.ui.addeditproduct

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.dodo.borrowly.R
import dev.dodo.borrowly.ui.common.component.CustomButton
import dev.dodo.borrowly.ui.common.component.CustomTextField
import dev.dodo.borrowly.ui.common.component.TitleBar
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddProductScreen(
    navController: NavController,
    idProduct: String? = ""
) {
    val viewModel: AddEditProductViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    AddEditProductContent(
        uiState = uiState,
        onNameProductChange = viewModel::changeNameProduct,
        onQuantityChange = viewModel::changeQuantity,
        onDescriptionChange = viewModel::changeDescription,
        onAddProduct = viewModel::addMyProduct,
        onEditProduct = viewModel::savChangeProduct,
        idProduct = idProduct,
        refreshProduct = {
            if (idProduct != null) {
                viewModel.refreshProduct(idProduct)
            }
        },
        navController = navController
    )
}

@Composable
fun AddEditProductContent(
    uiState: AddEditProductUiState,
    onNameProductChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddProduct: (() -> Unit) -> Unit,
    onEditProduct: (() -> Unit) -> Unit,
    idProduct: String?,
    refreshProduct: () -> Unit,
    navController: NavController,
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(idProduct) {
        refreshProduct()
    }

    Scaffold(
        topBar = {
            TitleBar(
                title = if (idProduct.isNullOrBlank()) "Tambah Barang" else "Edit Barang",
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
                contentPadding = PaddingValues(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Gambar",
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700
                        )
                        Spacer(Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(152.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFE8ECF4),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(color = Color(0xFFF7F8F9))
                                .clickable {},
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_gallery_add),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF717171)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "+Tambah Foto",
                                color = Color(0xFF717171),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Nama Barang",
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.nameProduct,
                            onValueChange = {
                                onNameProductChange(it)
                            },
                            placeholder = "Masukkan Nama Barang",
                            errorMessage = uiState.nameProductErrorMessage
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Jumlah",
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.quantity,
                            onValueChange = {
                                onQuantityChange(it)
                            },
                            placeholder = "Masukkan Jumlah",
                            errorMessage = uiState.quantityErrorMessage
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Deskripsi",
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.description,
                            onValueChange = {
                                onDescriptionChange(it)
                            },
                            placeholder = "Masukkan Deskripsi",
                            isSingleLine = false,
                            minLines = 8,
                            errorMessage = uiState.descriptionErrorMessage
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        height = 50.dp,
                        text = "${if (idProduct.isNullOrBlank()) "Tambah" else "Edit"} Barang",
                        onClick = {
                            if (idProduct.isNullOrBlank()) {
                                onAddProduct {
                                    navController.popBackStack()
                                }
                            } else {
                                onEditProduct {
                                    navController.popBackStack()
                                }
                            }
                        }
                    )
                    Spacer(Modifier.size(30.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddProductScreenPrev() {
    BorrowlyTheme {
        AddEditProductContent(
            uiState = AddEditProductUiState(),
            onNameProductChange = { TODO() },
            onQuantityChange = { TODO() },
            onDescriptionChange = { TODO() },
            onAddProduct = { TODO() },
            idProduct = "",
            refreshProduct = { TODO() },
            onEditProduct = { TODO() },
            navController = rememberNavController()
        )
    }
}