package dev.dodo.borrowly.ui.profile

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import dev.dodo.borrowly.R
import dev.dodo.borrowly.common.model.User
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.ui.AppDestination
import dev.dodo.borrowly.ui.main.MainViewModel
import dev.dodo.borrowly.ui.main.NavItem
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    ProfileContent(
        uiState = uiState,
        onUserRefresh = viewModel::refreshUser,
        onLogout = viewModel::logout,
        onHasLoanRequest = viewModel::checkLoanRequest,
        onSelectTab = mainViewModel::changeSelectedTab,
        onStatusFilter = { mainViewModel.openStatusWithFilter(it) },
        navController = navController
    )
}

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onUserRefresh: () -> Unit,
    onLogout: (() -> Unit) -> Unit,
    onHasLoanRequest: () -> Unit,
    onSelectTab: (NavItem) -> Unit,
    onStatusFilter: (Status) -> Unit,
    navController: NavController
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        onUserRefresh()
        onHasLoanRequest()
    }

    val isProfileIncomplete =
        uiState.user?.username.isNullOrBlank() ||
                uiState.user.phoneNumber.isBlank() ||
                uiState.user.address.isBlank()

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    AppColors.primary,
                                    Color(0xFF28326D)
                                )
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = uiState.user?.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape),
                            placeholder = painterResource(R.drawable.ic_profile_pic),
                            error = painterResource(R.drawable.ic_profile_pic)
                        )
                        Spacer(Modifier.size(16.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            (uiState.user?.username ?: uiState.user?.email)?.let {
                                Text(
                                    text = it,
                                    color = AppColors.textLight,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(Modifier.size(2.dp))

                            val text = if (uiState.user?.phoneNumber.isNullOrBlank()) {
                                "-"
                            } else {
                                uiState.user.phoneNumber
                            }

                            Text(
                                text = text,
                                color = AppColors.textLight.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400,
                            )
                        }
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColors.textLight)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectTab(NavItem.STATUS)
                                    onStatusFilter(Status.ALL)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status",
                                color = AppColors.text,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W700,
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_no_stick),
                                contentDescription = null,
                                tint = AppColors.text
                            )
                        }
                        Spacer(Modifier.size(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusItem.entries.forEach { item ->
                                Column(
                                    modifier = Modifier
                                        .clickable {
                                            onSelectTab(NavItem.STATUS)
                                            onStatusFilter(item.title)
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(item.icon),
                                        contentDescription = null,
                                        tint = AppColors.text
                                    )
                                    Spacer(Modifier.size(4.dp))
                                    Text(
                                        text = item.title.label,
                                        color = AppColors.text,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColors.textLight)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Peminjaman",
                            color = AppColors.text,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W700,
                        )
                        Spacer(Modifier.size(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate(AppDestination.MyProduct)
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_archive_box),
                                    contentDescription = null,
                                    tint = AppColors.text
                                )
                                Spacer(Modifier.size(4.dp))
                                Text(
                                    text = "Barang",
                                    color = AppColors.text,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                )
                            }
                            Spacer(Modifier.size(24.dp))
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate(AppDestination.LoanRequest)
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp),
                                    contentAlignment = Alignment.TopEnd
                                ){
                                    if (uiState.hasLoanRequest) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFF383C))
                                        )
                                    }
                                    Icon(
                                        painter = painterResource(R.drawable.ic_bell),
                                        contentDescription = null,
                                        tint = AppColors.text
                                    )
                                }
                                Spacer(Modifier.size(4.dp))
                                Text(
                                    text = "Permintaan",
                                    color = AppColors.text,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                )
                            }
                        }
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                AppDestination.EditProfile(
                                    idUser = uiState.user?.idUser ?: ""
                                )
                            )
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColors.textLight)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Edit Profile",
                                    color = AppColors.text,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W700,
                                )

                                if (isProfileIncomplete) {
                                    Spacer(Modifier.size(4.dp))
                                    Text(
                                        text = "Profilmu hampir siap. Lengkapi untuk membuka akses fitur",
                                        color = Color(0xFFFF383C),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.W400,
                                    )
                                }
                            }
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_no_stick),
                                contentDescription = null,
                                tint = AppColors.text
                            )
                        }
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLogout {
                                navController.navigate(AppDestination.Login) {
                                    popUpTo(0)
                                }
                            }
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColors.textLight)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Keluar dari akun",
                                color = Color(0xFFFF383C),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W700,
                            )
                            Spacer(Modifier.size(10.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_logout),
                                contentDescription = null,
                                tint = Color(0xFFFF383C)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPrev() {
    BorrowlyTheme {
        Surface {
            ProfileContent(
                uiState = ProfileUiState(
                    user = User(
                        image = "",
                        email = "bijana@example.com",
                        username = "Bijana Putra",
                        phoneNumber = "087861839394",
                        address = "Jln. Angjay mabar No 66 Blok N2",
                        password = "qwerty"
                    )
                ),
                onUserRefresh = { TODO() },
                onLogout = { TODO() },
                onHasLoanRequest = { TODO() },
                onSelectTab = { TODO() },
                onStatusFilter = { TODO() },
                navController = rememberNavController()
            )
        }
    }
}