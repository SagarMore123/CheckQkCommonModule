package com.astrika.checqk.discount.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.astrika.checqk.discount.R
import com.astrika.checqk.discount.adapters.DiscountDaysAdapter
import com.astrika.checqk.discount.adapters.MembershipTypeAdapter
import com.astrika.checqk.discount.adapters.OneDashboardMembershipHolderAdapter
import com.astrika.checqk.discount.databinding.FragmentOneDashboardBinding
import com.astrika.checqk.discount.model.SystemValueMasterDTO
import com.astrika.checqk.discount.model.discount.CorporateMembershipOneDashboardDTO
import com.astrika.checqk.discount.model.discount.OneDashboardMembershipHolderDTO
import com.astrika.checqk.discount.model.discount.OutletDiscountDetailsDTO
import com.astrika.checqk.discount.model.timings.DayDTO
import com.astrika.checqk.discount.model.timings.DiscountDaysTimingDTO
import com.astrika.checqk.discount.utils.AutocompleteViewModuleActivity
import com.astrika.checqk.discount.utils.Constants
import com.astrika.checqk.discount.utils.CustomProgressBarModule
import com.astrika.checqk.discount.utils.Utils
import com.astrika.checqk.discount.view.viewmodels.OneDashboardDiscountViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class OneDashboardFragment : Fragment(),
    OneDashboardMembershipHolderAdapter.OnItemClickListener,
    MembershipTypeAdapter.OnItemClickListener,
    DiscountDaysAdapter.OnAddTimeSlotItemClick,
    DiscountDaysAdapter.OnTimingItemEditClickListener,
    DiscountDaysAdapter.OnTimingItemDeleteClickListener {

    private lateinit var binding: FragmentOneDashboardBinding
    private lateinit var viewModel: OneDashboardDiscountViewModel

    private lateinit var discountMembershipHolderAdapter: OneDashboardMembershipHolderAdapter
    private lateinit var discountMembershipTypeAdapter: MembershipTypeAdapter
    private lateinit var discountDaysAdapter: DiscountDaysAdapter
    var progressBar = CustomProgressBarModule()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_one_dashboard,
            container,
            false
        )

        viewModel = Utils.obtainBaseObservable(
            requireActivity(),
            OneDashboardDiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        if (arguments?.getSerializable(Constants.CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY) != null) {
            viewModel.corporateMembershipOneDashboardDiscountMasterArrayList =
                arguments?.getSerializable(Constants.CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY) as ArrayList<CorporateMembershipOneDashboardDTO>
            if (!viewModel.corporateMembershipOneDashboardDiscountMasterArrayList.isNullOrEmpty()) {
                viewModel.corporateMembershipOneDashboardDTO =
                    viewModel.corporateMembershipOneDashboardDiscountMasterArrayList[0]
            }
        }

        if (arguments?.getSerializable(Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY) != null) {
            viewModel.outletDiscountDetailsArrayList =
                arguments?.getSerializable(Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY) as ArrayList<OutletDiscountDetailsDTO>
        }



        discountMembershipHolderAdapter =
            OneDashboardMembershipHolderAdapter(requireActivity(), this)
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


        viewModel.getmOnCorporateClick().observeChange(requireActivity(), Observer {

            val requestCode = Constants.CORPORATE_MEMBERSHIP_ONE_DASHBOARD_CODE
            val intent = Intent(activity, AutocompleteViewModuleActivity::class.java)
            intent.putExtra(Constants.SELECTED_DROPDOWN_ITEM_RESULT_CODE, requestCode)
            intent.putExtra(
                Constants.CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY,
                viewModel.corporateMembershipOneDashboardDiscountMasterArrayList
            )
            this.startActivityForResult(intent, requestCode)

        })

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


    override fun onItemClick(position: Int, dto: OneDashboardMembershipHolderDTO) {
        viewModel.membershipHolderSelection(position)
    }


    override fun onItemClick(position: Int, systemValueMasterDTO: SystemValueMasterDTO) {
        viewModel.membershipTypeSelection(position)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Constants.CORPORATE_MEMBERSHIP_ONE_DASHBOARD_CODE) {
            val corporateMembershipOneDashboardDTO =
                data?.getSerializableExtra(Constants.SELECTED_DROPDOWN_ITEM) as CorporateMembershipOneDashboardDTO

            if (corporateMembershipOneDashboardDTO != null) {
                viewModel.corporateMembershipOneDashboardDTO = corporateMembershipOneDashboardDTO
                viewModel.populateCorporateMasters()
            }
        }


    }


}