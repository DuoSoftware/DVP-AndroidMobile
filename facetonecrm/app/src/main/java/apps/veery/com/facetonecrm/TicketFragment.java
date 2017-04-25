package apps.veery.com.facetonecrm;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import apps.veery.com.adapter.EndlessRecyclerViewScrollListener;
import apps.veery.com.adapter.TicketAdapter;
import apps.veery.com.model.Auth;
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

public class TicketFragment extends Fragment implements RecyclerView.OnItemTouchListener,
        ActionMode.Callback {

    public static final int REQUEST_CODE_TICKET_FRAGMENT = 121;
    static final int NEW_TICKET_REQ_CODE = 3202;

    private static final int MY_TICKETS = 100;
    private static final int MY_GROUP_TICKETS = 101;
    private static final int ALL_TICKETS = 102;
    private static int SELECTED_TICKET_TYPE = 100; // 100 is default. save this ticket type onSaveInstanceState()


//    Context context;
    List<Ticket> tickets;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView = null;
    public static String filterid = "";
    public static String filterTitle="";
    TicketApiInterface apiService = TicketService.getClient().create(TicketApiInterface.class);
    TicketAdapter ticketAdapter;
    SweetAlertDialog sweetAlertDialog;

    private Auth authUser;

    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;

    GestureDetectorCompat gestureDetector;
    ActionMode actionMode;
    ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_ticket_view, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //get number of selected items

            switch (item.getItemId()) {
//                            case R.id.context_favourite:
//                                //SQL - Favourite Item
//                                mode.finish();
//                            case R.id.context_delete:
//                                //SQL - Delete Item
//                                mode.finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
//                        mActionMode = null;
            if (actionMode != null)
                actionMode = null;
            ticketAdapter.clearSelections();
        }
    };


    public TicketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if(null != sharedpreferences.getString(MainActivity.USER,null)) {
            authUser = new Gson().fromJson(sharedpreferences.getString(MainActivity.USER, null), Auth.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);
        sweetAlertDialog = new Components().showLoadingSuccessMessage(getActivity());
        sweetAlertDialog.show();
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_ticket);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewTicketActivity.class);
                intent.putExtra("requestCode", NEW_TICKET_REQ_CODE);
                startActivityForResult(intent, NEW_TICKET_REQ_CODE);
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.ticket_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(this);
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        recyclerView.setItemAnimator(animator);

        gestureDetector =
                new GestureDetectorCompat(getActivity(), new RecyclerViewDemoOnGestureListener());


        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Inbox");

        if (!filterid.equals("")) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(filterTitle);
            TicketViewServiceGenarator.TicketViewServiceFactory.getInstance().getTicketViewToTitle(filterid, "Bearer " + authUser.getAccessToken()).enqueue(new Callback<TicketResponse>() {
                @Override
                public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                    if (response.isSuccessful()) {
                        tickets = response.body().getResults();
                        if (tickets != null) {
                            ticketAdapter = new TicketAdapter(tickets, R.layout.ticket_item, getActivity());
                            recyclerView.setAdapter(ticketAdapter);
                        } else {
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction().replace(R.id.contentContainer, new TicketFilterFragment()).commit();
                            Toast.makeText(getContext(), "Empty Data For this Filter", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                    }
                    sweetAlertDialog.hide();
                }

                @Override
                public void onFailure(Call<TicketResponse> call, Throwable t) {
                    sweetAlertDialog.hide();
                }
            });

        } else if (filterid.equals("")) {
            recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    sweetAlertDialog.show();
                    // if still in search mode get result for that search
                        if(null != searchView.getQuery() && !searchView.getQuery().toString().isEmpty()){
                            customLoadMoreDataFromApi(page,searchView.getQuery().toString());
                        }
                    else {
                        customLoadMoreDataFromApi(page, null);
                    }
                }
            });

            customLoadMoreDataFromApi(0,null);
        }
        setHasOptionsMenu(true);
        return v;
    }



    private void customLoadMoreDataFromApi(int page,String searchText) {
        Call<TicketResponse> call;
        // getting tickets if in search mode
        if(null != searchText){
            if(page == 0) //get the results in new adapter for new search query. ticketAdapter != null for next pages
            ticketAdapter = null;

            call = apiService.getSearchedTickets("Bearer " + authUser.getAccessToken(),searchText, 10, page);
        }
        else{
            if(page == 0)
                ticketAdapter = null;

            switch (SELECTED_TICKET_TYPE){
                case MY_TICKETS:
                    call = apiService.getMyTickets("Bearer " + authUser.getAccessToken(), 10, page); // change the number of tickets to be fetched
                    break;
                case MY_GROUP_TICKETS:
                    call = apiService.getMyGroupTickets("Bearer " + authUser.getAccessToken(), 10, page); // change the number of tickets to be fetched
                    break;
                case ALL_TICKETS:
                    call = apiService.getTickets("Bearer " + authUser.getAccessToken(), 10, page); // change the number of tickets to be fetched
                    break;
                default:  // default value is for MY_TICKETS
                    call = apiService.getMyTickets("Bearer " + authUser.getAccessToken(), 10, page); // change the number of tickets to be fetched
                    break;
            }
//            call = apiService.getMyTickets("Bearer " + authUser.getAccessToken(), 10, page); // change the number of tickets to be fetched
        }


         call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                sweetAlertDialog.hide();
                if (response.isSuccessful()) {
                    List<Ticket> tickets = response.body().getResults();

                    if (null != tickets) {
                        if (null == ticketAdapter) { // FIRST CALL
                            recyclerView.getRecycledViewPool().clear();  // prevent ANR when search for tickets
                            ticketAdapter = new TicketAdapter(tickets, R.layout.ticket_item, getActivity());
                            recyclerView.setAdapter(ticketAdapter);
                        } else {
                            int previousItemCount = ticketAdapter.getItemCount();
                            ticketAdapter.addNewItems(tickets);
                            ticketAdapter.notifyItemRangeInserted(previousItemCount, tickets.size());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Tickets not retrieved.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to get a response. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                sweetAlertDialog.hide();
                Toast.makeText(getActivity(), "Error occurred. Check your connection. " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ticket_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem mainMenuItemToRemove = menu.findItem(R.id.menu_home_refresh);
        mainMenuItemToRemove.setVisible(false);

        MenuItem searchItem = menu.findItem(R.id.menu_ticket_view_search);

        // this listener is used for getting tickets when user click back button in search view
        MenuItemCompat.setOnActionExpandListener(searchItem,new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                ticketAdapter = null;
                customLoadMoreDataFromApi(0,null); // get the first ticket set.
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

            searchView = (SearchView) searchItem.getActionView();
            searchView.setSubmitButtonEnabled(true);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
//                    ticketAdapter.filter(newText); this was the previous one
                    if(newText.length()>3) //if only length > 3 call the service
                    customLoadMoreDataFromApi(0,newText);
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    customLoadMoreDataFromApi(0,query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_ticket_view_filter) {  //ticket filtering
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            TicketFilterFragment fragmentB = new TicketFilterFragment();
            transaction.replace(R.id.contentContainer, fragmentB);
            transaction.commit();
        }
        else if(id == R.id.menu_ticket_view_search){  //ticket searching
            // do nothing. search view listener will handle this
        }
        else if(id == R.id.menu_ticket_my_group){
            SELECTED_TICKET_TYPE = MY_GROUP_TICKETS;
            customLoadMoreDataFromApi(0,null);
            item.setChecked(true);
        }
        else if(id == R.id.menu_ticket_all){
            SELECTED_TICKET_TYPE = ALL_TICKETS;
            customLoadMoreDataFromApi(0,null);
            item.setChecked(true);
        }
        else if(id == R.id.menu_ticket_my_tickets){
            SELECTED_TICKET_TYPE = MY_TICKETS;
            customLoadMoreDataFromApi(0,null);
            item.setChecked(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == NEW_TICKET_REQ_CODE) { // append a new ticket to the top
            ticketAdapter = null; // must set to null inorder to call all the fresh tickets
            customLoadMoreDataFromApi(0,null);
        }
        else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TICKET_FRAGMENT){ // update a ticket
            if(null != data.getStringExtra("ticketOperationType")){
                if(data.getStringExtra("ticketOperationType").equals("updateTicket")){
                    String passingTicket = data.getStringExtra("passingTicket");
                    int ticketPosition = Integer.parseInt(data.getStringExtra("ticketPosition"));
                    Ticket changedTicket = new Gson().fromJson(passingTicket,Ticket.class);
                    ticketAdapter.updateItem(ticketPosition,changedTicket);
                }
                else{ //data.getStringExtra("ticketOperationType").equals("createSubTicket")
                    ticketAdapter = null; // must set to null inorder to call all the fresh tickets
                    customLoadMoreDataFromApi(0,null);
                }
            }
        }
    }

    private void myToggleSelection(int idx) {
        ticketAdapter.toggleSelection(idx, null);
        int count = ticketAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }

//        actionMode.setTitle(ticketAdapter.getSelectedItemCount()+" selected");
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_ticket_view, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                myToggleSelection(recyclerView.getChildAdapterPosition(view));
            } else {
                if(null != view) {
                    TextView tv_tag = (TextView) view.findViewById(R.id.ticket_tag); // tv_tag is a json string which represents the ticket
                    Intent intent = new Intent(getActivity(), TicketDetailActivity.class);
                    intent.putExtra("tv_tag", tv_tag.getText().toString());
                    intent.putExtra("ticketPosition", String.valueOf(recyclerView.getChildAdapterPosition(view)));
                    startActivityForResult(intent, REQUEST_CODE_TICKET_FRAGMENT);
                }
            }
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above
            actionMode = view.startActionMode(mActionModeCallback);
            int idx = recyclerView.getChildAdapterPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }
}
