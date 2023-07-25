package com.ppojin.kafkatester

import org.apache.kafka.common.utils.Utils
import spock.lang.Specification

class HelloSpockSpec extends Specification {

    def "DefaultPartitioner#partition"() {
        expect:
        var temp1 = Utils.murmur2(key.getBytes())
        var temp2 = Utils.toPositive(temp1)

        Math.floorMod(temp2, 10) == partition

        println key + " -> " + temp1 + " -> " + temp2 + " -> " + partition

        where:
        key | partition
        "a" | 4
        "b" | 6
        "c" | 4
        "d" | 3
        "e" | 4
    }
}