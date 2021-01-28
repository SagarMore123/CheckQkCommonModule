package com.astrika.checqk.discount.master_controller.source

import androidx.room.TypeConverter
import com.astrika.checqk.discount.model.ImageDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageListDTOConverter {

    /**
     * Convert a image list to a Json
     */
    @TypeConverter
    fun fromImageSubCategoryJson(image: ArrayList<ImageDTO>?): String {
        if (image == null)
            return ""
        return Gson().toJson(image)
    }

    /**
     * Convert a json to a Image list
     */
    @TypeConverter
    fun toImageSubCategory(jsonImage: String): ArrayList<ImageDTO>? {
        if (jsonImage == null || jsonImage == "")
            return null
        val notesType = object : TypeToken<ArrayList<ImageDTO>>() {}.type
        return Gson().fromJson<ArrayList<ImageDTO>>(jsonImage, notesType)
    }
}