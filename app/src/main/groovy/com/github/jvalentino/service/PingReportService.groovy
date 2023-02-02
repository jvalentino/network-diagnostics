package com.github.jvalentino.service

import com.github.jvalentino.data.ChartSettings
import com.github.jvalentino.util.DateUtil
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import groovy.transform.CompileDynamic

/**
 * Used for outputting speed stats to CSV
 * @author john.valentino
 */
@CompileDynamic
@SuppressWarnings([
        'JavaIoPackageAccess'
])
class PingReportService {

    static final String[] HEADERS = [
            'Start Date/Time',
            'Success',
            'Response (ms)',
    ]

    ChartService chartService = new ChartService()

    void report(File csvFile, File chartFile, Date date, boolean success, Long response) {
        this.outputCsv(csvFile, date, success, response)
        this.outputChart(csvFile, chartFile)
    }

    protected void outputCsv(File file, Date date, boolean success, Long response) {
        boolean writeHeaders = false
        if (!file.exists()) {
            writeHeaders = true
        }
        CSVWriter writer = new CSVWriter(new FileWriter(file, true))

        String[] entry = [
                DateUtil.fromDate(date),
                success,
                response,
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
                        values.add(new BigDecimal(cell.toLong()))
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
            title = 'Ping'
            seriesName = 'Ping Response'
            valueAxisLabel = 'ms'
        }

        chartService.outputChart(settings)
    }

}
