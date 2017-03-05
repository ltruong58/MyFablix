package com.example.along.myfablix;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieSearchActivity extends AppCompatActivity {

    private List<Movie> movieList;
    private MovieListAdapter movieListAdapter;

    private EditText titleEditText;
    private ListView movieListView;

    private SearchMovie mAuthTask = null;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);
        titleEditText = (EditText) findViewById(R.id.movieTitleEditText);
        movieListView = (ListView) findViewById(R.id.moviesListView);
        movieList = new ArrayList<>();

        movieListAdapter = new MovieListAdapter(this, R.layout.movie_item, movieList);
        movieListView.setAdapter(movieListAdapter);
    }

    public void searchMovies(View view)
    {

        movieList.clear();
        movieListAdapter.notifyDataSetChanged();

        String title = titleEditText.getText().toString();

        mAuthTask = new SearchMovie(title);
        mAuthTask.execute((Void) null);

    }

    public class SearchMovie extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final String mTitle;

        SearchMovie(String title) {
            mTitle = title;
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            StringBuilder sb = new StringBuilder();
            JSONObject json = null;

            try {
                String query = URLEncoder.encode(mTitle, "utf-8");
                URL url = new URL("http://ec2-54-215-239-206.us-west-1.compute.amazonaws.com:8080/Fabflix/androidSearch?title="+query);
                // Simulate network access.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                int status = urlConnection.getResponseCode();


                switch (status) {
                    case 200:
                    case 201:

                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        json = new JSONObject(sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayList<Movie> result = new ArrayList<Movie>();
            buildList(json, result);
            return result;
        }

        private void buildList(JSONObject json, ArrayList<Movie> moviesList)
        {
            Iterator<String> iter = json.keys();
            int id;
            String title;
            int year;
            String director;
            URL bannerUrl;
            URL trailerUrl;
            while(iter.hasNext())
            {
                try {

                    JSONObject temp = (JSONObject) json.get(iter.next());

                    title = (String)temp.get("title");
                    id = (int) temp.get("id");

                    director = (String) temp.get("director");
                    //id = Integer.parseInt((String) temp.get("id"));
                    year = (int) temp.get("year");

                    bannerUrl = new URL((String) temp.get("Banner_url"));
                    trailerUrl = new URL((String) temp.get("Trailer_url"));
                    moviesList.add(new Movie(id, bannerUrl, year, trailerUrl, director, title));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesList) {
            mAuthTask = null;
            int totalMovies = moviesList.size();
            if(totalMovies != 0)
            {
                for(int i = 0; i < totalMovies; i++)
                {
                    movieListAdapter.add(moviesList.get(i));
                }
            }
            else
                Toast.makeText(context, "No movie match search key", Toast.LENGTH_LONG).show();

        }

    }
}
