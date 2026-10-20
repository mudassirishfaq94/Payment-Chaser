package com.paymentchaser.app.di

import android.content.Context
import androidx.room.Room
import com.paymentchaser.app.data.local.ClientDao
import com.paymentchaser.app.data.local.PaymentDao
import com.paymentchaser.app.data.local.PaymentDatabase
import com.paymentchaser.app.data.local.ReminderLogDao
import com.paymentchaser.app.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePaymentDatabase(@ApplicationContext context: Context): PaymentDatabase {
        return Room.databaseBuilder(
            context,
            PaymentDatabase::class.java,
            PaymentDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePaymentDao(database: PaymentDatabase): PaymentDao {
        return database.paymentDao()
    }

    @Provides
    fun provideClientDao(database: PaymentDatabase): ClientDao {
        return database.clientDao()
    }

    @Provides
    fun provideReminderLogDao(database: PaymentDatabase): ReminderLogDao {
        return database.reminderLogDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSyncRepository(
        syncRepositoryImpl: SyncRepositoryImpl
    ): SyncRepository
}
