package de.fhe.ai.colivingpilot.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.integration.android.IntentIntegrator
import de.fhe.ai.colivingpilot.MainActivity
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.network.RetrofitClient
import de.fhe.ai.colivingpilot.network.data.response.BackendResponseNoData
import de.fhe.ai.colivingpilot.util.UiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinWgActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_wg)

        val scanQrBtn = findViewById<Button>(R.id.join_wg_activity_join_wg_activity_button_join_qr)

        scanQrBtn.setOnClickListener {
            // Initiieren des QR-Code-Scanners
            val integrator = IntentIntegrator(this)
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

        val joinBtn = findViewById<Button>(R.id.join_wg_activity_button_join)
        joinBtn.setOnClickListener { 
            UiUtils.hideKeyboard(this)
            
            val codeField = findViewById<TextInputLayout>(R.id.join_wg_activity_textfield_code)
            
            val progressBar = findViewById<ProgressBar>(R.id.join_wg_activity_progress)
            
            fun setFormLocked(locked: Boolean) {
                joinBtn.visibility = if (locked) View.GONE else View.VISIBLE
                progressBar.visibility = if (locked) View.VISIBLE else View.GONE
                scanQrBtn.isEnabled = !locked
                codeField.isEnabled = !locked
            }
            
            setFormLocked(true)
            
            val code = codeField.editText?.text.toString()
            RetrofitClient.instance.joinWg(code).enqueue(object : Callback<BackendResponseNoData> {
                override fun onResponse(
                    call: Call<BackendResponseNoData>,
                    response: Response<BackendResponseNoData>
                ) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@JoinWgActivity, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        Log.e(CoLiPiApplication.LOG_TAG, "Response unsuccessful: ${response.errorBody()?.string()}")
                        // TODO: Translate status field
                        UiUtils.showSnackbar(this@JoinWgActivity, joinBtn, R.string.snackbar_something_went_wrong, Snackbar.LENGTH_SHORT, R.color.red)

                        setFormLocked(false)
                    }
                }

                override fun onFailure(call: Call<BackendResponseNoData>, t: Throwable) {
                    Log.e(CoLiPiApplication.LOG_TAG, "Request failed: ${t.message}")
                    UiUtils.showSnackbar(this@JoinWgActivity, joinBtn, R.string.snackbar_something_went_wrong, Snackbar.LENGTH_SHORT, R.color.red)

                    setFormLocked(false)
                }
            })
        }
    }

    //TODO auf neuere Varaiante umstellen!!!
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // Hier kannst du mit dem gescannten QR-Code-Inhalt arbeiten (z.B. anzeigen)
                // result.contents enthält den gescannten Text
                // Beispiel:
                val codeField = findViewById<TextInputLayout>(R.id.join_wg_activity_textfield_code)
                codeField.editText?.setText(result.contents)
                Toast.makeText(this, "Gescannter QR-Code: ${result.contents}", Toast.LENGTH_LONG).show()
            } else {
                // Wenn der Scan abgebrochen wurde

                Toast.makeText(this, "Scan abgebrochen", Toast.LENGTH_LONG).show()
            }
        }
    }
}