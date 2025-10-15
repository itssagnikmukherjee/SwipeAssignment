package com.itssagnikmukherjee.swipeassignment.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.itssagnikmukherjee.swipeassignment.R
import com.itssagnikmukherjee.swipeassignment.ui.theme.swipeTypography
import com.itssagnikmukherjee.swipeassignment.utils.UiConstants

@Composable
fun ProductImageComponent(
    modifier: Modifier = Modifier,
    imageUri: Uri = Uri.EMPTY,
    onChooseFromPhotos: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(
            modifier = Modifier
                .size(140.dp).border(1.dp, Color.Gray ,RoundedCornerShape(UiConstants.DEFAULT_RADIUS))
            ,contentAlignment = Alignment.Center
        ) {

            if (imageUri != Uri.EMPTY) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(UiConstants.DEFAULT_RADIUS)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUri)
                            .scale(scale = Scale.FILL)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
            }else{
                Icon(
                    painter = painterResource(R.drawable.outline_image_24),
                    contentDescription = "Add Photo", tint = Color.Gray, modifier = Modifier.size(32.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val chooseUploadTxt =  if(imageUri != Uri.EMPTY) "Change Photo" else "Upload Photo"
            val chooseUploadIcon = if(imageUri != Uri.EMPTY) Icons.Default.Repeat else Icons.Default.Upload
            TextButton(
                onClick = { onChooseFromPhotos() },
                modifier = Modifier,
                shape = RoundedCornerShape(UiConstants.DEFAULT_RADIUS),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector =chooseUploadIcon,
                    contentDescription = "Choose from Photos",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.width(8.dp))
                Text(chooseUploadTxt, color = Color.White, style = swipeTypography.bodySmall)
            }
        }
    }
}