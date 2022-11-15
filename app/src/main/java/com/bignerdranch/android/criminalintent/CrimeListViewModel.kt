package com.bignerdranch.android.criminalintent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    //val crimeListLiveData = crimeRepository.getCrimes()
    val crimeListLiveData = getList()
}

private fun getList(): LiveData<List<Crime>> {
    return MutableLiveData(listOf(
        Crime(title = "Crime 1", requiresPolice = false),
        Crime(title = "Crime 2", requiresPolice = false),
        Crime(title = "Crime 3", requiresPolice = true)
    ))
}
