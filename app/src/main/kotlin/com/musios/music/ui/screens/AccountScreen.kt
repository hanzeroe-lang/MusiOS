/**
 * Convx Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.musios.music.ui.screens

import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.ExperimentalFoundationApi

import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.lazy.grid.GridCells
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.lazy.grid.GridItemSpan
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.foundation.lazy.grid.items
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.Icon
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.Text
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.TopAppBar
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.material3.TopAppBarScrollBehavior
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.Composable
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.collectAsState
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.getValue
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.runtime.rememberCoroutineScope
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.Modifier
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.platform.LocalHapticFeedback
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.res.painterResource
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.res.stringResource
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.compose.ui.unit.dp
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.musios.music.ui.utils.appTopBarWindowInsets
import androidx.navigation.NavController
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.LocalPlayerAwareWindowInsets
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.R
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.utils.rememberGridColumns
import com.musios.music.constants.GridItemSize
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.constants.GridItemsSizeKey
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.constants.GridThumbnailHeight
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.ChipsRow
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.IconButton
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.LocalMenuState
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.YouTubeGridItem
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.shimmer.GridItemPlaceHolder
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.component.shimmer.ShimmerHost
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.menu.YouTubeAlbumMenu
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.menu.YouTubeArtistMenu
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.menu.YouTubePlaylistMenu
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.utils.backToMain
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.utils.bounceClick
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.ui.utils.combinedBounceClick
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.utils.rememberEnumPreference
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.viewmodels.AccountContentType
import com.musios.music.ui.utils.appTopBarWindowInsets
import com.musios.music.viewmodels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccountScreen(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val menuState = LocalMenuState.current
    val haptic = LocalHapticFeedback.current

    val coroutineScope = rememberCoroutineScope()

    val playlists by viewModel.playlists.collectAsState()
    val albums by viewModel.albums.collectAsState()
    val artists by viewModel.artists.collectAsState()
    val selectedContentType by viewModel.selectedContentType.collectAsState()
    val gridItemSize by rememberEnumPreference(GridItemsSizeKey, GridItemSize.BIG)

    LazyVerticalGrid(
        columns = rememberGridColumns(),
        contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues(),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            ChipsRow(
                chips = listOf(
                    AccountContentType.PLAYLISTS to stringResource(R.string.filter_playlists),
                    AccountContentType.ALBUMS to stringResource(R.string.filter_albums),
                    AccountContentType.ARTISTS to stringResource(R.string.filter_artists),
                ),
                currentValue = selectedContentType,
                onValueUpdate = { viewModel.setSelectedContentType(it) },
            )
        }

        when (selectedContentType) {
            AccountContentType.PLAYLISTS -> {
                items(
                    items = playlists.orEmpty().distinctBy { it.id },
                    key = { it.id },
                ) { item ->
                    YouTubeGridItem(
                        item = item,
                        fillMaxWidth = true,
                        modifier = Modifier
                            .combinedBounceClick(
                                onClick = {
                                    navController.navigate("online_playlist/${item.id}")
                                },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    menuState.show {
                                        YouTubePlaylistMenu(
                                            playlist = item,
                                            coroutineScope = coroutineScope,
                                            onDismiss = menuState::dismiss,
                                        )
                                    }
                                },
                            ),
                    )
                }

                if (playlists == null) {
                    items(8) {
                        ShimmerHost {
                            GridItemPlaceHolder(fillMaxWidth = true)
                        }
                    }
                }
            }

            AccountContentType.ALBUMS -> {
                items(
                    items = albums.orEmpty().distinctBy { it.id },
                    key = { it.id }
                ) { item ->
                    YouTubeGridItem(
                        item = item,
                        fillMaxWidth = true,
                        modifier = Modifier
                            .combinedBounceClick(
                                onClick = {
                                    navController.navigate("album/${item.id}")
                                },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    menuState.show {
                                        YouTubeAlbumMenu(
                                            albumItem = item,
                                            navController = navController,
                                            onDismiss = menuState::dismiss
                                        )
                                    }
                                }
                            )
                    )
                }

                if (albums == null) {
                    items(8) {
                        ShimmerHost {
                            GridItemPlaceHolder(fillMaxWidth = true)
                        }
                    }
                }
            }

            AccountContentType.ARTISTS -> {
                items(
                    items = artists.orEmpty().distinctBy { it.id },
                    key = { it.id }
                ) { item ->
                    YouTubeGridItem(
                        item = item,
                        fillMaxWidth = true,
                        modifier = Modifier
                            .combinedBounceClick(
                                onClick = {
                                    navController.navigate("artist/${item.id}")
                                },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    menuState.show {
                                        YouTubeArtistMenu(
                                            artist = item,
                                            onDismiss = menuState::dismiss
                                        )
                                    }
                                }
                            )
                    )
                }

                if (artists == null) {
                    items(8) {
                        ShimmerHost {
                            GridItemPlaceHolder(fillMaxWidth = true)
                        }
                    }
                }
            }
        }
    }

    TopAppBar(
            windowInsets = appTopBarWindowInsets(),
        title = { Text(stringResource(R.string.account)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain,
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                )
            }
        },
    )
}
