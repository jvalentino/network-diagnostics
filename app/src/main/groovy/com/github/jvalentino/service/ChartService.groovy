package com.github.jvalentino.service

import com.github.jvalentino.data.ChartSettings
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.title.LegendTitle
import org.jfree.chart.title.TextTitle
import org.jfree.data.time.Second
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.xy.XYDataset

import java.awt.Font
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * Used for generated simple line chats on a time-based axis
 * @author john.valentino
 */
@CompileDynamic
@Slf4j
@SuppressWarnings([
        'DuplicateNumberLiteral',
        'DuplicateStringLiteral',
        'JavaIoPackageAccess',
        'UnnecessarySetter',
        'UnnecessaryGetter',
        'UnnecessaryPackageReference',
        'SimpleDateFormatMissingLocale',
])
class ChartService {

    void outputChart(ChartSettings settings) {
        JFreeChart chart = this.generateChart(settings)
        this.writeChartToFile(chart, settings.file, settings.width, settings.height)
    }

    protected JFreeChart generateChart(ChartSettings settings) {
        XYDataset dataset = this.generateDataset(settings.seriesName, settings.dates, settings.values)

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                settings.title,  // title
                'Date/Time',             // x-axis label
                settings.valueAxisLabel,   // y-axis label
                dataset,            // data
                true,               // create legend?
                true,               // generate tooltips?
                false               // generate URLs?
        )

        chart.setTitle(
                new TextTitle(settings.title,
                        new java.awt.Font('SansSerif', java.awt.Font.BOLD, 10)
                )
        )

        LegendTitle legend = chart.getLegend()
        Font labelFont = new Font('SansSerif', Font.BOLD, 10)
        legend.setItemFont(labelFont)

        XYPlot plot = (XYPlot) chart.getPlot()
        DateAxis axis = (DateAxis) plot.getDomainAxis()
        axis.setDateFormatOverride(new SimpleDateFormat('MM/dd HH:mm:ss Z'))

        plot.getDomainAxis().setLabelFont(labelFont)
        plot.getRangeAxis().setLabelFont(labelFont)

        plot.getDomainAxis().setTickLabelFont(labelFont)
        plot.getRangeAxis().setTickLabelFont(labelFont)

        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer()
        for (int i = 0; i < dataset.seriesCount; i++) {
            r.setSeriesShapesVisible(i, true)
        }

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis()
        DecimalFormat pctFormat = new DecimalFormat('#.##')
        rangeAxis.setNumberFormatOverride(pctFormat)

        chart
    }

    protected XYDataset generateDataset(String seriesName, List<Date> dates, List<BigDecimal> values) {
        TimeSeriesCollection dataset = new TimeSeriesCollection()

        TimeSeries series = new TimeSeries(seriesName)

        for (int i = 0; i < dates.size(); i++) {
            Second minute = new Second(dates.get(i))
            series.add(minute, values.get(i))
        }

        dataset.addSeries(series)

        dataset
    }

    protected void writeChartToFile(JFreeChart chart, File file, int width, int height) {
        ChartUtilities.writeChartAsJPEG(new FileOutputStream(file), chart, width, height)
    }

}
