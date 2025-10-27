package com.itssagnikmukherjee.swipeassignment.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.itssagnikmukherjee.swipeassignment.R
import com.itssagnikmukherjee.swipeassignment.domain.models.ProductResponse
import com.itssagnikmukherjee.swipeassignment.ui.theme.swipeTypography
import com.itssagnikmukherjee.swipeassignment.utils.UiConstants
import okhttp3.internal.format

@Composable
fun ProductListItem(
    modifier: Modifier = Modifier,
    product: ProductResponse,
    isPending: Boolean = false,
    isSelected: Boolean = false,
    qtyCount: Int = 0,
    onIncrement: (ProductResponse) -> Unit = {},
    onDecrement: (ProductResponse) -> Unit = {},
    onRemove: (ProductResponse) -> Unit = {}
) {
    Row {
        if(isSelected) {
            Box(modifier = Modifier.width(6.dp).height(102.dp).background(Color.Black))
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onIncrement(product)
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            val painter = rememberAsyncImagePainter(model = product.image)
            if ((product.image?: "").isEmpty() || painter.state is AsyncImagePainter.State.Error) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_image_24),
                        contentDescription = "Package Icon",
                        tint = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            else{
                AsyncImage(
                    model = product.image.toString(),
                    contentDescription = product.product_name,
                    modifier = Modifier.size(75.dp).clip(RoundedCornerShape(UiConstants.DEFAULT_RADIUS)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.outline_image_24),
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Product Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Product Name
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ){
                    Text(
                        text = product.product_name,
                        style = swipeTypography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if(isPending) {
                        Box(modifier = Modifier.padding(horizontal = 6.dp).background(Color.Black, shape = RoundedCornerShape(4.dp))) {
                            Text("Pending", style = swipeTypography.bodySmall, color = Color.White,modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }

                // Product Type and Tax
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(modifier = Modifier.padding(top = 4.dp).background(
                        color = Color.Black.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(6.dp)
                    ).padding(horizontal = 10.dp)) {
                        Text(
                            text = product.product_type,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "Tax: ${
                            if (product.tax % 1 == 0.0) {
                                product.tax.toInt()
                            } else {
                                product.tax
                            }
                        }%",
                        style = swipeTypography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }

                // Price
                Text(
                    text = "₹${format("%.2f", product.price)}",
                    style = swipeTypography.bodyMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Column {
                if (qtyCount > 0) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ){
                    IconButton(onClick = {onRemove(product)}, modifier = Modifier.size(22.dp)) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                    }
                        Spacer(Modifier.height(20.dp))
                    ProductCountButton(
                        count = qtyCount,
                        onIncrement = { onIncrement(product) },
                        onDecrement = { onDecrement(product) }
                    )
                    }
                }else{
                    IconButton(onClick = { onIncrement(product) }, modifier = Modifier.size(30.dp),colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White, containerColor = Color.Black)) { Text("+", fontSize = 22.sp, fontWeight = FontWeight.Light) }
                }
            }
        }
    }
}

@Composable
fun ProductCountButton(
    modifier: Modifier = Modifier,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = modifier.height(36.dp).background(color = Color.LightGray.copy(alpha = 0.5f), shape = CircleShape).padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onDecrement, modifier = Modifier.size(30.dp),colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White, containerColor = Color.Black.copy(alpha = 0.6f))) { Text("-", fontSize = 22.sp, fontWeight = FontWeight.Light) }

        Text(
            text = "$count",
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        IconButton(onClick = onIncrement, modifier = Modifier.size(30.dp),colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White, containerColor = Color.Black)) { Text("+", fontSize = 22.sp, fontWeight = FontWeight.Light) }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListItemPreview(){
    ProductListItem(
        product = ProductResponse("1",1.22,"1","1",20.0),
        isSelected = true,
        qtyCount = 12,
        onIncrement = {},
        onDecrement = {},
        onRemove = {}
    )
}