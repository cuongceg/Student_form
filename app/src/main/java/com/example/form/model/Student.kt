package com.example.form.model

data class Student(
    val id: String,
    val name: String,
    val gender : String,
    val email : String,
    val phone : String,
    val date : String,
    val address : String,
    val hobby : List<String>
)
