package android.example.redditpubl;

import android.content.Intent;
import android.example.redditpubl.Comments.CommentsActivity;
import android.example.redditpubl.model.Feed;
import android.example.redditpubl.model.entry.Entry;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    URLS urls = new URLS();

    private Button btnRefreshFeed;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRefreshFeed = (Button) findViewById(R.id.btnRefreshFeed);



        init();

        btnRefreshFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });


    }

    private void init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urls.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);

        Call<Feed> call = feedAPI.getFeed();
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                Log.d(TAG, "onResponse: Server Response:" + response.toString());

                List<Entry> entries = response.body().getEntrys();

                Log.d(TAG, "onResponse: entries;" + response.body().getEntrys());

//                Log.d(TAG, "onResponse: author: " + entries.get(0).getAuthor().getName());
                Log.d(TAG, "onResponse: updated: " + entries.get(0).getUpdated());

                Date dateNow = new Date();
                Date updateDate;
                for (int i = 0; i < entries.size(); i++) {
                    String[] updated = entries.get(i).getUpdated().split("T");
                    String[] upDate = updated[0].split("-");
                    String[] upTime = updated[1].split(":");
                    Log.d(TAG, "onResponse: date: " + upDate[0]+"."+upDate[1]+"." + upDate[2] + " AD at " +
                            upTime[0] + ":" + upTime[1] + ":00 UTC");
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                        updateDate = format.parse(upDate[0]+"."+upDate[1]+"." + upDate[2] + " AD at " +
                                upTime[0] + ":" + upTime[1] + ":00 UTC");
                        dateNow = new Date();

                        entries.get(i).setUpdated(TimeUnit.MILLISECONDS.toHours(dateNow.getTime() - updateDate.getTime()) + " hours ago");

                    }
                    catch (Exception j){
                        j.printStackTrace();
                    }

                }
                Log.d(TAG, "onResponse: date " + dateNow);
//                Log.d(TAG, "onResponse: title: " + entries.get(0).getTitle());
                final ArrayList<Post> posts = new ArrayList<>();
                for (int i = 0; i < entries.size(); i++) {
                    ExtractXML extractXML1 = new ExtractXML(entries.get(i).getContent(), "<a href=");
                    List<String> postContent =  extractXML1.start();

                    // -2 тому що там силка на фото заникана в контенті
                    int lastPosition = postContent.size() - 2;
                    posts.add(new Post(
                            entries.get(i).getTitle(),
                            entries.get(i).getAuthor().getName(),
                            entries.get(i).getUpdated(),
                            postContent.get(0),
                            postContent.get(lastPosition)
                    ));
                }
                for (int j = 0; j < posts.size(); j++) {
                    Log.d(TAG, "onResponse: \n"+
                            "Post url: " + posts.get(j).getPostURL() + "\n"+
                            "Thumbnail url: " + posts.get(j).getThubnailURL() + "\n"+
                            "Title: " + posts.get(j).getTitle() + "\n"+
                            "Author: " + posts.get(j).getAuthor() + "\n"+
                            "Updated: " + posts.get(j).getData_update() + "\n");
                }
                ListView listView = (ListView) findViewById(R.id.listView);
                CustomListAdapter customListAdapter= new CustomListAdapter(MainActivity.this, R.layout.card_layout_main, posts);
                listView.setAdapter(customListAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d(TAG, "onItemClick: Clicked " + posts.get(position).toString());
                        Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                        intent.putExtra("@string/post_url", posts.get(position).getPostURL());
                        intent.putExtra("@string/post_thumbnail", posts.get(position).getThubnailURL());
                        intent.putExtra("@string/post_title", posts.get(position).getTitle());
                        intent.putExtra("@string/post_author", posts.get(position).getAuthor());
                        intent.putExtra("@string/post_updated", posts.get(position).getData_update());
                        startActivity(intent);
                    }
                });

            }



            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS:" + t.getMessage());
                Toast.makeText(MainActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
