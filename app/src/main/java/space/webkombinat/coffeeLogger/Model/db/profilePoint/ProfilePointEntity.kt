package space.webkombinat.coffeeLogger.Model.db.profilePoint

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_point_data_table")
data class ProfilePointEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "point_id")
    val id: Int = 0,

    @ColumnInfo(name = "profile_id_point")
    val roast_profile_id: Int = 0,

    @ColumnInfo(name = "point_index")
    val point_index: Int,

    @ColumnInfo(name = "been_temp")
    val been_temp: Int
)
