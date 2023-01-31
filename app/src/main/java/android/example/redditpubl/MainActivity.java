package android.example.redditpubl;

import android.example.redditpubl.model.Feed;
import android.example.redditpubl.model.entry.Entry;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String BASE_URL = "https://www.reddit.com/";

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
                .baseUrl(BASE_URL)
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
//                Log.d(TAG, "onResponse: updated: " + entries.get(0).getUpdated());
//                Log.d(TAG, "onResponse: title: " + entries.get(0).getTitle());
                ArrayList<Post> posts = new ArrayList<>();
                for (int i = 0; i < entries.size(); i++) {
                    ExtractXML extractXML1 = new ExtractXML(entries.get(i).getContent(), "<a href=");
                    List<String> postContent =  extractXML1.start();

                    ExtractXML extractXML2 = new ExtractXML(entries.get(i).getContent(), "<img src=");
                    /*try {
                        postContent.add(extractXML2.start().get(0));
                    }catch (NullPointerException e){
                        postContent.add(null);
                        Log.e(TAG, "onResponse: NullPointerException(thumbnail): "+ e.getMessage() );
                    }
                    catch (IndexOutOfBoundsException e){
                        postContent.add(null);
                        Log.e(TAG, "onResponse: IndexOutOfBoundsException(thumbnail): "+ e.getMessage() );
                    }*/

                    // -2 тому що там силка на фото заникана і короче так надо (если не понятно контент чекни)
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

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS:" + t.getMessage());
                Toast.makeText(MainActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
