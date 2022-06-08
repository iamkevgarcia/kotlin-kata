package com.kata

import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class InputFileClient(
    private val filePath: Path
) {

    init {
        if (!Files.exists(filePath)) {
            throw FileNotFoundException("File not found. Path was: ${filePath.toAbsolutePath()}")
        }
    }

    fun <R> getAll(mapTo: (String) -> R): List<R> =
        Files.newInputStream(filePath)
            .bufferedReader()
            .use { bR -> bR.lines().map(mapTo).collect(Collectors.toList()) }
}
