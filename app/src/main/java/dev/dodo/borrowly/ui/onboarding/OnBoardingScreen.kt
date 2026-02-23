package dev.dodo.borrowly.ui.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.dodo.borrowly.R
import dev.dodo.borrowly.common.helper.setHasOnboarded
import dev.dodo.borrowly.ui.AppDestination
import dev.dodo.borrowly.ui.common.shape.WaveShape
import dev.dodo.borrowly.ui.theme.AppColors
import dev.dodo.borrowly.ui.theme.BorrowlyTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnBoardingScreen(
    navController: NavHostController,
    innerPadding: PaddingValues = PaddingValues()
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.60f)
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
                        text = "Selamat Datang",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W700,
                        color = AppColors.text
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Kelola dan pantau barang yang \n dipinjam dengan mudah.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600,
                        color = AppColors.secondary
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 120.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable{
                                    scope.launch {
                                        setHasOnboarded(context)
                                    }
                                    navController.navigate(AppDestination.Login) {
                                        popUpTo(AppDestination.OnBoarding) { inclusive = true }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Lanjutkan",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W700,
                                color = AppColors.primary
                            )
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                painterResource(R.drawable.ic_next_onboarding),
                                contentDescription = null,
                                tint = AppColors.primary
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
private fun OnBoardingScreenPrev(
    
) {
    BorrowlyTheme {
        OnBoardingScreen(
            navController = rememberNavController(),
        )
    }
}