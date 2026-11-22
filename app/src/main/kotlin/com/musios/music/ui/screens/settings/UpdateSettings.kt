package com.musios.music.ui.screens.settings

import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.layout.Column
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.layout.Spacer
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.layout.height
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.layout.padding
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.layout.size
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.rememberScrollState
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.verticalScroll
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.Icon
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.GlassSwitchCompat as Switch
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.SwitchDefaults
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.Text
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.TopAppBar
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.TopAppBarScrollBehavior
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.Composable
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.LaunchedEffect
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.getValue
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.mutableStateOf
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.remember
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.setValue
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.Modifier
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.platform.LocalContext
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.res.painterResource
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.res.stringResource
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.unit.dp
import com.musios.music.ui.utils.appTopBarWindowInsets
import android.content.Intent
import com.musios.music.ui.utils.appTopBarWindowInsets
import android.net.Uri
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.navigation.NavController
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.LocalPlayerAwareWindowInsets
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.R
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.IconButton
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.Material3SettingsGroup
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.Material3SettingsItem
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.component.UpdateInfoDialog
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.utils.backToMain
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.getAutoUpdateCheckSetting
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.saveAutoUpdateCheckSetting
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.getUpdateAvailableState
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.saveUpdateAvailableState
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.getUpdateNotificationsSetting
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.saveUpdateNotificationsSetting
import com.musios.music.ui.utils.appTopBarWindowInsets
import android.widget.Toast
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.res.pluralStringResource
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.getDownloadedApkCount
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.clearDownloadedApks
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.getBetaUpdatesSetting
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.saveBetaUpdatesSetting
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.vivimusic.updater.autoClearOldApks
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.MaterialTheme
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.BuildConfig

//here b5.0.1 must be used for the beta tag

/**
 * Convx Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context = LocalContext.current
    var autoUpdateEnabled by remember { mutableStateOf(getAutoUpdateCheckSetting(context)) }
    var updateNotificationsEnabled by remember { mutableStateOf(getUpdateNotificationsSetting(context)) }
    var betaUpdatesEnabled by remember { mutableStateOf(getBetaUpdatesSetting(context)) }
    val isUpdateAvailable = getUpdateAvailableState(context) && autoUpdateEnabled
    var apkCount by remember { mutableStateOf(getDownloadedApkCount(context)) }
    var showInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        autoClearOldApks(context)
        apkCount = getDownloadedApkCount(context)
    }

    if (showInfoDialog) {
        UpdateInfoDialog(onDismiss = { showInfoDialog = false })
    }

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Material3SettingsGroup(
            title = stringResource(R.string.app_updates_title),
            items = listOf(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.network_update),
                    title = { Text(stringResource(R.string.system_update)) },
                    description = {
                        if (isUpdateAvailable) {
                            Text(
                                text = stringResource(R.string.update_available),
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(stringResource(R.string.app_update_uptodate))
                        }
                    },
                    onClick = {
                        val isFoss = !BuildConfig.CAST_AVAILABLE
                        if (isFoss) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/cosmictaserdev-creator/Convx"))
                            context.startActivity(intent)
                        } else {
                            navController.navigate("update")
                        }
                    }
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.info),
                    title = {
                        Text(stringResource(R.string.version, BuildConfig.VERSION_NAME))
                    },
                    description = {
                        val arch = BuildConfig.ARCHITECTURE
                        val variant = if (BuildConfig.CAST_AVAILABLE) "GMS" else "FOSS"
                        Text("$arch - $variant")
                    }
                ),
                
                Material3SettingsItem(
                    icon = painterResource(R.drawable.update),
                    title = { Text(stringResource(R.string.auto_update_check)) },
                    description = { Text(stringResource(R.string.auto_update_check_subtitle)) },
                    trailingContent = {
                        Switch(
                            checked = autoUpdateEnabled,
                            onCheckedChange = { enabled ->
                                autoUpdateEnabled = enabled
                                saveAutoUpdateCheckSetting(context, enabled)
                                if (!enabled) {
                                    saveUpdateAvailableState(context, false)
                                }
                            },
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (autoUpdateEnabled) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = {
                        autoUpdateEnabled = !autoUpdateEnabled
                        saveAutoUpdateCheckSetting(context, autoUpdateEnabled)
                        if (!autoUpdateEnabled) {
                            saveUpdateAvailableState(context, false)
                        }
                    }
                ),

                Material3SettingsItem(
                    icon = painterResource(R.drawable.notification),
                    title = { Text(stringResource(R.string.update_notifications)) },
                    description = { Text(stringResource(R.string.update_notifications_subtitle)) },
                    trailingContent = {
                        Switch(
                            checked = updateNotificationsEnabled,
                            onCheckedChange = { enabled ->
                                updateNotificationsEnabled = enabled
                                saveUpdateNotificationsSetting(context, enabled)
                            },
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (updateNotificationsEnabled) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = {
                        updateNotificationsEnabled = !updateNotificationsEnabled
                        saveUpdateNotificationsSetting(context, updateNotificationsEnabled)
                    }
                ),

                Material3SettingsItem(
                    icon = painterResource(R.drawable.biotech),
                    title = { Text(stringResource(R.string.beta_updates)) },
                    description = { Text(stringResource(R.string.beta_updates_subtitle)) },
                    trailingContent = {
                        Switch(
                            checked = betaUpdatesEnabled,
                            onCheckedChange = { enabled ->
                                betaUpdatesEnabled = enabled
                                saveBetaUpdatesSetting(context, enabled)
                            },
                            thumbContent = {
                                Icon(
                                    painter = painterResource(
                                        id = if (betaUpdatesEnabled) R.drawable.check else R.drawable.close
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    },
                    onClick = {
                        betaUpdatesEnabled = !betaUpdatesEnabled
                        saveBetaUpdatesSetting(context, betaUpdatesEnabled)
                    }
                ),

                Material3SettingsItem(
                    icon = painterResource(R.drawable.delete),
                    title = { Text(stringResource(R.string.clear_downloaded_updates)) },
                    description = {
                        if (apkCount == 0) {
                            Text(
                                text = stringResource(R.string.clear_downloaded_updates_desc),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = pluralStringResource(R.plurals.n_apk_found, apkCount, apkCount),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingContent = {
                        IconButton(
                            onClick = { showInfoDialog = true },
                            onLongClick = {}
                        ) {
                            Icon(
                                painterResource(R.drawable.info),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    onClick = {
                        if (apkCount > 0) {
                            if (clearDownloadedApks(context)) {
                                apkCount = 0
                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to delete some files", Toast.LENGTH_SHORT).show()
                                apkCount = getDownloadedApkCount(context)
                            }
                        }
                    }
                )

//                Material3SettingsItem(
//                    icon = painterResource(R.drawable.info),
//                    title = { Text(stringResource(R.string.namespace)) },
//                    description = { Text(BuildConfig.APPLICATION_ID) }
//                )

            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Material3SettingsGroup(
            title = stringResource(R.string.changelog),
            items = listOf(
                Material3SettingsItem(
                    icon = painterResource(R.drawable.history),
                    title = { Text(stringResource(R.string.changelog)) },
                    description = { Text(stringResource(R.string.view_version_history)) },
                    onClick = { navController.navigate("settings/changelog") }
                ),
                Material3SettingsItem(
                    icon = painterResource(R.drawable.commit),
                    title = { Text(stringResource(R.string.commits)) },
                    description = { Text(stringResource(R.string.view_commit_history)) },
                    onClick = { navController.navigate("settings/commits") }
                )
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    TopAppBar(
            windowInsets = appTopBarWindowInsets(),
        title = { Text(stringResource(R.string.update_settings_title)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null
                )
            }
        }
    )
}
