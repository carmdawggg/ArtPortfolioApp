package com.carmenward.artportfolio;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carmenward.artportfolio.data.DatabaseDescription;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ArtworkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface ContactsFragmentListener{
        void onContactSelected(Uri contactUri);

        void onAddContact();
    }

    private static final int CONTACTS_LOADER = 0;
    private ContactsFragmentListener listener;
    private ArtworkAdapter artworkAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_artwork, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(
                new GridLayoutManager(this.getContext(), 2));

        artworkAdapter = new ArtworkAdapter(new ArtworkAdapter.ContactClickListener(){
            @Override
            public void onClick(Uri contactUri){
                listener.onContactSelected(contactUri);
            }
        });

        recyclerView.setAdapter(artworkAdapter);

        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        recyclerView.setHasFixedSize(true);

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(
          new View.OnClickListener(){
              @Override
              public void onClick(View view){
                  listener.onAddContact();
              }
          }
        );

        return view;

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (ContactsFragmentListener) context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CONTACTS_LOADER, null, this);
        updateContactList();
    }

    public void updateContactList(){
        artworkAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        switch(id){
            case CONTACTS_LOADER:
                return new CursorLoader(getActivity(),
                        DatabaseDescription.Artwork.CONTENT_URI,
                        null, null, null, null);
                default:
                    return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
            artworkAdapter.swapCursor(data);
            updateContactList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        artworkAdapter.swapCursor(null);
        updateContactList();
    }

}
