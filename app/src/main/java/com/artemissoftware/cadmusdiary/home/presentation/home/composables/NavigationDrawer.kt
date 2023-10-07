package com.artemissoftware.cadmusdiary.home.presentation.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artemissoftware.cadmusdiary.R
import com.core.ui.R as CoreUiR

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onDeleteAllClicked: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Image",
                    )
                    NavigationDrawerItem(
                        label = {
                            Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                                Image(
                                    painter = painterResource(id = CoreUiR.drawable.google_logo),
                                    contentDescription = "Google Logo",
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Sign Out",
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                        selected = false,
                        onClick = onSignOutClicked,
                    )
                    NavigationDrawerItem(
                        label = {
                            Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete All Icon",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Delete All Diaries",
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                        selected = false,
                        onClick = onDeleteAllClicked,
                    )
                },
            )
        },
        content = content,
    )
}

@Composable
private fun NavigationDrawerContent(
    onSignOutClicked: () -> Unit,
    onDeleteAllClicked: () -> Unit,
) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo Image",
    )
    NavigationDrawerItem(
        label = {
            Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                Image(
                    painter = painterResource(id = CoreUiR.drawable.google_logo),
                    contentDescription = "Google Logo",
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Sign Out",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        selected = false,
        onClick = onSignOutClicked,
    )
    NavigationDrawerItem(
        label = {
            Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete All Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Delete All Diaries",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        selected = false,
        onClick = onDeleteAllClicked,
    )
}

@Composable
@Preview
private fun NavigationDrawerPreview() {
    NavigationDrawerContent(
        onSignOutClicked = {},
        onDeleteAllClicked = {},
    )
}
