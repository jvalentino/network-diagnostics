package com.github.jvalentino.thread

import com.github.jvalentino.util.DateGenerator
import com.github.jvalentino.util.DateUtil
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import spock.lang.Specification

import java.util.concurrent.CountDownLatch

class SpeedTestSocketThreadTest extends Specification {

    SpeedTestSocketThread subject

    def setup() {
        subject = new SpeedTestSocketThread()
        subject.with {
            averageRate = new BigDecimal(0)
            startDate = null
            endDate = null
            speedTestSocket = GroovyMock(SpeedTestSocket)
            latch = GroovyMock(CountDownLatch)
            rates = []
            instance = Mock(SpeedTestSocketThread)
        }

        GroovyMock(DateGenerator, global:true)
    }

    def "test onProgress"() {
        given:
        float percent = 10.0
        SpeedTestReport report = GroovyMock()

        when:
        subject.onProgress(percent, report)

        then:
        1 * report.transferRateBit >> new BigDecimal(100)

        and:
        subject.rates.size() == 1
        subject.rates.first().toInteger() == 100
    }

    def "test onCompletion"() {
        given:
        SpeedTestReport report = GroovyMock()

        and:
        subject.rates = [
                new BigDecimal(200),
                new BigDecimal(50)
        ]

        when:
        subject.onCompletion(report)

        then:
        1 * report.transferRateBit >> new BigDecimal(100)
        1 * DateGenerator.date() >> DateUtil.toDate('2021-10-31T00:00:00.000+0000')
        1 * subject.latch.countDown()

        and:
        subject.averageRate.toDouble() == 116.67D
        subject.rates.size() == 3
        subject.minRate.toInteger() == 50
        subject.maxRate.toInteger() == 200

    }

}
