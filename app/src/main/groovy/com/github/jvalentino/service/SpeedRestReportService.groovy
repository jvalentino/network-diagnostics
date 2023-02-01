package com.github.jvalentino.service

import com.github.jvalentino.data.SpeedTestResult
import com.github.jvalentino.util.DateUtil
import com.github.jvalentino.util.FormatUtil
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

    void report(File file, SpeedTestResult report) {
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

}
