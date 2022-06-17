package uz.shokirov.passwordview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import uz.shokirov.passwordview.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var passcode = ""
    lateinit var handler2: Handler
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.tv1.setOnClickListener {
            writeText(1.toString())
        }

        binding.tv2.setOnClickListener {
            writeText(2.toString())
        }

        binding.tv3.setOnClickListener {
            writeText(3.toString())
        }

        binding.tv4.setOnClickListener {
            writeText(4.toString())
        }
        binding.tv5.setOnClickListener {
            writeText(6.toString())
        }
        binding.tv6.setOnClickListener {
            writeText(6.toString())
        }
        binding.tv7.setOnClickListener {
            writeText(7.toString())
        }
        binding.tv8.setOnClickListener {
            writeText(8.toString())
        }
        binding.tv9.setOnClickListener {
            writeText(9.toString())
        }
        binding.tv0.setOnClickListener {
            writeText(0.toString())
        }
        binding.tvDelete.setOnClickListener {
            deleteChar()
        }

        binding.tvFinger.setOnClickListener {
            fingerScan()
        }

    }

    private fun fingerScan() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = androidx.biometric.BiometricPrompt(this, executor,
            object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "$errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }


            })
        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Barmoq izidan foydalanish")
            .setNegativeButtonText("Parol terish")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }


    private fun deleteChar() {
        if (passcode.isNotEmpty()) {
            var beforeLast = passcode.length - 1
            var result = passcode.subSequence(0, beforeLast)
            passcode = result.toString()
            binding.firstPinView.setText("$passcode")
        } else {

        }

    }


    private fun writeText(password: String) {
        passcode += "$password"
        binding.firstPinView.setText(passcode)
        if (passcode.length == 4) {
            checkCode(passcode)
        }

    }

    private fun checkCode(passcode: String) {
        handler2 = Handler()
        if (passcode == "1234") {
            binding.firstPinView.setTextColor(Color.GREEN)
            handler2.postDelayed(
                {
                    binding.firstPinView.setTextColor(Color.WHITE)
                    startActivity(Intent(this, MainActivity2::class.java))
                    this.passcode = ""
                    binding.firstPinView.text?.clear()
                }, 600
            )

        } else {
            binding.firstPinView.setTextColor(Color.RED)
            vibration()
            handler2.postDelayed(
                {
                    this.passcode = ""
                    binding.firstPinView.setTextColor(Color.WHITE)
                    binding.firstPinView.text?.clear()
                }, 600
            )
        }
    }

    private fun vibration() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(300)
        }
    }
}