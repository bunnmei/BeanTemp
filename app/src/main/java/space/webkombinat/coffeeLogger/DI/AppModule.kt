package space.webkombinat.coffeeLogger.DI

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import space.webkombinat.coffeeLogger.Model.data.UserPreferencesRepository

import space.webkombinat.coffeeLogger.Model.db.RoastProfileDatabase
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointDao
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointRepository
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileDao
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileRepository
import space.webkombinat.coffeeLogger.ViewModel.LoggedDetailScreenVM
import space.webkombinat.coffeeLogger.ViewModel.LoggedListScreenVM
import space.webkombinat.coffeeLogger.ViewModel.LoggingChartAndOperateScreenVM
import space.webkombinat.coffeeLogger.ViewModel.MainActivityVM
import space.webkombinat.coffeeLogger.ViewModel.SettingsScreenVM
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

//   Pref
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("settings")
        }
    )

//  DB
    @Provides
    @Singleton
    fun provideRoastProfileDatabase(app: Application): RoastProfileDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = RoastProfileDatabase::class.java,
            name = "Roast_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRoastDao(database: RoastProfileDatabase): RoastProfileDao {
        return database.roastDao()
    }

    @Provides
    fun provideProfileRepository(dao: RoastProfileDao): RoastProfileRepository {
        return RoastProfileRepository(dao)
    }

    @Provides
    fun providePointDao(database: RoastProfileDatabase): ProfilePointDao {
        return database.pointDao()
    }
//
    @Provides
    fun providePointRepository(dao: ProfilePointDao): ProfilePointRepository {
        return ProfilePointRepository(dao)
    }

    @Provides
    @Singleton
    fun provideMainActivity(
        userPreferencesRepository: UserPreferencesRepository
    ): MainActivityVM {
        return MainActivityVM(userPreferencesRepository)
    }

    @Provides
    @Singleton
    fun provideLoggedListVM(
        roastRepo:RoastProfileRepository,
    ): LoggedListScreenVM {
        return LoggedListScreenVM(roastRepo = roastRepo)
    }
    @Provides
    @Singleton
    fun provideLoggedDetailVM(
        roastRepo:RoastProfileRepository,
    ): LoggedDetailScreenVM {
        return LoggedDetailScreenVM(repo = roastRepo)
    }

    @Provides
    @Singleton
    fun provideSettingsVM(
        userPreferencesRepository: UserPreferencesRepository
    ): SettingsScreenVM {
        return SettingsScreenVM(userPreferencesRepository)
    }


    @Provides
    @Singleton
    fun provideLoggedChartAndOperateScreenVM(
        roastRepo:RoastProfileRepository,
        pointRepo:ProfilePointRepository
    ): LoggingChartAndOperateScreenVM {
        return LoggingChartAndOperateScreenVM(
            roastRepo = roastRepo,
            pointRepo = pointRepo
        )
    }
}