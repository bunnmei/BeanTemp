package space.webkombinat.coffeeLogger.Model.db.roastProfile

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import space.webkombinat.coffeeLogger.Model.db.RoastAndPoint

@Dao
interface RoastProfileDao {

    @Transaction
    @Query("SELECT * FROM roast_profile_data_table WHERE profile_id = :id")
    fun roastAndPoint(id: Int): Flow<List<RoastAndPoint>>

    @Query("SELECT * FROM roast_profile_data_table")
    fun allProfile(): Flow<List<RoastProfileEntity>>

    @Insert
    suspend fun insert(profile: RoastProfileEntity): Long

    @Update
    suspend fun update(profile: RoastProfileEntity)

    @Delete
    suspend fun delete(profile: RoastProfileEntity)
}