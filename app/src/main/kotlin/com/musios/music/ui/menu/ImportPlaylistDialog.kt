/**
 * Convx Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.musios.music.ui.menu

import com.musios.music.ui.component.sf
import com.musios.music.ui.component.SymbolIcon
import com.musios.music.ui.ios.compat.Icon
import com.musios.music.ui.ios.compat.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.musios.music.LocalDatabase
import com.musios.music.LocalSyncUtils
import com.musios.music.R
import com.musios.music.db.entities.PlaylistEntity
import com.musios.music.ui.component.TextFieldDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun ImportPlaylistDialog(
    isVisible: Boolean,
    onGetSong: suspend () -> List<String>, // list of song ids. Songs should be inserted to database in this function.
    playlistTitle: String,
    onDismiss: () -> Unit,
) {
    val database = LocalDatabase.current.get()
    val syncUtils = LocalSyncUtils.current.get()
    val coroutineScope = rememberCoroutineScope()

    val textFieldValue by remember { mutableStateOf(TextFieldValue(text = playlistTitle)) }

    if (isVisible) {
        TextFieldDialog(
            icon = { SymbolIcon(symbol = sf("plus"), contentDescription = null) },
            title = { Text(text = stringResource(R.string.import_playlist)) },
            initialTextFieldValue = textFieldValue,
            autoFocus = false,
            onDismiss = onDismiss,
            onDone = { finalName ->
                val newPlaylist = PlaylistEntity(
                    name = finalName
                )
                database.query { insert(newPlaylist) }

                coroutineScope.launch(Dispatchers.IO) {
                    val playlist = database.playlist(newPlaylist.id).firstOrNull()

                    if (playlist != null) {
                        val songIds = onGetSong()
                        database.addSongToPlaylist(playlist, songIds)
                        syncUtils.uploadPlaylistToYouTube(playlist.id, songIds)
                    }

                    onDismiss()
                }
            }
        )
    }
}
