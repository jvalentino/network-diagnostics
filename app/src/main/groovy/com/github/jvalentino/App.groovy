package com.github.jvalentino

import com.github.jvalentino.thread.InfiniteSpeedTest
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

/**
 * Main application
 * @author john.valentino
 */
@CompileDynamic
@Slf4j
@SuppressWarnings(['JavaIoPackageAccess'])
class App {

    static final String MB_1 = 'https://freetestdata.com/wp-content/uploads/2021/10/Free_Test_Data_1MB_MOV.mov'
    static final String MB_5 = 'https://freetestdata.com/wp-content/uploads/2022/02/Free_Test_Data_5MB_WMV.wmv'
    static final String MB_10 = 'https://freetestdata.com/wp-content/uploads/2022/02/Free_Test_Data_10MB_WMV.wmv'
    static final String MB_20 = 'https://freetestdata.com/wp-content/uploads/2022/02/Free_Test_Data_20MB_WMV.wmv'

    File downloadReportFile = new File('report-download.csv')

    static void main(String[] args) {
        new App().execute()
    }

    void execute() {
        InfiniteSpeedTest speedTest = new InfiniteSpeedTest(downloadReportFile, MB_1)
        speedTest.start()

        speedTest.join()
    }

}
