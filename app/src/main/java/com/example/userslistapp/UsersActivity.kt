package com.example.userslistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userslistapp.UserActivity.Companion.USER_ACTIVITY_RESULT_CODE
import com.example.userslistapp.databinding.ActivityUsersBinding
import java.util.*


open class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    private val myAdapter = MyAdapter()

    private val usersList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.usersList.layoutManager = LinearLayoutManager(this)
        binding.usersList.adapter = myAdapter

        binding.submitButton.setOnClickListener {
            submitForm()
        }
        myAdapter.setCallBack(object : MyAdapter.CallBack {
            override fun onEditClick(id: String) {
                val user = usersList.firstOrNull { user -> user.id == id }
                if (user != null) {
                    UserActivity.start(this@UsersActivity, user)
                }
            }

            override fun onDeleteClick(id: String) {
                deleteUser(id)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check that it is the SecondActivity with an OK result
        if (requestCode == USER_ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
                val user = data?.getParcelableExtra<User>(UserActivity.USER)
                val action = data?.getIntExtra(UserActivity.action, -1)

                if (user != null) {
                    if (action == UserActivity.ACTION_EDIT) {
                        editUser(user)
                    } else if (action == UserActivity.ACTION_DELETE) {
                        deleteUser(user.id)
                    }
                }
            }
        }
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
            val id = UUID.randomUUID().toString()
            val newUser = User(id, name!!, lastName!!, email!!)
            addUser(newUser)
        }

    }

    private fun addUser(newUser: User) {
        usersList.add(newUser)
        updateAdapter()

        clearInput()
    }

    private fun deleteUser(id: String) {
        val userIndex = usersList.indexOfFirst { user -> user.id == id }
        usersList.removeAt(userIndex)
        updateAdapter()
    }

    private fun editUser(user: User) {
        val userIndex = usersList.indexOfFirst { it.id == user.id }
        usersList[userIndex] = user
        updateAdapter()
    }

    private fun updateAdapter() {
        myAdapter.submitList(usersList.toList())
    }

    private fun isValidEmail(email: String?): Boolean {
        return if (email.isNullOrBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    private fun clearInput() = with(binding) {
        nameEditText.text.clear()
        lastNameEditText.text.clear()
        emailEditText.text.clear()
    }

    companion object {
        fun start(context: Context, userList: List<User>) {
            val intent = Intent(context, UsersActivity::class.java)
            context.startActivity(intent)

        }
    }
}
