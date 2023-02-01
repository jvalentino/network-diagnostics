package com.github.jvalentino.thread

import com.github.jvalentino.service.PingReportService
import com.github.jvalentino.util.DateGenerator
import com.github.jvalentino.util.SleepUtil
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

/**
 * Ping forever
 * @author john.valentino
 */
@CompileDynamic
@Slf4j
class InfinitePingThread extends Thread {

    static final String S_PATTERN = "\u001B[32m"
    static final String E_PATTERN = "\u001B[0m"

    protected File pingReportFile
    protected boolean running = true
    protected PingReportService pingReportService = new PingReportService()

    InfinitePingThread(File pingReportFile) {
        this.pingReportFile = pingReportFile
    }

    @Override
    void run() {
        while (running) {

            Date start = DateGenerator.date()
            boolean result = isReachable('google.com', 443, 5000)
            Date end = DateGenerator.date()

            long time = end.time - start.time
            logit("Ping ${result} in ${time}ms")

            pingReportService.report(pingReportFile, start, result, time)

            SleepUtil.sleep(5_000L)
        }
    }

    boolean isReachable(String addr, int openPort, int timeOutMillis) {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis)
            }
            return true
        } catch (IOException ex) {
            return false
        }
    }

    protected void logit(String message) {
        log.info("${S_PATTERN}${message}${E_PATTERN}")
    }
}
