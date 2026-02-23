package dev.dodo.borrowly.ui.auth.register

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.dodo.borrowly.R
import dev.dodo.borrowly.ui.AppDestination
import dev.dodo.borrowly.ui.common.component.CustomButton
import dev.dodo.borrowly.ui.common.component.CustomTextField
import dev.dodo.borrowly.ui.common.shape.WaveShape
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val viewModel: RegisterViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    RegisterContent(
        uiState = uiState,
        onEmailChange = viewModel::changeEmail,
        onUsernameChange = viewModel::changeUsername,
        onPasswordChange = viewModel::changePassword,
        onConfirmPasswordChange = viewModel::changeConfirmPassword,
        onRegister = viewModel::register,
        navController = navController
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegister: (() -> Unit) -> Unit,
    navController: NavController
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .clip(WaveShape())
                ) {
                    Image(
                        painter = painterResource(R.drawable.wave_pattern),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 50.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Buat Akun Baru",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W700,
                        color = AppColors.text
                    )
                    Box(
                        modifier = Modifier
                            .width(125.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(AppColors.primary),
                    )
                    Spacer(Modifier.height(26.dp))
                    Text(
                        text = "Email",
                        color = AppColors.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700
                    )
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(
                        value = uiState.email,
                        onValueChange = {
                            onEmailChange(it)
                        },
                        placeholder = "Masukkan Email",
                        errorMessage = uiState.emailErrorMessage
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Username",
                        color = AppColors.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700
                    )
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(
                        value = uiState.username,
                        onValueChange = {
                            onUsernameChange(it)
                        },
                        placeholder = "Masukkan Username",
                        errorMessage = uiState.usernameErrorMessage
                    )
                    Spacer(Modifier.height(24.dp))
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
                        errorMessage = uiState.passwordErrorMessage
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Konfirmasi Password",
                        color = AppColors.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700
                    )
                    Spacer(Modifier.height(8.dp))
                    CustomTextField(
                        value = uiState.confirmPassword,
                        onValueChange = {
                            onConfirmPasswordChange(it)
                        },
                        placeholder = "Masukkan Konfirmasi Password",
                        isPassword = true,
                        errorMessage = uiState.confirmPasswordErrorMessage
                    )
                    Spacer(Modifier.height(24.dp))
                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Buat Akun",
                        onClick = {
                            onRegister {
                                navController.popBackStack()
                            }
                        }
                    )
                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Sudah punya akun? ",
                            color = AppColors.text,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600
                        )
                        Text(
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(AppDestination.Login)
                                },
                            text = "Masuk",
                            color = AppColors.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPrev() {
    BorrowlyTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onEmailChange = { TODO() },
            onUsernameChange = { TODO() },
            onPasswordChange = { TODO() },
            onConfirmPasswordChange = { TODO() },
            onRegister = { TODO() },
            navController = rememberNavController()
        )
    }
}

