package com.example.oneread.Retrofit;

import androidx.annotation.Nullable;
import com.example.oneread.Model.Book;
import com.example.oneread.Model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.reactivex.Observable;
import retrofit2.http.*;

public interface IServiceAPI {


    @POST("user/register")
    Observable<JsonObject> registerAccount(@Body User user);

    @POST("user/login")
    Observable<JsonObject> login(@Body User user);

    @POST("book/all")
    Observable<JsonObject> getBooks(@Body JsonObject filter);

    @GET("book/top-follow")
    Observable<JsonObject> getTopFollow();

    @GET("book/top-rating")
    Observable<JsonObject> getTopRating();

    @GET("book/last-update")
    Observable<JsonObject> getLastUpdate();

    @GET("book/top-view-day")
    Observable<JsonObject> getTopDay();

    @GET("book/top-view-month")
    Observable<JsonObject> getTopMonth();

    @GET("book/top-view-year")
    Observable<JsonObject> getTopYear();

    @GET("book/top-search")
    Observable<JsonObject> getTopSearch();

    @GET("genre/all")
    Observable<JsonObject> getGenres();

    @GET("book/detail/one-punch-man")
    Observable<Book> getBook();

//    @GET("genres/{genre}/{query}")
//    Observable<List<Comic>> filterComics(@Path("genre")String genre,@Path("query")String query);
//
//    @GET("genres/{genre}")
//    Observable<List<Comic>> filterComics(@Path("genre")String genre);
//
//    @GET("comics")
//    Observable<List<Comic>> getComics();
//
//    @GET("genres")
//    Observable<List<Genre>> getGenres();
//
//    @GET("search/{query}")
//    Observable<List<Comic>> getSearchComic(@Path("query")String query);
//
//    @GET("{endpoint}")
//    Observable<List<DetailComic>> getDetailComic(@Path("endpoint")String endpoint);
//
//    @GET("chapter/{chapter_endpoint}")
//    Observable<List<Chapter>> getChapter(@Path("chapter_endpoint")String chapter_endpoint);
//
//    @POST("chapter")
//    @FormUrlEncoded
//    Observable<List<Chapter>> getListChapter(@Field("data") String data);
//
//    @POST("filter_genres")
//    @FormUrlEncoded
//    Observable<List<Comic>> filterGenres(@Field("data") String data);
}
