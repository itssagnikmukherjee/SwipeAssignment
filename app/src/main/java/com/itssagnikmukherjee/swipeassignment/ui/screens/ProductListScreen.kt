package com.itssagnikmukherjee.swipeassignment.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.itssagnikmukherjee.swipeassignment.domain.models.ProductResponse
import com.itssagnikmukherjee.swipeassignment.ui.components.ProductListItem
import com.itssagnikmukherjee.swipeassignment.ui.navigation.AddProductScreen
import com.itssagnikmukherjee.swipeassignment.ui.theme.swipeTypography
import com.itssagnikmukherjee.swipeassignment.ui.viewmodel.ProductListViewModel
import com.itssagnikmukherjee.swipeassignment.utils.UiConstants
import org.koin.androidx.compose.koinViewModel
@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel = koinViewModel(),
    modifier: Modifier
) {
    val state by viewModel.state.collectAsState()
    val pendingCount by viewModel.pendingProductsCount.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedProducts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    val categories = state.categories
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize().padding(horizontal = UiConstants.DEFAULT_HORIZONTAL_PADDING)) {
        // Header with sync status
        Column(modifier = Modifier.padding(16.dp)){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Swipe Store",
                        style = swipeTypography.headlineLarge
                    )
                    Text(
                        text = "Sagnik Mukherjee",
                        style = swipeTypography.bodySmall
                    )
                }
                Row(
                    modifier = Modifier
                        .background(Color.Transparent, shape = RoundedCornerShape(UiConstants.DEFAULT_RADIUS)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    // Add Product Button
                    Button(
                        onClick = { navController.navigate(AddProductScreen) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(UiConstants.DEFAULT_RADIUS)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        if (pendingCount <= 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Product", style = swipeTypography.bodySmall, color = Color.White)
                        }
                    }

                    // Pending products indicator
                    if (pendingCount > 0) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Row(
                            modifier = Modifier
                                .clickable { viewModel.syncPendingProducts() }
                                .background(
                                    color = if (state.isOnline) Color.Black.copy(alpha = 0.8f) else Color.Black,
                                    shape = RoundedCornerShape(UiConstants.DEFAULT_RADIUS)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (state.isSyncing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$pendingCount pending",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 6.dp),
                                style = swipeTypography.bodySmall
                            )
                        }
                    }
                }
            }

            // Offline indicator
            if (!state.isOnline) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFA726), shape = RoundedCornerShape(UiConstants.DEFAULT_RADIUS))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Offline - Showing cached data",
                        style = swipeTypography.bodySmall,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // Sync message
            state.syncMessage?.let { message ->
                LaunchedEffect(message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Search products...", style = swipeTypography.bodyMedium, color = Color.Black.copy(alpha = 0.6f)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category, style = swipeTypography.bodyMedium) },
                    leadingIcon = if (selectedCategory == category) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Black.copy(alpha = 0.8f),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product List
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val filteredProducts = state.products.filter { product ->
                val matchesSearch = product.product_name.contains(searchQuery, ignoreCase = true) ||
                        product.product_type.contains(searchQuery, ignoreCase = true)
                val matchesCategory = selectedCategory == "All" || product.product_type == selectedCategory
                matchesSearch && matchesCategory
            }

            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No products found",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn {
                    items(filteredProducts) { product ->
                        val count = selectedProducts[product.product_name] ?: 0
                        val isSelected = count > 0
                        val isPending = state.pendingProducts.any { it.productName == product.product_name }
                        ProductListItem(
                            product = product,
                            isSelected = isSelected,
                            qtyCount = count,
                            isPending = isPending,
                            onIncrement = { prod ->
                                selectedProducts = selectedProducts.toMutableMap().apply {
                                    put(prod.product_name, (get(prod.product_name) ?: 0) + 1)
                                }
                            },
                            onDecrement = { prod ->
                                selectedProducts = selectedProducts.toMutableMap().apply {
                                    val currentCount = get(prod.product_name) ?: 0
                                    if (currentCount > 1) {
                                        put(prod.product_name, currentCount - 1)
                                    } else {
                                        remove(prod.product_name)
                                    }
                                }
                            },
                            onRemove = {
                                selectedProducts = selectedProducts.toMutableMap().apply {
                                    remove(it.product_name)
                                }
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)
                    }
                }
            }
        }
    }
}