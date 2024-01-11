package com.phc.project.Models



class Doctor {
    data class DoctorData(val username:String,
                          val email:String,
                          val password:String,
                          val address:String,
                          val phone:String,
                          val image:String,
                          val specialization:String,
                          val rate:String,
                          val status:String,
                          val rating:String)

}