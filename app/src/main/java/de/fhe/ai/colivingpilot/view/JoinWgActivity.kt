package de.fhe.ai.colivingpilot.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import de.fhe.ai.colivingpilot.MainActivity
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.network.NetworkResultNoData
import de.fhe.ai.colivingpilot.network.RetrofitClient
import de.fhe.ai.colivingpilot.network.data.response.BackendResponseNoData
import de.fhe.ai.colivingpilot.util.UiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinWgActivity : AppCompatActivity() {

    private lateinit var scanQrBtn: Button
    private lateinit var joinBtn: Button
    private lateinit var codeField: TextInputLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_wg)
        
        scanQrBtn = findViewById(R.id.join_wg_activity_button_join_qr)
        
        joinBtn = findViewById(R.id.join_wg_activity_button_join)
        joinBtn.setOnClickListener { 
            UiUtils.hideKeyboard(this)
            
            codeField = findViewById(R.id.join_wg_activity_textfield_code)
            progressBar = findViewById(R.id.join_wg_activity_progress)
            
            setFormLocked(true)
            
            val code = codeField.editText?.text.toString()
            tryJoinWg(code)
        }
    }

    private fun setFormLocked(locked: Boolean) {
        joinBtn.visibility = if (locked) View.GONE else View.VISIBLE
        progressBar.visibility = if (locked) View.VISIBLE else View.GONE
        scanQrBtn.isEnabled = !locked
        codeField.isEnabled = !locked
    }

    private fun tryJoinWg(code: String) {
        CoLiPiApplication.instance.repository.joinWg(code, object : NetworkResultNoData {
            override fun onSuccess() {
                val intent = Intent(this@JoinWgActivity, JoinedWgActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("mode", "joined")
                startActivity(intent)
            }

            override fun onFailure(code: String?) {
                // TODO: Translate status field
                UiUtils.showSnackbar(this@JoinWgActivity, joinBtn, R.string.snackbar_something_went_wrong, Snackbar.LENGTH_SHORT, R.color.red)
                setFormLocked(false)
            }
        })
    }

}