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
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import apps.veery.com.facetonecrm.R;
import apps.veery.com.facetonecrm.TicketFragment;
import apps.veery.com.model.count.Count;
import apps.veery.com.model.ticketviews.Result;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.TicketViewServiceGenarator;
import apps.veery.com.utilities.Components;
import apps.veery.com.utilities.Validations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lakshan on 9/21/2016.
 */
public class TicketViewsAdapter extends RecyclerView.Adapter<TicketViewsAdapter.ViewHolder>  {
    View v;
    private List<Result> tickets;
    private int rowLayout;
    private Context context;

    public TicketViewsAdapter(Context context, List<Result> tickets) {
        super();
        this.tickets=tickets;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
         v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ticket_filter_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder v, int position) {
        final Result ticketDeatails=tickets.get(position);


        TicketViewServiceGenarator.TicketViewServiceFactory.getInstance().getTicketViewCount(ticketDeatails.getId(),"Bearer "+ AppControler.getUser().getAccessToken()).enqueue(new Callback<Count>() {
            @Override
            public void onResponse(Call<Count> call, Response<Count> response) {
                if(response.isSuccessful()){
                    if(response.body().getIsSuccess()){
                        Validations.setTextToTextView(v.textcount,response.body().getResult().toString());

                        ticketDeatails.setCount(response.body().getResult());

                    }
                }else {
                    try {
                        new Components().openAlert(context,"Error",response.errorBody().string());
                    } catch (IOException e) {
                        new Components().openAlert(context,"Error",e.getMessage());
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Count> call, Throwable t) {
                new Components().openAlert(context,"Error",t.getMessage());
            }
        });
        Validations.setTextToTextView(v.filter_title,ticketDeatails.getTitle());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textcount, filter_title;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface type_regular = Typeface.createFromAsset(itemView.getResources().getAssets(),"fonts/AvenirNextLTPro-Regular.otf");
            textcount = (TextView)itemView.findViewById(R.id.tv_ticketviews_count);
            textcount.setTypeface(type_regular);
            textcount.setTextSize(16);
            filter_title = (TextView)itemView.findViewById(R.id.tv_ticketviews_title);
            filter_title.setTypeface(type_regular);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int pos=this.getPosition();
            Result a = tickets.get(pos);

            if(!textcount.getText().equals("0")){
                TicketFragment ticket=new TicketFragment();
                ticket.filterid=a.getId();
                ticket.filterTitle=a.getTitle();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.contentContainer,ticket).commit();

            }


//            FragmentTransaction transaction =((Activity)context).getFragmentManager().beginTransaction();

//            Fragment fragmentB = new TicketFragment();
//            transaction.replace(R.id.contentContainer, fragmentB);
//            transaction.commit();
        }



    }
}
