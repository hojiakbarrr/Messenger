package com.example.messenger.ui.main.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.messenger.R
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.databinding.ProfileFragmentBinding
import com.example.messenger.model.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

private const val IMAGE_REQUEST = 1


class ProfileFragment : Fragment() {
    private val binding: ProfileFragmentBinding by lazy {
        ProfileFragmentBinding.inflate(layoutInflater)
    }

    private val binding_main: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    private var auth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileInfo()


    }

    private fun profileInfo() {
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth!!.currentUser

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        reference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                binding.apply {
                    nameUser.text = user!!.userName
                    mailUser.text = user.email

                    refreshImage(user)

                    porfileImage.setOnClickListener {
                        openGallery()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun refreshImage(user: User) {
        if (user.imageUrl.isNullOrEmpty()) {
            binding.porfileImage.setImageResource(R.drawable.ic_person)
        } else {
            Picasso.get().load(user.imageUrl).into(binding.porfileImage)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            uri = data.data
            uploadImage(uri)
        }
    }

    private fun uploadImage(uri: Uri?) {
        val reference: StorageReference =
            FirebaseStorage.getInstance().getReference("images").child(UUID.randomUUID().toString())
        val dialog = ProgressDialog(context)
        dialog.setTitle("Loading...")
        dialog.show()
        if (uri != null) {
            reference.putFile(uri)
                .addOnSuccessListener(OnSuccessListener<Any?> {
                    dialog.dismiss()
                    reference.getDownloadUrl()
                        .addOnSuccessListener(OnSuccessListener { uri -> updateUser(uri)
                        })

                }).addOnProgressListener { snapshot ->
                    val progress: Double =
                        100.0 * snapshot.bytesTransferred / snapshot.totalByteCount
                    dialog.setMessage("Uploaded" + progress.toInt() + "%")
                }
        }
    }

    private fun updateUser(uri: Uri?) {
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        val map = HashMap<String, Any>()
        map["imageUrl"] = uri.toString()
        reference.updateChildren(map)
    }
}