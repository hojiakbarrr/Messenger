package com.example.messenger.ui.register.sign_in

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.messenger.ui.main.MainActivity
import com.example.messenger.databinding.SignInFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SIgnInFragment : Fragment() {
    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SIgnInFragment()
    }

    private lateinit var viewModel: SIgnInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)

        binding.logInBtn.setOnClickListener {
            validateData()
        }

        return binding.root
    }

    private fun validateData() {
        val email = binding.logEmailText.text
        val password = binding.logInPassword.text
        when{
            email.isNullOrEmpty() ->{
                binding.logEmailText.error = "Email is Empty"
            }
            password.isNullOrEmpty() -> {
                binding.logInPassword.error = "Password is Empty"
            }
            else -> {
                loginUser(email.toString().trim(), password.toString().trim())
            }

        }
    }

    private fun loginUser(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"Вы успешно авторизовались",Toast.LENGTH_LONG).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)

                    /**
                     *
                     */
                    val firebaseUser: FirebaseUser = it.result.user!!
                    intent.putExtra("email_id", email)
                    intent.putExtra("user_id", firebaseUser.uid)
                    startActivity(intent).also {
                        activity?.finish()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Error${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SIgnInViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}