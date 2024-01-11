package com.phc.project.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.phc.project.Activities.Login
import com.phc.project.Models.Doctor

import com.phc.project.R
import com.phc.project.Utils.DBReferences
import com.phc.project.Utils.FileUtils
import com.phc.project.Utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_register_doctor.*
import java.util.*


val TAG = "RegisterPatient"
lateinit var emailLayout: TextInputLayout
lateinit var passwordLayout: TextInputLayout
lateinit var cPasswordLayout: TextInputLayout
lateinit var usernameLayout: TextInputLayout
lateinit var addressLayout: TextInputLayout
lateinit var phoneLayout: TextInputLayout
lateinit var specializationLayout: TextInputLayout


lateinit var email: TextInputEditText
lateinit var username: TextInputEditText
lateinit var address: TextInputEditText
lateinit var phone: TextInputEditText
lateinit var password: TextInputEditText
lateinit var cPassword: TextInputEditText
lateinit var sp: TextInputEditText

lateinit var profilePic: CircleImageView
lateinit var mButton: Button
lateinit var dialog: Dialog
lateinit var loginText :TextView

var uri: Uri? = null

var storage: FirebaseStorage? = null
var storageReference: StorageReference? = null

class RegisterDoctor : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_doctor, container, false)
        initViews(view)
        mButton.setOnClickListener {
            validateFields()
        }

        profilePic.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(2, 2)
                .setFixAspectRatio(true)
                .start(context!!, this)
        }
        loginText.setOnClickListener {
            val intent = Intent(activity!!,Login::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.enter,R.anim.exit)
            activity!!.finish()
        }
        return view
    }

    private fun validateFields() {
        val bool1 = validate(email, emailLayout)
        val bool2 = validate(username, usernameLayout)
        val bool3 = validate(address, addressLayout)
        val bool4 = validate(phone, phoneLayout)
        val bool5 = validate(password, passwordLayout)
        val bool6 = validate(cPassword, cPasswordLayout)
        val bool7 = validate(et_sp, edit_specializationlayout)
        val bool8 = validate(hourly_rate,edit_hourly_ratelayout)
        if (bool1 && bool2 && bool3 && bool4 && bool5 && bool6 && bool7&& bool8) {
            if (passwordMatches()) {
                if (uri != null) {
                    uploadUserWithImage(uri!!)
                } else {
                    uploadUserWithoutImage(
                        Doctor.DoctorData(
                            username.text!!.trim().toString(),
                            email.text!!.trim().toString(),
                            password.text!!.trim().toString(),
                            address.text!!.trim().toString(),
                            phone.text!!.trim().toString(),
                            "",
                            et_sp.text?.trim().toString(),
                            hourly_rate.text?.trim().toString(),
                            "pending",
                            3.toString()
                        )
                    )
                }
            } else {
                Utils.showLongMessage(context!!, "Password and Confirm Password does not match..")
                cPassword.setBackgroundResource(R.drawable.bg_error)
            }
        } else {
            Utils.showLongMessage(context!!, "Please fill all input fields..")
        }
    }

    private fun initViews(view: View?) {
        username = view!!.findViewById(R.id.edit_username)
        address = view!!.findViewById(R.id.edit_address)
        phone = view!!.findViewById(R.id.edit_phone)
        email = view!!.findViewById(R.id.edit_email)
        password = view!!.findViewById(R.id.edit_password)
        cPassword = view!!.findViewById(R.id.edit_cpassword)
        mButton = view!!.findViewById(R.id.register)
       // sp = view!!.findViewById(R.id.et_sp)

        usernameLayout = view!!.findViewById(R.id.edit_usernamelayout)

        phoneLayout = view!!.findViewById(R.id.edit_phonelayout)
        addressLayout = view!!.findViewById(R.id.edit_addresslayout)
        emailLayout = view!!.findViewById(R.id.edit_emaillayout)
        passwordLayout = view!!.findViewById(R.id.edit_passwordlayout)
        cPasswordLayout = view!!.findViewById(R.id.edit_cpasswordlayout)
       // specializationLayout = view!!.findViewById(R.id.edit_specializationlayout)
        loginText = view!!.findViewById(R.id.text_logindescs)

        profilePic = view!!.findViewById(R.id.profile_pic)
        dialog = Utils.progressDialog(context)
        storage = FirebaseStorage.getInstance();
        storageReference = storage!!.getReference();


    }

    private fun validate(
        email: TextInputEditText,
        emailLayout: TextInputLayout
    ): Boolean {
        if (email.text?.trim().toString().equals("") or TextUtils.isEmpty(email.text?.trim())) {
            emailLayout.setBackgroundResource(R.drawable.bg_error)
            return false
        }
        return true
    }

    private fun passwordMatches(): Boolean {
        if (password.text?.trim().toString().equals(cPassword.text?.trim().toString())) {
            return true
        }
        return false
    }
    private fun uploadUserWithoutImage(patientData: Doctor.DoctorData){
        dialog.show()
        DBReferences.doctorRef.orderByChild("email").equalTo(email.text?.trim().toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Utils.showShortMessage(
                        context!!,
                        "Something went wrong, please try later."
                    )
                    dialog.dismiss()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        dialog.dismiss()
                        Utils.showShortMessage(
                            context!!,
                            "User already exists with this email..."
                        )
                    } else {
                        DBReferences.doctorRef.push().setValue(
                            patientData
                        ).addOnCompleteListener {
                            dialog.dismiss()
                            if (it.isSuccessful) {
                                Utils.showShortMessage(
                                    context!!,
                                    "Registered Successfully..."
                                )
                            } else {
                                Utils.showShortMessage(
                                    context!!,
                                    "Something went wrong, please try later."
                                )
                            }
                        }
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.getUri()
                val fileSize = FileUtils.getFile(activity, resultUri).length() / 1024
                if (fileSize < 3072) {
                    uri = resultUri
                    profilePic.setImageURI(resultUri)

                } else {
                    Log.d(TAG, "File size is " + fileSize + " KB")
                    Utils.showLongMessage(
                        context!!,
                        "Image size is should not be greater than 3 MB"
                    )
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.getError();
            }
        }
    }

    private fun uploadUserWithImage(uri: Uri) {
        dialog.show()
        DBReferences.doctorRef.orderByChild("email").equalTo(email.text?.trim().toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Utils.showShortMessage(
                        context!!,
                        "Something went wrong, please try later."
                    )
                    dialog.dismiss()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        dialog.dismiss()
                        Utils.showShortMessage(
                            context!!,
                            "User already exists with this email..."
                        )
                    } else {

                        val ref =
                            storageReference!!.child("doctors/" + UUID.randomUUID().toString())
                        ref.putFile(uri).addOnSuccessListener(object :
                            OnSuccessListener<UploadTask.TaskSnapshot> {
                            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                                ref.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { it ->
                                    if (it != null) {
                                        Log.d(TAG, "Uploaded image url " + it.toString())
                                        DBReferences.doctorRef.push().setValue(
                                            Doctor.DoctorData(
                                                username.text!!.trim().toString(),
                                                email.text!!.trim().toString(),
                                                password.text!!.trim().toString(),
                                                address.text!!.trim().toString(),
                                                phone.text!!.trim().toString(),
                                                it.toString(),
                                                et_sp.text?.trim().toString(),
                                                hourly_rate.text?.trim().toString(),
                                                "pending",
                                                3.toString()
                                            )
                                        ).addOnCompleteListener {
                                            dialog.dismiss()
                                            if (it.isSuccessful) {
                                                Utils.showShortMessage(
                                                    context!!,
                                                    "Registered Successfully..."
                                                )
                                            } else {
                                                Utils.showShortMessage(
                                                    context!!,
                                                    "Something went wrong, please try later."
                                                )
                                            }
                                        }
                                    }
                                })
                            }
                        }).addOnFailureListener(object : OnFailureListener {
                            override fun onFailure(@NonNull e: Exception) {
                                dialog.dismiss()
                                Utils.showShortMessage(
                                    context!!,
                                    "Oops someting went wrong, please try later"
                                )
                            }
                        })
                            .addOnProgressListener(object :
                                OnProgressListener<UploadTask.TaskSnapshot?> {
                                override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
//                    val progress: Double =
//                        100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                            .getTotalByteCount()
//                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                                }
                            })
                    }
                }
            })


    }

}
