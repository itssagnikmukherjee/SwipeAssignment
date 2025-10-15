package com.itssagnikmukherjee.swipeassignment.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itssagnikmukherjee.swipeassignment.ui.theme.swipeTypography

@Composable
fun SwipeCategoryList(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategory: String = "",
    onCategorySelected: (String) -> Unit = {}
) {
    val filteredCategories = categories.filterNot { it.equals("All", ignoreCase = true) }
    var showDialog by remember { mutableStateOf(false) }

    SwipeTextField(
        modifier = modifier.fillMaxWidth(),
        value = TextFieldValue(selectedCategory),
        onValueChange = {},
        label = "Category",
        placeholder = "Select Category",
        isCategory = true,
        onClick = { showDialog = true },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.None
        ),
        isTrailingIconVisible = true,
        trailingIcon = Icons.Default.KeyboardArrowDown,
        isLeadingIconVisible = true,
        leadingIcon = Icons.Default.Category
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Choose Category", style = swipeTypography.headlineLarge) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    filteredCategories.forEach { category ->
                        TextButton(
                            onClick = {
                                onCategorySelected(category)
                                showDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = category,
                                style = swipeTypography.bodyMedium,
                                color = if (selectedCategory == category) Color.Black else Color.Gray
                            )
                        }
                    }
                }
            },
            confirmButton = {},
        )
    }
}