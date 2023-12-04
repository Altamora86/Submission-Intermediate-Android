package com.tugas.submissionawal.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.tugas.submissionawal.R
import com.tugas.submissionawal.data.pref.UserPreference
import com.tugas.submissionawal.databinding.ActivityLoginRoomBinding
import com.tugas.submissionawal.ViewModelFactory
import com.tugas.submissionawal.upload.isValidEmail
import com.tugas.submissionawal.upload.validateMinLegth
import com.tugas.submissionawal.view.main.MainActivity
import com.tugas.submissionawal.view.register.RegisterRoom

class LoginRoom : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityLoginRoomBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        loginViewModel = ViewModelProvider(this, View   ModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]

        playAnimation()
        setUpAction()
        buttonEnable()
        passwordEditText()

        loginViewModel.getLogin().observe(this) {
            if (it.error) {
                showLoading(false)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
            else{
                showLoading(true)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginRoom, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setUpAction() {
        val email = binding.edtEmail.text
        val password = binding.edtPassword.text
        binding.btnLogin.setOnClickListener {
            when {
                email?.isEmpty()!! -> {
                    Toast.makeText(this, getString(R.string.massage_email), Toast.LENGTH_SHORT).show()
                }
                password?.isEmpty()!! -> {
                    Toast.makeText(this, getString(R.string.massage_password), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    setUpLogin()
                    showLoading(true)
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginRoom, RegisterRoom::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun setUpLogin() {
        binding.apply {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            loginViewModel.login(email, password)
        }
    }
    private fun buttonEnable() {
        val emailEditText = binding.edtEmail.text
        val passwordEditText = binding.edtPassword.text
        binding.btnLogin.isEnabled =
            isValidEmail(emailEditText.toString()) && validateMinLegth(passwordEditText.toString())
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val imagelogo = ObjectAnimator.ofFloat(binding.imageLogo, View.ALPHA, 1f).setDuration(500)
        val tvwelcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val tvdescription = ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(500)
        val tvemailtitle = ObjectAnimator.ofFloat(binding.tvEmailTitle, View.ALPHA, 1f).setDuration(500)
        val edtemail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val tvpasswordtitle = ObjectAnimator.ofFloat(binding.tvPasswordTitle, View.ALPHA, 1f).setDuration(500)
        val edtpassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvaccount = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, 1f).setDuration(500)
        val tvregister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(edtemail, edtpassword, tvaccount, tvregister)
        }

        AnimatorSet().apply {
            playSequentially(imagelogo, tvwelcome, tvdescription, tvemailtitle, tvpasswordtitle,  btnlogin, together)
            startDelay = 500
        }.start()
    }

    private fun showLoading(loading: Boolean) { binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE }

    private fun passwordEditText() {
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}