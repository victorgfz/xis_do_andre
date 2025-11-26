package com.example.xisdoandre.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.xisdoandre.data.model.DeliveryAddress
import com.example.xisdoandre.data.model.Product
import com.example.xisdoandre.ui.util.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<Product>,
    navController: NavController,
    onOrderPlaced: () -> Unit,
    cartViewModel: CartViewModel = viewModel()
) {
    var bairro by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var complemento by remember { mutableStateOf("") }

    val paymentOptions = listOf("Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX")
    var selectedPayment by remember { mutableStateOf(paymentOptions[0]) }

    val subtotal = cartItems.sumOf { it.preco }
    val deliveryFee = 5.00
    val total = subtotal + deliveryFee

    val placeOrderState by cartViewModel.placeOrderState.collectAsState()

    LaunchedEffect(placeOrderState) {
        when (val state = placeOrderState) {
            is PlaceOrderUiState.Success -> {
                cartViewModel.resetState()
                onOrderPlaced()
            }
            is PlaceOrderUiState.Error -> {
                // TODO: Mostrar um Snackbar ou Toast com o erro state.message
            }
            else -> Unit // Idle or Loading
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrinho de Compras", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val address = DeliveryAddress(bairro, endereco, numero, complemento)
                    cartViewModel.placeOrder(cartItems, address, selectedPayment)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = cartItems.isNotEmpty() && placeOrderState !is PlaceOrderUiState.Loading
            ) {
                if (placeOrderState is PlaceOrderUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp)
                } else {
                    Text(
                        text = "Finalizar Pedido - ${formatCurrency(total)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SectionCard(title = "Seu Pedido", cartItems) }
            item { DeliveryAddressSection(bairro, {bairro=it}, endereco, {endereco=it}, numero, {numero=it}, complemento, {complemento=it}) }
            item { PaymentMethodSection(paymentOptions, selectedPayment) { selectedPayment = it } }
            item { OrderSummarySection(subtotal, deliveryFee, total) }
        }
    }
}

@Composable
private fun SectionCard(title: String, cartItems: List<Product>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            if (cartItems.isEmpty()) {
                Text("Seu carrinho está vazio.", modifier = Modifier.padding(16.dp))
            } else {
                cartItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.emoji, fontSize = 24.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(item.nome, fontWeight = FontWeight.Bold)
                            Text(formatCurrency(item.preco), color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeliveryAddressSection(
    bairro: String, onBairroChange: (String) -> Unit,
    endereco: String, onEnderecoChange: (String) -> Unit,
    numero: String, onNumeroChange: (String) -> Unit,
    complemento: String, onComplementoChange: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Text("Endereço de Entrega", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = bairro, onValueChange = onBairroChange, label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = endereco, onValueChange = onEnderecoChange, label = { Text("Endereço") }, modifier = Modifier.weight(2f))
                OutlinedTextField(value = numero, onValueChange = onNumeroChange, label = { Text("Nº") }, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = complemento, onValueChange = onComplementoChange, label = { Text("Complemento (Opcional)") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun PaymentMethodSection(paymentOptions: List<String>, selectedPayment: String, onPaymentSelected: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Text("Forma de Pagamento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            paymentOptions.forEach { option ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(selected = (option == selectedPayment), onClick = { onPaymentSelected(option) })
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (option == selectedPayment), onClick = { onPaymentSelected(option) })
                    Text(text = option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun OrderSummarySection(subtotal: Double, deliveryFee: Double, total: Double) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Text("Resumo do Pedido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            SummaryRow("Subtotal", formatCurrency(subtotal))
            SummaryRow("Taxa de entrega", formatCurrency(deliveryFee))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow("Total", formatCurrency(total), isTotal = true)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal)
    }
}
