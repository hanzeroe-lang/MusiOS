package com.musios.music.ui.lyrics

/**
 * Plain-text replacements for the BitChord Compose-Resource strings used by
 * the ported lyrics renderer. Kept here so [PlayerLyrics.kt] stays verbatim.
 */
internal object LyricsStrings {
    const val instrumental = "Instrumental"
    const val lyricsNotAvailable = "Lyrics not available"
    const val alreadyRomanized = "These lyrics already use Latin characters"
    const val romanizationUnavailable = "Romanization is unavailable for these lyrics"
    const val romanized = "Romanized pronunciation"
    const val savedWithDownload = "Lyrics saved with this download"
    const val translationUnavailable = "Translation is unavailable right now"
    const val noLyricsForTrack = "No lyrics for this track"
    const val noLyricsFound = "No lyrics found"
    const val openLyrics = "Lyrics"
    const val romanize = "Romanize lyrics"
    const val romanizing = "Romanizing lyrics."
    const val showOriginal = "Show original lyrics"
    const val singAlong = "Sing along, with vocals lowered"
    const val translate = "Translate lyrics"

    fun alreadyInLanguage(language: String) = "These lyrics are already in $language"
    fun translatedTo(language: String) = "Translated to $language"
    fun translatingTo(language: String) = "Translating to $language."

    val introLines = listOf(
        "The beat is landing",
        "The song is starting",
        "Warming up",
        "Setting the mood",
        "Bass first, words later",
        "Wait for it",
        "Feel that build",
        "Just the groove for now",
        "The hook is on the way",
        "Let it breathe",
        "Cue the vocals",
        "First notes in",
    )
}
