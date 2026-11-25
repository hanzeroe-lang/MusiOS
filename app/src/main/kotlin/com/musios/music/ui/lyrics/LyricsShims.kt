package com.musios.music.ui.lyrics

import android.os.Build
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.musios.music.lyrics.bitchord.LyricLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

/** The player's side margin; scrollable lyric panels reach back across it. */
internal val PLAYER_GUTTER = 30.dp

/** RenderEffect-backed blur only exists from API 31. */
internal val renderEffectBlurSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/** Ink for glyphs drawn over artwork. */
internal val LocalPlayerInk = staticCompositionLocalOf { Color.White }
internal val LocalPlayerInkGlow = staticCompositionLocalOf { Color.Transparent }

/** Mirrors the BitChord settings the renderer reads, with app defaults. */
internal object PlayerSettings {
    val lyricsOffsetMs: StateFlow<Int> = MutableStateFlow(0)
    val lyricsBlur: StateFlow<Boolean> = MutableStateFlow(true)
    val reduceAnimation: StateFlow<Boolean> = MutableStateFlow(false)
    val reduceDynamicBlur: StateFlow<Boolean> = MutableStateFlow(false)
    val translationLanguage: StateFlow<String> = MutableStateFlow("")

    fun setLyricsOffsetMs(value: Int) {
        (lyricsOffsetMs as MutableStateFlow).value = value
    }
}

/** Translation host. MusiOS has its own providers; this keeps the exact UI intact. */
internal sealed interface LyricsTranslationResult {
    data class Translated(val lines: List<LyricLine>) : LyricsTranslationResult
    data class SameLanguage(val language: String) : LyricsTranslationResult
    data object Unavailable : LyricsTranslationResult
}

internal sealed interface LyricsRomanizationResult {
    data class Romanized(val lines: List<LyricLine>) : LyricsRomanizationResult
    data object AlreadyRomanized : LyricsRomanizationResult
    data object Unavailable : LyricsRomanizationResult
}

internal interface PlayerHost {
    suspend fun translateLyrics(
        trackId: String,
        lines: List<LyricLine>,
        targetLanguageTag: String,
    ): LyricsTranslationResult = LyricsTranslationResult.Unavailable

    suspend fun romanizeLyrics(
        trackId: String,
        lines: List<LyricLine>,
        targetLanguageTag: String,
    ): LyricsRomanizationResult = LyricsRomanizationResult.Unavailable

    fun showMessage(message: String) = Unit
}

internal object PlayerPlatform {
    private val defaultHost = object : PlayerHost {}
    @Volatile
    private var installed: PlayerHost = defaultHost

    val host: PlayerHost get() = installed
    fun install(host: PlayerHost) { installed = host }
}

/** Haptics: no-op by default; the animation and seek code is unchanged. */
internal enum class LyricsHaptic { Select, Tap, ToggleOn }
internal class Haptics {
    fun play(@Suppress("UNUSED_PARAMETER") haptic: LyricsHaptic) = Unit
}
internal typealias Haptic = LyricsHaptic
@Composable
internal fun rememberHaptics(): Haptics = remember { Haptics() }

/** Icons used by the lyric status rows. */
internal object BitChordIcons {
    val MusicNote: ImageVector = Icons.Rounded.MusicNote
    val ChevronRight: ImageVector = Icons.Rounded.ChevronRight
}

/** Whether the app is actually on screen — gates the per-frame clock. */
@Composable
internal fun rememberIsForeground(): Boolean {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var foreground by remember(lifecycle) {
        mutableStateOf(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
    }
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { owner, _ ->
            foreground = owner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
    return foreground
}

@Composable
internal fun appLanguageTag(): String = Locale.getDefault().toLanguageTag()

internal fun translationLanguageName(code: String, inLocale: Locale): String =
    runCatching { Locale.forLanguageTag(code).getDisplayName(inLocale) }
        .getOrDefault(code)

/** Plain clip-and-translate on Android; the desktop uses a filtered layer. */
internal fun DrawScope.clipShiftedDown(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    dy: Float,
    block: DrawScope.() -> Unit,
) {
    clipRect(left = left, top = top, right = right, bottom = bottom) {
        translate(top = dy) { block() }
    }
}

/** Widens a scrollable panel back across the player's side gutter. */
internal fun Modifier.bleedHorizontally(gutter: Dp): Modifier = layout { measurable, constraints ->
    val extra = gutter.roundToPx() * 2
    val widened = if (constraints.hasBoundedWidth) {
        constraints.copy(
            minWidth = constraints.minWidth + extra,
            maxWidth = constraints.maxWidth + extra,
        )
    } else constraints
    val placeable = measurable.measure(widened)
    val width = (placeable.width - extra).coerceAtLeast(0)
    layout(width, placeable.height) {
        placeable.place(-(placeable.width - width) / 2, 0)
    }
}

/** Softens the list where it meets the header and the scrubber. */
internal fun Modifier.fadingEdges(): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        val fade = 28.dp.toPx()
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startY = 0f,
                endY = fade,
            ),
            blendMode = BlendMode.DstIn,
        )
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Black, Color.Transparent),
                startY = size.height - fade,
                endY = size.height,
            ),
            blendMode = BlendMode.DstIn,
        )
    }

/** Keeps an overscroll inside the lyric list instead of dragging the sheet. */
internal fun keepScrollInList(listState: LazyListState) = object : NestedScrollConnection {
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset = available

    override suspend fun onPreFling(available: Velocity): Velocity =
        if (available.y > 0f && !listState.canScrollBackward) available else Velocity.Zero

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity = available
}
