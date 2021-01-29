package com.astrika.checqk.discount.master_controller.sync

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.discount.master_controller.source.MasterRepository
import com.astrika.checqk.discount.model.CommonListingDTO
import com.astrika.checqk.discount.model.LoginDTO
import com.astrika.checqk.discount.model.SystemValueMasterDTO
import com.astrika.checqk.discount.model.discount.*
import com.astrika.checqk.discount.network.network_utils.IDataSourceCallback
import com.astrika.checqk.discount.utils.Constants

class SyncData {

    companion object {
        @Volatile
        var masterRepository: MasterRepository? = null

        private val commonListingDTO = CommonListingDTO()

        /**
         * Performs the network request for updated weather, parses the JSON from that request, and
         * inserts the new weather information into our ContentProvider. Will notify the user that new
         * weather has been loaded if the user hasn't been notified of the weather within the last day
         * AND they haven't disabled notifications in the preferences screen.
         *
         * @param context Used to access utility methods and the ContentResolver
         */

        var signUpMasterServicesCounts = 11
        var signUpMasterServices = MutableLiveData<Int>(0)


        var loginMasterServices = 6
        var loginMasterServicesCounts = MutableLiveData<Int>(0)

        @Synchronized
        fun splashSyncData(context: Context?) {
//            context?.let { Constants.clearSharedPrefs(it) }
            masterRepository = MasterRepository.getInstance(context)

/*
            */
//--------  Login By Visitor section -----------

            try {

                masterRepository?.loginMasters(LoginDTO(), object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {

                        syncData(context)

                    }

                    override fun onDataNotFound() {
                        loginMasterServicesCounts.value = -1
                    }

                    override fun onError(error: String) {
                        loginMasterServicesCounts.value = -1
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                loginMasterServicesCounts.value = -1
            }


        }


        @Synchronized
        fun syncData(context: Context?) {
            masterRepository = MasterRepository.getInstance(context)

            /*--------SVM section -----------*/

            try {

                masterRepository?.fetchSystemValueMasterDataRemote(object :
                    IDataSourceCallback<List<SystemValueMasterDTO>> {

                    override fun onDataFound(data: List<SystemValueMasterDTO>) {
                        loginMasterServicesCounts.value =
                            loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onDataNotFound() {
                        loginMasterServicesCounts.value =
                            loginMasterServicesCounts.value?.plus(1)
                    }

                    override fun onError(error: String) {
                        loginMasterServicesCounts.value =
                            loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Discount Category section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.DISCOUNT_CATEGORY_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchDiscountCategoryMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<OutletDiscountCategoryDTO>> {

                        override fun onDataFound(data: List<OutletDiscountCategoryDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*--------  Discount Membership Plan section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.DISCOUNT_MEMBERSHIP_PLAN_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchDiscountMembershipPlanMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>> {

                        override fun onDataFound(data: List<OutletDiscountMembershipPlanDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*-------- Cravx Cards Discount Membership Holder section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchCravxCardDiscountMembershipHolderMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<CravxCardsMembershipHolderDTO>> {

                        override fun onDataFound(data: List<CravxCardsMembershipHolderDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)

            }

            /*-------- HW Discount Membership Holder section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.HW_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchHWDiscountMembershipHolderMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<HWMembershipHolderDTO>> {

                        override fun onDataFound(data: List<HWMembershipHolderDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

            /*-------- In-House Discount Membership Holder section -----------*/
            try {

                commonListingDTO.defaultSort.sortField =
                    Constants.IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD
                commonListingDTO.defaultSort.sortOrder = Constants.SORT_ORDER

                masterRepository?.fetchInHouseDiscountMembershipHolderMasterDataRemote(
                    commonListingDTO,
                    object :
                        IDataSourceCallback<List<InHouseMembershipHolderDTO>> {

                        override fun onDataFound(data: List<InHouseMembershipHolderDTO>) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }

                        override fun onError(error: String) {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataNotFound() {
                            loginMasterServicesCounts.value =
                                loginMasterServicesCounts.value?.plus(1)
                        }
                    })

            } catch (e: Exception) {
                loginMasterServicesCounts.value =
                    loginMasterServicesCounts.value?.plus(1)
            }

        }
    }
}