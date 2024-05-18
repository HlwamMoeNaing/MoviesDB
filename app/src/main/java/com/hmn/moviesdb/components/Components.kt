package com.hmn.moviesdb.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hmn.moviesdb.ui.theme.Purple80

@Composable
fun AppTextField(
    value: String,
    @StringRes label: Int,
    hint: String = "",
    onValueChanged: (value: String) -> Unit,
    isPasswordField: Boolean = false,
    isClickOnly: Boolean = false,
    onClick: () -> Unit = {},
    leadingIcon: ImageVector? = null,
    @StringRes error: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onDone: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isClickOnly) {
                        onClick()
                    }
                },
            value = value,
            onValueChange = { onValueChanged(it) },
            singleLine = true,
            isError = error != null,
            readOnly = isClickOnly,
            enabled = !isClickOnly,
            supportingText = {
                if (error != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(error),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,
            label = { Text(text = stringResource(label)) },
            placeholder = { Text(text = hint) },
            leadingIcon = {
                leadingIcon?.let {
                    Icon(it, hint, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            trailingIcon = {
                if (error != null) {
                    Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
                }
            },

            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDone()
                },
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
        )
    }
}


@Composable
fun LoadingDialog(
    title: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(false) } // true

    LaunchedEffect(showDialog) {
        openDialog = showDialog
    }

    if (openDialog) {
        Dialog(
            onDismissRequest = {
                openDialog = false
                onDismiss()
            }, properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                modifier = modifier.fillMaxWidth(0.9f),
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }

}


@Composable
fun WarningDialog(
    title:String = "try again",
    attemptAccount: Int,
    message: String,
    onProcess: () -> Unit,
    onDismiss: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(true)
    }

    val messageStatus = if (attemptAccount > 3) {
        "Too many attempts,please check your internet connection or try again later"
    } else {
        message
    }

    if (showDialog) {
        Dialog(onDismissRequest = {
            showDialog = false
            onDismiss()
        }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp)),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RSAFIcon(
                                icon = Icons.Default.Warning,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = messageStatus,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Row(modifier = Modifier.align(Alignment.End)) {
                            if (attemptAccount <= 2) {
                                TextButton(onClick = {
                                    showDialog = false
                                    onProcess()
                                }) {
                                    Text(text = title.uppercase())
                                }
                            } else {
                                TextButton(onClick = {
                                    showDialog = false
                                    onDismiss()
                                }) {
                                    Text(text = "Ok".uppercase())
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RSAFIcon(
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Icon(imageVector = icon, tint = color, contentDescription = null, modifier = modifier)
}


@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    title: String,
    cancelTitle: String,
    confirmTitle: String,
    onDismiss: () -> Unit = {},
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    var openDialog by remember { mutableStateOf(showDialog) }

    LaunchedEffect(showDialog) {
        openDialog = showDialog
    }

    if (openDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = {
                openDialog = false
                onDismiss()
            }
        ) {
            ConfirmationDialogContent(
                modifier = modifier,
                title = title,
                cancelTitle = cancelTitle,
                confirmTitle = confirmTitle,
                onCancelClick = {
                    onCancelClick()
                    openDialog = false
                },
                onConfirmClick = {
                    onConfirmClick()
                    openDialog = false
                })
        }
    }
}

@Composable
private fun ConfirmationDialogContent(
    modifier: Modifier = Modifier,
    title: String,
    cancelTitle: String,
    confirmTitle: String,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(16.dp)),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = title)
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    ) {
                        RSAFTextButton(title = cancelTitle, color = Purple80) {
                            onCancelClick()
                        }

                        RSAFTextButton(title = confirmTitle, color = Purple80) {
                            onConfirmClick()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RSAFTextButton(
    title: String,
    visible: Boolean = true,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    if (visible) {
        TextButton(onClick = onClick) {
            Text(text = title, color = color)
        }
    }
}