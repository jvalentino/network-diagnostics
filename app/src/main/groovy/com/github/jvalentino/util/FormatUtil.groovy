package com.github.jvalentino.util

import groovy.transform.CompileDynamic

import java.text.DecimalFormat

/**
 * General formatting util
 * @author john.valentino
 */
@CompileDynamic
@SuppressWarnings([
        'NoDouble',
        'DuplicateNumberLiteral',
])
class FormatUtil {

    static String formatRateFromBits(long bits) {
        formatRate((bits / 8))
    }

    static String formatRate(double size) {
        String hrSize = null

        double b = size
        double k = size / 1024.0
        double m = ((size / 1024.0) / 1024.0)
        double g = (((size / 1024.0) / 1024.0) / 1024.0)
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0)

        if (t > 1) {
            hrSize = formatDecimal(t).concat(' TB/s')
        } else if (g > 1) {
            hrSize = formatDecimal(g).concat(' GB/s')
        } else if (m > 1) {
            hrSize = formatDecimal(m).concat(' MB/s')
        } else if (k > 1) {
            hrSize = formatDecimal(k).concat(' KB/s')
        } else {
            hrSize = formatDecimal(b).concat(' Bytes/s')
        }

        hrSize
    }

    static String formatDecimal(double value) {
        DecimalFormat dec = new DecimalFormat('0.00')
        dec.format(value)
    }

}
