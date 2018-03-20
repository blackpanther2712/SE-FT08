package com.ft08.trailblazelearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.models.ContributedItem;
import com.squareup.picasso.Picasso;

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
    LinearLayout linearLayout;
    CardView cardView;


    public ImageTypeViewHolder(View itemView){
        super(itemView);

        this.userName = (TextView) itemView.findViewById(R.id.user_name);
        this.imageItem = (ImageView) itemView.findViewById(R.id.item_image);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
        this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        this.cardView = (CardView) itemView.findViewById(R.id.card_view);

    }
}

public class ContributedItemAdapter extends RecyclerView.Adapter {

    private ArrayList<ContributedItem> dataSet;
    Context context;
    int total_types;

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
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContributedItem object = dataSet.get(position);
        if (object != null) {
            switch (object.type) {

                case ContributedItem.IMAGE_TYPE:


                    //holder.setIsRecyclable(false);
                    ((ImageTypeViewHolder) holder).userName.setText(object.getUserName());
                    Picasso.with(((ImageTypeViewHolder) holder).imageItem.getContext()).load(object.getFileURL()).into(((ImageTypeViewHolder) holder).imageItem);
                    ((ImageTypeViewHolder) holder).title.setText(object.getTitle());
                    ((ImageTypeViewHolder) holder).description.setText(object.getDescription());

                    break;


            }

        }

    }

    @Override
    public int getItemCount() {

        return dataSet.size();
    }


}
