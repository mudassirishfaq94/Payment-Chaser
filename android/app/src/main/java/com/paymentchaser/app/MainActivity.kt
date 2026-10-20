package com.paymentchaser.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.paymentchaser.app.ui.analytics.AnalyticsScreen
import com.paymentchaser.app.ui.analytics.AnalyticsViewModel
import com.paymentchaser.app.ui.auth.AuthScreen
import com.paymentchaser.app.ui.auth.AuthViewModel
import com.paymentchaser.app.ui.clients.ClientsScreen
import com.paymentchaser.app.ui.clients.ClientsViewModel
import com.paymentchaser.app.ui.dashboard.DashboardScreen
import com.paymentchaser.app.ui.dashboard.DashboardViewModel
import com.paymentchaser.app.ui.onboarding.OnboardingScreen
import com.paymentchaser.app.ui.payment.AddPaymentScreen
import com.paymentchaser.app.ui.payment.PaymentDetailsScreen
import com.paymentchaser.app.ui.payment.PaymentDetailsViewModel
import com.paymentchaser.app.ui.payment.PaymentsListScreen
import com.paymentchaser.app.ui.payment.PaymentsListViewModel
import com.paymentchaser.app.ui.premium.PremiumScreen
import com.paymentchaser.app.ui.reminders.SmartRemindersScreen
import com.paymentchaser.app.ui.reminders.SmartRemindersViewModel
import com.paymentchaser.app.ui.settings.SettingsScreen
import com.paymentchaser.app.ui.settings.SettingsViewModel
import com.paymentchaser.app.ui.theme.PaymentChaserTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class AppDestination(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Dashboard : AppDestination("dashboard", "Home", Icons.Default.Home)
    object Payments : AppDestination("payments", "Payments", Icons.Default.ReceiptLong)
    object Clients : AppDestination("clients", "Clients", Icons.Default.Group)
    object Settings : AppDestination("settings", "Settings", Icons.Default.Tune)
    object AddPayment : AppDestination("add_payment", "Add", Icons.Default.Add)
    object PaymentDetails : AppDestination("payment_details/{paymentId}", "Details", Icons.Default.Info)
    object Reminders : AppDestination("reminders", "Reminders", Icons.Default.Schedule)
    object Analytics : AppDestination("analytics", "Analytics", Icons.Default.Analytics)
    object Premium : AppDestination("premium", "Premium", Icons.Default.Bolt)
    object Onboarding : AppDestination("onboarding", "Onboarding", Icons.Default.Start)
    object Auth : AppDestination("auth", "Auth", Icons.Default.Lock)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaymentChaserTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val isBottomNavVisible = currentRoute in listOf(
                    AppDestination.Dashboard.route,
                    AppDestination.Payments.route,
                    AppDestination.Clients.route,
                    AppDestination.Settings.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (isBottomNavVisible) {
                            NavigationBar {
                                val bottomItems = listOf(
                                    AppDestination.Dashboard,
                                    AppDestination.Payments,
                                    AppDestination.Clients,
                                    AppDestination.Settings
                                )
                                bottomItems.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                                        label = { Text(screen.title) },
                                        selected = currentRoute == screen.route,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppDestination.Dashboard.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Dashboard
                        composable(AppDestination.Dashboard.route) {
                            val viewModel: DashboardViewModel = hiltViewModel()
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToAddPayment = { navController.navigate(AppDestination.AddPayment.route) },
                                onNavigateToPaymentDetails = { id -> navController.navigate("payment_details/$id") },
                                onNavigateToPaymentsList = { navController.navigate(AppDestination.Payments.route) },
                                onNavigateToPremium = { navController.navigate(AppDestination.Premium.route) }
                            )
                        }

                        // 2. Add Payment
                        composable(AppDestination.AddPayment.route) {
                            AddPaymentScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onSavePayment = { amountCents, clientName, dueDateMillis, invoiceNo ->
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 3. Payments List
                        composable(AppDestination.Payments.route) {
                            val viewModel: PaymentsListViewModel = hiltViewModel()
                            PaymentsListScreen(
                                viewModel = viewModel,
                                onNavigateToDetails = { id -> navController.navigate("payment_details/$id") }
                            )
                        }

                        // 4. Payment Details
                        composable(
                            route = AppDestination.PaymentDetails.route,
                            arguments = listOf(navArgument("paymentId") { type = NavType.StringType })
                        ) {
                            val viewModel: PaymentDetailsViewModel = hiltViewModel()
                            PaymentDetailsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 5. Clients
                        composable(AppDestination.Clients.route) {
                            val viewModel: ClientsViewModel = hiltViewModel()
                            ClientsScreen(viewModel = viewModel)
                        }

                        // 6. Smart Reminders
                        composable(AppDestination.Reminders.route) {
                            val viewModel: SmartRemindersViewModel = hiltViewModel()
                            SmartRemindersScreen(viewModel = viewModel)
                        }

                        // 7. Analytics
                        composable(AppDestination.Analytics.route) {
                            val viewModel: AnalyticsViewModel = hiltViewModel()
                            AnalyticsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 8. Settings
                        composable(AppDestination.Settings.route) {
                            val viewModel: SettingsViewModel = hiltViewModel()
                            SettingsScreen(
                                viewModel = viewModel,
                                onNavigateToReminders = { navController.navigate(AppDestination.Reminders.route) },
                                onNavigateToAnalytics = { navController.navigate(AppDestination.Analytics.route) },
                                onNavigateToPremium = { navController.navigate(AppDestination.Premium.route) },
                                onSignedOut = { navController.navigate(AppDestination.Auth.route) }
                            )
                        }

                        // 9. Premium
                        composable(AppDestination.Premium.route) {
                            PremiumScreen(onNavigateBack = { navController.popBackStack() })
                        }

                        // 10. Onboarding
                        composable(AppDestination.Onboarding.route) {
                            OnboardingScreen(
                                onFinishOnboarding = { _, _ ->
                                    navController.navigate(AppDestination.Dashboard.route) {
                                        popUpTo(AppDestination.Onboarding.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 11. Auth
                        composable(AppDestination.Auth.route) {
                            val viewModel: AuthViewModel = hiltViewModel()
                            AuthScreen(
                                viewModel = viewModel,
                                onAuthSuccess = {
                                    navController.navigate(AppDestination.Dashboard.route) {
                                        popUpTo(AppDestination.Auth.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
