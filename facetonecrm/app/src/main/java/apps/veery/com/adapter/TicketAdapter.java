package apps.veery.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.SparseBooleanArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import apps.veery.com.facetonecrm.R;
import apps.veery.com.model.Ticket;
import apps.veery.com.utilities.DateMatcher;
import apps.veery.com.utilities.FontChanger;

/**
 * Created by Sijith on 9/12/2016.
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    public static final String VOICE_MAIL = "voicemail";
    public static final String NEW = "new";
    public static final String CALL = "call";
    public static final String CHAT = "chat";
    public static final String EMAIL = "email";
    public static final String SMS = "sms";
    public static final String FACEBOOK_POST = "facebook-post";
    public static final String FACEBOOK_CHAT = "facebook-chat";
    public static final String TWITTER = "twitter";
    public static final String SKYPE = "skype";
    public static final String API = "api";
    public static final String WEB_WIDGET = "web-widget";
    private List<Ticket> tickets;
    private List<Ticket> ticketsCopy;
    private int rowLayout;
    private static Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    Gson gson = new Gson();

    public TicketAdapter(List<Ticket> tickets, int rowLayout, Context context) {
        this.tickets = tickets;
        this.ticketsCopy = new ArrayList<Ticket>();
        ticketsCopy.addAll(tickets);
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView ticketSubject, ticketStatus,ticketPriority,ticketDescription,ticketReference;
        TextView ticketCreated,ticketType,ticketTag,commentCount,ticket_by;
        ImageView channelImage;
        View priorityBulb;

        public TicketViewHolder(View v) {
            super(v);
            ticketTag = (TextView) v.findViewById(R.id.ticket_tag);
            commentCount = (TextView) v.findViewById(R.id.ticket_comment_amount);
            ticketSubject = (TextView) v.findViewById(R.id.ticket_subject);
            ticketStatus = (TextView) v.findViewById(R.id.ticket_status);
            ticketPriority = (TextView) v.findViewById(R.id.ticket_priority);
            priorityBulb = v.findViewById(R.id.priority_bulb);
            ticketDescription = (TextView) v.findViewById(R.id.ticket_description);
            ticketReference = (TextView) v.findViewById(R.id.ticket_reference);
            ticketCreated = (TextView) v.findViewById(R.id.ticket_created);
            ticketType=(TextView)v.findViewById(R.id.ticket_type);
            ticket_by =(TextView)v.findViewById(R.id.ticket_by);
            channelImage =(ImageView)v.findViewById(R.id.channel_image);

            List<View> sendViews = new ArrayList<>();
            sendViews.add(commentCount);
            sendViews.add(ticketSubject);
            sendViews.add(ticketStatus);
            sendViews.add(ticketPriority);
            sendViews.add(ticketDescription);
            sendViews.add(ticketReference);
            sendViews.add(ticketCreated);
            sendViews.add(ticketType);
            sendViews.add(ticket_by);
            FontChanger.changeFont(sendViews,context,FontChanger.FONT_REGULAR);
        }

    }

    public void addNewItems(List<Ticket> newTickets){
        if(null != newTickets)
            tickets.addAll(newTickets);
    }

    public void addNewItem(Ticket newTicket){
        if(null != newTicket)
            tickets.add(0,newTicket);
    }

    public void updateItem(int position,Ticket updateTicket){
        if(null != updateTicket)
            tickets.add(position,updateTicket);
        notifyItemChanged(position);
    }

    @Override
    public TicketAdapter.TicketViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View viewHolder = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TicketViewHolder(viewHolder);
    }


    @Override
    public void onBindViewHolder(TicketViewHolder holder, final int position) {
        String priority = tickets.get(position).getPriority();
        holder.ticketTag.setText(gson.toJson(tickets.get(position))); // serializing the object for passing to next views
        holder.ticketSubject.setText(tickets.get(position).getSubject());
        holder.commentCount.setText(String.valueOf(tickets.get(position).getComments().size()));
        holder.ticketStatus.setText(tickets.get(position).getStatus());
        holder.ticketPriority.setText(priority);
        holder.ticketDescription.setText(tickets.get(position).getDescription());
        holder.ticketReference.setText(tickets.get(position).getSubmitter().getName());
        holder.ticketCreated.setText(DateMatcher.getTicketDuration(tickets.get(position).getCreated_at()));
        holder.ticketType.setText(tickets.get(position).getType());
        // for now....check the chanel and place icon appropriately *********
//        holder.channelImage.setImageResource(R.drawable.facebook_icon);
        switch(tickets.get(position).getChannel()){
            case VOICE_MAIL:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            case NEW:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            case CALL:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            case CHAT:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            case EMAIL:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            case SMS:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            case FACEBOOK_POST:
                holder.channelImage.setImageResource(R.drawable.facebook_icon);
                break;
            case FACEBOOK_CHAT:
                holder.channelImage.setImageResource(R.drawable.facebook_chat);
                break;
            case TWITTER:
                holder.channelImage.setImageResource(R.drawable.twitter);
                break;
            case SKYPE:
                holder.channelImage.setImageResource(R.drawable.skype);
                break;
            case API:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            case WEB_WIDGET:
                holder.channelImage.setImageResource(R.drawable.common_logo);
                break;
            default:
                holder.channelImage.setImageResource(R.drawable.common_logo);

        }
        holder.itemView.setSelected(selectedItems.get(position,false)); // highlight the view on long press

        if (priority.equals("urgent")) {
            holder.priorityBulb.setBackgroundResource(android.R.color.holo_red_dark);
        } else if (priority.equals("high")) {
            holder.priorityBulb.setBackgroundResource(android.R.color.holo_orange_dark);
        } else if (priority.equals("normal")) {
            holder.priorityBulb.setBackgroundResource(android.R.color.darker_gray);
        } else {
            holder.priorityBulb.setBackgroundResource(R.color.logDarkBackgroung);
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    // methods for select and deselect the list items
    public void toggleSelection(int pos,View passedView) {

        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    // for search tickets. needs separate ticketsCopy to hold the original data.
    public void filter(String text) {
        tickets.clear();
        if(text.isEmpty()){
            tickets.addAll(ticketsCopy);
        } else{
            text = text.toLowerCase();
            for(Ticket item: ticketsCopy){
                if(item.getSubject().toLowerCase().contains(text)){
                    tickets.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
