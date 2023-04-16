
package org.techtowm.retrofit2.Data;

import android.os.Parcel;
import android.os.Parcelable;
/*
Parcel 객체이다. LoginActivity 에서 로그인 성공시 데이터베이스에서 'userIdx' 와
'userAge' 데이터를 받아와 Intent에 put하고 UserActivity에서 get 한후 TextView에 setText 한다.
 */
public class UserData implements Parcelable {
    public String userId;
    public Integer userIdx;
    public String userName;
    //public Integer userAge;

    public UserData( String id, Integer idx, String name ) {
        userId = id;
        userIdx = idx;
        userName = name;
        // userAge = age;
    }

    public UserData(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        if (in.readByte() == 0) {
            userIdx = null;
        } else {
            userIdx = in.readInt();
        }
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(userName);
        if (userIdx == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userIdx);
        }
    }
}