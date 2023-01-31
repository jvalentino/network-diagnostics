package com.github.jvalentino

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
        log.info('Running')
    }

}
