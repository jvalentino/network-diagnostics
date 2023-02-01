package com.github.jvalentino.thread

import com.github.jvalentino.data.SpeedTestResult
import com.github.jvalentino.util.DateGenerator
import com.github.jvalentino.util.FormatUtil
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

    static final String S_PATTERN = "\u001B[34m"
    static final String E_PATTERN = "\u001B[0m"

    SpeedTestResult speedTestResult = new SpeedTestResult()

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
        logit("Downloading ${url}")
        speedTestResult.startDate = DateGenerator.date()
        speedTestSocket.startDownload(url)
        super.start()
    }

    void run() {
        latch.await()
    }

    protected void onError(SpeedTestError speedTestError, String errorMessage) {
        // called when a download/upload error occur
        // speedTestError.toString()
        speedTestResult.endDate = DateGenerator.date()
        speedTestResult.averageRate = new BigDecimal(0)
        speedTestResult.minRate = new BigDecimal(0)
        speedTestResult.maxRate = new BigDecimal(0)
        speedTestResult.error = speedTestError.toString()
        latch.countDown()
    }

    protected void onCompletion(SpeedTestReport report) {
        speedTestResult.endDate = DateGenerator.date()
        synchronized (rates) {
            BigDecimal rate = report.transferRateBit
            rates.add(rate)

            logit("${S_PATTERN}100% at ${FormatUtil.formatRateFromBits(rate.toLong())}${E_PATTERN}")

            BigDecimal sum = new BigDecimal(0)
            for (BigDecimal current : rates) {
                sum = sum.add(current)

                if (current < speedTestResult.minRate) {
                    speedTestResult.minRate = current
                }

                if (current > speedTestResult.maxRate) {
                    speedTestResult.maxRate = current
                }
            }

            BigDecimal average = sum.divide(rates.size().toBigDecimal(), 2, RoundingMode.HALF_UP)
            logit("Average: ${FormatUtil.formatRateFromBits(average.toLong())} " +
                    "(${speedTestResult.startDate} - ${speedTestResult.endDate})")
            speedTestResult.averageRate = average
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
                logit("${percent}% at ${FormatUtil.formatRateFromBits(rate.toLong())}")
            }
        }
    }

    protected void logit(String message) {
        log.info("${S_PATTERN}${message}${E_PATTERN}")
    }

}
