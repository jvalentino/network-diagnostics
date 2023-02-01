package com.github.jvalentino.util

import spock.lang.Specification
import spock.lang.Unroll

class FormatUtilTest extends Specification {

    @Unroll
    void "test format for #input to #output"() {
        when:
        String result = FormatUtil.formatRateFromBits(input)

        then:
        result == output

        where:
        input           || output
        1L              || '0.12 Bytes/s'
        10L             || '1.25 Bytes/s'
        100L            || '12.50 Bytes/s'
        1_000L          || '125.00 Bytes/s'
        10_000L         || '1.22 KB/s'
        100_000L        || '12.21 KB/s'
        1_000_000L      || '122.07 KB/s'
        10_000_000L     || '1.19 MB/s'
        100_000_000L    || '11.92 MB/s'
        1_000_000_000L  || '119.21 MB/s'
        10_000_000_000L || '1.16 GB/s'
    }
}
