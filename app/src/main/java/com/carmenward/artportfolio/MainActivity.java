package com.carmenward.artportfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements ArtworkFragment.ContactsFragmentListener, DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {

    public static final String CONTACT_URI = "contact_uri";
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
    public void onContactSelected(Uri contactUri){
        if(findViewById(R.id.fragmentContainer) != null){
            displayContact(contactUri, R.id.fragmentContainer);
        }

    }

    @Override
    public void onAddContact(){
        if(findViewById(R.id.fragmentContainer)!=null){
            displayAddEditFragment(R.id.fragmentContainer,  null);
        }
    }

    private void displayContact(Uri contactUri, int viewId){
        DetailFragment detailFragment = new DetailFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTACT_URI, contactUri);
        detailFragment.setArguments(arguments);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewId, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayAddEditFragment(int viewId, Uri contactUri){
        AddEditFragment addEditFragment = new AddEditFragment();

        if(contactUri != null){
            Bundle arguments = new Bundle();
            arguments.putParcelable(CONTACT_URI, contactUri);
            addEditFragment.setArguments(arguments);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewId, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onContactDeleted(){
        getSupportFragmentManager().popBackStack();
        artworkFragment.updateContactList();
    }

    @Override
    public void onEditContact(Uri contactUri){
        if(findViewById(R.id.fragmentContainer) != null){
            displayAddEditFragment(R.id.fragmentContainer, contactUri);
        }
    }

    @Override
    public void onAddEditCompleted(Uri contactUri){
        getSupportFragmentManager().popBackStack();
        artworkFragment.updateContactList();

    }

}
