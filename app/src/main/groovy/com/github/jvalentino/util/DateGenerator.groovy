package com.github.jvalentino.util

import groovy.transform.CompileDynamic

/**
 * For making a new date
 */
@CompileDynamic
@SuppressWarnings(['NoJavaUtilDate'])
class DateGenerator {

    static Date date() {
        new Date()
    }

}
