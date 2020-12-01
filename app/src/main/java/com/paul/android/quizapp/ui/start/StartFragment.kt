package com.paul.android.quizapp.ui.start

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.paul.android.quizapp.R
import com.paul.android.quizapp.databinding.FragmentStartBinding
import com.paul.android.quizapp.ui.main.MainActivity
import javax.inject.Inject

class StartFragment : Fragment() {

    companion object {
        private const val FIREBASE_SIGN_IN_REQ_CODE = 1
    }

    @Inject
    lateinit var startFragmentViewModelFactory: StartFragmentViewModelFactory

    private val viewModel: StartFragmentViewModel by viewModels {
        startFragmentViewModelFactory.create()
    }

    private lateinit var binding: FragmentStartBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        binding.playQuizButton.setOnClickListener {
            viewModel.handleLoading()
        }
        binding.logInButton.setOnClickListener {
            startSignIn()
        }
        binding.logOutButton.setOnClickListener {
            viewModel.signOutUser()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.isLoadingRequiredLiveData().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isLoadingRequired ->
                if (isLoadingRequired) {
                    findNavController().navigate(R.id.action_startFragment_to_loadingCreateQuizFragment)
                } else {
                    findNavController().navigate(R.id.action_startFragment_to_createGameFragment)
                }
            }
        }
        viewModel.userLiveData().observe(viewLifecycleOwner) { user ->
            val (loggedInVisibility, notLoggedInVisibility) = if (user == null) {
                Pair(View.GONE, View.VISIBLE)
            } else {
                Pair(View.VISIBLE, View.GONE)
            }
            with(binding) {
                logInButton.visibility = notLoggedInVisibility
                signUpButton.visibility = notLoggedInVisibility
                logOutButton.visibility = loggedInVisibility
                helloMessage.visibility = loggedInVisibility
                user?.displayName?.let {
                    helloMessage.text = getString(R.string.hello_message, it)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FIREBASE_SIGN_IN_REQ_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                viewModel.updateUser()
            } else {
                response?.error?.errorCode?.let {
                    Log.e("FirebaseUI", it.toString())
                }
            }
        }
    }

    private fun startSignIn() {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            FIREBASE_SIGN_IN_REQ_CODE
        )
    }
}