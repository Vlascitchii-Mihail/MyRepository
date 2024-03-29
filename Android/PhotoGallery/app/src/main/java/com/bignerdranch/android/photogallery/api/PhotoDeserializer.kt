package com.bignerdranch.android.photogallery.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import com.google.gson.Gson

//custom deserializer
class PhotoDeserializer: JsonDeserializer<PhotoResponse> {
    private var page: Int? = 0
    private var pages: Int? = 0
    var perpage: Int? = 0
    var total: Int? = 0

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PhotoResponse {

        //getting interior data from inserted jsObject's MAP array
        val jsObject = json?.asJsonObject?.get("photos")?.asJsonObject
        page = jsObject?.get("page")?.asInt
        pages = jsObject?.get("pages")?.asInt
        perpage = jsObject?.get("perpage")?.asInt
        total = jsObject?.get("total")?.asInt

        //This method deserializes the Json read from the specified parse tree
        // into an object of the specified type.
        /**
         * @param jsObject - sorce data
         * @param PhotoResponse::class.java - deserialize data to this type
         */
        val photos = Gson().fromJson(jsObject, PhotoResponse::class.java)
        page?.let { photos.setPage(it) }
        pages?.let { photos.setPages(it) }
        return photos
    }
}