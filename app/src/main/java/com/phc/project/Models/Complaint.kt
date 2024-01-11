package com.phc.project.Models



class Complaint {
    data class ComplaintData(
        val name: String,
        val email: String,
        val blood_group: String,
        val cnic: String,
        val phone: String,
        val address: String,
        val image: String,
        val complain: String,
        val status: String,
        val doc_email:String,
        val complaintId: String
    )
}