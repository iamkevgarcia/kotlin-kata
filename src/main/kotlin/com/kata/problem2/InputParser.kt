package com.kata.problem2

object InputParser {

    @JvmStatic
    fun <T> parse(rawLine: String, mapTo: (Int, Int, Char, String) -> T): T {
        check(rawLine.isNotEmpty())

        val dashIndex = rawLine.indexOf('-').also { check(it > 0) }
        val firstSpaceIndex = rawLine.indexOf(' ').also { check(it > dashIndex) }
        val colonIndex = rawLine.indexOf(':').also { check(it > firstSpaceIndex) }

        val firstNumber = rawLine.subSequence(0, dashIndex).toString().toInt()
        val secondNumber = rawLine.subSequence(dashIndex + 1, firstSpaceIndex).toString().toInt()
        val letter = rawLine.subSequence(firstSpaceIndex + 1, colonIndex).also { check(it.length == 1) }.first()
        val text = rawLine.substring(colonIndex + 1).also { check(it.first().isWhitespace()) }.trimStart()

        return mapTo(firstNumber, secondNumber, letter, text)
    }
}
