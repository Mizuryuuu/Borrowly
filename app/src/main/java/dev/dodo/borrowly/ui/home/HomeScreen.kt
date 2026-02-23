package dev.dodo.borrowly.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.dodo.borrowly.R
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.model.User
import dev.dodo.borrowly.ui.AppDestination
import dev.dodo.borrowly.ui.common.component.SearchBox
import dev.dodo.borrowly.ui.home.component.ProductCard
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController
) {
    val viewModel: HomeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    HomeContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onChangeSearch,
        onRefreshUser = viewModel::refreshUser,
        onRefreshProduct = viewModel::refreshProduct,
        resetSearch = viewModel::resetSearch,
        onSearch = viewModel::searchProduct,
        navController = navController
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onRefreshProduct: () -> Unit = {},
    onRefreshUser: () -> Unit = {},
    onSearch: (String) -> Unit,
    resetSearch: () -> Unit = {},
    navController: NavController
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        onRefreshUser()
        onRefreshProduct()
    }

    AnimatedVisibility(
        visible = uiState.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
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

    AnimatedVisibility(
        visible = !uiState.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                state = lazyGridState,
                contentPadding = PaddingValues(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 28.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Hallo! \uD83D\uDC4B\uD83C\uDFFB",
                                    color = AppColors.text,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Spacer(Modifier.height(8.dp))
                                uiState.user?.username?.let {
                                    Text(
                                        text = it,
                                        color = AppColors.text,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.W700
                                    )
                                }
                            }
                            Image(
                                painter = painterResource(R.drawable.ic_profile_pic),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        clip = true,
                                        ambientColor = Color.Black.copy(alpha = 0.4f),
                                        spotColor = Color.Black.copy(alpha = 0.4f)
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        SearchBox(
                            value = uiState.searchQuery,
                            onValueChange = {
                                onSearchQueryChange(it)
                            },
                            placeholder = "Cari tanaman...",
                            modifier = Modifier.fillMaxWidth(),
                            onClear = {
                                resetSearch()
                            },
                            onSearch = {
                                onSearch(uiState.searchQuery)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                if (uiState.isEmpty) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tidak ada produk yang ditemukan",
                                fontWeight = FontWeight.W400,
                                color = AppColors.secondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(uiState.product) { item ->
                        ProductCard(
                            modifier = Modifier.fillMaxWidth(),
                            image = item.product?.image,
                            title = item.product?.title,
                            owner = item.username,
                            address = item.location,
                            onClick = {
                                item.product?.id?.let {
                                    navController.navigate(
                                        AppDestination.DetailProduct(idProduct = it)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPrev() {
    BorrowlyTheme {
        Surface {
            HomeContent(
                uiState = HomeUiState(
                    user = User(
                        image = "",
                        username = "Bijana Putra",
                        email = "bijanaputra@example.com",
                        password = "qwerty123",
                        address = "Jln Kampret ah ajg GG 67 Blok N",
                        phoneNumber = "087861839394",
                    ),
                    product = listOf(
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
                onSearchQueryChange = { },
                onSearch = { },
                navController = rememberNavController()
            )
        }
    }
}