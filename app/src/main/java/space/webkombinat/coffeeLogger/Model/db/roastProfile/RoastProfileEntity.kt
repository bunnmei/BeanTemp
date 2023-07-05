package space.webkombinat.coffeeLogger.Model.db.roastProfile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roast_profile_data_table")
data class RoastProfileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profile_id")
    val id: Int = 0,

    @ColumnInfo(name = "profile_name")
    var name: String?,

    @ColumnInfo(name = "profile_description")
    var description: String?,

    @ColumnInfo(name = "first_crack")
    var first_crack: Float?,

    @ColumnInfo(name = "second_crack")
    var second_crack: Float?,

    @ColumnInfo(name = "created_at")
    var create_at: String,

    @ColumnInfo(name = "created_position")
    var created_position: Int
)