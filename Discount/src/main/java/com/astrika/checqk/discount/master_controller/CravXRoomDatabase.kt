package com.astrika.checqk.discount.master_controller

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.astrika.checqk.discount.master_controller.source.ImageDTOConverter
import com.astrika.checqk.discount.master_controller.source.LongListDTOConverter
import com.astrika.checqk.discount.master_controller.source.daos.SystemValueMasterDao
import com.astrika.checqk.discount.master_controller.source.daos.discount.*
import com.astrika.checqk.discount.model.SystemValueMasterDTO
import com.astrika.checqk.discount.model.discount.*
import com.astrika.checqk.discount.utils.Constants

// Annotates class to be a Room Database with a table (entity) of the SubCategory class
@Database(
    entities = [SystemValueMasterDTO::class,
        OutletDiscountCategoryDTO::class, OutletDiscountMembershipPlanDTO::class,
        CravxCardsMembershipHolderDTO::class, HWMembershipHolderDTO::class, InHouseMembershipHolderDTO::class,
        OneDashboardMembershipHolderDTO1::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ImageDTOConverter::class,
    LongListDTOConverter::class
)
abstract class CravXRoomDatabase : RoomDatabase() {

    abstract fun systemValueMasterDao(): SystemValueMasterDao
/*

    abstract fun outLetTypeDao(): OutletTypeMasterDao
    abstract fun mealTypeMasterDao(): MealTypeMasterDao
    abstract fun memberDao(): FacilityMasterDao
    abstract fun skillDao(): CuisineMasterDao
    abstract fun statusDao(): FoodTypeMasterDao
    abstract fun socialMediaMasterDao(): SocialMediaMasterDao
    abstract fun knownLanguagesDao(): KnownLanguagesDao
    abstract fun designationDao(): DesignationDao
    abstract fun groupRoleStaffDao(): GroupRoleStaffDao

    // Address DAOs
    abstract fun countryDao(): CountryMasterDao
    abstract fun stateDao(): StateMasterDao
    abstract fun cityDao(): CityMasterDao
    abstract fun areaDao(): AreaMasterDao
    abstract fun pincodeDao(): PincodeMasterDao
*/

    // Discount
    abstract fun discountCategoryDao(): DiscountCategoryMasterDao
    abstract fun discountMembershipPlanDao(): DiscountMembershipPlanDao
    abstract fun cravxCardMembershipHolderDao(): CravxCardsMembershipHolderDao
    abstract fun hwMembershipHolderDao(): HWMembershipHolderDao
    abstract fun inHouseMembershipHolderDao(): InHouseMembershipHolderDao
/*
    abstract fun groupRoleDao(): GroupRoleDao
    abstract fun dashboardDrawerDao(): DashboardDrawerDao
*/


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same journalTime.
        @Volatile
        private var INSTANCE: CravXRoomDatabase? = null

        fun getDatabase(context: Context): CravXRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CravXRoomDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}