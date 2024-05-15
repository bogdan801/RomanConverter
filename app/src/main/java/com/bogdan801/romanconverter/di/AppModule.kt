package com.bogdan801.romanconverter.di

import android.content.Context
import com.bogdan801.romanconverter.data.local_db.realm.objects.Record
import com.bogdan801.romanconverter.data.repository.RepositoryImpl
import com.bogdan801.romanconverter.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

/*    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, Database::class.java, "database")
            //.fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDao(db :Database) = db.dbDao*/

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): Realm {
        return Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Record::class
                )
            )
        )
    }

    @Provides
    @Singleton
    fun provideRepository(realm: Realm): Repository {
        return RepositoryImpl(realm)
    }
}