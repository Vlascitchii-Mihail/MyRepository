package com.vm.timemanager.data

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {

    //converts Data to int
    @TypeConverter fun fromDate(date: Date?) : Long? {
        return date?.time
    }

    //converts int to Data
    @TypeConverter fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

}