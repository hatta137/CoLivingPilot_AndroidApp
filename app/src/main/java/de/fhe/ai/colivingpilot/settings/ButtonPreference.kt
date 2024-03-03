package de.fhe.ai.colivingpilot.settings

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import de.fhe.ai.colivingpilot.R
import de.fhe.ai.colivingpilot.core.CoLiPiApplication
import de.fhe.ai.colivingpilot.network.NetworkResultNoData

class ButtonPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        widgetLayoutResource = R.layout.custom_button_preference
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val leaveButton: Button = holder.findViewById(R.id.button_leave_wg) as Button

        leaveButton.setOnClickListener {
            CoLiPiApplication.instance.repository.leaveWg(object : NetworkResultNoData {
                override fun onSuccess() {
                }

                override fun onFailure(code: String?) {
                }
            })
        }
    }
}
