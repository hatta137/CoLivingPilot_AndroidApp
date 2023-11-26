package de.fhe.ai.colivingpilot.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.http.RetrofitClient
import de.fhe.ai.colivingpilot.http.data.request.LoginRequest
import de.fhe.ai.colivingpilot.http.data.response.BackendResponse
import de.fhe.ai.colivingpilot.http.data.response.datatypes.JwtData
import de.fhe.ai.colivingpilot.util.UiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.button_login_login)
        loginBtn.setOnClickListener {
            UiUtils.hideKeyboard(this)

            val usernameField = findViewById<TextInputLayout>(R.id.textfield_username)
            val passwordField = findViewById<TextInputLayout>(R.id.textfield_password)
            val username = usernameField.editText?.text.toString()
            val password = passwordField.editText?.text.toString()
            val loginRequest = LoginRequest(username, password)

            val progressBar = findViewById<ProgressBar>(R.id.progress_login)

            fun setFormLocked(locked: Boolean) {
                loginBtn.visibility = if (locked) View.GONE else View.VISIBLE
                progressBar.visibility = if (locked) View.VISIBLE else View.GONE
                usernameField.isEnabled = !locked
                passwordField.isEnabled = !locked
            }

            setFormLocked(true)

            RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<BackendResponse<JwtData>> {
                override fun onResponse(
                    call: Call<BackendResponse<JwtData>>,
                    response: Response<BackendResponse<JwtData>>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            val token = it.data.token
                            val app = application as CoLiPiApplication
                            app.getKeyValueStore().writeString("jwt", token)
                            Log.i(CoLiPiApplication.LOG_TAG, "Received JWT: $token")
                        }

                        UiUtils.showSnackbar(this@LoginActivity, loginBtn, R.string.snackbar_login_successful, Snackbar.LENGTH_SHORT, R.color.green)
                    } else {
                        Log.e(CoLiPiApplication.LOG_TAG, "Login response unsuccessful: ${response.errorBody()?.string()}")
                        UiUtils.showSnackbar(this@LoginActivity, loginBtn, R.string.snackbar_login_unsuccessful, Snackbar.LENGTH_SHORT, R.color.red)
                    }

                    setFormLocked(false)
                }

                override fun onFailure(call: Call<BackendResponse<JwtData>>, t: Throwable) {
                    Log.e(CoLiPiApplication.LOG_TAG, "Login request failed: ${t.message}")
                    UiUtils.showSnackbar(this@LoginActivity, loginBtn, R.string.snackbar_something_went_wrong, Snackbar.LENGTH_SHORT, R.color.red)

                    setFormLocked(false)
                }
            })
        }
    }
}