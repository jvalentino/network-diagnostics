package com.github.jvalentino.thread

import com.github.jvalentino.util.DateGenerator
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

import java.math.RoundingMode
import java.util.concurrent.CountDownLatch

/**
 * A thread I had to build around the speed test so that I could wait for it to finish
 * @john.valentino
 */
@Slf4j
@CompileDynamic
@SuppressWarnings(['NoFloat'])
class SpeedTestSocketThread extends Thread {

    BigDecimal averageRate = new BigDecimal(0)
    Date startDate
    Date endDate

    protected SpeedTestSocket speedTestSocket = new SpeedTestSocket()
    protected CountDownLatch latch = new CountDownLatch(1)
    protected List<BigDecimal> rates = []
    protected SpeedTestSocketThread instance = this

    SpeedTestSocketThread() {
        // add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            void onCompletion(SpeedTestReport report) {
                instance.onCompletion(report)
            }

            @Override
            void onError(SpeedTestError speedTestError, String errorMessage) {
                instance.onError(speedTestError, errorMessage)
            }

            @Override
            void onProgress(float percent, SpeedTestReport report) {
                instance.onProgress(percent, report)
            }

        })
    }

    void start(String url) {
        startDate = DateGenerator.date()
        speedTestSocket.startDownload(url)
        super.start()
    }

    void run() {
        latch.await()
    }

    protected void onError(SpeedTestError speedTestError, String errorMessage) {
        // called when a download/upload error occur
        // speedTestError.toString()
        latch.countDown()
    }

    protected void onCompletion(SpeedTestReport report) {
        endDate = DateGenerator.date()
        synchronized (rates) {
            BigDecimal rate = report.transferRateBit
            rates.add(rate)

            log.info("100% at ${rate} bit/s")

            BigDecimal sum = new BigDecimal(0)
            for (BigDecimal current : rates) {
                sum = sum.add(current)
            }

            BigDecimal average = sum.divide(rates.size().toBigDecimal(), 2, RoundingMode.HALF_UP)
            log.info("Average: ${average} bit/s (${startDate} - ${endDate})")
            averageRate = average
        }
        // called when download/upload is complete
        latch.countDown()
    }

    protected void onProgress(float percent, SpeedTestReport report) {
        // called to notify download/upload progress

        synchronized (rates) {
            BigDecimal rate = report.transferRateBit
            rates.add(rate)

            if (rates.size() % 100 == 0 || rates.size() == 1) {
                log.info("${percent}% at ${rate} bit/s")
            }
        }
    }

}
