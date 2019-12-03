package com.carmenward.artportfolio;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.carmenward.artportfolio.data.DatabaseDescription;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ArtworkAdapter artworkAdapter;
    private static final int SELECT_PICTURE = 100;
    private Uri selectedImageUri;
    public interface AddEditFragmentListener{
        void onAddEditCompleted(Uri artworkUri);
    }

    private static final int ARTWORK_LOADER = 0;
    private AddEditFragmentListener listener;
    private Uri artworkUri;
    private boolean addingNewArtwork = true;

    private TextInputLayout titleTextInputLayout;
    private TextInputLayout dateCreatedTextInputLayout;
    private TextInputLayout mediumTextInputLayout;
    private TextInputLayout dimensionsTextInputLayout;
    private ImageView imageView;
    private FloatingActionButton saveArtworkFAB;

    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (AddEditFragmentListener) context;
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

        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);
        titleTextInputLayout = (TextInputLayout) view.findViewById(R.id.titleTextInputLayout);
        titleTextInputLayout.getEditText().addTextChangedListener(titleChangedListener);

        dateCreatedTextInputLayout = (TextInputLayout) view.findViewById(R.id.creationDateTextInputLayout);
        dateCreatedTextInputLayout.getEditText().addTextChangedListener(dateCreatedChangedListener);
        mediumTextInputLayout = (TextInputLayout) view.findViewById(R.id.mediumTextInputLayout);
        mediumTextInputLayout.getEditText().addTextChangedListener(mediumChangedListener);
        dimensionsTextInputLayout = (TextInputLayout) view.findViewById(R.id.dimensionsTextInputLayout);
        dimensionsTextInputLayout.getEditText().addTextChangedListener(dimensionsChangedListener);

        imageView = (ImageView) view.findViewById(R.id.image_add_edit);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openImageChooser();
            }
        });

        saveArtworkFAB = (FloatingActionButton) view.findViewById(R.id.saveFloatingActionButton);

        saveArtworkFAB.setOnClickListener(saveArtworkButtonClicked);
        updateSaveButtonFAB();

        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout);

        Bundle arguments = getArguments();

        if(arguments !=null){
            addingNewArtwork = false;
            artworkUri = arguments.getParcelable(MainActivity.ARTWORK_URI);
        }

        if(artworkUri != null){
            getLoaderManager().initLoader(ARTWORK_LOADER, null, this);
        }
        return view;

    }

    public void openImageChooser(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SelectPicture"),SELECT_PICTURE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_PICTURE){
                selectedImageUri=data.getData();
                if(null!=selectedImageUri){
                    imageView.setImageURI(selectedImageUri);
                    updateSaveButtonFAB();
                }
            }
        }
    }


    private final TextWatcher titleChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateSaveButtonFAB();

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private final TextWatcher dateCreatedChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateSaveButtonFAB();

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private final TextWatcher mediumChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateSaveButtonFAB();

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private final TextWatcher dimensionsChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            updateSaveButtonFAB();

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void updateSaveButtonFAB(){
        String input = titleTextInputLayout.getEditText().getText().toString();

        if(input.trim().length() != 0){
            saveArtworkFAB.show();
        }
        else{
            saveArtworkFAB.hide();
        }

    }

    private final View.OnClickListener saveArtworkButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getView().getWindowToken(), 0
            );
            saveArtwork();
        }
    };

    private void saveArtwork(){
        ContentValues contentValues = new ContentValues();

        InputStream iStream = null;
        try {
            if(selectedImageUri != null) {
                iStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                byte[] inputData = ImageUtils.getBytes(iStream);
                contentValues.put(DatabaseDescription.Artwork.COLUMN_IMAGE, inputData);
                Log.d("gey", "ima ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        contentValues.put(DatabaseDescription.Artwork.COLUMN_TITLE,
                titleTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Artwork.COLUMN_DATE_CREATED,
                dateCreatedTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Artwork.COLUMN_MEDIUM,
                mediumTextInputLayout.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Artwork.COLUMN_DIMENSIONS,
                dimensionsTextInputLayout.getEditText().getText().toString());

        if(addingNewArtwork){
            Uri newArtworkUri = getActivity().getContentResolver().insert(DatabaseDescription.Artwork.CONTENT_URI, contentValues);

            if(newArtworkUri != null){
                Snackbar.make(coordinatorLayout, R.string.artwork_added, Snackbar.LENGTH_LONG).show();
                listener.onAddEditCompleted(newArtworkUri);
            }
            else{
                Snackbar.make(coordinatorLayout, R.string.artwork_not_added, Snackbar.LENGTH_LONG).show();
            }


        }
        else{

            int updatedRows = getActivity().getContentResolver().update(artworkUri, contentValues, null, null);

            if(updatedRows > 0){
                listener.onAddEditCompleted(artworkUri);
                Snackbar.make(coordinatorLayout, R.string.artwork_updated, Snackbar.LENGTH_LONG).show();
            }
            else{
                Snackbar.make(coordinatorLayout, R.string.artwork_not_updated, Snackbar.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){

        switch(id){
            case ARTWORK_LOADER:
                return new CursorLoader(getActivity(), artworkUri, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){

        if(data != null && data.moveToFirst()){
            int titleIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_TITLE);
            int dateCreatedIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_DATE_CREATED);
            int mediumIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_MEDIUM);
            int dimensionsIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_DIMENSIONS);
            int imageIndex = data.getColumnIndex(DatabaseDescription.Artwork.COLUMN_IMAGE);

            titleTextInputLayout.getEditText().setText(data.getString(titleIndex));
            dateCreatedTextInputLayout.getEditText().setText(data.getString(dateCreatedIndex));
            mediumTextInputLayout.getEditText().setText(data.getString(mediumIndex));
            dimensionsTextInputLayout.getEditText().setText(data.getString(dimensionsIndex));

            imageView.setImageBitmap(ImageUtils.getImage(data.getBlob(imageIndex)));

            updateSaveButtonFAB();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){}

}
