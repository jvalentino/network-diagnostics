package com.github.jvalentino.util

import groovy.transform.CompileDynamic

/**
 * Yes, sleeping
 * @author john.valentino
 */
@CompileDynamic
class SleepUtil {

    static void sleep(long value) {
        Thread.sleep(value)
    }

}
