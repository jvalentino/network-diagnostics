package com.github.jvalentino.thread

import com.github.jvalentino.service.SpeedRestReportService
import groovy.transform.CompileDynamic

/**
 * Runs the speed test on loop
 * @author john.valentino
 */
@CompileDynamic
class InfiniteSpeedTest extends Thread {

    protected File downloadReportFile
    protected String url
    protected boolean running = true
    protected File downloadChartFile

    protected SpeedRestReportService speedRestReportService = new SpeedRestReportService()

    InfiniteSpeedTest(File downloadReportFile, downloadChartFile, String url) {
        this.downloadReportFile = downloadReportFile
        this.url = url
        this.downloadChartFile = downloadChartFile
    }

    @Override
    void run() {
        while (running) {
            SpeedTestSocketThread thread = new SpeedTestSocketThread()
            thread.start(url)
            thread.join()

            speedRestReportService.report(downloadReportFile, downloadChartFile, thread.speedTestResult)
        }
    }

}
