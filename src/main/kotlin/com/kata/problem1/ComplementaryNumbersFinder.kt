package com.kata.problem1

data class PairResult(
    val pair: Pair<Int, Int>?
) {
    val product get() = pair?.let { it.first * it.second }
    val isEmpty = pair == null

    companion object {
        fun empty() = PairResult(null)
    }
}

data class TripleResult(
    val triple: Triple<Int, Int, Int>?
) {
    val product get() = triple?.let { it.first * it.second * it.third }
    val isEmpty = triple == null

    val tripleAsList get() = triple?.toList() ?: emptyList()

    companion object {
        fun empty() = TripleResult(null)
    }
}

private const val SUMS_TO_ERR_MSG = "parameter `sumsTo` must be > 0"

class ComplementaryNumbersFinder {

    fun findPair(numbers: List<Int>, sumsTo: Int): PairResult =
        numbers
            .also { require(sumsTo > 0) { SUMS_TO_ERR_MSG }  }
            .takeIf { it.size > 1 }
            ?.findPairThatSumsTo(sumsTo)
            ?.let { PairResult(it) } ?: PairResult.empty()

    fun findTriple(numbers: List<Int>, sumsTo: Int): TripleResult =
        numbers
            .also { require(sumsTo > 0) { SUMS_TO_ERR_MSG } }
            .takeIf { it.size > 2 }
            ?.findTripleThatSumsTo(sumsTo)
            ?.let { TripleResult(it) } ?: TripleResult.empty()
}

private fun List<Int>.findPairThatSumsTo(to: Int): Pair<Int, Int>? {
    val numbersRead = mutableSetOf<Int>()
    this.forEach { number ->
        if (number < 1) return@forEach

        val complementary = to - number // TODO: abs?
        if (numbersRead.contains(complementary)) {
            return complementary to number
        }

        numbersRead.add(number)
    }

    return null
}

private fun List<Int>.findTripleThatSumsTo(to: Int): Triple<Int, Int, Int>? {
    for (i in 2 until this.size) {
        val current = this[i]
        val max = to - current
        this.subList(0, i).findPairThatSumsTo(max)
            ?.let { return Triple(it.first, it.second, current) }
    }

    return null
}
