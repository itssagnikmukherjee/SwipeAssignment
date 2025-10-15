package com.itssagnikmukherjee.swipeassignment.ui.screens

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.itssagnikmukherjee.swipeassignment.ui.components.ProductImageComponent
import com.itssagnikmukherjee.swipeassignment.ui.components.SwipeTextField
import com.itssagnikmukherjee.swipeassignment.ui.navigation.ProductListScreen
import com.itssagnikmukherjee.swipeassignment.ui.viewmodel.ProductListViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.itssagnikmukherjee.swipeassignment.ui.components.SwipeCategoryList
import com.itssagnikmukherjee.swipeassignment.ui.navigation.AddProductScreen
import com.itssagnikmukherjee.swipeassignment.ui.theme.swipeTypography
import com.itssagnikmukherjee.swipeassignment.utils.UiConstants

@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductListViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val categories = state.categories
    val isLoading = state.isLoading
    val isOnline = state.isOnline

    var productName by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf(TextFieldValue("")) }
    var taxRate by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
        ){
            // Header
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { navController.navigate(ProductListScreen) },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
                Text("Add Product", modifier = Modifier.align(Alignment.Center), style = swipeTypography.headlineLarge)
            }

            // Offline indicator
            if (!isOnline) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFA726), shape = RoundedCornerShape(UiConstants.DEFAULT_RADIUS))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Offline Mode - Product will be synced later",
                        color = Color.White,
                        style = swipeTypography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Product image component
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
            ) { uri: Uri? ->
                if (uri != null) {
                    selectedImageUri = uri
                }
            }
            ProductImageComponent(
                imageUri = selectedImageUri ?: Uri.EMPTY,
                onChooseFromPhotos = {
                    imagePickerLauncher.launch("image/*")
                }
            )
            Spacer(Modifier.height(10.dp))
            SwipeTextField(
                isLeadingIconVisible = true,
                leadingIcon = Icons.Default.Inventory,
                value = productName,
                onValueChange = { productName = it },
                label = "Product Name",
                placeholder = "Enter Product Name",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(Modifier.height(10.dp))
            SwipeCategoryList(
                modifier = Modifier.fillMaxWidth(),
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                }
            )
            Spacer(Modifier.height(10.dp))
            SwipeTextField(
                value = sellingPrice,
                onValueChange = {
                    if (it.text.matches(Regex("^\\d*(\\.\\d{0,2})?\$"))) {
                        sellingPrice = it
                    }
                },
                label = "Selling Price",
                placeholder = "Enter Selling Price",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isLeadingIconVisible = true,
                leadingIcon = Icons.Default.CurrencyRupee,
            )
            Spacer(Modifier.height(10.dp))
            SwipeTextField(

                value = taxRate,
                onValueChange = {
                    val newValue = it.text
                    if (newValue.matches(Regex("^\\d*(\\.\\d{0,2})?\$"))) {
                        val numericValue = newValue.toDoubleOrNull()
                        if (numericValue == null || numericValue <= 100.0) {
                            taxRate = it
                        }
                    }
                },
                label = "Tax Rate",
                placeholder = "Enter Tax Rate",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                isLeadingIconVisible = true,
                leadingIcon = Icons.Default.Percent,
            )
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                when {
                    productName.text.isBlank() -> {
                        Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT).show()
                    }
                    selectedCategory.isBlank() -> {
                        Toast.makeText(context, "Please select category", Toast.LENGTH_SHORT).show()
                    }
                    sellingPrice.text.isBlank() -> {
                        Toast.makeText(context, "Please enter selling price", Toast.LENGTH_SHORT).show()
                    }
                    taxRate.text.isBlank() -> {
                        Toast.makeText(context, "Please enter tax rate", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.addProduct(
                            productName = productName.text,
                            productType = selectedCategory,
                            price = sellingPrice.text,
                            tax = taxRate.text,
                            imageUri = selectedImageUri,
                            context = context,
                            onSuccess = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                navController.navigate(ProductListScreen)
                            },
                            onError = {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(UiConstants.DEFAULT_RADIUS),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Product", style = swipeTypography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}