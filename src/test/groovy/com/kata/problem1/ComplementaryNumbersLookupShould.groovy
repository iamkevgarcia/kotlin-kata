package com.kata.problem1

import com.kata.InputFileClient
import java.nio.file.FileSystems
import kotlin.Pair
import spock.lang.Specification
import spock.lang.Unroll

class ComplementaryNumbersLookupShould extends Specification {

    InputFileClient numberClient
    ComplementaryNumbersFinder finder

    def setup() {
        numberClient = new InputFileClient(FileSystems.default.getPath("./input/input1.txt"))
        finder = new ComplementaryNumbersFinder()
    }

    def "find pair from file that sums to 2020"() {
        given: "input numbers from file"
        def inputNumbers = numberClient.getAll({ it.toInteger() })
        def sumsTo = 2020

        when:
        def result = finder.findPair(inputNumbers, sumsTo)

        then: "pair is found"
        result.pair.first == 1093
        result.pair.second == 927
    }

    def "find triple from file that sums to 2020"() {
        given: "input numbers from file"
        def inputNumbers = numberClient.getAll({ it.toInteger() })
        def sumsTo = 2020

        when:
        def result = finder.findTriple(inputNumbers, sumsTo)

        then: "triple is found"
        result.tripleAsList == [481, 19, 1520]
    }

    @Unroll
    def "return pair for input: #inputNumbers"() {
        when:
        def result = finder.findPair(inputNumbers, sumsTo)

        then:
        result.pair == expectedPair
        result.product == expectedPairProduct

        where:
        inputNumbers            | sumsTo | expectedPair        | expectedPairProduct
        [1, 2]                  | 3      | new Pair(1, 2)      | 2
        [1093, 1618, 1795, 927] | 2020   | new Pair(1093, 927) | 1013211
    }

    @Unroll
    def "return triple for input: #inputNumbers"() {
        when:
        def result = finder.findTriple(inputNumbers, sumsTo)

        then:
        expectedTriple.containsAll(result.tripleAsList)
        result.product == expectedTripleProduct

        where:
        inputNumbers                             | sumsTo | expectedTriple  | expectedTripleProduct
        [1, 2, 3]                                | 6      | [1, 2, 3]       | 6
        [3, 3, 3]                                | 9      | [3, 3, 3]       | 27
        [1721, 979, 366, 166, 1, 299, 675, 1456] | 2020   | [979, 366, 675] | 241861950
    }


    def "return no pair if just a number was given"() {
        given: "a list containing one number"
        def inputNumbers = [1]
        def sumsTo = 1

        when:
        def complementary = finder.findPair(inputNumbers, sumsTo)

        then: "result is empty"
        complementary.isEmpty()
    }

    def "return no triple if just a two numbers were given"() {
        given: "a list containing two numbers"
        def inputNumbers = [3, 3]
        def sumsTo = 6

        when:
        def result = finder.findTriple(inputNumbers, sumsTo)

        then: "result is empty"
        result.isEmpty()
    }

    def "not consider zeroes valid for pairs"() {
        given: "a list containing with a pair that has got a zero"
        def inputNumbers = [3, 0]
        def sumsTo = 3

        when:
        def result = finder.findPair(inputNumbers, sumsTo)

        then: "result is empty"
        result.isEmpty()
    }

    def "not consider zeroes valid for triples"() {
        given: "a list containing with a triple that has got a zero"
        def inputNumbers = [3, 0, 1, 5]
        def sumsTo = 4

        when:
        def result = finder.findTriple(inputNumbers, sumsTo)

        then: "result is empty"
        result.isEmpty()
    }

    def "check that passed `sums to` parameter is greater than 0"() {
        when:
        lookupFun(finder, sumsTo)

        then:
        thrown(IllegalArgumentException)

        where:
        sumsTo | lookupFun
        0      | { sut, numbers -> sut.findPair([2, 3], sumsTo) }
        -1     | { sut, numbers -> sut.findPair([2, 3], sumsTo) }
        0      | { sut, numbers -> sut.findTriple([1, 3, 1], sumsTo) }
        -1     | { sut, numbers -> sut.findTriple([1, 3, 1], sumsTo) }
    }

    def "ignore input numbers that are not positive"() {
        when:
        def result = lookupFun(finder, inputNumbers)

        then:
        result.isEmpty()

        where:
        inputNumbers | lookupFun
        [-1, 2, 3]   | { sut, numbers -> sut.findPair(numbers, 3) }
        [1, 2, -3]   | { sut, numbers -> sut.findTriple(numbers, 6) }
    }
}
