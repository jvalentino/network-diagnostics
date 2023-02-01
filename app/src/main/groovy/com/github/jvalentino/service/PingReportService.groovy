package com.github.jvalentino.service

import com.github.jvalentino.util.DateUtil
import com.opencsv.CSVWriter
import groovy.transform.CompileDynamic

/**
 * Used for outputting speed stats to CSV
 * @author john.valentino
 */
@CompileDynamic
class PingReportService {

    static final String[] HEADERS = [
            'Start Date/Time',
            'Success',
            'Response (ms)',
    ]

    void report(File file, Date date, boolean success, Long response) {
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

}
