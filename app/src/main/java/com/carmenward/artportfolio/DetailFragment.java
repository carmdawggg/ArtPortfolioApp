package com.carmenward.artportfolio;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.carmenward.artportfolio.data.DatabaseDescription;



public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface DetailFragmentListener{
        void onArtworkDeleted();
        void onEditArtwork(Uri artworkUri);
    }

    private static final int ARTWORK_LOADER = 0;
    public static DetailFragmentListener listener;
    public static Uri artworkUri;

    private TextView titleTextView;
    private TextView dateCreatedTextView;
    private TextView mediumTextView;
    private TextView dimensionsTextView;
    private ImageView imageView;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (DetailFragmentListener) context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();

        if(arguments != null){
            artworkUri = arguments.getParcelable(MainActivity.ARTWORK_URI);
        }

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        dateCreatedTextView = (TextView) view.findViewById(R.id.dateCreatedTextView);
        mediumTextView = (TextView) view.findViewById(R.id.mediumTextView);
        dimensionsTextView = (TextView) view.findViewById(R.id.dimensionsTextView);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        getLoaderManager().initLoader(ARTWORK_LOADER, null, this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_edit:
                listener.onEditArtwork(artworkUri);
                return true;
            case R.id.action_delete:
                deleteArtwork();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteArtwork(){
        getActivity().getContentResolver().delete(artworkUri, null, null);
        listener.onArtworkDeleted();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        CursorLoader cursorLoader;

        switch(id){
            case ARTWORK_LOADER:
                cursorLoader = new CursorLoader(getActivity(), artworkUri, null, null, null, null);
                break;
                default:
                    cursorLoader = null;
                    break;
        }

        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){

        if(data != null && data.moveToFirst()){
            int titleIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_TITLE);
            int dateCreatedIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_DATE_CREATED);
            int mediumIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_MEDIUM);
            int dimensionsIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_DIMENSIONS);
            int imageIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_IMAGE);

            titleTextView.setText(data.getString(titleIndex));
            dateCreatedTextView.setText(data.getString(dateCreatedIndex));
            mediumTextView.setText(data.getString(mediumIndex));
            dimensionsTextView.setText(data.getString(dimensionsIndex));
            imageView.setImageBitmap(ImageUtils.convert(data.getString(imageIndex)));

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }



}
