package com.musios.music.history

import com.musios.music.db.MusicDatabase
import com.musios.music.db.entities.EventWithSong
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomHistoryLocalDataSource @Inject constructor(
    private val database: MusicDatabase,
) : HistoryLocalDataSource {
    override suspend fun loadEvents(limit: Int, offset: Int, hideVideoSongs: Boolean, query: String): List<EventWithSong> =
        database.historyEventsPage(limit, offset, hideVideoSongs, query)

    override fun observeEventCount(): Flow<Int> = database.eventCount()
}
