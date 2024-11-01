package com.self.todo.di

import android.content.Context
import androidx.room.Room
import com.self.todo.data.ToDoDataBase
import com.self.todo.util.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context = context,
        klass = ToDoDataBase::class.java,
        name = DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(dataBase: ToDoDataBase) = dataBase.toDoDao()

}