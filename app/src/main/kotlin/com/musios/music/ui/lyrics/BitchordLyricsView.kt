package com.musios.music.ui.lyrics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.musios.music.lyrics.LyricsEntry
import com.musios.music.lyrics.WordTimestamp
import com.musios.music.lyrics.bitchord.LyricAlignment
import com.musios.music.lyrics.bitchord.LyricLine
import com.musios.music.lyrics.bitchord.LyricWord
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Bridge between MusiOS's playback/lyrics state and the ported BitChord
 * animation system. The renderer itself is [LyricsPanel]; this supplies the
 * dated playhead it expects from `player.currentPosition`.
 */
@Composable
fun BitchordLyricsView(
    lines: List<LyricLine>,
    trackKey: String,
    positionMs: () -> Long,
    isPlaying: Boolean,
    onSeekToLine: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val position = remember { PlaybackPosition() }

    // The renderer expects a dated reading about twice a second and derives a
    // smooth frame clock from it. Report the app's live player position; a real
    // backwards jump is announced as a seek so the clock glides instead of
    // treating it as a bad reading.
    LaunchedEffect(position) {
        var last = positionMs()
        while (isActive) {
            val now = positionMs()
            if (now < last - 1_500L) position.seeks++
            last = now
            position.report(now)
            delay(100L)
        }
    }

    val playhead = rememberLyricPlayhead(position)
    LyricsPanel(
        lines = lines,
        trackKey = trackKey,
        playhead = playhead,
        looking = false,
        isPlaying = isPlaying,
        onSeekToLine = onSeekToLine,
        controlsOpen = false,
        onRevealControls = {},
        onHideControls = {},
        modifier = modifier.fillMaxSize(),
    )
}

/** Converts the app's parsed LRC entries into the renderer's synced model. */
internal fun List<LyricsEntry>.toBitChordLines(): List<LyricLine> {
    val result = mutableListOf<LyricLine>()
    for (entry in this) {
        val words: List<LyricWord> = entry.words.orEmpty().map { w: WordTimestamp ->
            LyricWord(
                startMs = (w.startTime * 1000.0).toLong(),
                endMs = (w.endTime * 1000.0).toLong(),
                text = w.text,
            )
        }
        val line = LyricLine(
            timeMs = entry.time,
            text = entry.text,
            words = words,
            sungUntilMs = entry.words?.lastOrNull()?.endTime
                ?.let { (it * 1000.0).toLong() },
            alignment = if (entry.agent == "v2") LyricAlignment.End else LyricAlignment.Start,
        )
        if (entry.isBackground && result.isNotEmpty()) {
            val lead = result.removeAt(result.lastIndex)
            result += lead.copy(background = line)
        } else {
            result += line
        }
    }
    return result
}
