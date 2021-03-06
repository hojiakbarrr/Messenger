package com.example.messenger.ui.register.sign_up

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.messenger.ui.main.MainActivity
import com.example.messenger.databinding.SignUpFragmentBinding
import com.example.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {
    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)

        binding.creatNewUser.setOnClickListener {
            validateData()
        }






        return binding.root
    }

    private fun validateData() {
        val email = binding.editTextEmail.text
        val password = binding.editPassword.text
        val confirmPassword = binding.confirmPassword.text
        val name = binding.editTextName.text

        when {
            email.isNullOrEmpty() -> {
                binding.editTextEmail.error = "Email can't be empty"
            }

            password.isNullOrEmpty() -> {
                binding.editPassword.error = "Password can't be empty"
            }
            confirmPassword.isNullOrEmpty() -> {
                binding.confirmPassword.error = "Confirm password can't be empty"
            }

            password.isNotEmpty() -> {
                if (password.toString() == confirmPassword.toString()) {
                    registerUser(email.toString().trim(),
                        password.toString().trim(),
                        name.toString().trim())
                } else {
                    binding.confirmPassword.error = "Passwords do not match"
                    Log.d("Error", "$confirmPassword -- $password")
                }
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser!!.uid as String
                    val reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    val user = User()
                    user.email = email
                    user.userName = name
                    user.status = "online"
                    user.id = userId

                    reference.setValue(user).addOnCompleteListener { task->
                        if (task.isSuccessful){

                        }
                    }

                    /**
                     * ??????????
                     */
                    Toast.makeText(requireContext(),
                        "???? ?????????????? ????????????????????????????????????",
                        Toast.LENGTH_LONG).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        // TODO: Use the ViewModel
    }

}