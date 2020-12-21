package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.profile.*
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.edit_profile_name_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class EditProfileNameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val editProfileViewModel: EditProfileViewModel by viewModels { viewModelFactory }


    companion object {
        fun newInstance() = EditProfileNameFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_profile_name_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val currentAccount = arguments?.get("profile") as IndividualProfileResponse
//        val name = arguments?.get("name") as String
        et_first_name.setText(currentAccount.firstName, TextView.BufferType.EDITABLE)
        et_last_name.setText(currentAccount.lastName, TextView.BufferType.EDITABLE)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        name_save_button.setOnClickListener {
            val new_first_name = et_first_name.text.toString()
            val new_last_name = et_last_name.text.toString()
            editProfileViewModel.updateAccount(
                PatchIndividualAccountRequest(
                    firstName = new_first_name,
                    hide = currentAccount.hide,
                    lastName = new_last_name,
                    location = currentAccount.location,
                    needs = currentAccount.needs,
                    objectives = currentAccount.objectives
                )
            )
        }


    }
}