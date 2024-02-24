package com.example.elogapp.repository


import androidx.lifecycle.LiveData
import com.example.elogapp.database.DropdownMaster
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.dvir.NewDvirResponse
import com.example.elogapp.repository.responses.master.MasterResponse
import okhttp3.RequestBody

class NewDvirRepository(private val api: MyApi,
                        private val db: MyRoomDb) : SafeApiRequest() {

    /**
     * Download DVIR Data From Web..
     */
    suspend fun getDvirList(userId: Int, clientId: Int, key: String): MasterResponse {

        return apiRequest { api.getMasterData(userId, clientId, key) }

    }

    /**
     * Save DVIR Events
     */
    suspend fun saveNewDvirData(event: RequestBody):  NewDvirResponse{

        return apiRequest { api.saveDvirEvent(event)

        }

    }

    /**
     * Get Defects List from Dropworn Master Table
     */
    fun getDropdownList (type: String): LiveData<List<DropdownMaster>> {

        return db.getDropdownMasterDao().getDropdownListByType(type)
    }




    /**
     * Get Device List from Device Master Table
     */
//    fun getDeviceList (): LiveData<List<Device>> {
//
//        return db.getDeviceDao().getDeviceList()
//     }

    /**
     * Get Vehicle List from Vehicle Master Table
     */
//    fun getVehicleList (): LiveData<List<Vehicle>>{
//
//        return db.getVehicleDao().getVehicleList()
//    }

    /**
     * Get Driver List from Driver Master Table
     */
//    fun getDriverList (): LiveData<List<Driver>>{
//
//        return db.getDriverDao().getDriverList()
//    }

    /**
     * Get Trailer List from Trailer Master Table
     */
//    fun getTrailerList (): LiveData<List<Trailers>>{
//
//        return db.getTrailerDao().getTrailerList()
//    }


}