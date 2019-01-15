package com.codebosses.flicks.pojo.expandrecyclerviewpojo;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryItem implements Parcelable {

    private String name;
    private boolean isFavorite;

    public CategoryItem(String name, boolean isFavorite) {
        this.name = name;
        this.isFavorite = isFavorite;
    }

    protected CategoryItem(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryItem)) return false;

        CategoryItem artist = (CategoryItem) o;

        if (isFavorite() != artist.isFavorite()) return false;
        return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (isFavorite() ? 1 : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryItem> CREATOR = new Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel in) {
            return new CategoryItem(in);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };
}

