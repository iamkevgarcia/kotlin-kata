package com.kata.problem2

import com.kata.InputFileClient
import java.nio.file.FileSystems
import spock.lang.Specification
import spock.lang.Unroll

class LetterInputValidationShould extends Specification {

    InputFileClient inputFileClient

    def setup() {
        inputFileClient = new InputFileClient(FileSystems.default.getPath("input/input2.txt"))
    }

    def "parse all lines from file and find 666 valid letter occurrence lines"() {
        when: "parsing all input lines"
        def parsed = inputFileClient.getAll({ line -> LetterOccurrenceInput.from(line) })

        then: "666 valid lines were found"
        parsed.count { it.isValid() } == 666
    }

    def "parse all lines from file and find 151 valid letter position lines"() {
        when: "parsing all input lines"
        def parsed = inputFileClient.getAll({ line -> LetterPositionInput.from(line) })

        then: "230 valid lines were found"
        parsed.count { it.isValid() } == 151
    }

    @Unroll
    def "state input: <#line> as valid letter occurrence input"() {
        when:
        def parsed = LetterOccurrenceInput.from(line)

        then:
        parsed.isValid()

        where:
        line                       | _
        "1-1 a: lola"              | _
        "2-3 b: buba"              | _
        "2-3 a: akainua"           | _
        "2-3 a: akainua"           | _
        "2-10 c: clcgcsccccccc"    | _
        "10-11 c: clcgcsccccccclc" | _
    }

    @Unroll
    def "state input: <#invalidOccurrence> as invalid letter occurrence input"() {
        when:
        def parsed = LetterOccurrenceInput.from(invalidOccurrence)

        then:
        !parsed.isValid()

        where:
        invalidOccurrence      | errorMsg
        "1-2 a: bbb"           | _
        "1-2 a: lalalaland"    | _
        "12-20 l: lllllllllll" | _
    }

    def "not allow wrong occurrence from input"() {
        when:
        new LetterOccurrenceInput(minOccurrence, maxOccurrence, 'a' as char, "aa")

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == errorMsg

        where:
        minOccurrence | maxOccurrence | errorMsg
        -1            | 1             | "min occurrences must be positive"
        1             | -1            | "max occurrences must be greater than min occurrences"
        2             | 1             | "max occurrences must be greater than min occurrences"
    }

    @Unroll
    def "state input: <#line> as valid letter position input"() {
        when:
        def parsed = LetterPositionInput.from(line)

        then:
        parsed.isValid()

        where:
        line                 | _
        "1-1 a: a"           | _
        "2-3 a: aaanua"      | _
        "3-2 a: aaanua"      | _
        "1-10 c: cclgcscccc" | _
        "4-4 c: lllc"        | _
    }

    @Unroll
    def "state input: <#invalidPositions> as invalid letter positions input"() {
        when:
        def parsed = LetterPositionInput.from(invalidPositions)

        then:
        !parsed.isValid()

        where:
        invalidPositions       | errorMsg
        "1-2 a: lalalaland"    | _
        "12-20 l: lllllllllll" | _
    }

    def "not allow lesser than 1 positions from input"() {
        when:
        new LetterPositionInput(first, second, 'a' as char, "aa")

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == errorMsg

        where:
        first | second | errorMsg
        0     | 1      | "positions must be > 0"
        1     | 0      | "positions must be > 0"
        -1    | 1      | "positions must be > 0"
    }

    def "not parse lines with dodgy format"() {
        when:
        def neverExecuted = {}
        InputParser.parse(line, neverExecuted)

        then:
        thrown(IllegalStateException)

        where:
        line        | _
        "32 a: aaa" | _
        "1 a: aa"   | _
        "2-1 :s aa" | _
        "2:1 a- aa" | _
        ":1 a- aa"  | _
        "2_1 a: aa" | _
        "2 1 a aa"  | _
        ""          | _
    }
}
