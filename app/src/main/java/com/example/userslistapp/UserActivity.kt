package com.example.userslistapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.example.userslistapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding

    private val user by lazy {
        intent.getParcelableExtra<User>(USER)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showUser()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.deleteButton.setOnClickListener {
            deleteUser()
        }
        binding.editButton.setOnClickListener {
            submitForm()
        }
    }

    private fun showUser() = with(binding) {
        nameEditText.setText(user.name)
        lastNameEditText.setText(user.lastName)
        emailEditText.setText(user.email)
    }

    private fun submitForm() = with(binding) {
        val name = nameEditText.text?.toString()
        val lastName = lastNameEditText.text?.toString()
        val email = emailEditText.text?.toString()

        var isFormValid = true

        if (!(name != null && name.isNotBlank())) {
            nameEditText.error = ("Incorrect Name")
            isFormValid = false
        }

        if (!(lastName != null && lastName.isNotBlank())) {
            lastNameEditText.error = ("Incorrect LastName")
            isFormValid = false
        }

        if (!isValidEmail(email)) {
            emailEditText.error = ("Incorrect Email")
            isFormValid = false
        }

        if (isFormValid) {
            editUser(user.id, name!!, lastName!!, email!!)
        }

    }

    private fun editUser(id: String, name: String, lastName: String, email: String) {
        val intent = Intent()
        intent.putExtra(USER, User(id, name, lastName, email))
        intent.putExtra(action, ACTION_EDIT)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun deleteUser() {
        val intent = Intent()
        intent.putExtra(USER, user)
        intent.putExtra(action, ACTION_DELETE)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun isValidEmail(email: String?): Boolean {
        return if (email.isNullOrBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    companion object {
        const val ACTION_DELETE = 0
        const val ACTION_EDIT = 1

        const val USER_ACTIVITY_RESULT_CODE = 0

        const val USER = "user"
        const val action = "action"

        fun start(context: Activity, user: User) {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(USER, user)

            context.startActivityForResult(intent, USER_ACTIVITY_RESULT_CODE)
        }
    }
}