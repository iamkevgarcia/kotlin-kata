package com.kata.problem2

data class LetterOccurrenceInput(
    val minOccurrences: Int,
    val maxOccurrences: Int,
    val letter: Char,
    val string: String
) {

    init {
        require(minOccurrences > 0) { "min occurrences must be positive" }
        require(maxOccurrences >= minOccurrences) { "max occurrences must be greater than min occurrences" }
    }

    val isValid get() = string.count { it == letter } in minOccurrences..maxOccurrences

    companion object {

        @JvmStatic
        fun from(rawLine: String): LetterOccurrenceInput =
            require(rawLine.isNotEmpty())
                .let {
                    InputParser.parse(rawLine) { min, max, letter, string ->
                        LetterOccurrenceInput(
                            minOccurrences = min,
                            maxOccurrences = max,
                            letter = letter,
                            string = string
                        )
                    }
                }
    }
}

data class LetterPositionInput(
    val firstPosition: Int,
    val secondPosition: Int,
    val letter: Char,
    val string: String
) {
    init {
        require(firstPosition > 0 && secondPosition > 0) { "positions must be > 0" }
    }

    val isValid get() = firstPosition <= string.length
        && secondPosition <= string.length
        && string[firstPosition - 1] == letter
        && string[secondPosition - 1] == letter

    companion object {
        @JvmStatic
        fun from(rawLine: String): LetterPositionInput =
            check(rawLine.isNotEmpty())
                .let {
                    InputParser.parse(rawLine) { pos1, pos2, letter, string ->
                        LetterPositionInput(
                            firstPosition = pos1,
                            secondPosition = pos2,
                            letter = letter,
                            string = string
                        )
                    }
                }
    }
}
