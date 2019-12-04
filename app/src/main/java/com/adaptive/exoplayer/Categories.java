package com.adaptive.exoplayer;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.adaptive.exoplayer.database.Channel;
import com.adaptive.exoplayer.database.DatabaseHelper;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Categories.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Categories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Categories extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "hai";
    private final String tagname;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    EditText serchStream;
    private OnFragmentInteractionListener mListener;
    private static final int NUM_COLUMNS = 4;
    private ArrayList<String> mImageUrls= new ArrayList<String>();
    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mMovieurl = new ArrayList<String>();
    private Context context;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    List<Channel> channel_name;
    String cat;
    public Categories(String tagname) {
        // Required empty public constructor
        this.tagname = tagname;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Categories.
     */
    // TODO: Rename and change types and number of parameters
    public static Categories newInstance(String param1) {
        Categories fragment = new Categories(param1);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_vie);
        serchStream = (EditText) rootView.findViewById(R.id.serchStream);
        //        starting the recycler view part

        Log.i(TAG, "the recycler view id: "+container.findViewById(R.id.recycler_vie));
        Log.i(TAG, "the recycler view id: "+container.findViewById(R.id.serchStream));
        channel_name = new ArrayList<>();

//        initImageBitmaps();
//        initilalizeAlgolia();
//        channel_name.add(new Channel("https://i.imgur.com/PRgvj4c.png", "india", "hindi", "something", "miami", "http://dfsdfl.com/"));





//        ending the recycler view part
//        final Index index = initilalizeAlgolia();
        mDBHelper = new DatabaseHelper(getContext());
        Log.i(TAG, "onCreateView: tagname"+tagname);
        if (tagname == "IND") {
            channel_name = mDBHelper.getAllchannels("IN");
        }
        else if (tagname == "ALL"){
            channel_name = mDBHelper.getChannelfromCat("All");
        }
        else if (tagname == "ADULT"){
            channel_name = mDBHelper.getChannelfromCat("XXX");
        }
        else if (tagname == "SPORTS"){
            channel_name = mDBHelper.getChannelfromCat("Sport");
        }
        else if (tagname == "MOVIES"){
            channel_name = mDBHelper.getChannelfromCat("Movies");
        }
        else if (tagname == "CHN"){
            channel_name = mDBHelper.getAllchannels("CN");
        }
        else if (tagname == "NEWS"){
            channel_name = mDBHelper.getChannelfromCat("News");
        }
        else if (tagname == "BUSINESS"){
            Log.i(TAG, "onCreateView: on bussiness");
            channel_name = mDBHelper.getChannelfromCat("Business");
        }
        else if (tagname == "MUSIC"){
            channel_name = mDBHelper.getChannelfromCat("Music");
        }

//        if(tagname == "MALAYALAM"){
//            String cat = "Hindi";
//
//            mDBHelper = new DatabaseHelper(getContext());
//            try {
//                mDBHelper.updateDataBase();
//            } catch (IOException mIOException) {
//                throw new Error("UnableToUpdateDatabase");
//            }
//            try {
//                mDb = mDBHelper.getWritableDatabase();
//            } catch (SQLException mSQLException) {
//                throw mSQLException;
//            }
//            channel_name = mDBHelper.getAllchannels("Hindi");
//            Log.i(TAG, "onCreateView: "+channel_name.get(0).getUrl());
////            straggeredRecyclerViewAdapter.notifyDataSetChanged();
//        }
//        mDBHelper = new DatabaseHelper(getContext());
//        try {
//            mDBHelper.updateDataBase();
//        } catch (IOException mIOException) {
//            throw new Error("UnableToUpdateDatabase");
//        }
//        try {
//            mDb = mDBHelper.getWritableDatabase();
//        } catch (SQLException mSQLException) {
//            throw mSQLException;
//        }

        final StraggeredRecyclerViewAdapter straggeredRecyclerViewAdapter = new StraggeredRecyclerViewAdapter(getContext(), channel_name);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(straggeredRecyclerViewAdapter);
        final Index index = initilalizeAlgolia();

        serchStream.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


//                created for test
                Query query = new Query(serchStream.getText().toString())
                        .setAttributesToRetrieve("name", "image" , "url")
                        .setHitsPerPage(100);
                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                        try {
//                            mImageUrls.clear();
//                            mNames.clear();
//                            mMovieurl.clear();
                            channel_name.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("hits");
                            for(int k=0;k<jsonArray.length();k++){
                                try {
                                    channel_name.add(new Channel(jsonArray.getJSONObject(k).get("image").toString(), "","","",jsonArray.getJSONObject(k).get("name").toString(),jsonArray.getJSONObject(k).get("url").toString()));
//                                    mImageUrls.add(jsonArray.getJSONObject(k).get("image").toString());
//                                    mNames.add(jsonArray.getJSONObject(k).get("title").toString());
//                                    JSONObject object =  jsonArray.getJSONObject(k);
//                                    if(object.has("movie_url")){
//                                        mMovieurl.add(jsonArray.getJSONObject(k).get("movie_url").toString());
//                                    }
//                                    else{
//                                        mMovieurl.add("http://wow-share.ml");
//                                    }

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
        // Inflate the layout for this fragment

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        channel_name.add(new Channel("http://google.com", "india", "hindi", "something", "miami", "http://dfsdfl.com"));

//        mImageUrls.add("https://i.pinimg.com/236x/fc/c0/e0/fcc0e08ea82a91d87306c3e0ee80b862.jpg");
//        mNames.add("Isque");
//
//        mImageUrls.add("https://i.pinimg.com/236x/50/92/da/5092dadeaa640b154c5b1a066e702a15.jpg");
//        mNames.add("Mayanadhi");
//
//        mImageUrls.add("https://i.pinimg.com/236x/56/99/71/569971070ba6ce35f137bcd115f8846d.jpg");
//        mNames.add("Thannermathan dinangal");
//
//
//        mImageUrls.add("https://i.pinimg.com/236x/87/fc/56/87fc560f670306a677ff6fb9741cafdd.jpg");
//        mNames.add("Charly");
//
//        mImageUrls.add("https://i.pinimg.com/236x/d5/f7/0d/d5f70df1c2887f79841a572cfbd68f24.jpg");
//        mNames.add("Joseph");
//
//
//        mImageUrls.add("https://i.pinimg.com/236x/4e/5c/2b/4e5c2b42c874d24a8d0036945567b3d3.jpg");
//        mNames.add("Amala");
//
//        mImageUrls.add("https://i.pinimg.com/236x/e9/f3/c3/e9f3c357e825f5782694510579441db0.jpg");
//        mNames.add("June");
//
//        mImageUrls.add("https://i.pinimg.com/236x/93/a8/52/93a8522c1d5f96fb980ef1df80da95c0.jpg");
//        mNames.add("Raabta");
    }

    private Index initilalizeAlgolia(){
        Client client = new Client("K723ZLANUO", "a6d88c1f8b8a0ba18fd51e1799e8122f");
        Index index = client.getIndex("posts");
        return index;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "yup the title is: "+tagname);
    }
}
