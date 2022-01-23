package com.example.oneread.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import com.example.oneread.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Parcelable {

    @SerializedName("endpoint")
    @Expose
    private String endpoint;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("rate_count")
    @Expose
    private Integer rateCount;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("search_number")
    @Expose
    private Integer searchNumber;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("follow")
    @Expose
    private String follow;
    @SerializedName("view")
    @Expose
    private String view;
    @SerializedName("chapters")
    @Expose
    private List<Chapter> chapters;

    public Book() {
        chapters = new ArrayList<>();
        genres = new ArrayList<>();
    }

    protected Book(Parcel in) {
        endpoint = in.readString();
        title = in.readString();
        author = in.readString();
        thumb = in.readString();
        theme = in.readString();
        description = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readInt();
        }
        if (in.readByte() == 0) {
            rateCount = null;
        } else {
            rateCount = in.readInt();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        if (in.readByte() == 0) {
            searchNumber = null;
        } else {
            searchNumber = in.readInt();
        }
        genres = in.createTypedArrayList(Genre.CREATOR);
        follow = in.readString();
        view = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRateCount() {
        return rateCount;
    }

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSearchNumber() {
        return searchNumber;
    }

    public void setSearchNumber(Integer searchNumber) {
        this.searchNumber = searchNumber;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getStatusString() {
        if (status == 0) {
            return "Đang tiến hành";
        } else {
            return "Đã hoàn thành";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(endpoint);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(thumb);
        parcel.writeString(theme);
        parcel.writeString(description);
        parcel.writeString(type);
        if (rating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(rating);
        }
        if (rateCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(rateCount);
        }
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        if (searchNumber == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(searchNumber);
        }
        parcel.writeTypedList(genres);
        parcel.writeString(follow);
        parcel.writeString(view);
    }
}
