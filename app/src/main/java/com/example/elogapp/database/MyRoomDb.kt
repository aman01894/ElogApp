package com.example.elogapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.elogapp.database.dao.*
import com.example.elogapp.util.DataConverter

@Database(entities=[UserDetails::class,DocumentGallery::class, ShippingDocs::class,
    DriverDutyInfo::class, Outbound::class, Violation::class,
    EventLog::class, DropdownMaster::class, UnidentifiedEvents::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)

abstract class MyRoomDb : RoomDatabase() {
    abstract fun getDriverDutyDao(): DriverDutyInfoDao
    abstract fun getUserDao(): UserDao
    abstract fun getDocGalleryDao(): DocumentGalleryDao
    abstract fun getEventLogDao(): EventLogDao
    abstract fun getDropdownMasterDao(): DropdownMasterDao
    abstract fun getOutboundDao(): OutboundDao
    abstract fun getViolationDao(): ViolationDao
    abstract fun getShippingDocsDao(): ShippingDocsDao
    abstract fun getUnidentifiedEventDao(): UnidentifiedEventDao

    companion object {
        @Volatile  var instance: MyRoomDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                MyRoomDb::class.java, "Mydb.db")
//                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()//Comment this if do not need clear db @ upgrade
                .build()
    }
}