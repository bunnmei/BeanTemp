package space.webkombinat.coffeeLogger.Model.db.profilePoint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface ProfilePointDao {

    @Insert
    suspend fun insert(point: ProfilePointEntity)

    @Update
    suspend fun update(point: ProfilePointEntity)

    @Delete
    suspend fun delete(point: ProfilePointEntity)

}