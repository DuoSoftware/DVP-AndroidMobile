package apps.veery.com.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import apps.veery.com.facetonecrm.R;
import apps.veery.com.model.Engagement.SessionListResult;
import apps.veery.com.utilities.DateMatcher;
import apps.veery.com.utilities.Validations;

/**
 * Created by Lakshan on 10/7/2016.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    View v;
    private List<SessionListResult> tickets;

    public TimelineAdapter(List<SessionListResult> tickets) {
        super();
        this.tickets = tickets;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("TIMELINE ADAPTER", tickets.get(0).getChannelFrom());
        return viewHolder;
    }
    public void addNewItems(List<SessionListResult> newTickets){
        if(null != newTickets)
            tickets.addAll(newTickets);
    }
    @Override
    public void onBindViewHolder(ViewHolder v, int position) {
        final SessionListResult sd = tickets.get(position);
        Validations.setTextToTextView(v.textFrom, "FROM "+sd.getChannelFrom());
        Validations.setTextToTextView(v.textTo,"TO "+ sd.getChannelTo());
        Validations.setTextToTextView(v.textCreated, DateMatcher.getTicketDuration(sd.getCreatedAt())+" via "+sd.getChannel());
        if (null != sd.getNotes()) {
            if (0 != sd.getNotes().size()) {
                Validations.setTextToTextView(v.textNote, sd.getNotes().get(0).getBody());
            }else{
                Validations.setTextToTextView(v.textNote,"");
            }
        }else{
            Validations.setTextToTextView(v.textNote, "");
        }
        String circle=sd.getChannel();
        if(circle.equals("api")){
            v.iv_circle.setImageResource(R.drawable.timelineapi);
        }else if(circle.equals("call")){
            v.iv_circle.setImageResource(R.drawable.timelinecall);
        }else if(circle.equals("facebook")){
            v.iv_circle.setImageResource(R.drawable.timelinefacebook);
        }else if(circle.equals("twitter")){
            v.iv_circle.setImageResource(R.drawable.timelinetwitter);
        }else if(circle.equals("sms")){
            v.iv_circle.setImageResource(R.drawable.timelinesms);
        }else{
            v.iv_circle.setImageResource(R.drawable.timelineother);
        }

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textFrom, textTo, textNote, textCreated;
        public ImageView iv_circle;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface type_regular = Typeface.createFromAsset(itemView.getResources().getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
            textFrom = (TextView) itemView.findViewById(R.id.tv_timeline_from);
            textFrom.setTypeface(type_regular);
            textTo = (TextView) itemView.findViewById(R.id.tv_timeline_to);
            textTo.setTypeface(type_regular);
            textNote = (TextView) itemView.findViewById(R.id.tv_timeline_note);
            textNote.setTypeface(type_regular);
            textCreated = (TextView) itemView.findViewById(R.id.tv_timeline_created);
            textCreated.setTypeface(type_regular);
            iv_circle=(ImageView) itemView.findViewById(R.id.iv_timeline_circle);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
//            int pos = this.getPosition();
//            Result a = tickets.get(pos);
//
//            if(!textcount.getText().equals("0")){
//                TicketFragment ticket=new TicketFragment();
//                ticket.filterid=a.getId();
//                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
//                manager.beginTransaction().replace(R.id.contentContainer,ticket).commit();
//
//            }


        }


    }
}
