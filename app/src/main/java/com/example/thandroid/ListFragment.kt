package com.example.thandroid

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thandroid.adapter.ListRvAdapter
import com.example.thandroid.model.Work
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ListFragment : Fragment() {
    lateinit var rv : RecyclerView
    lateinit var adapter : ListRvAdapter
    lateinit var fab : FloatingActionButton

    companion object {
        fun newInstance() = ListFragment()
    }

    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getWorks(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.list_rv)
        fab = view.findViewById(R.id.add_fab)

        viewModel.works.observe(viewLifecycleOwner, Observer{
            adapter = ListRvAdapter(it, {
                // edit
                showEditWorkDialog(it)
            }, {
                // delete
                lifecycleScope.launch {
                    viewModel.deleteWork(requireContext(), it).join()
                    viewModel.getWorks(requireContext())
                }
            })
            rv.adapter = adapter
            rv.layoutManager = GridLayoutManager(requireContext(), 1)
        })
        fab.setOnClickListener {
            showAddWorkDialog()
            viewModel.getWorks(requireContext())
        }
    }
    private fun showAddWorkDialog() {
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_work, null)
        val name = dialogLayout.findViewById<TextInputEditText>(R.id.editTextWork)
        val content = dialogLayout.findViewById<TextInputEditText>(R.id.editTextWorkDescription)
        val date = dialogLayout.findViewById<TextView>(R.id.tvDate)
        val spinner = dialogLayout.findViewById<Spinner>(R.id.spinnerStatus)
        val checkBoxCoop = dialogLayout.findViewById<CheckBox>(R.id.checkBoxCoop)

        date.text = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)

        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.status_array))
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = statusAdapter

        // Show a DatePickerDialog when the date TextView is clicked
        val calendar = Calendar.getInstance()
        date.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val selectedDate = "${dayOfMonth}/${month + 1}/$year"
                date.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Work")
        builder.setView(dialogLayout)
        builder.setPositiveButton("Add") { dialog, _ ->
            if (name.text.toString().isNotEmpty() && content.text.toString().isNotEmpty()) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateInMs = sdf.parse(date.text.toString())?.time ?: 0L
                val work = Work(
                    0, // AutoGenerate will replace this with a valid ID
                    name.text.toString(),
                    content.text.toString(),
                    dateInMs,
                    spinner.selectedItem.toString(),
                    checkBoxCoop.isChecked
                )
                lifecycleScope.launch {
                    viewModel.addWork(requireContext(),work).join()
                    viewModel.getWorks(requireContext())
                }
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
    private fun showEditWorkDialog(work: Work) {
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_work, null)
        val name = dialogLayout.findViewById<TextInputEditText>(R.id.editTextWork)
        val content = dialogLayout.findViewById<TextInputEditText>(R.id.editTextWorkDescription)
        val date = dialogLayout.findViewById<TextView>(R.id.tvDate)
        val spinner = dialogLayout.findViewById<Spinner>(R.id.spinnerStatus)
        val checkBoxCoop = dialogLayout.findViewById<CheckBox>(R.id.checkBoxCoop)

        // Set the initial values
        name.setText(work.name)
        content.setText(work.content)
        date.text = SimpleDateFormat("dd/MM/yyyy").format(Date(work.date))
        val statusIndex = resources.getStringArray(R.array.status_array).indexOf(work.status)
        spinner.setSelection(statusIndex)
        checkBoxCoop.isChecked = work.coop

        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.status_array))
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = statusAdapter

        // Show a DatePickerDialog when the date TextView is clicked
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = work.date // Set the time of the calendar to the saved date
        date.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val selectedDate = "${dayOfMonth}/${month + 1}/$year"
                date.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Work")
        builder.setView(dialogLayout)
        builder.setPositiveButton("Save") { dialog, _ ->
            if (name.text.toString().isNotEmpty() && content.text.toString().isNotEmpty()) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateInMs = sdf.parse(date.text.toString())?.time ?: 0L
                val updatedWork = Work(
                    work.id,
                    name.text.toString(),
                    content.text.toString(),
                    dateInMs,
                    spinner.selectedItem.toString(),
                    checkBoxCoop.isChecked
                )
                lifecycleScope.launch {
                    viewModel.updateWork(requireContext(), updatedWork).join()
                    viewModel.getWorks(requireContext())
                }
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}