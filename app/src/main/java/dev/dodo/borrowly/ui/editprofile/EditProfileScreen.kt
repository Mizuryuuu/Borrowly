package dev.dodo.borrowly.ui.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun EditProfileScreen(
    navController: NavController
) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    EditProfileContent(
        uiState = uiState,
        onUsernameChange = viewModel::changeUsername,
        onEmailChange = viewModel::changeEmail,
        onPhoneNumberChange = viewModel::changePhoneNumber,
        onAddressChange = viewModel::changeAddress,
        onPasswordChange = viewModel::changePassword,
        onNewPasswordChange = viewModel::changeNewPassword,
        refreshUser = viewModel::refreshUser,
        onSaveClick = viewModel::saveProfileChanges,
        navController = navController,
    )

}

@Composable
fun EditProfileContent(
    uiState: EditProfileUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSaveClick: (() -> Unit) -> Unit,
    refreshUser: () -> Unit,
    navController: NavController
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        refreshUser()
    }

    Scaffold(
        topBar = {
            TitleBar(
                title = "Edit Profile",
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
                                .height(140.dp)
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
                        Row {
                            Text(
                                text = "Email",
                                color = AppColors.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                            Text(
                                text = "*",
                                color = Color(0xFFFF383C),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.email,
                            onValueChange = {
                                onEmailChange(it)
                            },
                            placeholder = "Masukkan Email",
                            errorMessage = uiState.emailErrorMessage,
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Row {
                            Text(
                                text = "Username",
                                color = AppColors.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                            Text(
                                text = "*",
                                color = Color(0xFFFF383C),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.username,
                            onValueChange = {
                                onUsernameChange(it)
                            },
                            placeholder = "Masukkan Username",
                            errorMessage = uiState.usernameErrorMessage,
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Row {
                            Text(
                                text = "No Hp",
                                color = AppColors.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                            Text(
                                text = "*",
                                color = Color(0xFFFF383C),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.phoneNumber,
                            onValueChange = {
                                onPhoneNumberChange(it)
                            },
                            placeholder = "Contoh: 08XXXXXXX",
                            errorMessage = uiState.phoneNumberErrorMessage,
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Row {
                            Text(
                                text = "Alamat",
                                color = AppColors.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                            Text(
                                text = "*",
                                color = Color(0xFFFF383C),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.address,
                            onValueChange = {
                                onAddressChange(it)
                            },
                            placeholder = "Masukkan Alamat Tinggal",
                            isSingleLine = false,
                            minLines = 8,
                            errorMessage = uiState.addressErrorMessage
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
                            text = "Password",
                            color = AppColors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.password,
                            onValueChange = {
                                onPasswordChange(it)
                            },
                            placeholder = "Masukkan Password",
                            isPassword = true,
                            errorMessage = uiState.passwordErrorMessage,
                            enabled = false,
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        Row {
                            Text(
                                text = "New Password",
                                color = AppColors.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                            Text(
                                text = "*",
                                color = Color(0xFFFF383C),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        CustomTextField(
                            value = uiState.newPassword,
                            onValueChange = {
                                onNewPasswordChange(it)
                            },
                            placeholder = "Masukkan Password",
                            isPassword = true,
                            errorMessage = uiState.newPasswordErrorMessage
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        height = 50.dp,
                        text = if (uiState.isLoading) "Menyimpan..." else "Simpan",
                        isEnabled = uiState.isFormValid && !uiState.isLoading,
                        onClick = {
                            onSaveClick {
                                navController.popBackStack()
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
private fun EditProfileScreenPrev() {
    BorrowlyTheme {
        EditProfileContent(
            uiState = EditProfileUiState(
                username = "Bijana Putra",
                email = "bijana@example.com",
                phoneNumber = "087861839394",
                address = "Jln. Angjay mabar No 66 Blok N2",
                password = "Qwerty",
            ),
            onUsernameChange = { TODO() },
            onEmailChange = { TODO() },
            onPasswordChange = { TODO() },
            onNewPasswordChange = { TODO() },
            onSaveClick = { TODO() },
            navController = rememberNavController(),
            onPhoneNumberChange = { TODO() },
            onAddressChange = { TODO() },
            refreshUser = { TODO() }
        )
    }
}