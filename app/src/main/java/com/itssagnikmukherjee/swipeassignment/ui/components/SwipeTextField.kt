package com.itssagnikmukherjee.swipeassignment.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.itssagnikmukherjee.swipeassignment.ui.theme.swipeTypography

@Composable
fun SwipeTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String = "",
    placeholder: String = "",
    enabled: Boolean = true,
    isCategory: Boolean = false,
    optionalLabel: AnnotatedString? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    isTrailingIconVisible: Boolean = false,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isLeadingIconVisible: Boolean = false,
    leadingIcon: ImageVector? = null,
    onLeadingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = if (isCategory) ImeAction.None else ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClick: (() -> Unit)? = null
) {
    Column {
        if (optionalLabel.isNullOrBlank()) {
            Text(
                text = label,
                style = swipeTypography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        } else {
            Text(
                text = optionalLabel,
                style = swipeTypography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.SemiBold,
            )
        }

        val interactionSource = remember { MutableInteractionSource() }

        if (isCategory && onClick != null) {
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        onClick()
                    }
                }
            }
        }

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                disabledContainerColor = Color.White,
                disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Gray,
                focusedIndicatorColor = Color.Gray,
                cursorColor = Color.Black
            ),
            isError = isError,
            enabled = enabled,
            singleLine = true,
            value = value,
            placeholder = {
                Text(
                    text = placeholder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            onValueChange = if (isCategory) { { } } else onValueChange,
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            readOnly = isCategory,
            interactionSource = interactionSource,
            trailingIcon = {
                if (isTrailingIconVisible && trailingIcon != null) {
                    if (onTrailingIconClick != null) {
                        IconButton(onClick = onTrailingIconClick) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = "Trailing Icon",
                                tint = Color.Gray
                            )
                        }
                    } else {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = "Trailing Icon",
                            tint = Color.Gray
                        )
                    }
                }
            },
            leadingIcon = {
                if (isLeadingIconVisible && leadingIcon != null) {
                    if (onLeadingIconClick != null) {
                        IconButton(onClick = onLeadingIconClick) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = "Leading Icon",
                                tint = Color.Gray
                            )
                        }
                    } else {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = "Leading Icon",
                            tint = Color.Gray
                        )
                    }
                }
            }
        )

        if (isError) {
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = errorMessage ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = swipeTypography.bodySmall
                )
            }
        }
    }
}