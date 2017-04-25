package apps.veery.com.facetonecrm;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import apps.veery.com.adapter.NotificationAdapter;
import apps.veery.com.adapter.TicketAdapter;
import apps.veery.com.model.NotificationMessage;
import apps.veery.com.utilities.SimpleItemTouchHelperCallback;

/**
 * Created by Sijith on 11/22/2016.
 */
public class NotificationFragment extends Fragment {

    List<NotificationMessage> passedMessages;
    RecyclerView recyclerView = null;
    LinearLayoutManager linearLayoutManager;
    NotificationAdapter notificationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification,container,false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");

        recyclerView = (RecyclerView) v.findViewById(R.id.notification_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        if(null != getArguments()) {
            passedMessages = getArguments().getParcelableArrayList("notificationMessages");
        }
        else{
//            Log.d("Test","passed messages null noti frag");
        }

        if (passedMessages != null) {
            notificationAdapter = new NotificationAdapter(passedMessages, R.layout.notification_item, getActivity());
            recyclerView.setAdapter(notificationAdapter);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(notificationAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        }else{
            Toast.makeText(getContext(), "No Data For notifications", Toast.LENGTH_SHORT).show();
        }

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem mainMenuItemToRemove = menu.findItem(R.id.menu_home_refresh);
        mainMenuItemToRemove .setVisible(false);
    }
}
