package com.ft08.discussionthread;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("APP", "Creating new view");
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.message_item, parent, false);
        }

        ImageView photo = (ImageView) row.findViewById(R.id.messagePhoto);
        TextView name = (TextView) row.findViewById((R.id.nameTextView));
        TextView message = (TextView) row.findViewById(R.id.messageTextView);

        Post post = getItem(position);
        name.setText(post.getName());
        boolean isPhoto = post.getPhotoUrl() != null;

        if(isPhoto) {
            message.setVisibility(View.GONE);
            photo.setVisibility(View.VISIBLE);
            Glide.with(photo.getContext())
                 .load(post.getPhotoUrl())
                 .into(photo);
        }
        else {
            message.setVisibility(View.VISIBLE);
            photo.setVisibility(View.GONE);
            message.setText(post.getText());
        }
        return row;
    }
}
