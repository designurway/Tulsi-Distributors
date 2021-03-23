package com.tulsidistributors.tdemployee.ui.home.fragment.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentUpdateProfileBinding
import de.hdodenhof.circleimageview.CircleImageView

class UpdateProfileFragment : Fragment() {

    lateinit var bindinig:FragmentUpdateProfileBinding
    lateinit var profileImg:CircleImageView
    lateinit var updateNameEt:EditText
    lateinit var updateImgBtn:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindinig = FragmentUpdateProfileBinding.inflate(inflater,container,false)
        return  bindinig.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImg = bindinig.profileImg
        updateNameEt = bindinig.updateNameEt
        updateImgBtn = bindinig.updateImgBtn





    }

}