package com.github.jvalentino

import com.github.jvalentino.thread.SpeedTestSocketThread
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j

/**
 * Main application
 * @author john.valentino
 */
@CompileDynamic
@Slf4j
class App {

    static void main(String[] args) {
        new App().execute()
    }

    void execute() {
        SpeedTestSocketThread thread = new SpeedTestSocketThread()
        thread.start('https://freetestdata.com/wp-content/uploads/2022/02/Free_Test_Data_20MB_WMV.wmv')

        thread.join()
    }

}
