package apps.veery.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.veery.com.facetonecrm.R;
import apps.veery.com.model.NotificationMessage;
import apps.veery.com.utilities.FontChanger;

/**
 * Created by Sijith on 11/22/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
        implements ItemTouchHelperAdapter{
    private List<NotificationMessage> messages;
    private int rowLayout;
    private static Context context;

    // if user removes notification messages signal to HomeActivity to change bottom bar count
    OnNotificationMessageChangedListener mCallback;
    public interface OnNotificationMessageChangedListener {
        void onMessageCountChanged(int messageCount);
    }

    public NotificationAdapter(List<NotificationMessage> messages, int rowLayout, Context context) {
        this.messages = messages;
        this.rowLayout = rowLayout;
        this.context = context;
        mCallback = (OnNotificationMessageChangedListener)context;
    }


    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View viewHolder = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new NotificationViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.messageBody.setText(messages.get(position).getNotificationMessage());
        holder.messageFrom.setText(messages.get(position).getFromUser());
        holder.messageDate.setText(messages.get(position).getReceivedDate());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onItemDismiss(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
        mCallback.onMessageCountChanged(messages.size());
    }


    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView messageFrom, messageBody, messageDate;

        public NotificationViewHolder(View v) {
            super(v);
            messageBody = (TextView) v.findViewById(R.id.notification_message);
            messageFrom = (TextView) v.findViewById(R.id.message_from);
            messageDate = (TextView) v.findViewById(R.id.message_created);

            List<View> sendViews = new ArrayList<>();
            sendViews.add(messageBody);
            sendViews.add(messageFrom);
            sendViews.add(messageDate);
            FontChanger.changeFont(sendViews,context,FontChanger.FONT_REGULAR);
        }

    }
}
