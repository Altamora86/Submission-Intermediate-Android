package com.tugas.submissionawal.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tugas.submissionawal.R
import com.tugas.submissionawal.databinding.ActivityRegisterRoomBinding
import com.tugas.submissionawal.upload.isValidEmail
import com.tugas.submissionawal.upload.validateMinLegth
import com.tugas.submissionawal.view.login.LoginRoom

class RegisterRoom : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterRoomBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]

        registerViewModel.getRegister().observe(this) {
            if (it == null) {
                showLoading(true)
            }
            else {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                showLoading(false)
                val intent = Intent(this@RegisterRoom, LoginRoom::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        setUpAction()
        playAnimation()
        buttonEnable()
        passwordEditText()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val btnbacktologin = ObjectAnimator.ofFloat(binding.btnBackToLogin, View.ALPHA, 1f).setDuration(500)
        val imagelogo = ObjectAnimator.ofFloat(binding.imageLogo, View.ALPHA, 1f).setDuration(500)
        val tvwelcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val tvdescription2 = ObjectAnimator.ofFloat(binding.tvDescription2, View.ALPHA, 1f).setDuration(500)
        val tvnametitle = ObjectAnimator.ofFloat(binding.tvNameTitle, View.ALPHA, 1f).setDuration(500)
        val edtname = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(500)
        val tvemailtitle = ObjectAnimator.ofFloat(binding.tvEmailTitle, View.ALPHA, 1f).setDuration(500)
        val edtemail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val tvpasswordtitle = ObjectAnimator.ofFloat(binding.tvPasswordTitle, View.ALPHA, 1f).setDuration(500)
        val edtpassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(edtname, edtemail, edtpassword)
        }

        AnimatorSet().apply {
            playSequentially(btnbacktologin, imagelogo, tvwelcome, tvdescription2, tvnametitle, tvemailtitle, tvpasswordtitle, btnRegister, together)
            startDelay = 500
        }.start()
    }

    private fun setUpAction() {
        val name = binding.edtName.text
        val email = binding.edtEmail.text
        val password = binding.edtPassword.text

        binding.btnRegister.setOnClickListener {
            when {
                name.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.massage_name), Toast.LENGTH_SHORT).show()
                }
                email?.isEmpty()!! -> {
                    Toast.makeText(this, getString(R.string.massage_email), Toast.LENGTH_SHORT).show()
                }
                password?.isEmpty()!! -> {
                    Toast.makeText(this, getString(R.string.massage_password), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    setUpRegister()
                    showLoading(true)
                }
            }
        }
        binding.btnBackToLogin.setOnClickListener {
            val intent = Intent(this@RegisterRoom, LoginRoom::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
    private fun buttonEnable() {
        val emailEditText = binding.edtEmail.text
        val passwordEditText = binding.edtPassword.text
        binding.btnRegister.isEnabled =
            isValidEmail(emailEditText.toString()) && validateMinLegth(passwordEditText.toString())
    }
    private fun setUpRegister() {
        binding.apply {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            registerViewModel.register(name, email, password)
        }
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