package com.github.jvalentino.service

import com.github.jvalentino.data.ChartSettings
import com.github.jvalentino.data.SpeedTestResult
import com.github.jvalentino.util.DateUtil
import com.github.jvalentino.util.FormatUtil
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import groovy.transform.CompileDynamic

/**
 * Used for outputting speed stats to CSV
 * @author john.valentino
 */
@CompileDynamic
class SpeedRestReportService {

    static final String[] HEADERS = [
            'Start Date/Time',
            'End Date/Time',
            'Average bit/s Down',
            'Min bit/s Down',
            'Max bit/s Down',
            'Error',
    ]

    ChartService chartService = new ChartService()

    void report(File csvFile, File chartFile, SpeedTestResult report) {
        this.outputCsv(csvFile, report)
        this.outputChart(csvFile, chartFile)
    }

    protected void outputCsv(File file, SpeedTestResult report) {
        boolean writeHeaders = false
        if (!file.exists()) {
            writeHeaders = true
        }
        CSVWriter writer = new CSVWriter(new FileWriter(file, true))

        String[] entry = [
                DateUtil.fromDate(report.startDate),
                DateUtil.fromDate(report.endDate),
                FormatUtil.formatDecimal(report.averageRate.toDouble()),
                FormatUtil.formatDecimal(report.minRate.toDouble()),
                FormatUtil.formatDecimal(report.maxRate.toDouble()),
                report.error,
        ]

        if (writeHeaders) {
            writer.writeNext(HEADERS, false)
        }

        writer.writeNext(entry, false)

        writer.close()
    }

    protected void outputChart(File csvFile, File chartFile) {
        List<Date> dates = []
        List<BigDecimal> values = []

        FileReader filereader = new FileReader(csvFile)

        // create csvReader object passing
        // file reader as a parameter
        CSVReader csvReader = new CSVReader(filereader)
        String[] nextRecord

        // we are going to read data line by line
        int line = 0
        while ((nextRecord = csvReader.readNext()) != null) {
            line++
            if (line == 1) {
                continue
            }

            int column = 0
            for (String cell : nextRecord) {
                switch (column) {
                    case 0:
                        dates.add(DateUtil.toDate(cell))
                        break
                    case 2:
                        values.add(cell.toBigDecimal())
                        break
                }
                column++
            }
        }

        ChartSettings settings = new ChartSettings(dates:dates, values:values)
        settings.with {
            file = chartFile
            width = 600
            height = 400
            title = 'Download Speed'
            seriesName = 'Download'
            valueAxisLabel = 'bit/s'
        }

        chartService.outputChart(settings)
    }

}
