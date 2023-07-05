package space.webkombinat.coffeeLogger.Model.db

import androidx.room.Embedded
import androidx.room.Relation
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointEntity
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileEntity

data class RoastAndPoint(
    @Embedded
    var profile: RoastProfileEntity,
    @Relation(parentColumn = "profile_id", entityColumn = "profile_id_point")
    var point: List<ProfilePointEntity> = emptyList()
)
