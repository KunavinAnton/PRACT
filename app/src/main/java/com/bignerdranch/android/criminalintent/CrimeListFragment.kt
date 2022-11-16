package com.bignerdranch.android.criminalintent

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var crimeRecyclerView: RecyclerView
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)}
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner, Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }

    private fun updateUI(crimes: List<Crime>) {
        val imageButton = view?.findViewById(R.id.imageButton) as ImageButton
        if (crimes.isEmpty()){
            imageButton.visibility = View.VISIBLE
            imageButton.setOnClickListener{
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)

                imageButton.visibility = View.INVISIBLE
            }
        } else{
            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        }

    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)
        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()

            solvedImageView.visibility = if (crime.isSolved) { View.VISIBLE } else { View.GONE }
        }

        override fun onClick(v: View) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }
//    private inner class CrimeHolderRequiresPolice(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
//        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
//        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
//        val requiresPoliceTextView: TextView = itemView.findViewById(R.id.requires_police)
//        private lateinit var crime: Crime
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        @SuppressLint("ResourceType")
//        fun bind(crime: Crime) {
//            this.crime = crime
//            titleTextView.text = this.crime.title
//            dateTextView.text = this.crime.date.toString()
//            requiresPoliceTextView.text = resources.getString(R.string.requieres_police)
//        }
//
//        override fun onClick(v: View) {
//            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
//        }
//    }

    private inner class CrimeAdapter(var crimes: List<Crime>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType){
                R.layout.list_item_crime -> CrimeHolder(view = layoutInflater.inflate(viewType, parent, false))
                else -> throw IllegalArgumentException("Unsupported layout")
            }
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val crime = crimes[position]
            when(holder){
                is CrimeHolder -> holder.bind(crime)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.list_item_crime
        }

        override fun getItemCount() = crimes.size
    }
}