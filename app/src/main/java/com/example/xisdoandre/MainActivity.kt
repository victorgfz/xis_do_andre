package com.example.xisdoandre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.xisdoandre.ui.theme.Typography
import java.net.URLDecoder
import java.net.URLEncoder

// ============ CONSTANTES DE ROTAS ============
object Routes {
    const val LOGIN = "login"
    const val HOME = "home/{userEmail}"

    fun home(userEmail: String): String {
        val encodedEmail = URLEncoder.encode(userEmail, "UTF-8")
        return "home/$encodedEmail"
    }
}

// ============ TEMA PERSONALIZADO ============
private val XisDoAndreColorScheme = lightColorScheme(
    primary = Color(0xFFF6202A),
    onPrimary = Color.White,
    secondary = Color(0xFFC97C75),
    onSecondary = Color.White,
    tertiary = Color(0xFFEABD66),
    onTertiary = Color.Black,
    background = Color(0xFF998E8D),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun XisDoAndreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = XisDoAndreColorScheme,
        typography = Typography,
        content = content
    )
}

// ============ ACTIVITY PRINCIPAL ============
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

// ============ NAVEGAÇÃO ============
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { email ->
                    navController.navigate(Routes.home(email)) {
                        // Remove a tela de login da pilha
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.HOME,
            arguments = listOf(
                navArgument("userEmail") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            val userEmail = URLDecoder.decode(encodedEmail, "UTF-8")

            HomeScreen(
                userEmail = userEmail,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

// ============ TELA DE LOGIN ============
@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo/Título
        Text(
            text = "🍔",
            style = MaterialTheme.typography.displayLarge,
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Xis do André",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Os melhores hambúrgueres da região!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showError = false
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            isError = showError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Senha
        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
                showError = false
            },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            isError = showError
        )

        if (showError) {
            Text(
                text = "Por favor, preencha todos os campos",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botão Entrar
        Button(
            onClick = {
                if (email.isNotBlank() && senha.isNotBlank()) {
                    onLoginSuccess(email)
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Entrar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* Criar conta */ }) {
            Text(
                text = "Não tem conta? Cadastre-se",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ============ DATA CLASS PARA PRODUTOS ============
data class Produto(
    val id: Int,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val emoji: String
)

// ============ TELA INICIAL (HOME) ============
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userEmail: String,
    onLogout: () -> Unit
) {
    var cartCount by remember { mutableStateOf(0) }

    // Lista de produtos de exemplo
    val produtos = remember {
        listOf(
            Produto(1, "X-Bacon", "Hambúrguer com bacon crocante, queijo e molho especial", 25.90, "🥓"),
            Produto(2, "X-Salada", "Hambúrguer com alface, tomate, queijo e maionese", 22.90, "🥬"),
            Produto(3, "X-Egg", "Hambúrguer com ovo, queijo e presunto", 23.90, "🥚"),
            Produto(4, "X-Tudo", "Hambúrguer completo com todos os ingredientes", 32.90, "🍔"),
            Produto(5, "X-Calabresa", "Hambúrguer com calabresa, queijo e cebola", 26.90, "🌭"),
            Produto(6, "X-Frango", "Hambúrguer de frango grelhado com queijo", 24.90, "🐔")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Xis do André",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Olá, ${userEmail.substringBefore("@")}!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Ver carrinho */ }) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge {
                                        Text(cartCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrinho"
                            )
                        }
                    }
                    TextButton(onClick = onLogout) {
                        Text("Sair", color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banner de boas-vindas
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🍔",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "Bem-vindo ao Xis do André!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Escolha seu xis favorito",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Título da seção
            item {
                Text(
                    text = "Nosso Cardápio",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Lista de produtos
            items(produtos) { produto ->
                ProdutoCard(
                    produto = produto,
                    onAddToCart = { cartCount++ }
                )
            }

            // Espaço final
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ============ CARD DE PRODUTO ============
@Composable
fun ProdutoCard(
    produto: Produto,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Informações do produto
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Emoji do produto
                Text(
                    text = produto.emoji,
                    fontSize = 48.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                // Nome, descrição e preço
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = produto.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = produto.descricao,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "R$ %.2f".format(produto.preco),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Botão adicionar
            FilledIconButton(
                onClick = onAddToCart,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar ao carrinho",
                    tint = Color.White
                )
            }
        }
    }
}