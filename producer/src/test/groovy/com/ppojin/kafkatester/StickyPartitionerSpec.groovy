package com.ppojin.kafkatester

import org.apache.kafka.clients.producer.internals.BuiltInPartitioner
import org.apache.kafka.common.utils.Utils
import spock.lang.Specification

class StickyPartitionerSpec extends Specification {

    def "StickyPartitionerTest"() {
        expect:
        int temp1 = Utils.murmur2(key.getBytes())
        int temp2 = Utils.toPositive(temp1)
        int temp3 = temp2 % partitionCount

        println key + " -> " + temp1 + " -> " + temp2 + " -> " + temp3 + " (expect: "+partition+")"

        partition == temp3
        partition == BuiltInPartitioner.partitionForKey(key.getBytes(), partitionCount)

        where:
        key | partitionCount | partition
        "a" | 10             | 4
        "b" | 10             | 6
        "c" | 10             | 4
        "d" | 10             | 3
        "e" | 10             | 4
        "f" | 10             | 5
        "g" | 10             | 3
        "h" | 10             | 5
        "i" | 10             | 8
        "j" | 10             | 6
    }
}