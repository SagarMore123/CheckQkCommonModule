package com.astrika.checqk.discount.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.commonmodules.discount.viewmodels.CravXCardsDiscountViewModel
import com.astrika.checqk.commonmodules.model.SystemValueMasterDTO
import com.astrika.checqk.commonmodules.model.discount.CravxCardsMembershipHolderDTO
import com.astrika.checqk.commonmodules.model.discount.OutletDiscountDetailsDTO
import com.astrika.checqk.commonmodules.model.timings.DayDTO
import com.astrika.checqk.commonmodules.model.timings.DiscountDaysTimingDTO
import com.astrika.checqk.commonmodules.utils.Constants
import com.astrika.checqk.commonmodules.utils.CustomProgressBarModule
import com.astrika.checqk.commonmodules.utils.Utils
import com.astrika.checqk.discount.R
import com.astrika.checqk.discount.adapters.CravxCardsMembershipHolderAdapter
import com.astrika.checqk.discount.adapters.DiscountDaysAdapter
import com.astrika.checqk.discount.adapters.MembershipTypeAdapter
import com.astrika.checqk.discount.databinding.FragmentCravxCardDiscountBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CravXCardDiscountFragment : Fragment(),
    CravxCardsMembershipHolderAdapter.OnItemClickListener,
    MembershipTypeAdapter.OnItemClickListener,
    DiscountDaysAdapter.OnAddTimeSlotItemClick,
    DiscountDaysAdapter.OnTimingItemEditClickListener,
    DiscountDaysAdapter.OnTimingItemDeleteClickListener {


    private lateinit var binding: FragmentCravxCardDiscountBinding
    private lateinit var viewModel: CravXCardsDiscountViewModel

    private lateinit var discountMembershipHolderAdapter: CravxCardsMembershipHolderAdapter
    private lateinit var discountMembershipTypeAdapter: MembershipTypeAdapter
    private lateinit var discountDaysAdapter: DiscountDaysAdapter
    var progressBar = CustomProgressBarModule()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cravx_card_discount,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            CravXCardsDiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        discountMembershipHolderAdapter = CravxCardsMembershipHolderAdapter(requireActivity(), this)
        binding.discountMembershipHolderRecyclerView.adapter = discountMembershipHolderAdapter

        discountMembershipTypeAdapter = MembershipTypeAdapter(requireActivity(), this)
        binding.discountMembershipTypeRecyclerView.adapter = discountMembershipTypeAdapter

        discountDaysAdapter = DiscountDaysAdapter(requireActivity(), this, this, this)
        binding.discountDaysRecyclerView.adapter = discountDaysAdapter

        observer()

        return binding.root
    }

    fun observer() {

        viewModel.showProgressBar.observe(requireActivity(), Observer {
            if (it)
                progressBar.show(requireActivity(), "Please Wait...")
            else
                progressBar.dialog?.dismiss()
        })


        viewModel.discountMembershipHolderListMutableLiveData.observe(
            requireActivity(), Observer {
                discountMembershipHolderAdapter.submitList(it)
            })

        viewModel.discountMembershipTypeListMutableLiveData.observe(
            requireActivity(), Observer {
                discountMembershipTypeAdapter.submitList(it)
            })

        viewModel.discountDayTimingsListMutableLiveData.observe(
            requireActivity(), Observer {
                discountDaysAdapter.submitList(it)
            })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getSerializable(Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY) != null) {
            viewModel.outletDiscountDetailsArrayList =
                arguments?.getSerializable(Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY) as ArrayList<OutletDiscountDetailsDTO>
        }
    }

    override fun onAddTimeSlotItemClick(position: Int, dayDTO: DayDTO) {

    }

    override fun onTimingDeleteItemClick(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {

    }

    override fun onTimingItemEditClick(
        position: Int,
        mainContainerPosition: Int,
        discountDaysTimingDTO: DiscountDaysTimingDTO
    ) {
    }


    override fun onItemClick(position: Int, dto: CravxCardsMembershipHolderDTO) {
        viewModel.membershipHolderSelection(position)
    }


    override fun onItemClick(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        viewModel.membershipTypeSelection(position)
    }

}