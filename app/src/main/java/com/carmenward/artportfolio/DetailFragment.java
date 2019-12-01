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
        void onContactDeleted();
        void onEditContact(Uri contactUri);
    }

    private static final int CONTACT_LOADER = 0;
    public static DetailFragmentListener listener;
    public static Uri contactUri;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView streetTextView;
    private TextView cityTextView;
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
            contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);
        }

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        streetTextView = (TextView) view.findViewById(R.id.streetTextView);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
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
                listener.onEditContact(contactUri);
                return true;
            case R.id.action_delete:
                deleteContact();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteContact(){
        getActivity().getContentResolver().delete(contactUri, null, null);
        listener.onContactDeleted();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        CursorLoader cursorLoader;

        switch(id){
            case CONTACT_LOADER:
                cursorLoader = new CursorLoader(getActivity(), contactUri, null, null, null, null);
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

            nameTextView.setText(data.getString(titleIndex));
            phoneTextView.setText(data.getString(dateCreatedIndex));
            emailTextView.setText(data.getString(mediumIndex));
            streetTextView.setText(data.getString(dimensionsIndex));
            imageView.setImageBitmap(ImageUtils.getImage(data.getBlob(imageIndex)));

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){

    }



}
