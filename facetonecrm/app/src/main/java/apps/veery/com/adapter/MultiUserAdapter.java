package apps.veery.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import apps.veery.com.facetonecrm.R;
import apps.veery.com.model.EngagementUser;
import apps.veery.com.utilities.FontChanger;

/**
 * Created by Sijith on 11/16/2016.
 */
public class MultiUserAdapter extends RecyclerView.Adapter<MultiUserAdapter.MultiUserViewHolder> {
    private List<EngagementUser> users;
    private int rowLayout;
    private static Context context;

    public MultiUserAdapter(List<EngagementUser> users, int rowLayout, Context context) {
        this.users = users;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MultiUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View viewHolder = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MultiUserViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(MultiUserViewHolder holder, int position) {
        Gson gson = new Gson();
        String jsonUser = gson.toJson(users.get(position));
        if (null != users.get(position).getAvatar() && !users.get(position).getAvatar().isEmpty()) {
            Picasso.with(context).load(users.get(position).getAvatar()).resize(50, 50).into(holder.multi_user_image);
        }

        holder.multi_item_user_json.setText(jsonUser);
        holder.multi_item_user_id.setText(users.get(position).getUserId());
        holder.multi_item_user_name.setText(users.get(position).getName());
        holder.multi_item_user_number.setText(users.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addNewUser(EngagementUser engagementUser){
        if(null != engagementUser)
            users.add(0,engagementUser);
            this.notifyItemInserted(0);
    }

    public static class MultiUserViewHolder extends RecyclerView.ViewHolder {
        ImageView multi_user_image;
        TextView multi_item_user_json,multi_item_user_id, multi_item_user_name, multi_item_user_number, multi_item_user_view;

        public MultiUserViewHolder(View v) {
            super(v);
            multi_user_image = (ImageView) v.findViewById(R.id.multi_user_image);
            multi_item_user_json = (TextView) v.findViewById(R.id.multi_item_user_json);
            multi_item_user_id = (TextView) v.findViewById(R.id.multi_item_user_id);
            multi_item_user_name = (TextView) v.findViewById(R.id.multi_item_user_name);
            multi_item_user_number = (TextView) v.findViewById(R.id.multi_item_user_number);
            multi_item_user_view = (TextView) v.findViewById(R.id.multi_item_user_view);

            multi_item_user_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //changing fonts
            List<View> sendViews = new ArrayList<>();
            sendViews.add(multi_item_user_name);
            sendViews.add(multi_item_user_number);
            sendViews.add(multi_item_user_view);
            FontChanger.changeFont(sendViews, context, FontChanger.FONT_REGULAR);

        }
    }

}
