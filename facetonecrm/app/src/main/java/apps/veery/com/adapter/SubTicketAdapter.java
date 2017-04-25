package apps.veery.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import apps.veery.com.facetonecrm.R;
import apps.veery.com.facetonecrm.TicketDetailActivity;
import apps.veery.com.model.SubTicket;
import apps.veery.com.model.Ticket;
import apps.veery.com.utilities.DateMatcher;

/**
 * Created by Sijith on 10/10/2016.
 */
public class SubTicketAdapter extends ArrayAdapter<SubTicket> {
    private List<SubTicket> subTicketList;
    private final Context context;
    Gson gson = new Gson();

    public SubTicketAdapter(Context context, int resource, List<SubTicket> subTickets) {
        super(context, resource, subTickets);
        this.context = context;
        this.subTicketList = subTickets;
    }

    static class ViewHolder {
        TextView sub_ticket_reference,sub_ticket_description,sub_ticket_status,sub_tv_tag;
        LinearLayout sub_ticket_item_container;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView == null){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_ticket_item,parent,false);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.sub_ticket_item_container = (LinearLayout) v.findViewById(R.id.sub_ticket_item_container);
            viewHolder.sub_ticket_reference = (TextView) v.findViewById(R.id.sub_ticket_reference);
            viewHolder.sub_ticket_description = (TextView) v.findViewById(R.id.sub_ticket_description);
            viewHolder.sub_ticket_status = (TextView) v.findViewById(R.id.sub_ticket_status);
            viewHolder.sub_tv_tag = (TextView) v.findViewById(R.id.sub_tv_tag);
            v.setTag(viewHolder);
        }
        else{
            v = convertView;
        }

        final ViewHolder holder = (ViewHolder) v.getTag();
//
        holder.sub_ticket_reference.setText(subTicketList.get(position).getReference());
        holder.sub_ticket_description.setText(subTicketList.get(position).getDescription());
        holder.sub_ticket_status.setText(subTicketList.get(position).getStatus());
        holder.sub_tv_tag.setText(gson.toJson(subTicketList.get(position)));
        holder.sub_ticket_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, TicketDetailActivity.class);
//                intent.putExtra("tv_tag", holder.sub_tv_tag.getText());
//                intent.putExtra("ticketPosition", "0"); // no need to pass a position because we are not gonna change this sub ticket
//                context.startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public int getCount() {
        return subTicketList.size();
    }
}
