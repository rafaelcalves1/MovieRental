package com.example.myapplication.view.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.controller.RegisterActivityViewModel
import com.example.myapplication.controller.RegisterActivityViewModelFactory
import com.example.myapplication.makeToast
import com.example.myapplication.model.database.LocadoraDatabase

class RegisterActivity: AppCompatActivity() {

    private lateinit var editTextDocument: AppCompatEditText
    private lateinit var editTextName: AppCompatEditText
    private lateinit var editTextPhoneNumber: AppCompatEditText
    private lateinit var editTextAge: AppCompatEditText
    private lateinit var editTextPassword: AppCompatEditText

    private lateinit var btnRegisterAsAdmin: AppCompatButton
    private lateinit var btnRegisterAsNormal: AppCompatButton

    private lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        editTextDocument = findViewById(R.id.document_label)
        editTextName = findViewById(R.id.name_label)
        editTextPhoneNumber = findViewById(R.id.phone_number_label)
        editTextAge = findViewById(R.id.age_label)
        editTextPassword = findViewById(R.id.password_label)

        btnRegisterAsAdmin = findViewById(R.id.btn_register_admin)
        btnRegisterAsNormal = findViewById(R.id.btn_register_normal)

        viewModel = ViewModelProvider(
            this,
            RegisterActivityViewModelFactory(LocadoraDatabase.instance(applicationContext))
        )[RegisterActivityViewModel::class.java]

        setuptListerners()
        setupObservers()
    }

    private fun setuptListerners(){
        btnRegisterAsNormal.setOnClickListener {
            viewModel.registerClient(
                document = editTextDocument.text.toString(),
                name = editTextName.text.toString(),
                age = editTextAge.text.toString(),
                phoneNumber = editTextPhoneNumber.text.toString(),
                password = editTextPassword.text.toString(),
                isAdmin = false
            )
        }

        btnRegisterAsAdmin.setOnClickListener {
            viewModel.registerClient(
                document = editTextDocument.text.toString(),
                name = editTextName.text.toString(),
                age = editTextAge.text.toString(),
                phoneNumber = editTextPhoneNumber.text.toString(),
                password = editTextPassword.text.toString(),
                isAdmin = true
            )
        }

        editTextDocument.doAfterTextChanged {
            viewModel.checkDocument(it.toString())
        }
    }

    private fun setupObservers(){
        viewModel.documentNotExist.observe(this){ cpfUtilizado->
            handleEnabledsViews(cpfUtilizado)
        }

        viewModel.error.observe(this){
            makeToast(it)
        }

        viewModel.registered.observe(this){
            makeToast("Usuário cadastrado")
            finish()
        }
    }

    private fun handleEnabledsViews(boolean: Boolean){
        editTextName.isEnabled = boolean
        editTextPhoneNumber.isEnabled = boolean
        editTextAge.isEnabled = boolean
        editTextPassword.isEnabled = boolean
        btnRegisterAsAdmin.isEnabled = boolean
        btnRegisterAsNormal.isEnabled = boolean
    }
}