package com.kafilicious.popularmovies.data.models.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

/*
 * Copyright (C) 2017 Popular Movies, Stage 2
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
@Entity(tableName = "video.db")
class Video {

    @Expose
    @PrimaryKey
    var video_id: Int = 0

    @Expose
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: String? = null

    @Expose
    @SerializedName("key")
    @ColumnInfo(name = "key")
    var key: String? = null

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String? = null

    @Expose
    @SerializedName("site")
    @ColumnInfo(name = "site")
    var site: String? = null

    @Expose
    @SerializedName("size")
    @ColumnInfo(name = "size")
    var size: Int = 0

    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "type")
    var type: String? = null
}
