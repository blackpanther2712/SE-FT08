package com.ft08.trailblazelearn.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.models.ContributedItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neelimabenny on 20/3/18.
 */

class ImageTypeViewHolder extends  RecyclerView.ViewHolder {

    TextView userName;
    ImageView imageItem;
    TextView title;
    TextView description;
    ImageView profilePicture;
    LinearLayout linearLayout;
    CardView cardView;


    public ImageTypeViewHolder(View itemView){
        super(itemView);

        this.userName = (TextView) itemView.findViewById(R.id.user_name);
        this.imageItem = (ImageView) itemView.findViewById(R.id.item_image);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
        this.profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
        this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        this.cardView = (CardView) itemView.findViewById(R.id.card_view);


    }
}

class AudioTypeViewHolder extends RecyclerView.ViewHolder{

    TextView userName;
    FloatingActionButton floatingActionButton;
    TextView title;
    TextView description;
    ImageView profilePicture;
    LinearLayout linearLayout;
    CardView cardView;

    public  AudioTypeViewHolder (View itemView){
        super(itemView);

        this.userName = (TextView) itemView.findViewById(R.id.user_name);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.floatingActionButton = (FloatingActionButton) itemView.findViewById(R.id.floatingActionButton);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
        this.profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
        this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        this.cardView = (CardView) itemView.findViewById(R.id.card_view);
    }
}

class DocumentTypeViewHolder extends RecyclerView.ViewHolder{
    TextView userName;
    FloatingActionButton floatingActionButton;
    TextView title;
    TextView description;
    ImageView profilePicture;
    LinearLayout linearLayout;
    CardView cardView;

    public DocumentTypeViewHolder(View itemView){
        super(itemView);
        this.userName = (TextView) itemView.findViewById(R.id.user_name);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.floatingActionButton = (FloatingActionButton) itemView.findViewById(R.id.floatingActionButton);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
        this.profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
        this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        this.cardView = (CardView) itemView.findViewById(R.id.card_view);

    }

}

class VideoTypeViewHolder extends RecyclerView.ViewHolder{
    TextView userName;
    ImageButton playVideoButton;
    TextView title;
    TextView description;
    ImageView profilePicture;
    LinearLayout linearLayout;
    CardView cardView;

    public VideoTypeViewHolder(View itemView){
        super(itemView);
        this.userName = (TextView) itemView.findViewById(R.id.user_name);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.playVideoButton = (ImageButton) itemView.findViewById(R.id.video_button);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
        this.profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
        this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        this.cardView = (CardView) itemView.findViewById(R.id.card_view);
    }

}



public class ContributedItemAdapter extends RecyclerView.Adapter {

    private ArrayList<ContributedItem> dataSet;
    Context context;
    int total_types;
    MediaPlayer mediaPlayer;
    private boolean fabStateVolume = false;

    public ContributedItemAdapter(ArrayList<ContributedItem> dataSet, Context context) {
        super();
        this.dataSet = dataSet;
        this.context = context;
        total_types = dataSet.size();
    }



    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return ContributedItem.IMAGE_TYPE;
            case 1:
                return ContributedItem.AUDIO_TYPE;
            case 2:
                return ContributedItem.DOCUMENT_TYPE;
            case 3:
                return ContributedItem.VIDEO_TYPE;
            default:
                return -1;
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        View view;

        if (viewType == ContributedItem.IMAGE_TYPE) {

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.image_blog_item, parent, false);
            return new ImageTypeViewHolder(view);

        } else if (viewType == ContributedItem.AUDIO_TYPE){

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.audio_blog_item,parent,false);
            return  new AudioTypeViewHolder(view);

        } else if (viewType == ContributedItem.DOCUMENT_TYPE){

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.document_blog_item,parent,false);
            return  new DocumentTypeViewHolder(view);

        } else if (viewType == ContributedItem.VIDEO_TYPE){

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.video_blog_item,parent,false);
            return  new VideoTypeViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final ContributedItem object = dataSet.get(position);
        if (object != null) {
            switch (object.type) {

                case ContributedItem.IMAGE_TYPE:


                    //holder.setIsRecyclable(false);
                    ((ImageTypeViewHolder) holder).userName.setText(object.getUserName());
                    Picasso.with(((ImageTypeViewHolder) holder).imageItem.getContext()).load(object.getFileURL()).into(((ImageTypeViewHolder) holder).imageItem);
                    Picasso.with(((ImageTypeViewHolder) holder).profilePicture.getContext()).load(object.getOwnerProfilePhotoUrl()).into(((ImageTypeViewHolder) holder).profilePicture);
                    ((ImageTypeViewHolder) holder).title.setText(object.getTitle());
                    ((ImageTypeViewHolder) holder).description.setText(object.getDescription());

                    break;

                case ContributedItem.AUDIO_TYPE:


                    //holder.setIsRecyclable(false);
                    ((AudioTypeViewHolder) holder).userName.setText(object.getUserName());
                    ((AudioTypeViewHolder) holder).title.setText(object.getTitle());
                    ((AudioTypeViewHolder) holder).description.setText(object.getDescription());
                    Picasso.with(((AudioTypeViewHolder) holder).profilePicture.getContext()).load(object.getOwnerProfilePhotoUrl()).into(((AudioTypeViewHolder) holder).profilePicture);
                    ((AudioTypeViewHolder) holder).floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (fabStateVolume) {

                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                }

                                ((AudioTypeViewHolder) holder).floatingActionButton.setImageResource(R.drawable.ic_action_play_icon);
                                fabStateVolume = false;

                            } else {

                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {

                                    mediaPlayer.setDataSource(object.getFileURL());

                                } catch (IOException e) {

                                    e.printStackTrace();
                                }

                                mediaPlayer.setLooping(true);
                                try {

                                    mediaPlayer.prepare();

                                } catch (IOException e) {

                                    e.printStackTrace();

                                }

                                mediaPlayer.start();

                                ((AudioTypeViewHolder) holder).floatingActionButton.setImageResource(R.drawable.ic_action_pause_icon);
                                fabStateVolume = true;

                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mediaPlayer.release();
                                    }
                                });
                            }
                        }
                    });

                    break;

                case ContributedItem.DOCUMENT_TYPE:


                    //holder.setIsRecyclable(false);
                    ((DocumentTypeViewHolder) holder).userName.setText(object.getUserName());
                    ((DocumentTypeViewHolder) holder).title.setText(object.getTitle());
                    ((DocumentTypeViewHolder) holder).description.setText(object.getDescription());
                    Picasso.with(((DocumentTypeViewHolder) holder).profilePicture.getContext()).load(object.getOwnerProfilePhotoUrl()).into(((DocumentTypeViewHolder) holder).profilePicture);
                    ((DocumentTypeViewHolder) holder).floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ((DocumentTypeViewHolder) holder).floatingActionButton.setImageResource(R.drawable.ic_action_document_icon);
                            Uri fireBaseUrl = Uri.parse(object.getFileURL());
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,fireBaseUrl);
                            context.startActivity(browserIntent);

                        }
                    });

                    break;

                case ContributedItem.VIDEO_TYPE:

                    ((VideoTypeViewHolder) holder).userName.setText(object.getUserName());
                    ((VideoTypeViewHolder) holder).title.setText(object.getTitle());
                    ((VideoTypeViewHolder) holder).description.setText(object.getDescription());
                    Picasso.with(((VideoTypeViewHolder) holder).profilePicture.getContext()).load(object.getOwnerProfilePhotoUrl()).into(((VideoTypeViewHolder) holder).profilePicture);
                    ((VideoTypeViewHolder) holder).playVideoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((VideoTypeViewHolder) holder).playVideoButton.setImageResource(R.drawable.ic_action_play_picture);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(object.getFileURL()),"video/mp4");
                            context.startActivity(intent);

                        }
                    });
            }

        }

    }

    @Override
    public int getItemCount() {

        return dataSet.size();
    }


}

