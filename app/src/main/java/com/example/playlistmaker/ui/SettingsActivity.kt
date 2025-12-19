package com.example.playlistmaker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var shareBtn: LinearLayout
    private lateinit var supportBtn: LinearLayout
    private lateinit var userAgreementBtn: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        backBtn = findViewById<ImageView>(R.id.back_btn)
        shareBtn = findViewById<LinearLayout>(R.id.share_btn)
        supportBtn = findViewById<LinearLayout>(R.id.support_btn)
        userAgreementBtn = findViewById<LinearLayout>(R.id.agreement_btn)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val horizontalPadding = resources.getDimensionPixelSize(R.dimen.horizontalScreenPadding)
            val verticalPadding = resources.getDimensionPixelSize(R.dimen.verticalScreenPadding)
            v.setPadding(
                horizontalPadding,
                systemBars.top + verticalPadding,
                horizontalPadding,
                systemBars.bottom
            )
            insets
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        backBtn.setOnClickListener { finish() }
        shareBtn.setOnClickListener { onShareBtnClick() }
        supportBtn.setOnClickListener { onSupportBtnClick() }
        userAgreementBtn.setOnClickListener { onUserAgreementBtnClick() }
    }

    private fun onShareBtnClick() {
        val message = getString(R.string.androidDevUrl)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(
            Intent.createChooser(
                shareIntent, null
            )
        )
    }

    private fun onSupportBtnClick() {
        val email = getString(R.string.emailSendTo)
        val themeMessage = getString(R.string.emailThemeMessage)
        val message = getString(R.string.messageToDevelopers)

        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
            putExtra(Intent.EXTRA_SUBJECT, themeMessage)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(supportIntent)
    }

    private fun onUserAgreementBtnClick() {
        val url = getString(R.string.agreementUrl)
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())

        startActivity(intent)
    }
}