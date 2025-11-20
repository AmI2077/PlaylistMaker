package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = resources.getDimensionPixelSize(R.dimen.padding_16)
            v.setPadding(padding, systemBars.top, padding, systemBars.bottom)
            insets
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val backBtn = findViewById<ImageView>(R.id.back_btn)
        val shareBtn = findViewById<AppCompatButton>(R.id.shareBtn)
        val supportBtn = findViewById<AppCompatButton>(R.id.supportBtn)
        val agreementBtn = findViewById<AppCompatButton>(R.id.agreementBtn)

        backBtn.setOnClickListener { onBackClick() }
        shareBtn.setOnClickListener { onShareClick() }
        supportBtn.setOnClickListener { onSupportClick() }
        agreementBtn.setOnClickListener { onAgreementClick() }
    }

    private fun onBackClick() {
        finish()
    }

    private fun onShareClick() {
        val message = this.getString(R.string.android_dev_url)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = "smsto:".toUri()
        intent.putExtra(Intent.EXTRA_TEXT, message)
        val chooser = Intent.createChooser(intent, this.getString(R.string.share_chooser_text))
        startActivity(chooser)
    }

    //здесь довольно много this.getString.. можно ли обьявить какой нибудь enum class который будет
    // хранить ссылки на строковые ресурсы и просто использовать поля из него?
    private fun onSupportClick() {
        val message = this.getString(R.string.email_message)
        val emailAddress = this.getString(R.string.email_address)
        val mailSubject = this.getString(R.string.email_subject)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = "mailto:".toUri()
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
        intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(intent)
    }

    private fun onAgreementClick() {
        val url = this.getString(R.string.user_agreement_url)
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}