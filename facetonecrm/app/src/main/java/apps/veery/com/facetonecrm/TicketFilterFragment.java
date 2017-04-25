package apps.veery.com.facetonecrm;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import apps.veery.com.adapter.TicketViewsAdapter;
import apps.veery.com.model.ticketviews.TicketViews;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.TicketViewServiceGenarator;
import apps.veery.com.utilities.Components;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketFilterFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    TextView title;
    Typeface type_regular,type_bold ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v=  inflater.inflate(R.layout.activity_ticket_filter, container, false);

//        type_regular = Typeface.createFromAsset(getAssets(),"fonts/AvenirNextLTPro-Regular.otf");
        type_bold=Typeface.createFromAsset(getResources().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        title=(TextView)v.findViewById(R.id.tv_ticketfilter_title);
        title.setTypeface(type_bold);

        recyclerView = (RecyclerView)v. findViewById(R.id.tikfil_rv_ticketfilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ticket Filter");
//        Components.setpDialog(getContext());
//        Components.showpDialog();
        final SweetAlertDialog dd=new Components().showLoadingSuccessMessage(getActivity());
        dd.show();
        TicketViewServiceGenarator.TicketViewServiceFactory.getInstance().getTicketView("Bearer "+ AppControler.getUser().getAccessToken()).enqueue(new Callback<TicketViews>() {
            @Override
            public void onResponse(Call<TicketViews> call, Response<TicketViews> response) {
                if (response.isSuccessful()){
                    recyclerView.setAdapter(new TicketViewsAdapter(getActivity(),response.body().getResult()));
                    dd.setTitleText("Success!")
                            .setConfirmText("OK")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    Components.dismissMessage(dd);
                }else {
                    try {
                        new Components().openAlert(getContext(),"Error",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<TicketViews> call, Throwable t) {
                new Components().openAlert(getContext(),"Error",t.getMessage());
            }
        });
    return v;
    }

}
