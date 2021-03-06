package com.bignerdranch.android.criminalintent

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.DateFormat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import androidx.lifecycle.Observer
import java.util.UUID
import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button

private const val TAG = "CrimeListFragment"

class CrimeListFragment: Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }


    private var callbacks: Callbacks? = null
    private lateinit var crimeRecyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var textEmpty: TextView
    private var adapter : CrimeAdapter? = CrimeAdapter()
//    private lateinit var  viewEmptyList: View

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

//        viewEmptyList = inflater.inflate(R.layout.list_item_empty, container, false)
//        addButton = view.findViewById(R.id.add_new_crime) as Button
//        addButton.setOnClickListener {
//            val crime = Crime()
//            crimeListViewModel.addCrime(crime)
//            callbacks?.onCrimeSelected(crime.id)
//        }

        addButton = view?.findViewById(R.id.add_new_crime) as Button
        addButton.setOnClickListener {
            val crime = Crime()
            crimeListViewModel.addCrime(crime)
            callbacks?.onCrimeSelected(crime.id)
        }
        textEmpty = view.findViewById(R.id.list_is_empty) as TextView


        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView

        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        crimeRecyclerView.adapter = adapter



//        else view2.visibility = View.GONE

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner, Observer { crimes -> crimes?.let {
            updateUI(it)
        }})
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class CrimeHolder(view: View) :RecyclerView.ViewHolder(view), View.OnClickListener  {
        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
//            dateTextView.text = this.crime.date.toString()
            dateTextView.text = DateFormat.format("HH:mm EEEE, dd MMM, yyyy.", this.crime.date)
            solvedImageView.visibility = if (crime.isSolved) {
                solvedImageView.contentDescription = getString(R.string.is_solved)
                View.VISIBLE
            } else {
                solvedImageView.contentDescription = getString(R.string.is_not_solved)
                View.GONE
            }
        }

        override fun onClick(v: View) {
//            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Crime>() {
        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem == newItem
        }
    }

    private inner class CrimeAdapter: ListAdapter<Crime, CrimeHolder>(DiffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {

            val view: View = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
//            val view: View = when(viewType) {
//                0 -> layoutInflater.inflate(R.layout.list_item_crime, parent, false)
//                else -> layoutInflater.inflate(R.layout.list_item_requires_police, parent, false)
//            }
            return CrimeHolder(view)
        }

        override fun getItemCount() = currentList.size

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = currentList[position]
            holder.bind(crime)
        }

    }

    companion object {
        fun newInstance() : CrimeListFragment {
            return CrimeListFragment()
        }
    }

    private fun updateUI(crimes: List<Crime>) {

        if(crimes.isNotEmpty()) {
            adapter?.submitList(crimes)
            textEmpty.visibility = View.GONE
            addButton.visibility = View.GONE
        }
    }
}