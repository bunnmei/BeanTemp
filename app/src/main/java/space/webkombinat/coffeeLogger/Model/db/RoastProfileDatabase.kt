package space.webkombinat.coffeeLogger.Model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointDao
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointEntity
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileDao
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileEntity

@Database(
    entities = [RoastProfileEntity::class, ProfilePointEntity::class],
    version = 2
)
abstract class RoastProfileDatabase: RoomDatabase() {

    abstract fun roastDao(): RoastProfileDao
    abstract fun pointDao(): ProfilePointDao
}