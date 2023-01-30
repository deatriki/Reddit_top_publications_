package android.example.redditpubl;

import android.example.redditpubl.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedAPI {
    String URL = "https://www.reddit.com/";

    @GET("top.rss")
    Call<Feed> getFeed();
}
