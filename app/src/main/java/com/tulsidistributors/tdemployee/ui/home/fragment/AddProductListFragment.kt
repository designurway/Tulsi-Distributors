package com.tulsidistributors.tdemployee.ui.home.fragment

import RecyclerViewSwipeCallback
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.designurway.tdapplication.adapter.SelectedProductAdapter
import com.designurway.tdapplication.model.SelectedProductModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.dataModel
import com.tulsidistributors.tdemployee.databinding.*
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductData
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductModel
import com.tulsidistributors.tdemployee.ui.adapter.AddProductAdapter
import com.tulsidistributors.tdemployee.ui.adapter.AddProductItemClickListner
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
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
    var id: String = ""
    var shop_address: String = ""
    var shopName: String = ""
    var totalQty: Int = 0
    var total: Int = 0
    lateinit var oldQntyTv: TextView
    var bottomSheetDialog: BottomSheetDialog? = null
    lateinit var productItem: ArrayList<DealerProductData>
    var selectedProductAdapter: SelectedProductAdapter? = null
    var position: Int? = null
    lateinit var textviewItem: TextView
    lateinit var body: EditText
    var qnt: String? = null

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

        id = args.dealerId
        shop_address = args.address
        shopName = args.shopName

        productRecycler = binding.listRv
        show_btn = binding.showBtn

        binding.shopAddressTv.text = shop_address
        binding.shopNameTv.text = shopName


        val layoutManager = LinearLayoutManager(requireContext())
        productRecycler.layoutManager = layoutManager

        binding.addProdSearch.searchItemEt.doOnTextChanged { text, start, before, count ->

            if (text!!.length >= 2) {
                searchDealerProductItem(id, searchQuery = text.toString())
            } else {
                getDealerProductItem(id)
            }
        }

        binding.showBtn.setOnClickListener {
            openAddedItemBottomSheet()
        }

        getDealerProductItem(id)

    }

    private fun openAddedItemBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)

        val binding: FragmentBottomSheetListProductBinding =
            FragmentBottomSheetListProductBinding.inflate(LayoutInflater.from(requireContext()))

        val bottomSheetRecycler = binding.listSelectedRv
        val quantity = binding.quantityTv
        val totalAmount = binding.totalTv

        quantity.text = "Qnty: ${list.size}"

        for (i in 0..list.size - 1) {
            total += list.get(i).proPrice.toInt()
        }

        totalAmount.text = "Total: ${total}"

        selectedProductAdapter = SelectedProductAdapter(list, requireContext())

        bottomSheetRecycler.layoutManager = LinearLayoutManager(requireContext())
        bottomSheetRecycler.adapter = selectedProductAdapter

        bottomSheetDialog?.setContentView(binding.root)
        bottomSheetDialog?.show()

        val simpleItemTouchCallBack:ItemTouchHelper.SimpleCallback = object :RecyclerViewSwipeCallback(requireContext()){

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

                    showDialogForSelectedItem(binding,position!!)

                    selectedProductAdapter?.notifyDataSetChanged()

                } else {


                    productItem[list.get(position!!).position].buttonTxt = "Add"
                    selectedProductAdapter?.notifyDataSetChanged()
                    productRecycler.adapter?.notifyItemChanged(list.get(position!!).position)
                    list.removeAt(position!!)
                    quantity.text = "Qnty" + list.size.toString()
                    total = 0
                    for (i in 0..list.size - 1) {
                        total += list.get(i).proPrice.toInt()
                    }

                    totalAmount.text = "Total:" + total.toString()

                }

            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(bottomSheetRecycler)

        getDealerProductItem(id)


    }


    private fun getDealerProductItem(dealerId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = getDealerProductItemApiCall(dealerId = dealerId)

            try {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (responseData?.status.equals("1")) {
                        productItem =
                            responseData!!.product_details


                        val adapter = AddProductAdapter(productItem, this@AddProductListFragment)
                        productRecycler.adapter = adapter
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${responseData?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code ${response.code()} Response Message ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun getDealerProductItemApiCall(dealerId: String): Response<DealerProductModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getDealerProductItem("1", "TD001")
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
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${responseData?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code ${response.code()} Response Message ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun searchDealerProductItemApiCall(
        dealerId: String,
        searchQuery: String
    ): Response<DealerProductModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.searchDealerProductItem("1", searchQuery, "TD001")
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
        brandId: String
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
                        productPice.toInt())
                )
            } else {
                Toast.makeText(requireContext(), "Already added", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun openDialogBox(oldQnty: TextView, position: Int) {
        oldQntyTv = oldQnty
        showDialog("Qnt")
    }


    private fun showDialog(title: String) {
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
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    private fun showDialogForSelectedItem( binding: FragmentBottomSheetListProductBinding,Position:Int) {

        var qtyTxt = binding.qntyTv
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
            if (position!=null){

                list.get(position!!).proQnty = qnt!!
                val price=qnt!!.toInt()*(list.get(position!!).secondPrice)
                list.get(position!!).proPrice=price.toString()
                selectedProductAdapter?.notifyDataSetChanged()


            }
            total=0
            var totalqnt=0
            for (i in 0..list.size - 1) {

                total += list.get(i).proPrice.toInt()
                totalqnt += list.get(i).proQnty.toInt()

            }
            totalTxt.text = "Total:" + total.toString()
            qtyTxt.text="Qty:"+totalqnt.toString()

        }
        noBtn.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()

    }



}