package de.fhe.ai.colivingpilot.view

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.button_login_login)
        loginBtn.setOnClickListener {
            // Close keyboard if it's open
            try {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (_: Exception) {
            }

            val usernameField = findViewById<TextInputLayout>(R.id.textfield_username)
            val passwordField = findViewById<TextInputLayout>(R.id.textfield_password)
            val username = usernameField.editText?.text.toString()
            val password = passwordField.editText?.text.toString()
            val loginRequest = LoginRequest(username, password)

            RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<BackendResponse<JwtData>> {
                override fun onResponse(
                    call: Call<BackendResponse<JwtData>>,
                    response: Response<BackendResponse<JwtData>>
                ) {
                    val loginResponse = response.body()
                    if (response.isSuccessful) {
                        loginResponse?.let {
                            val token = it.data.token
                            val app = application as CoLiPiApplication
                            app.getKeyValueStore().writeString("jwt", token)
                            Log.i(CoLiPiApplication.LOG_TAG, "Received JWT: $token")
                        }

                        Snackbar.make(findViewById(R.id.button_login_login), "Erfolgreich eingeloggt", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(ContextCompat.getColor(this@LoginActivity, R.color.green))
                            .show()
                    } else {
                        Log.e(CoLiPiApplication.LOG_TAG, "Login response unsuccessful: ${response.errorBody()?.string()}")
                        Snackbar.make(findViewById(R.id.button_login_login), "Login fehlgeschlagen", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(ContextCompat.getColor(this@LoginActivity, R.color.red))
                            .show()
                    }
                }

                override fun onFailure(call: Call<BackendResponse<JwtData>>, t: Throwable) {
                    Log.e(CoLiPiApplication.LOG_TAG, "Login request failed: ${t.message}")
                    Snackbar.make(findViewById(R.id.button_login_login), "Etwas ist schiefgelaufen", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(this@LoginActivity, R.color.red))
                        .show()
                }
            })
        }
    }
}