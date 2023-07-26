package com.ppojin.kafkatester

import org.apache.kafka.clients.producer.internals.BuiltInPartitioner
import org.apache.kafka.clients.producer.internals.DefaultPartitioner
import org.apache.kafka.common.utils.Utils
import spock.lang.Specification

class PartitionerSpec extends Specification {

    def "DefaultPartitioner#partition"() {
        expect:
        var temp1 = Utils.murmur2(key.getBytes())
        var temp2 = Utils.toPositive(temp1)

        println key + " -> " + temp1 + " -> " + temp2 + " -> " + partition

        (temp2 % partitionCount) == partition
        BuiltInPartitioner.partitionForKey(key.getBytes(), partitionCount) == partition

        where:
        key | partitionCount | partition
        "a" | 10             | 4
        "b" | 10             | 6
        "c" | 10             | 4
        "d" | 10             | 3
        "e" | 10             | 4
    }
}