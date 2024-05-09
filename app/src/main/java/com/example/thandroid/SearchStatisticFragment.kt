package com.example.thandroid

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thandroid.adapter.SearchRvAdapter
import com.example.thandroid.adapter.StatisticRvAdapter
import com.example.thandroid.model.Statistic
import com.example.thandroid.model.Work
import com.google.android.material.textfield.TextInputEditText

class SearchStatisticFragment : Fragment() {

    lateinit var search_rv : RecyclerView
    lateinit var statistic_rv : RecyclerView
    lateinit var editTextName : TextInputEditText
    lateinit var searchAdapter : SearchRvAdapter
    lateinit var statisticAdapter : StatisticRvAdapter
    lateinit var works : List<Work>
    lateinit var statistics : MutableList<Statistic>

    companion object {
        fun newInstance() = SearchStatisticFragment()
    }

    private val viewModel: SearchStatisticViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllWorks(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextName = view.findViewById(R.id.editTextName)
        search_rv = view.findViewById(R.id.search_rv)
        statistic_rv = view.findViewById(R.id.statistic_rv)
        works = listOf()

        searchAdapter = SearchRvAdapter(emptyList())
        search_rv.adapter = searchAdapter
        search_rv.layoutManager = GridLayoutManager(requireContext(), 1)

        editTextName.addTextChangedListener{
            //searchAdapter.submitList(works.filter { it.name.contains(editTextName.text.toString()) || it.content.contains(editTextName.text.toString())}.sortedBy { it.date })
            val filteredWorks = mutableListOf<Work>()
            for(work in works){
                if(work.name.lowercase().contains(editTextName.text.toString()) || work.content.lowercase().contains(editTextName.text.toString())){
                    filteredWorks.add(work)
                }
            }
            if(editTextName.text.toString().isEmpty()){
                searchAdapter.submitList(emptyList())
            }else{
                searchAdapter.submitList(filteredWorks)
            }
        }

        viewModel.works.observe(viewLifecycleOwner, Observer{ it ->
            works = it.sortedBy { it.date }
            statistics = mutableListOf()
            statistics.add(Statistic("Chưa thực hiện", 0))
            statistics.add(Statistic("Đang thực hiện", 0))
            statistics.add(Statistic("Hoàn thành", 0))
            for(work in works){
                if(work.status == "Chưa thực hiện"){
                    statistics[0].amount += 1
                }else if(work.status == "Đang thực hiện"){
                    statistics[1].amount += 1
                }else{
                    statistics[2].amount += 1
                }
            }
            statisticAdapter = StatisticRvAdapter(statistics)
            statistic_rv.adapter = statisticAdapter
            statistic_rv.layoutManager = GridLayoutManager(requireContext(), 1)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllWorks(requireContext())
    }
}