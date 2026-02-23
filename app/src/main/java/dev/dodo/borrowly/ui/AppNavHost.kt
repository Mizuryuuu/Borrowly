package dev.dodo.borrowly.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import dev.dodo.borrowly.common.helper.PrefKey
import dev.dodo.borrowly.common.helper.dataStore
import dev.dodo.borrowly.ui.addeditproduct.AddProductScreen
import dev.dodo.borrowly.ui.auth.login.LoginScreen
import dev.dodo.borrowly.ui.auth.register.RegisterScreen
import dev.dodo.borrowly.ui.detailproduct.DetailProductScreen
import dev.dodo.borrowly.ui.detailstatus.DetailStatusScreen
import dev.dodo.borrowly.ui.editprofile.EditProfileScreen
import dev.dodo.borrowly.ui.home.HomeScreen
import dev.dodo.borrowly.ui.loanrequest.LoanRequestScreen
import dev.dodo.borrowly.ui.main.MainScreen
import dev.dodo.borrowly.ui.main.MainViewModel
import dev.dodo.borrowly.ui.main.NavItem
import dev.dodo.borrowly.ui.myproduct.MyProductScreen
import dev.dodo.borrowly.ui.onboarding.OnBoardingScreen
import dev.dodo.borrowly.ui.profile.ProfileScreen
import dev.dodo.borrowly.ui.status.StatusScreen
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    val hasOnboarded by context.dataStore.data
        .map { prefs ->
            prefs[PrefKey.HAS_ONBOARDED] ?: false
        }
        .collectAsState(initial = false)

    val startDestination = when {
        currentUser != null -> AppDestination.Main
        !hasOnboarded -> AppDestination.OnBoarding
        else -> AppDestination.Login
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<AppDestination.OnBoarding> {
            OnBoardingScreen(
                navController = navController,
            )
        }
        composable<AppDestination.Login> {
            LoginScreen(
                navController = navController
            )
        }
        composable<AppDestination.Register> {
            RegisterScreen(
                navController = navController
            )
        }
        composable<AppDestination.Main> {
            val viewModel: MainViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()
            MainScreen(
                homeScreen = {
                    HomeScreen(
                        navController = navController
                    )
                },
                statusScreen = {
                    StatusScreen(
                        initialFilter = uiState.statusFilter,
                        navController = navController
                    )
                },
                profileScreen = {
                    ProfileScreen(
                        mainViewModel = viewModel,
                        navController = navController

                    )
                },
                navController = navController
            )
        }
        composable<AppDestination.DetailProduct> { backStackEntry ->
            val args = backStackEntry.toRoute<AppDestination.DetailProduct>()

            DetailProductScreen(
                idProduct = args.idProduct,
                navController = navController
            )
        }
        composable<AppDestination.DetailLoanRequest> { backStackEntry ->
            val args = backStackEntry.toRoute<AppDestination.DetailLoanRequest>()
            DetailStatusScreen(
                idLoan = args.idLoan,
                navController = navController
            )
        }
        composable<AppDestination.LoanRequest> {
            LoanRequestScreen(
                navController = navController
            )
        }
        composable<AppDestination.MyProduct> {
            MyProductScreen(
                navController = navController
            )
        }
        composable<AppDestination.AddEditProduct> { backStackEntry ->
            val args = backStackEntry.toRoute<AppDestination.AddEditProduct>()
            AddProductScreen(
                idProduct = args.idProduct,
                navController = navController
            )
        }
        composable<AppDestination.EditProfile> {
            EditProfileScreen(
                navController = navController
            )
        }
    }
    
}