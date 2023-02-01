package com.github.jvalentino.data

import groovy.transform.CompileDynamic

/**
 * Represents the result of a speed test
 * @author john.valentino
 */
@CompileDynamic
class SpeedTestResult {

    BigDecimal averageRate = new BigDecimal(0)
    BigDecimal minRate = new BigDecimal(Integer.MAX_VALUE)
    BigDecimal maxRate = new BigDecimal(Integer.MIN_VALUE)
    Date startDate
    Date endDate
    String error = ''

}
