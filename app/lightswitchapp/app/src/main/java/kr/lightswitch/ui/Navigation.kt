package kr.lightswitch.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.lightswitch.ui.flag.FlagScreen
import kr.lightswitch.ui.flag.FlagViewModel
import kr.lightswitch.ui.theme.pretendard
import timber.log.Timber
import kr.lightswitch.ui.login.LoginScreen
import kr.lightswitch.ui.login.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val (navTitleState, setNavTitleState) = remember {
        mutableStateOf("LightSwitch")
    }
    val (navBtnState, setNavBtnState) = remember {
        mutableStateOf(false)
    }
    navController.addOnDestinationChangedListener { // 라우팅 발생 시 마다 호출되도록
        _, destination, _ ->
        when (destination.route) {
            NavScreen.Home.route -> {
                Timber.d("home")
                setNavBtnState(false)
                setNavTitleState("LightSwitch")
            }
            NavScreen.Login.route -> {
                Timber.d("login")
                setNavBtnState(true)
                setNavTitleState("로그인")
            }
            NavScreen.Flags.route -> {
                Timber.d("flags")
                setNavBtnState(true)
                setNavTitleState("플래그 관리")
            }
//    val mainViewModel: MainViewModel = viewModel()
    NavHost(navController = navController, startDestination = NavScreen.Home.route) {
        composable(
            route = NavScreen.Home.route
        ) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(mainViewModel = mainViewModel, onBtnClick = {navController.navigate(NavScreen.Flags.route)}, onLoginBtnClick = {navController.navigate(NavScreen.Login.route)})
        }
        composable(
            route = NavScreen.Flags.route,
        ) { backStackEntry ->
            FlagScreen()
        }
        composable(
            route = NavScreen.Login.route,
        ) { backStackEntry ->
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(loginViewModel = loginViewModel, navController = navController)
        }
    }

    val navigationIcon: (@Composable () -> Unit) =
        if (navBtnState) {
            {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "arrowBack")
                }
            }
        } else {
            {} // empty view
        }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(text = navTitleState, style = MaterialTheme.typography.titleLarge) }, navigationIcon = navigationIcon)
    }) {
        Column(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = NavScreen.Home.route) {
                composable(
                    route = NavScreen.Home.route
                ) {
                    val mainViewModel: MainViewModel = hiltViewModel()
                    MainScreen(mainViewModel = mainViewModel, onBtnClick = {navController.navigate(NavScreen.Flags.route)})
                }
                composable(
                    route = NavScreen.Flags.route,
                ) { backStackEntry ->
                    val flagViewModel: FlagViewModel = hiltViewModel()
                    FlagScreen(flagViewModel = flagViewModel)
                }
                composable(
                    route = NavScreen.Login.route,
                ) { backStackEntry ->

                }
            }
        }
    }

}



sealed class NavScreen(val route: String) {

    object Home : NavScreen("Home")

    object Login : NavScreen("Login")

    object Flags : NavScreen("Flags")
}