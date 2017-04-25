package apps.veery.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import apps.veery.com.facetonecrm.R;

/**
 * Created by sajith on 3/27/17.
 */

public class AttachmentAdapter extends ArrayAdapter<String> {
    private List<String> attachments;
    private final Context context;

    public AttachmentAdapter(Context context, int resource, List<String> attachments) {
        super(context, resource, attachments);
        this.context = context;
        this.attachments = attachments;
    }


    private static class ViewHolder {
        ImageView attachmentItem;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView == null){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_item,parent,false);

            AttachmentAdapter.ViewHolder viewHolder = new AttachmentAdapter.ViewHolder();

            viewHolder.attachmentItem = (ImageView) v.findViewById(R.id.attachment_id);
            v.setTag(viewHolder);
        }
        else{
            v = convertView;
        }

        final AttachmentAdapter.ViewHolder holder = (AttachmentAdapter.ViewHolder) v.getTag();
        if (null != attachments) {
            Picasso.with(context).load(attachments.get(position)).resize(50, 50).into(holder.attachmentItem);
        }
        holder.attachmentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, TicketDetailActivity.class);
//                intent.putExtra("tv_tag", holder.sub_tv_tag.getText());
//                intent.putExtra("ticketPosition", "0"); // no need to pass a position because we are not gonna change this sub ticket
//                context.startActivity(intent);
                Log.d("Test","attachment clicked");
            }
        });

        return v;
    }

    @Override
    public int getCount() {
        return attachments.size();
    }

}
