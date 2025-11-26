package com.example.xisdoandre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.xisdoandre.data.model.Product
import com.example.xisdoandre.ui.cart.CartScreen
import com.example.xisdoandre.ui.common.OrderSuccessScreen
import com.example.xisdoandre.ui.history.OrderHistoryScreen
import com.example.xisdoandre.ui.home.HomeScreen
import com.example.xisdoandre.ui.theme.XisDoAndreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XisDoAndreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var cartItems by remember { mutableStateOf(listOf<Product>()) }

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                cartItemCount = cartItems.size,
                onNavigateToCart = { navController.navigate(Routes.CART) },
                onNavigateToOrderHistory = { navController.navigate(Routes.ORDER_HISTORY) },
                onAddToCart = { product -> cartItems = cartItems + product }
            )
        }
        composable(Routes.CART) {
            CartScreen(
                cartItems = cartItems,
                navController = navController,
                onOrderPlaced = {
                    cartItems = emptyList() // Limpa o carrinho
                    navController.navigate(Routes.ORDER_SUCCESS) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }
        composable(Routes.ORDER_SUCCESS) {
            OrderSuccessScreen(
                onBackToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(navController = navController)
        }
    }
}

object Routes {
    const val HOME = "home"
    const val CART = "cart"
    const val ORDER_SUCCESS = "order_success"
    const val ORDER_HISTORY = "order_history"
}