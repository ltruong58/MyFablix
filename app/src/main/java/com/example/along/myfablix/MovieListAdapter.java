package com.example.along.myfablix;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 3/2/2017.
 */

public class MovieListAdapter extends ArrayAdapter<Movie>{
    private Context mContext;
    private List<Movie> mMovieList = new ArrayList<>();
    private int mResourceId;

    private TextView titleTextView;
    private LinearLayout movieItemLinearLayout;
    private ImageView bannerImageView;
    /**
     * Creates a new <code>TaskListAdapter</code> given a mContext, resource id and list of movies.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param movies The list of movies to display
     */
    public MovieListAdapter(Context c, int rId, List<Movie> movies) {
        super(c, rId, movies);
        mContext = c;
        mResourceId = rId;
        mMovieList = movies;
    }

    /**
     * Gets the view associated with the layout
     * @param pos The position of the Task selected.
     * @param convertView The converted view.
     * @param parent The parent - ArrayAdapter
     * @return The new view with all content (CheckBox) set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final Movie selectedMovie= mMovieList.get(pos);
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        movieItemLinearLayout = (LinearLayout) view.findViewById(R.id.movieItemLayout);
        bannerImageView = (ImageView) view.findViewById(R.id.bannerImageView);

        titleTextView.setText(selectedMovie.getTitle());

        URL url = selectedMovie.getBannerUrl();

        new DownloadImageTask(bannerImageView)
                .execute(url.toString());
        /*try {
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            bannerImageView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        movieItemLinearLayout.setTag(selectedMovie);

        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
