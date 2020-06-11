package com.lchowaniec.runmate_me.data.models

import android.os.Parcel
import android.os.Parcelable

class User(): Parcelable{
    var user_id:String= ""
    var email:String= ""
    var username :String = ""
    var description: String = ""
    var distance: Float = 0F
    var friends: Int = 0
    var activities: Int =0
    var profile_photo :String = ""
    var last_online:Long = 0L

    constructor(parcel: Parcel) : this() {
        user_id = parcel.readString().toString()
        email = parcel.readString().toString()
        username = parcel.readString().toString()
        description = parcel.readString().toString()
        distance = parcel.readFloat()
        friends = parcel.readInt()
        activities = parcel.readInt()
        profile_photo = parcel.readString().toString()
        last_online = parcel.readLong()
    }

    constructor(user_id: String, email: String, username: String ): this() {
        this.user_id = user_id
        this.email = email
        this.username = username
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(description)
        parcel.writeFloat(distance)
        parcel.writeInt(friends)
        parcel.writeInt(activities)
        parcel.writeString(profile_photo)
        parcel.writeLong(last_online)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}
