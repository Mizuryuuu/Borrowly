package dev.dodo.borrowly.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.model.User
import dev.dodo.borrowly.ui.AppDestination
import dev.dodo.borrowly.ui.common.component.PopupHelper
import dev.dodo.borrowly.ui.home.HomeContent
import dev.dodo.borrowly.ui.home.HomeUiState
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    homeScreen: @Composable () -> Unit,
    statusScreen: @Composable () -> Unit,
    profileScreen: @Composable () -> Unit,
    navController: NavController
) {
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    MainContent(
        uiState = uiState,
        onSelectedNavItemChange = viewModel::changeSelectedTab,
        onRefreshUser = viewModel::onRefreshUser,
        homeScreen = { homeScreen() },
        statusScreen = { statusScreen() },
        profileScreen = { profileScreen() },
        navController = navController
    )
    
}

@Composable
fun MainContent(
    uiState: MainUiState,
    onSelectedNavItemChange: (NavItem) -> Unit,
    onRefreshUser: () -> Unit,
    homeScreen: @Composable () -> Unit,
    statusScreen: @Composable () -> Unit,
    profileScreen: @Composable () -> Unit,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        onRefreshUser()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (uiState.selectedNavItem) {
                    NavItem.HOME -> homeScreen()
                    NavItem.STATUS -> statusScreen()
                    NavItem.PROFILE -> profileScreen()
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFEDEDED)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.textLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavItem.entries.forEach { item ->
                        val isSelected = uiState.selectedNavItem == item
                        val contentColor = if (isSelected) {
                            AppColors.primary
                        } else {
                            Color(0xFF98A0AA)
                        }
                        Column(
                            modifier = Modifier
                                .height(46.dp)
                                .weight(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource(),
                                    onClick = {
                                        onSelectedNavItemChange(item)
                                    }
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = contentColor
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = item.title,
                                color = contentColor,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) {
                                    FontWeight.W600
                                } else {
                                    FontWeight.W400
                                },
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.showPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(enabled = false, onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            PopupHelper(
                title = "Lengkapi Profil",
                message = "Lengkapi data dulu biar bisa akses fitur.",
                buttonText = "Lengkapi",
                onClick = {
                    navController.navigate(
                        AppDestination.EditProfile(
                            idUser = uiState.user?.idUser ?: ""
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    BorrowlyTheme { 
        MainContent(
            uiState = MainUiState(
                selectedNavItem = NavItem.HOME,
                user = User(
                    image = "",
                    username = "",
                    email = "",
                    password = "",
                    address = "",
                    phoneNumber = ""
                )
            ),
            onSelectedNavItemChange = { },
            homeScreen = { HomeContent(
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
                                total = 1,
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
            ) },
            statusScreen = { },
            profileScreen = { },
            onRefreshUser = { },
            navController = rememberNavController()
        )
    }
}