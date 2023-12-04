package de.fhe.ai.colivingpilot.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "task_assigned_users",
    primaryKeys = ["user_id", "task_id"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskAssignedUser(
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "task_id") val taskId: String
)
