package com.bignerdranch.android.pennydrop.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.bignerdranch.android.pennydrop.types.Player

@Entity(
    tableName = "game_status",

    //The list of Primary Key column names.
    //If you would like to define an auto generated primary key, you can use
    // PrimaryKey annotation on the field with PrimaryKey.autoGenerate() set to true.
    primaryKeys = ["gameId", "playerId"],

    //List of ForeignKey constraints on this entity.
    //Returns: The list of ForeignKey constraints on this entity.
    foreignKeys = [

        /**
         * @param entity - the parent Entity to reference.
         * @param parentColumns - The list of column names in the PARENT Entity.
         * Number of columns must match the number of columns specified in childColumns().
         * @param childColumns - The list of column names in the CURRENT Entity.
         * Number of columns must match the number of columns specified in parentColumns().
         */
        ForeignKey(
            entity = Game::class,
            parentColumns = ["gameId"],
            childColumns = ["gameId"]
        ),
        ForeignKey(
            entity = Player::class,
            parentColumns = ["playerId"],
            childColumns = ["playerId"]
        )
    ]
)

data class GameStatus(
    val gameId: Long,

    //Помогает ускорить поиск при использовании GameStatus в качестве соединения
    @ColumnInfo(index = true) val playerId: Long,
    val gamePlayerNumber: Int,
    val isRolling: Boolean = false,
    val pennies: Int = Player.defaultPennyCount
) {
}