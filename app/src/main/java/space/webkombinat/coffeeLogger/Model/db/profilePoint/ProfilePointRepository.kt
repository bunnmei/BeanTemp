package space.webkombinat.coffeeLogger.Model.db.profilePoint

class ProfilePointRepository(
    private val profilePointDao: ProfilePointDao
) {
    suspend fun insertPoint(point: ProfilePointEntity) {
        profilePointDao.insert(point)
    }

    suspend fun updatePoint(point: ProfilePointEntity) {
        profilePointDao.update(point)
    }

    suspend fun deletePoint(point: ProfilePointEntity) {
        profilePointDao.delete(point)
    }
}