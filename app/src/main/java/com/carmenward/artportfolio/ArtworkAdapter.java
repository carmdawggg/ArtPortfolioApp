package com.carmenward.artportfolio;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.carmenward.artportfolio.data.DatabaseDescription;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ViewHolder> {

    public interface ContactClickListener{
        void onClick(Uri contactUri);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public final CardView cardView;
        public final ImageView imageView;
        private long rowID;

        public ViewHolder(View itemView){
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_holder);
            cardView = (CardView) itemView.findViewById(R.id.card_container);
            imageView = (ImageView) itemView.findViewById(R.id.image_holder);

            itemView.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            clickListener.onClick(DatabaseDescription.Artwork.buildArtworkUri(rowID));
                        }
                    }
            );
        }

        public void setRowID(long rowID){
            this.rowID = rowID;
        }

    }

    public static Cursor cursor = null;

    private final ContactClickListener clickListener;

    public ArtworkAdapter(ContactClickListener clickListener){
        this.clickListener = clickListener;
    }

        @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view  = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_view, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cursor.moveToPosition(position);
                    holder.setRowID(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Artwork._ID)));
                    holder.textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.Artwork.COLUMN_TITLE)));
                    final byte[] bytes = cursor.getBlob(cursor.getColumnIndex(DatabaseDescription.Artwork.COLUMN_IMAGE));
                    holder.imageView.post(new Runnable() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void run() {
                            holder.imageView.setImageBitmap(ImageUtils.getImage(bytes));
                            Log.d("Image Loaded From Database", "wassup");
                        }

                    });
                }
                catch (Exception e){
                    Log.d("ERROR", e.getLocalizedMessage());
                }
            }
        }).start();

    }

    @Override
    public int getItemCount(){
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

}
