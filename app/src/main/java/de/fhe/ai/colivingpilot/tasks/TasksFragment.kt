package de.fhe.ai.colivingpilot.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import de.fhe.ai.colivingpilot.R

/**
 * A simple [Fragment] subclass.
 * Use the [TasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TasksFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val text : TextView = view.findViewById(R.id.textView)
        //text.text = "Hendrik hasst Unity & Menschen & Android Studio"
        var customAdapter: Adapter
        val originalData = arrayListOf("Dario", "Kevin", "Max", "Hendrik", "Niklas", "Flo", "Robin", "Maris", "Yannick")
        customAdapter = Adapter(ArrayList(originalData)) // Eine kopierte Liste wird dem Adapter übergeben

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = customAdapter

        val resetButton: Button = view.findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            // Erstellen Sie eine neue Liste basierend auf den originalen Daten und setzen Sie den Adapter zurück
            customAdapter.reset(ArrayList(originalData))
        }

        resetButton.setOnLongClickListener {
            customAdapter.clear()
        }

        val nameTextView: TextView = view.findViewById(R.id.nameEditText)
        val joinButton : Button = view.findViewById(R.id.join)
        joinButton.setOnClickListener {
            customAdapter.addBiggi(nameTextView.text.toString())
        }
    }

}