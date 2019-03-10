
package com.codebosses.flicks.pojo.castandcrew;

import android.os.Parcel;
import android.os.Parcelable;

public class CrewData implements Parcelable {

    private String cast_id;
    private String department;
    private Integer gender;
    private Integer id;
    private String job;
    private String name;
    private String profile_path;
    private String credit_id;

    protected CrewData(Parcel in) {
        cast_id = in.readString();
        department = in.readString();
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
        job = in.readString();
        name = in.readString();
        profile_path = in.readString();
        credit_id = in.readString();
    }

    public static final Creator<CrewData> CREATOR = new Creator<CrewData>() {
        @Override
        public CrewData createFromParcel(Parcel in) {
            return new CrewData(in);
        }

        @Override
        public CrewData[] newArray(int size) {
            return new CrewData[size];
        }
    };

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public String getCast_id() {
        return cast_id;
    }

    public void setCast_id(String cast_id) {
        this.cast_id = cast_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(cast_id);
        dest.writeString(department);
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
        dest.writeString(job);
        dest.writeString(name);
        dest.writeString(profile_path);
        dest.writeString(credit_id);
    }
}
