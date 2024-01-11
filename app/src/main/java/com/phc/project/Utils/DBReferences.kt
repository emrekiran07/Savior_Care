package com.phc.project.Utils

import com.google.firebase.database.FirebaseDatabase



class DBReferences {
    companion object{
        val rootReference = FirebaseDatabase.getInstance().reference
        val patientRef = rootReference.child("Patients")
        val doctorRef = rootReference.child("Doctors")
        val complaintRef = rootReference.child("Complaints")

    }

}