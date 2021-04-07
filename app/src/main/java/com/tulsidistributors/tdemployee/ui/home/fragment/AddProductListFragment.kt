package com.tulsidistributors.tdemployee.ui.home.fragment

import RecyclerViewSwipeCallback
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.designurway.tdapplication.adapter.SelectedProductAdapter
import com.designurway.tdapplication.model.SelectedProductModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.*
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductData
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductModel
import com.tulsidistributors.tdemployee.model.place_order_model.PlaceOrderData
import com.tulsidistributors.tdemployee.model.place_order_model.PlaceOrderModel
import com.tulsidistributors.tdemployee.ui.adapter.AddProductAdapter
import com.tulsidistributors.tdemployee.ui.adapter.AddProductItemClickListner
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import com.tulsidistributors.tdemployee.utils.Common
import com.tulsidistributors.tdemployee.utils.dataFound
import com.tulsidistributors.tdemployee.utils.noDataFound
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class AddProductListFragment : Fragment(), AddProductItemClickListner {

    val args: AddProductListFragmentArgs by navArgs()
    lateinit var binding: FragmentAddProductListBinding
    lateinit var productRecycler: RecyclerView
    lateinit var show_btn: AppCompatButton
    val list: ArrayList<SelectedProductModel> = ArrayList()
    var placeOderList: ArrayList<PlaceOrderData> = ArrayList()

    var dealer_id: String = ""
    var routingId: String = ""
    var shop_address: String = ""
    var shopName: String = ""
    var totalPrice: Int = 0
    var totalQuantity: Int = 0
    lateinit var oldQntyTv: TextView
    var bottomSheetDialog: BottomSheetDialog? = null
    lateinit var productItem: ArrayList<DealerProductData>
    var selectedProductAdapter: SelectedProductAdapter? = null
    var position: Int? = null
    lateinit var textviewItem: TextView
    lateinit var body: EditText
    var qnt: String? = null
    lateinit var userLoginPrefrence: UserLoginPreferences
    lateinit var saleExectiveId: String
    lateinit var refrenceId: String
    lateinit var empId: String
    lateinit var sadIc: ImageView
    lateinit var mainLayout: ConstraintLayout
    lateinit var mContext: Context
    lateinit var shopAddressTv: TextView
    lateinit var shopNameTv: TextView
    lateinit var addProductBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddProductListBinding.inflate(inflater, container, false)

        (activity as HomePageActivity).hideToolBar()

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        dealer_id = args.dealerId
        shop_address = args.address
        shopName = args.shopName
        routingId = args.routingId

        productRecycler = binding.listRv
        show_btn = binding.showBtn
        sadIc = binding.sadIc
        mainLayout = binding.mainLayout
        shopAddressTv = binding.shopAddressTv
        shopNameTv = binding.shopNameTv
        addProductBtn = binding.addProductBtn

        binding.shopAddressTv.text = shop_address
        binding.shopNameTv.text = shopName

        refrenceId = "Refno${Common().generateRandomNumber()}"


        userLoginPrefrence = UserLoginPreferences(requireActivity().dataStore)

        getLoginDetails()

        val layoutManager = LinearLayoutManager(requireContext())
        productRecycler.layoutManager = layoutManager

        binding.addProdSearch.searchItemEt.doAfterTextChanged { text ->

            if (text!!.length >= 3) {
                searchDealerProductItem(dealer_id, searchQuery = text.toString())
            } else {
                getDealerProductItem(dealer_id, empId)
            }
        }

        binding.showBtn.setOnClickListener {
            openAddedItemBottomSheet()
        }

        addProductBtn.setOnClickListener {
            val action =
                AddProductListFragmentDirections.actionAddProductListFragment2ToSearchStockItemFragment2(
                    dealer_id
                )
            findNavController().navigate(action)
        }


        productRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


                /* if (dy > 0) {
                     //Scrolling down
                     shopAddressTv.visibility = View.GONE
                     shopNameTv.visibility = View.GONE

                 } else if (dy < 0) {
                     shopAddressTv.visibility = View.VISIBLE
                     shopNameTv.visibility = View.VISIBLE
                 }
             */


            }
        })

    }


    private fun openAddedItemBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)

        val binding: FragmentBottomSheetListProductBinding =
            FragmentBottomSheetListProductBinding.inflate(LayoutInflater.from(requireContext()))

        val bottomSheetRecycler = binding.listSelectedRv
        val quantity = binding.quantityTv
        val totalAmount = binding.totalTv
        val confirmOrder = binding.confirmOrder

        quantity.text = "Qnty: ${list.size}"

        for (i in 0..list.size - 1) {
            totalPrice += list.get(i).proPrice.toInt()
        }

        totalAmount.text = "Total: ${totalPrice}"

        selectedProductAdapter = SelectedProductAdapter(list, requireContext())

        bottomSheetRecycler.layoutManager = LinearLayoutManager(requireContext())
        bottomSheetRecycler.adapter = selectedProductAdapter

        bottomSheetDialog?.setContentView(binding.root)
        bottomSheetDialog?.show()

        val simpleItemTouchCallBack: ItemTouchHelper.SimpleCallback =
            object : RecyclerViewSwipeCallback(requireContext()) {

                override fun onMove(
                    list_selected_rv: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    position = viewHolder.adapterPosition

                    textviewItem = viewHolder.itemView.findViewById<TextView>(R.id.qnty_tvItem)

                    if (direction == ItemTouchHelper.RIGHT) {

                        showDialogForSelectedItem(binding, position!!)


                    } else {


                        productItem[list.get(position!!).position].buttonTxt = "Add"
                        selectedProductAdapter?.notifyDataSetChanged()
                        productRecycler.adapter?.notifyItemChanged(list.get(position!!).position)
                        list.removeAt(position!!)
                        quantity.text = "Qnty" + list.size.toString()
                        totalPrice = 0
                        for (i in 0..list.size - 1) {
                            totalPrice += list.get(i).proPrice.toInt()
                        }

                        totalAmount.text = "Total:" + totalPrice.toString()

                    }

                }

            }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(bottomSheetRecycler)

        confirmOrder.setOnClickListener {
            showToast(mContext, "Refrence Id $refrenceId")
            placeOrder()
        }


    }

    private fun placeOrder() {

        val purchaseDate = Common().getCurrentDate("yyyy-MM-dd")
        val placeOrderModel = PlaceOrderModel(
            refrenceId,
            dealer_id,
            sales_executive_id = saleExectiveId,
            totalPrice.toString(),
            "500",
            purchaseDate,
            "100",
            placeOderList
        )



        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = placeOrderApiCall(placeOrderModel)

                if (response.isSuccessful) {
                    dataFound(mainLayout, sadIc)

                    val responseData = response.body()
                    showToast(mContext, "${responseData!!.message}")

                } else {
                    showToast(mContext, "Response Message ${response.message()}")
                    noDataFound(sadIc, mainLayout)

                }

            } catch (e: java.lang.Exception) {
                noDataFound(sadIc, mainLayout)
                binding.addProductBtn.visibility = View.VISIBLE
                showToast(mContext, "Execption Occured ${e.message}")

            }
        }
    }


    private suspend fun placeOrderApiCall(placeOrderModel: PlaceOrderModel): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {


            BaseClient.getInstance.placeOrder(
                placeOrderModel
            )
        }
    }

    private fun getDealerProductItem(dealerId: String, empId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = getDealerProductItemApiCall(dealerId = dealerId, empId = empId)

            try {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (responseData?.status.equals("1")) {
                        productItem =
                            responseData!!.product_details


                        val adapter = AddProductAdapter(productItem, this@AddProductListFragment)
                        productRecycler.adapter = adapter

                        dataFound(mainLayout, sadIc)
                    } else {

                        noDataFound(sadIc, mainLayout)

                        showToast(mContext, "${responseData?.message}")
                    }
                } else {
                    noDataFound(sadIc, mainLayout)
                    showToast(
                        mContext,
                        "Response Code ${response.code()} Response Message ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                noDataFound(sadIc, mainLayout)
                showToast(mContext, "Exception Occured ${e.message}")

            }
        }
    }

    private suspend fun getDealerProductItemApiCall(
        dealerId: String,
        empId: String
    ): Response<DealerProductModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getDealerProductItem(dealerId, empId)
        }
    }


    private fun searchDealerProductItem(dealerId: String, searchQuery: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = searchDealerProductItemApiCall(dealerId = dealerId, searchQuery)

            try {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (responseData?.status.equals("1")) {
                        productItem =
                            responseData!!.product_details
                        val adapter = AddProductAdapter(productItem, this@AddProductListFragment)
                        productRecycler.adapter = adapter

                        dataFound(mainLayout, sadIc)
                    } else {
                        noDataFound(sadIc, mainLayout)
                        showToast(mContext, "${responseData?.message}")

                    }
                } else {
                    noDataFound(sadIc, mainLayout)
                    showToast(
                        mContext,
                        "Response Code ${response.code()} Response Message ${response.message()}"
                    )
                }
            } catch (e: Exception) {

                noDataFound(sadIc, mainLayout)

                showToast(mContext, "Exception Occured ${e.message}")

            }
        }
    }

    private suspend fun searchDealerProductItemApiCall(
        dealerId: String,
        searchQuery: String
    ): Response<DealerProductModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.searchDealerProductItem(dealer_id, searchQuery, empId)
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as HomePageActivity).hideToolBar()
    }


    override fun onPause() {
        super.onPause()

        (activity as HomePageActivity).showToolbar()
    }


    override fun plusButtonClicked(position: Int, productQty: Int, productQtyTxt: TextView) {

        productItem[position].quantity = productQty

        val qty = productItem[position].quantity

        productQtyTxt.text = qty.toString()


    }

    override fun minusButtonClicked(position: Int, productQty: Int, productQtyTxt: TextView) {


        productItem[position].quantity = productQty


        val qty = productItem[position].quantity

        productQtyTxt.text = qty.toString()
    }

    override fun onAddButtonClicked(
        addBtn: Button,
        position: Int,
        prodactName: String,
        prodctQuantity: Int,
        productPice: String,
        brandId: String,
        productId: String
    ) {

        addBtn.text = "Already Added"

        if (list.isNullOrEmpty()) {

            list.add(
                SelectedProductModel(
                    prodactName,
                    prodctQuantity.toString(),
                    productPice,
                    brandId,
                    position,
                    productPice.toInt()
                )


            )

            placeOderList.add(
                PlaceOrderData(
                    saleExectiveId,
                    productId,
                    refrenceId,
                    dealer_id,
                    routingId,
                    "59",
                    totalQuantity.toString()
                )
            )

        } else {

            val names = list.map {
                it.bandId
            }


            if (!names.contains(brandId)) {
                list.add(
                    SelectedProductModel(
                        prodactName,
                        prodctQuantity.toString(),
                        productPice,
                        brandId,
                        position,
                        productPice.toInt()
                    )
                )

                placeOderList.add(
                    PlaceOrderData(
                        saleExectiveId,
                        productId,
                        refrenceId,
                        dealer_id,
                        routingId,
                        "59",
                        totalQuantity.toString()
                    )
                )

            } else {
                showToast(mContext, "Already added")

            }

        }
    }

    override fun openDialogBox(oldQnty: TextView, position: Int, product_id: String) {
        oldQntyTv = oldQnty
        showDialog("Qnt",product_id)


    }


    private fun showDialog(title: String,product_id:String) {
        /* val binding:AddQtyDialogBinding = AddQtyDialogBinding
             .inflate(LayoutInflater.from(getContext()))*/

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_qty_dialog)
        val body = dialog.findViewById(R.id.tvBody) as EditText

        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_No) as Button

        yesBtn.setOnClickListener {
            dialog.dismiss()
            oldQntyTv.text = body.text

            updateStock(
                saleExtId = saleExectiveId,
                dealerId = dealer_id,
                productId = product_id,
                quantity =  body.text.toString()
            )
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }



    private fun updateStock(
        saleExtId: String,
        dealerId: String,
        productId: String,
        quantity: String
    ) {
        showToast(mContext,"Quantity $quantity")
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = updateStockApiCall(saleExtId, dealerId, productId, quantity)
                if (response.isSuccessful) {
                    val responseData = response.body()
                    showToast(mContext, responseData!!.message)
                } else {
                    showToast(mContext, "Response Error ${response.message()}")
                }
            } catch (e: Exception) {
                showToast(mContext, "Exception Occured ${e.message}")
            }
        }
    }

    private suspend fun updateStockApiCall(
        saleExtId: String,
        dealerId: String,
        productId: String,
        quantity: String
    ): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.insertStock(
                sales_executive_id = saleExtId,
                dealer_id = dealerId,
                product_id = productId,
                quantity = quantity
            )
        }
    }


    private fun showDialogForSelectedItem(
        binding: FragmentBottomSheetListProductBinding,
        Position: Int
    ) {

        var qtyTxt = binding.quantityTv
        var totalTxt = binding.totalTv


        val dialog = Dialog(requireContext())

        val dialogBinding = CustomLayoutBinding.inflate(LayoutInflater.from(requireContext()))


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        body = dialogBinding.tvBody

        val yesBtn = dialog.findViewById(R.id.btn_yes) as Button
        val noBtn = dialog.findViewById(R.id.btn_No) as Button

        yesBtn.setOnClickListener {
            dialog.dismiss()

            qnt = body.text.toString()
            if (position != null) {

                list.get(position!!).proQnty = qnt!!
                val price = qnt!!.toInt() * (list.get(position!!).secondPrice)
                list.get(position!!).proPrice = price.toString()
                selectedProductAdapter?.notifyDataSetChanged()

                placeOderList[position!!].order_quantity = qnt!!


            }
            totalPrice = 0

            for (i in 0..list.size - 1) {

                totalPrice += list.get(i).proPrice.toInt()
                totalQuantity += list.get(i).proQnty.toInt()

            }
            totalTxt.text = "Total:" + totalPrice.toString()
            qtyTxt.text = "Qty:" + totalQuantity.toString()

        }
        noBtn.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()

    }

    private fun getLoginDetails() {
        userLoginPrefrence.saleExecutiveIdFlow.asLiveData().observe(viewLifecycleOwner) {
            saleExectiveId = it.toString()

        }

        userLoginPrefrence.empIdFlow.asLiveData().observe(viewLifecycleOwner) {
            empId = it.toString()

            getDealerProductItem(dealer_id, it.toString())
        }
    }


}