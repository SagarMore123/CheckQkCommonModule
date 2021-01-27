package com.astrika.checqk.commonmodules.discount.viewmodels

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.astrika.checqk.commonmodules.master_controller.source.MasterRepository
import com.astrika.checqk.commonmodules.master_controller.source.daos.SystemValueMasterDao
import com.astrika.checqk.commonmodules.model.SystemValueMasterDTO
import com.astrika.checqk.commonmodules.model.discount.HWMembershipHolderDTO
import com.astrika.checqk.commonmodules.model.discount.OutletDiscountDetailsDTO
import com.astrika.checqk.commonmodules.model.timings.DayDTO
import com.astrika.checqk.commonmodules.model.timings.DaysEnum
import com.astrika.checqk.commonmodules.model.timings.DiscountDaysTimingDTO
import com.astrika.checqk.commonmodules.model.timings.DiscountEnum
import com.astrika.checqk.commonmodules.network.network_utils.IDataSourceCallback
import com.astrika.checqk.commonmodules.source.UserRepository
import com.astrika.checqk.commonmodules.utils.Constants
import com.astrika.checqk.commonmodules.utils.GenericBaseObservable
import com.astrika.checqk.commonmodules.utils.Utils

class HWDiscountViewModel(
    var activity: Activity,
    var application: Application,
    owner: LifecycleOwner?,
    view: View?,
    private var masterRepository: MasterRepository,
    private var userRepository: UserRepository
) : GenericBaseObservable(application, owner, view) {

    private var outLetId = 0L
    private var selectedMembershipHolderId: Long = 0
    private var selectedMemberShipTypeDTO = SystemValueMasterDTO()
    var showProgressBar = MutableLiveData<Boolean>()
    private var sharedPreferences: SharedPreferences = Constants.getSharedPreferences(application)


    // Discount Membership Holder -> DIWALI SPECIAL, VIP TRAVELLERS
    private var discountMembershipHolderArrayList = ArrayList<HWMembershipHolderDTO>()
    var discountMembershipHolderListMutableLiveData = MutableLiveData<List<HWMembershipHolderDTO>>()

    // Discount Membership Type -> Regular, VIP
    private var tempDiscountMembershipTypeArrayList = ArrayList<SystemValueMasterDTO>()
    private var discountMembershipTypeArrayList = ArrayList<SystemValueMasterDTO>()
    var discountMembershipTypeListMutableLiveData = MutableLiveData<List<SystemValueMasterDTO>>()

    // Discount Days
    private var discountDayTimingsArrayList = ArrayList<DayDTO>()
    var discountDayTimingsListMutableLiveData = MutableLiveData<List<DayDTO>>()

    var outletDiscountDetailsArrayList = ArrayList<OutletDiscountDetailsDTO>()
    var outletDiscountDetailsDTO = OutletDiscountDetailsDTO()

    init {
        outLetId =
            Constants.decrypt(sharedPreferences.getString(Constants.OUTLET_ID, ""))?.toLong() ?: 0L
        initiateDays()
        populateMasters()
    }

    private fun populateMasters() {


        // Cravx Cards Discount Membership Holder -> DIWALI SPECIAL, VIP TRAVELLERS
        masterRepository.fetchHWDiscountMembershipHolderMasterDataLocal(object :
            IDataSourceCallback<List<HWMembershipHolderDTO>> {

            override fun onDataFound(data: List<HWMembershipHolderDTO>) {
                discountMembershipHolderArrayList = data as ArrayList<HWMembershipHolderDTO>
                discountMembershipHolderListMutableLiveData.value =
                    discountMembershipHolderArrayList


                // Discount Membership Type -> Regular, VIP
                discountMembershipTypeArrayList.clear()

                masterRepository.fetchSystemMasterValueByNameLocal(
                    SystemValueMasterDao.MEMBERSHIP_TYPE,
                    object : IDataSourceCallback<List<SystemValueMasterDTO>> {

                        override fun onDataFound(data: List<SystemValueMasterDTO>) {
                            discountMembershipTypeArrayList =
                                data as ArrayList<SystemValueMasterDTO>

                            if (!discountMembershipHolderArrayList.isNullOrEmpty()) {
                                membershipHolderSelection(0)
                            }

                        }

                        override fun onError(error: String) {
//                                            getmSnackbar().value = error
                        }
                    })


            }

            override fun onError(error: String) {
//                getmSnackbar().value = error
            }
        })


    }

    private fun initiateDays() {

        discountDayTimingsArrayList.clear()
        discountDayTimingsListMutableLiveData.value = arrayListOf()
        for (item in DaysEnum.values()) {

            val dayDTO = DayDTO()
            dayDTO.dayId = item.id.toLong()
            dayDTO.dayName = item.value
            dayDTO.dayIsCheckedOrClosed = true

            // discount
            dayDTO.allDaySameDiscountFlag = true

            val discountDaysTimingDTO = DiscountDaysTimingDTO()
            discountDaysTimingDTO.weekDay = item.id.toLong()
            discountDaysTimingDTO.allDaySameDiscountFlag = true
            dayDTO.discountDayAndTimings.add(discountDaysTimingDTO)
            discountDayTimingsArrayList.add(dayDTO)
        }

        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList
    }

    private fun populateDiscountData(outletDiscountDetailsDTO: OutletDiscountDetailsDTO) {

        //Response timings loop
        for (times in outletDiscountDetailsDTO.discountTimingList) {

            for ((i, item) in discountDayTimingsArrayList.withIndex()) { // Setting data to UI loop

                if (item.dayId == times.weekDay && !item.discountDayAndTimings.contains(times)) { // Check for same day
                    item.allDaySameDiscountFlag = times.allDaySameDiscountFlag
                    item.assuredDiscountString = times.assuredDiscount.toString()

                    times.assuredDiscountString = times.assuredDiscount.toString()
                    times.discountApplicableString = times.discountApplicable.toString()
                    item.discountDayAndTimings.add(times)
                }
            }
        }

        // Removing empty timing content only if list contains multiple timings data
        for (item in discountDayTimingsArrayList) {
            if (item.discountDayAndTimings.size > 1) {
                for ((i, item2) in item.discountDayAndTimings.withIndex()) {
                    if (item2.assuredDiscountString.isNullOrBlank()
                        && item2.discountApplicableString.isNullOrBlank()
                        && item2.complimentaryApplicable.isNullOrBlank()
                        && item2.terms.isNullOrBlank()
                    ) {
                        item.discountDayAndTimings.removeAt(i)
                        break
                    }
                }
            }
        }

        // Rendering UI Week Timing list
        discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList

    }

    private fun updateDiscount() {
        initiateDays()

        for (outletDetails in outletDiscountDetailsArrayList) {

            if (outletDetails.cardId == selectedMembershipHolderId
                && outletDetails.userMembershipTypeId == selectedMemberShipTypeDTO.serialId
            ) {
                outletDiscountDetailsDTO = outletDetails
                populateDiscountData(outletDiscountDetailsDTO)
                break
            }

        }

    }


    fun membershipHolderSelection(position: Int) {

        if (selectedMembershipHolderId != discountMembershipHolderArrayList[position].hwDiscountCardId) {
            for ((i, item) in discountMembershipHolderArrayList.withIndex()) {

                if (i == position) {
                    item.selected = true
                    selectedMembershipHolderId =
                        item.hwDiscountCardId // Membership Holder Selection

                    tempDiscountMembershipTypeArrayList.clear()
                    discountMembershipTypeListMutableLiveData.value = arrayListOf()
                    for (userMembershipTypeId in item.userMembershipTypeId ?: arrayListOf()) {

                        for (localUserMembershipType in discountMembershipTypeArrayList) {

                            if (localUserMembershipType.serialId == userMembershipTypeId) {
                                tempDiscountMembershipTypeArrayList.add(localUserMembershipType)
                                break
                            }
                        }


                    }

                    // checking for Membership Type is already selected or not
                    if (!tempDiscountMembershipTypeArrayList.isNullOrEmpty()) {
                        // Membership Type Selection
                        selectedMemberShipTypeDTO = SystemValueMasterDTO()
                        membershipTypeSelection(0)
                    }


                } else {
                    item.selected = false
                }

            }
            discountMembershipHolderListMutableLiveData.value = discountMembershipHolderArrayList
            updateDiscount()
        }
    }


    fun membershipTypeSelection(position: Int) {

        if (selectedMemberShipTypeDTO.serialId != tempDiscountMembershipTypeArrayList[position].serialId) {
            for ((i, item) in tempDiscountMembershipTypeArrayList.withIndex()) {

                if (i == position) {
                    item.selected = true
//                    selectedMembershipTypeId = item.serialId
                    selectedMemberShipTypeDTO = item
                } else {
                    item.selected = false
                }
            }
            discountMembershipTypeListMutableLiveData.value = tempDiscountMembershipTypeArrayList
            updateDiscount()
        }
    }


    fun onSaveDiscountClick() {

        if (Utils.commonDiscountValidations(
                outLetId,
                outletDiscountDetailsDTO,
                selectedMembershipHolderId,
                selectedMemberShipTypeDTO,
                discountDayTimingsArrayList,
                discountDayTimingsListMutableLiveData
            )
        ) {

            outletDiscountDetailsDTO.outletDiscountCategory = DiscountEnum.HW_CARD.name
            showProgressBar.value = true
            userRepository.saveOutletDiscountDetails(outletDiscountDetailsDTO,
                object : IDataSourceCallback<String> {

                    override fun onDataFound(data: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = data
                    }

                    override fun onError(error: String) {
                        showProgressBar.value = false
                        getmSnackbar().value = error
                    }
                })
        }

    }

}