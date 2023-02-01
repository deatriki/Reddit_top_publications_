package android.example.redditpubl;

import android.example.redditpubl.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface FeedAPI {


    @GET("{feed_name}/.rss")
    Call<Feed> getFeed(@Path("feed_name") String feed_name);

    @GET("top.rss")
    Call<Feed> getFeed();
}
