package com.adaptive.exoplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int NUM_COLUMNS = 4;
    private ArrayList<String> mImageUrls= new ArrayList<String>();
    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mMovieurl = new ArrayList<String>();
    RecyclerView recyclerView;
    EditText serchStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        serchStream = (EditText) findViewById(R.id.serchStream);
        initImageBitmaps();

//        starting the recycler view part
        final StraggeredRecyclerViewAdapter straggeredRecyclerViewAdapter = new StraggeredRecyclerViewAdapter(this, mNames, mImageUrls ,mMovieurl);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(straggeredRecyclerViewAdapter);
//        ending the recycler view part

        final Index index = initilalizeAlgolia();

        serchStream.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


//                created for test
                Query query = new Query(serchStream.getText().toString())
                        .setAttributesToRetrieve("title", "image" , "movie_url")
                        .setHitsPerPage(100);
                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                        try {
                            mImageUrls.clear();
                            mNames.clear();
                            mMovieurl.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("hits");
                            for(int k=0;k<jsonArray.length();k++){
                                try {
                                    mImageUrls.add(jsonArray.getJSONObject(k).get("image").toString());
                                    mNames.add(jsonArray.getJSONObject(k).get("title").toString());
                                    JSONObject object =  jsonArray.getJSONObject(k);
                                    if(object.has("movie_url")){
                                        mMovieurl.add(jsonArray.getJSONObject(k).get("movie_url").toString());
                                    }
                                    else{
                                        mMovieurl.add("http://wow-share.ml");
                                    }

                                    straggeredRecyclerViewAdapter.notifyDataSetChanged();
                                } catch (JSONException te) {
                                    te.printStackTrace();
                                }
                            }
                            Log.d(TAG, "requestCompleted: "+jsonObject.getJSONArray("hits"));
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

//                ends the test
//                initializeRecyclerView();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://i.pinimg.com/564x/72/d7/5f/72d75ff6081f73ec1ee11d59df62bc0e.jpg");
        mNames.add("Virus");

        mImageUrls.add("https://i.pinimg.com/236x/fc/c0/e0/fcc0e08ea82a91d87306c3e0ee80b862.jpg");
        mNames.add("Isque");

        mImageUrls.add("https://i.pinimg.com/236x/50/92/da/5092dadeaa640b154c5b1a066e702a15.jpg");
        mNames.add("Mayanadhi");

        mImageUrls.add("https://i.pinimg.com/236x/56/99/71/569971070ba6ce35f137bcd115f8846d.jpg");
        mNames.add("Thannermathan dinangal");


        mImageUrls.add("https://i.pinimg.com/236x/87/fc/56/87fc560f670306a677ff6fb9741cafdd.jpg");
        mNames.add("Charly");

        mImageUrls.add("https://i.pinimg.com/236x/d5/f7/0d/d5f70df1c2887f79841a572cfbd68f24.jpg");
        mNames.add("Joseph");


        mImageUrls.add("https://i.pinimg.com/236x/4e/5c/2b/4e5c2b42c874d24a8d0036945567b3d3.jpg");
        mNames.add("Amala");

        mImageUrls.add("https://i.pinimg.com/236x/e9/f3/c3/e9f3c357e825f5782694510579441db0.jpg");
        mNames.add("June");

        mImageUrls.add("https://i.pinimg.com/236x/93/a8/52/93a8522c1d5f96fb980ef1df80da95c0.jpg");
        mNames.add("Raabta");
    }
    private Index initilalizeAlgolia(){
        Client client = new Client("K723ZLANUO", "a6d88c1f8b8a0ba18fd51e1799e8122f");
        Index index = client.getIndex("posts");
        return index;
    }
}
