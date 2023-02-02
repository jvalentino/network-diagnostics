package com.github.jvalentino.data

import groovy.transform.CompileDynamic

/**
 * SEttings for generating a line chart on a time series
 * @author john.valentino
 */
@CompileDynamic
class ChartSettings {

    File file
    int width
    int height
    String title
    List<Date> dates
    List<BigDecimal> values
    String seriesName
    String valueAxisLabel

}
