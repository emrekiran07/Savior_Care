package com.phc.project.Activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import com.phc.project.Fragments.profilePic
import com.phc.project.Fragments.uri
import com.phc.project.Models.Complaint
import com.phc.project.Models.Patient
import com.phc.project.R
import com.phc.project.Utils.DBReferences
import com.phc.project.Utils.FileUtils
import com.phc.project.Utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.internal.Util
import java.util.*
import javax.microedition.khronos.egl.EGLDisplay

class ComplainForm : AppCompatActivity() {

    lateinit var picture: CircleImageView
    lateinit var etName: EditText
    lateinit var etBGroup: EditText
    lateinit var etCNIC: EditText
    lateinit var etPhone: EditText
    lateinit var etComplaint: EditText
    lateinit var etAddress: EditText
    lateinit var submitButton: Button
    lateinit var backButton: ImageView
    var uri: Uri? = null
    lateinit var pref: SharedPreferences
    lateinit var dialog: Dialog
    val TAG = "ComplainForm"

    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    var docEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complain_form)
        initViews()
        val intent = getIntent()
        docEmail = intent.getStringExtra("email")
        picture.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(2, 2)
                .setFixAspectRatio(true)
                .start(this)
        }
        submitButton.setOnClickListener {
            validateFieldsData()
        }
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateFieldsData() {
        val bool1 = validate(etName)
        val bool2 = validate(etBGroup)
        val bool3 = validate(etCNIC)
        val bool4 = validate(etPhone)
        val bool5 = validate(etAddress)
        val bool6 = validate(etComplaint)
        if (bool1 && bool2 && bool3 && bool4 && bool5 && bool6) {
            if (uri != null) {
                submitComplaintWithImage()
            } else {
                submitComplaintWithoutImage()
            }
        }
    }

    private fun submitComplaintWithImage() {
        dialog.show()
        val ref = storageReference!!.child("complaints/" + UUID.randomUUID().toString())
        ref.putFile(uri!!).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                ref.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { it ->
                    if (it != null) {
                        Log.d(TAG, "Uploaded image url " + it.toString())
                        val complaintRef = DBReferences.complaintRef.push()
                        val complaintId = complaintRef.key
                        val data = Complaint.ComplaintData(
                            etName.text.trim().toString(),
                            pref.getString("email", "")!!,
                            etBGroup.text.trim().toString(),
                            etCNIC.text.trim().toString(),
                            etPhone.text.trim().toString(),
                            etAddress.text.trim().toString(),
                            it.toString(),
                            etComplaint.text.trim().toString(),
                            "pending",
                            docEmail.toString(),
                            complaintId.toString()
                        )
                        complaintRef.setValue(data).addOnCompleteListener {
                            dialog.dismiss()
                            if (it.isSuccessful) {
                                Utils.showShortMessage(
                                    this@ComplainForm,
                                    "Your complaint registered successfully"
                                )
                                etName.text.clear()
                                etBGroup.text.clear()
                                etPhone.text.clear()
                                etCNIC.text.clear()
                                etComplaint.text.clear()
                                etAddress.text.clear()
                            } else {
                                Utils.showShortMessage(
                                    this@ComplainForm,
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
                   this@ComplainForm,
                    "Oops someting went wrong, please try later"
                )
            }
        }).addOnProgressListener(object :
                OnProgressListener<UploadTask.TaskSnapshot?> {
                override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
//                    val progress: Double =
//                        100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                            .getTotalByteCount()
//                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
            })
    }

    private fun submitComplaintWithoutImage() {
        dialog.show()
        val complaintRef = DBReferences.complaintRef.push()
        val complaintId = complaintRef.key
        val data = Complaint.ComplaintData(
            etName.text.trim().toString(),
            pref.getString("email", "")!!,
            etBGroup.text.trim().toString(),
            etCNIC.text.trim().toString(),
            etPhone.text.trim().toString(),
            etAddress.text.trim().toString(),
            "",
            etComplaint.text.trim().toString(),
            "pending",
            docEmail.toString(),
            complaintId.toString()
        )
        complaintRef.setValue(data).addOnCompleteListener {
            dialog.dismiss()
            if (it.isSuccessful) {
                Utils.showShortMessage(
                    this,
                    "Your complaint registered successfully"
                )
                etName.text.clear()
                etBGroup.text.clear()
                etPhone.text.clear()
                etCNIC.text.clear()
                etComplaint.text.clear()
                etAddress.text.clear()
            } else {
                Utils.showShortMessage(
                    this,
                    "Something went wrong, please try later."
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.getUri()
                val fileSize = FileUtils.getFile(this, resultUri).length() / 1024
                if (fileSize < 3072) {
                    uri = resultUri
                    picture.setImageURI(resultUri)
                } else {
                    Log.d(com.phc.project.Fragments.TAG, "File size is " + fileSize + " KB")
                    Utils.showLongMessage(
                        this,
                        "Image size is should not be greater than 3 MB"
                    )
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.getError();
            }
        }
    }

    private fun validate(
        email: EditText
    ): Boolean {
        if (email.text?.trim().toString().equals("") or TextUtils.isEmpty(email.text?.trim())) {
            email.setBackgroundResource(R.drawable.bg_error)
            return false
        } else {
            email.setBackgroundResource(R.drawable.edit_text_shape)
            return true
        }
    }

    private fun initViews() {
        picture = findViewById(R.id.picture)
        etName = findViewById(R.id.name)
        etBGroup = findViewById(R.id.blood_group)
        etCNIC = findViewById(R.id.cnic)
        etPhone = findViewById(R.id.phone)
        etAddress = findViewById(R.id.address)
        etComplaint = findViewById(R.id.complain)
        submitButton = findViewById(R.id.submit_button)
        pref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        dialog = Utils.progressDialog(this)
        backButton = findViewById(R.id.back)
        storage = FirebaseStorage.getInstance();
        storageReference = storage!!.getReference();
    }
}
