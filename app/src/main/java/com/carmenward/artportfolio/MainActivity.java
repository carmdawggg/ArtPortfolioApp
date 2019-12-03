package com.carmenward.artportfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements ArtworkFragment.ContactsFragmentListener, DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {

    public static final String ARTWORK_URI = "artwork_uri";
    private ArtworkFragment artworkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            artworkFragment = new ArtworkFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, artworkFragment);
            transaction.commit();
    }

    @Override
    public void onArtworkSelected(Uri artworkUri){
        if(findViewById(R.id.fragmentContainer) != null){
            displayArtwork(artworkUri, R.id.fragmentContainer);
        }

    }

    @Override
    public void onAddArtwork(){
        if(findViewById(R.id.fragmentContainer)!=null){
            displayAddEditFragment(R.id.fragmentContainer,  null);
        }
    }

    private void displayArtwork(Uri artworkUri, int viewId){
        DetailFragment detailFragment = new DetailFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(ARTWORK_URI, artworkUri);
        detailFragment.setArguments(arguments);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewId, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayAddEditFragment(int viewId, Uri artworkUri){
        AddEditFragment addEditFragment = new AddEditFragment();

        if(artworkUri != null){
            Bundle arguments = new Bundle();
            arguments.putParcelable(ARTWORK_URI, artworkUri);
            addEditFragment.setArguments(arguments);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewId, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onArtworkDeleted(){
        getSupportFragmentManager().popBackStack();
        artworkFragment.updateArtworkList();
    }

    @Override
    public void onEditArtwork(Uri artworkUri){
        if(findViewById(R.id.fragmentContainer) != null){
            displayAddEditFragment(R.id.fragmentContainer, artworkUri);
        }
    }

    @Override
    public void onAddEditCompleted(Uri artworkUri){
        getSupportFragmentManager().popBackStack();
        artworkFragment.updateArtworkList();

    }

}
