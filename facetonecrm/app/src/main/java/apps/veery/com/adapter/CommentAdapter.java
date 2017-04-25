package apps.veery.com.adapter;

/**
 * Created by Lakshan on 9/22/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import apps.veery.com.facetonecrm.R;
import apps.veery.com.model.Comment;
import apps.veery.com.utilities.DateMatcher;
import apps.veery.com.utilities.FontChanger;
import de.hdodenhof.circleimageview.CircleImageView;



public class CommentAdapter extends ArrayAdapter<Comment> {
    private List<Comment> commentsList;
    private final Context context;
//    SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
//    SimpleDateFormat sdfFormat = new SimpleDateFormat("MMM dd @ HH:mm a");
//    Date date;

    public CommentAdapter(Context context, int resource, List<Comment> comments) {
        super(context, resource, comments);
        this.context = context;
        this.commentsList = comments;
    }

    static class ViewHolder {
        ImageView avatar;
        TextView date,name,comment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView == null){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.avatar = (CircleImageView) v.findViewById(R.id.comment_item_avatar);
            viewHolder.date = (TextView) v.findViewById(R.id.comment_item_date);
            viewHolder.name = (TextView) v.findViewById(R.id.comment_item_user_name);
            viewHolder.comment = (TextView) v.findViewById(R.id.comment_item_comment);
            List<View> sendViews = new ArrayList<>();
            sendViews.add(viewHolder.date);
            sendViews.add(viewHolder.name);
            sendViews.add(viewHolder.comment);
            FontChanger.changeFont(sendViews,context,FontChanger.FONT_REGULAR);
            v.setTag(viewHolder);
        }
        else{
            v = convertView;
        }

        ViewHolder holder = (ViewHolder) v.getTag();
        String url = commentsList.get(position).getAuthor().getAvatar();
        if(null != url){
            Picasso.with(parent.getContext())
                    .load(url)
                    .placeholder(R.drawable.avatar)
                    .resize(50, 50)
                    .into(holder.avatar);
        }
//
//        sdfToDate.setTimeZone(TimeZone.getTimeZone("UTC"));
//        try {
//            date = sdfToDate.parse(commentsList.get(position).getCreatedAt());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        holder.date.setText(sdfFormat.format(date));
        holder.date.setText(DateMatcher.getTicketDuration(commentsList.get(position).getCreatedAt()));
        holder.name.setText(commentsList.get(position).getAuthor().getName());
        holder.comment.setText(commentsList.get(position).getBody());

        return v;
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }
}

