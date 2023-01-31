package com.github.jvalentino.thread

import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

import java.util.concurrent.CountDownLatch

/**
 * A thread I had to build around the speed test so that I could wait for it to finish
 * @john.valentino
 */
@Slf4j
@CompileDynamic
@SuppressWarnings(['NoFloat'])
class SpeedTestSocketThread extends Thread {

    protected SpeedTestSocket speedTestSocket = new SpeedTestSocket()
    protected CountDownLatch latch = new CountDownLatch(1)

    SpeedTestSocketThread() {
        // add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
                latch.countDown()
            }

            @Override
            void onError(SpeedTestError speedTestError, String errorMessage) {
                // called when a download/upload error occur
                // speedTestError.toString()
                latch.countDown()
            }

            @Override
            void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
            }

        })
    }

    void start(String url) {
        speedTestSocket.startDownload(url)
        super.start()
    }

    void run() {
        latch.await()
    }

}
