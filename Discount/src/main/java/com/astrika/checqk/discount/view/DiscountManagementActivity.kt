package com.astrika.checqk.discount.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.astrika.checqk.discount.R
import com.astrika.checqk.discount.adapters.DiscountCategoriesAdapter
import com.astrika.checqk.discount.databinding.ActivityDiscountManagementBinding
import com.astrika.checqk.discount.model.discount.DiscountCategoryDTO
import com.astrika.checqk.discount.network.NetworkController
import com.astrika.checqk.discount.utils.Constants
import com.astrika.checqk.discount.utils.Utils
import com.astrika.checqk.discount.view.viewmodels.DiscountViewModel
import com.astrika.checqk.discount.view.viewmodels.DiscountViewModel.Companion as DiscountViewModel1

class DiscountManagementActivity : AppCompatActivity(),
    DiscountCategoriesAdapter.OnItemClickListener {

    private lateinit var binding: ActivityDiscountManagementBinding
    lateinit var navController: NavController
    private lateinit var discountViewModel: DiscountViewModel
    private lateinit var discountCategoriesAdapter: DiscountCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discount_management)
//        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)


        discountViewModel = Utils.obtainBaseObservable(
            this,
            DiscountViewModel::class.java,
            this,
            binding.root
        )!!
        binding.discountViewModel = discountViewModel
        binding.lifecycleOwner = this

        discountCategoriesAdapter = DiscountCategoriesAdapter(this, this)
        binding.discountCategoryRecyclerView.adapter = discountCategoriesAdapter
        observer()

    }


    override fun onResume() {
        super.onResume()

        val bundle = intent?.extras
        if (bundle?.containsKey("OutletId") != null) {
            if (bundle.getLong("OutletId") != null) {
//            val outletId = intent?.getLongExtra("OutletId", 0)
                DiscountViewModel1.outletId.value = bundle.getLong("OutletId")
            }

        }
    }

    private fun observer() {

        discountViewModel.discountCategoryListMutableLiveData.observe(this, Observer {
            discountCategoriesAdapter.list = it
        })

        discountViewModel.observableBoolean.observe(this, Observer {
            if (it) {
                val bundle = Bundle()
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.membershipDiscountArrayList
                )
                navController.navigate(R.id.MembershipDiscountFragment, bundle)

            }
        })

        DiscountViewModel1.outletId.observe(this, {
            if (it != null && it != 0L) {
                discountViewModel.sharedPreferences.edit()?.putString(
                    Constants.ACCESS_TOKEN,
                    Constants.encrypt(NetworkController.accessToken)
                )?.apply()
                discountViewModel.sharedPreferences.edit()?.putString(
                    Constants.OUTLET_ID,
                    Constants.encrypt(it.toString())
                )?.apply()
                discountViewModel.sharedPreferences.edit()?.putString(
                    Constants.PRODUCT_ID,
                    Constants.encrypt(DiscountViewModel1.productId.toString())
                )?.apply()
                discountViewModel.populateOutletDiscountDetailsList()
            }
        })


    }

    companion object {
        fun abc() {

        }
    }

    fun xyz() {

    }

    override fun onItemClick(position: Int, discountCategoryDTO: DiscountCategoryDTO) {
        discountViewModel.categorySelection(position, discountCategoryDTO)
        displayFragment(position)
    }

    private fun displayFragment(position: Int) {

        val bundle = Bundle()
        when (position) {

            0 -> {

                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.membershipDiscountArrayList
                )
                navController.navigate(R.id.MembershipDiscountFragment, bundle)
            }
            1 -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.cravxCardDiscountArrayList
                )
                navController.navigate(R.id.CravXCardDiscountFragment, bundle)
            }
            2 -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.hwCardDiscountArrayList
                )
                navController.navigate(R.id.HungryWungryDiscountFragment, bundle)
            }
            3 -> {
                bundle.putSerializable(
                    Constants.CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY,
                    discountViewModel.oneDashboardDiscountMastersArrayList
                )
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.oneDashboardDiscountArrayList
                )

                navController.navigate(R.id.OneDashboardFragment, bundle)
            }
            4 -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.inHouseDiscountArrayList
                )
                navController.navigate(R.id.InHouseCardsFragment, bundle)
            }

            else -> {
                bundle.putSerializable(
                    Constants.OUTLET_DISCOUNT_DETAILS_DTO_KEY,
                    discountViewModel.membershipDiscountArrayList
                )
                navController.navigate(R.id.MembershipDiscountFragment, bundle)
            }
        }

    }

    override fun onBackPressed() {
//        Constants.changeActivity<DashboardActivity>(this)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}