package com.example.form

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.form.controller.AddressHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val addressHelper = AddressHelper(resources)
        val idText = findViewById<TextView>(R.id.etStudentId)
        val nameText = findViewById<TextView>(R.id.etName)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val genderRadioButton = findViewById<RadioGroup>(R.id.rgGender)
        val cbSports = findViewById<CheckBox>(R.id.cbSports)
        val cbMovies = findViewById<CheckBox>(R.id.cbMovies)
        val cbMusic = findViewById<CheckBox>(R.id.cbMusic)
        val cbAgree = findViewById<CheckBox>(R.id.cbAgree)
        val spProvince = findViewById<Spinner>(R.id.spProvince)
        val spDistrict = findViewById<Spinner>(R.id.spDistrict)
        val spWard = findViewById<Spinner>(R.id.spWard)
        val btnShowDatePicker = findViewById<Button>(R.id.btnShowDatePicker)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val etPhone = findViewById<TextView>(R.id.etPhone)
        val etEmail = findViewById<TextView>(R.id.etEmail)

        var selectedDate = ""
        calendarView.visibility = View.GONE

        btnShowDatePicker.setOnClickListener {
            calendarView.visibility = if (calendarView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            btnShowDatePicker.text = selectedDate
            calendarView.visibility = View.GONE
        }

        val provinces = addressHelper.getProvinces()
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvince.adapter = provinceAdapter

        spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedProvince = provinces[position]
                val districts = addressHelper.getDistricts(selectedProvince)
                val districtAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, districts)
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spDistrict.adapter = districtAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedProvince = spProvince.selectedItem.toString()
                val selectedDistrict = spDistrict.selectedItem.toString()
                val wards = addressHelper.getWards(selectedProvince, selectedDistrict)
                val wardAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, wards)
                wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spWard.adapter = wardAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        btnSubmit.setOnClickListener {
            val studentId = idText.text.toString()
            val name = nameText.text.toString()
            val gender = if (genderRadioButton.checkedRadioButtonId != -1)
                findViewById<RadioButton>(genderRadioButton.checkedRadioButtonId).text.toString()
            else ""
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()
            val ward = spWard.selectedItem.toString()
            val district = spDistrict.selectedItem.toString()
            val province = spProvince.selectedItem.toString()
            val interests = mutableListOf<String>()
            if (cbSports.isChecked) interests.add("Thể thao")
            if (cbMovies.isChecked) interests.add("Điện ảnh")
            if (cbMusic.isChecked) interests.add("Âm nhạc")

            // Validate form
            if (studentId.isBlank() || name.isBlank() || gender.isBlank() || email.isBlank() ||
                phone.isBlank() || selectedDate.isBlank() || ward == "Select" ||
                district == "Select" || province == "Select" || !cbAgree.isChecked) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Thông tin đã được gửi thành công!", Toast.LENGTH_SHORT).show()
            }
        }


    }
}