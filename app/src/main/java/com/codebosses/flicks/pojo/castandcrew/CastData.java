
package com.codebosses.flicks.pojo.castandcrew;

import android.os.Parcel;
import android.os.Parcelable;

public class CastData implements Parcelable {

    private Integer cast_id;
    private String character;
    private String credit_id;
    private Integer gender;
    private Integer id;
    private String name;
    private Integer order;
    private String profile_path;

    protected CastData(Parcel in) {
        if (in.readByte() == 0) {
            cast_id = null;
        } else {
            cast_id = in.readInt();
        }
        character = in.readString();
        credit_id = in.readString();
        if (in.readByte() == 0) {
            gender = null;
        } else {
            gender = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        if (in.readByte() == 0) {
            order = null;
        } else {
            order = in.readInt();
        }
        profile_path = in.readString();
    }

    public static final Creator<CastData> CREATOR = new Creator<CastData>() {
        @Override
        public CastData createFromParcel(Parcel in) {
            return new CastData(in);
        }

        @Override
        public CastData[] newArray(int size) {
            return new CastData[size];
        }
    };

    public Integer getCast_id() {
        return cast_id;
    }

    public void setCast_id(Integer cast_id) {
        this.cast_id = cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (cast_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cast_id);
        }
        dest.writeString(character);
        dest.writeString(credit_id);
        if (gender == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gender);
        }
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        if (order == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(order);
        }
        dest.writeString(profile_path);
    }
}
