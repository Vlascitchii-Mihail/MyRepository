package data_base

import androidx.room.Dao
import androidx.room.Query
import com.bignerdranch.android.criminalintent.Crime
import java.util.UUID
import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Insert

@Dao interface CrimeDao {
     @Query("SELECT * FROM Crime") fun getCrimes(): LiveData<List<Crime>>
     @Query("SELECT * FROM Crime WHERE id =(:id)") fun getCrime(id:UUID): LiveData<Crime?>
     @Update fun updateCrime(crime: Crime)
     @Insert fun addCrime(crime: Crime)
}