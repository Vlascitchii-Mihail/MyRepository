package com.vm.timemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey (autoGenerate = true) var id: Int = 0,
    val day: String? = null,
    var taskName: String = "",
    var startTime: Date? = null,
    val endTime: Date? = null,
    var taskDescription: String = ""
)
