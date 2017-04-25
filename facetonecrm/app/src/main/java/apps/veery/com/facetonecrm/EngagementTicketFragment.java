package apps.veery.com.facetonecrm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import apps.veery.com.adapter.EndlessRecyclerViewScrollListener;
import apps.veery.com.adapter.TicketAdapter;
import apps.veery.com.model.Ticket;
import apps.veery.com.model.TicketResponse;
import apps.veery.com.restInterfaces.TicketApiInterface;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.TicketService;
import apps.veery.com.service.TicketViewServiceGenarator;
import apps.veery.com.utilities.Components;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EngagementTicketFragment extends Fragment {
    View v;
    public static final int REQUEST_CODE_TICKET_FRAGMENT = 121;
    static final int NEW_TICKET_REQ_CODE = 3202;
    Context context;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView = null;
    TicketAdapter ticketAdapter;
    SweetAlertDialog dd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_ticket, container, false);
        context = getActivity();
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_ticket);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewTicketActivity.class);
                intent.putExtra("requestCode", NEW_TICKET_REQ_CODE);
                startActivityForResult(intent, NEW_TICKET_REQ_CODE);
            }
        });
        dd = new Components().showLoadingSuccessMessage(getActivity());

        recyclerView = (RecyclerView) v.findViewById(R.id.ticket_recycler_view);
        recyclerView.removeAllViews();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Engagement Ticket");

        if (!EngagementFragment.ENGAGEMENT_ID.equals("")) {
//            List<Ticket> ticketsnull = new ArrayList<>();
//            ticketAdapter = new TicketAdapter(ticketsnull, R.layout.ticket_item, context);
//            recyclerView.setAdapter(ticketAdapter);
            recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
//                    Components.setpDialog(context);
//                    Components.showpDialog();
//                    customLoadMoreDataFromApi(page);
                    engagementTickets(page);
                }
            });
            engagementTickets(0);

        }
        return v;
    }



    public void engagementTickets(int page) {
        dd.show();
        TicketViewServiceGenarator.TicketViewServiceFactory.getInstance().getEngagementTickets("Bearer " + AppControler.getUser().getAccessToken(), EngagementFragment.USER_ID, 10, page).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        dd.dismiss();
                        List<Ticket> tickets = response.body().getResults();
                        if (tickets.size() != 0) {
                            if (null == ticketAdapter) { // FIRST CALL
                                ticketAdapter = new TicketAdapter(tickets, R.layout.ticket_item, context);
                                recyclerView.setAdapter(ticketAdapter);
                            } else {
                                int previousItemCount = ticketAdapter.getItemCount();
                                ticketAdapter.addNewItems(tickets);
                                ticketAdapter.notifyItemRangeInserted(previousItemCount, tickets.size());
                            }

                        } else {
                            dd.setTitleText("opps!").setContentText("You don't have Engagement Tickets")
                                    .setConfirmText("OK")
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }

                    } else {

                        dd.setTitleText("opps!").setContentText(response.body().getCustomMessage())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                } else {
                    try {

                        dd.setTitleText("opps!").setContentText(response.errorBody().string())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);


                    } catch (IOException e) {
                        dd.setTitleText("opps!").setContentText(e.getMessage())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                t.printStackTrace();
                dd.setTitleText("opps!").setContentText(t.getMessage())
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        });
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i("FILTER", "FILTER SELECTED Context");
//        switch (item.getItemId()) {
//            case R.id.ctx_menu_remove_backup:
//                // do your stuff
//                break;
//            case R.id.ctx_menu_restore_backup:
//                // do your stuff
//                break;
//        }
        Log.d("Test", "id id = " + item.getItemId());
        return super.onContextItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//        inflater.inflate(R.menu.menu_ticket_view, menu);
////        MenuItem searchItem = menu.findItem(R.id.menu_ticket_view_search);
////        MenuItem filterItem = menu.findItem(R.id.menu_ticket_view_filter);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        if (id == R.id.menu_ticket_view_filter) {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
//            TicketFilterActivity fragmentB = new TicketFilterActivity();
//            transaction.replace(R.id.contentContainer, fragmentB);
//            transaction.commit();
////            Log.i("FILTER","FILTER SELECTED");
////            Intent i=new Intent(getActivity(),TicketFilterActivity.class);
////            startActivity(i);
////            getActivity().finish();
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == NEW_TICKET_REQ_CODE) { // append a new ticket to the top
            engagementTickets(0);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TICKET_FRAGMENT) { // update a ticket
            String passingTicket = data.getStringExtra("passingTicket");
            int ticketPosition = Integer.parseInt(data.getStringExtra("ticketPosition"));
            Ticket changedTicket = new Gson().fromJson(passingTicket, Ticket.class);
            ticketAdapter.updateItem(ticketPosition, changedTicket);
        }
    }

}