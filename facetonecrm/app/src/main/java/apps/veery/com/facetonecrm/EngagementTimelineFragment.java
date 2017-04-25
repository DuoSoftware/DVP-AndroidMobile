package apps.veery.com.facetonecrm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apps.veery.com.adapter.EndlessRecyclerViewScrollListener;
import apps.veery.com.adapter.TimelineAdapter;
import apps.veery.com.model.Engagement.SessionList;
import apps.veery.com.model.Engagement.SessionListResult;
import apps.veery.com.service.AppControler;
import apps.veery.com.service.EngagementServiceGenarator;
import apps.veery.com.utilities.Components;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class EngagementTimelineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static View v;
    static RecyclerView recyclerView;
    static SweetAlertDialog dd;
    static SweetAlertDialog ds;
    static String ENGAGEMENT_ID;
    static List<String> globle_sessions;
    LinearLayoutManager linearLayoutManager;
    static TimelineAdapter timeLineAdapter;
    public EngagementTimelineFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
//    public static EngagementTimelineFragment newInstance(String param1, String param2) {
//        EngagementTimelineFragment fragment = new EngagementTimelineFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        globle_sessions=EngagementFragment.globle_sessions;
        ENGAGEMENT_ID=EngagementFragment.ENGAGEMENT_ID;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_engagement_timeline, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_timeline);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);

        globle_sessions = new ArrayList<>();
        dd = new Components().showLoadingSuccessMessage(getActivity());
        ds = new Components().showLoadingSuccessMessage(getActivity());
        List<SessionListResult> tt=new ArrayList<>();
        timeLineAdapter =new TimelineAdapter(tt);
        recyclerView.setAdapter(timeLineAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                decideNextList(page);
            }
        });
        decideNextList(1);

//        getProfilebyId();

//        if(!EngagementFragment.USER_ID.equals("")){
//            getEngagement(EngagementFragment.USER_ID);
//        }


        return v;
    }

    private static void decideNextList(int page){
        Log.d("TIMELEINE",EngagementFragment.globle_sessions.toString());
        List<String> out = new ArrayList<String>();
        int first_element= 10*(page-1);
        int second_element= 10*page;


        for (int i = first_element; i < second_element; i++) {
            out.add(EngagementFragment.globle_sessions.get(i));
        }
        getSessions(EngagementFragment.ENGAGEMENT_ID,out);
    }


    private static void getSessions(String engageId, List<String> out) {
        Log.d("SESSIONSTWO", "INSIDE");
        ds = new Components().showLoadingSuccessMessage(v.getContext());
        ds.show();

        EngagementServiceGenarator.EngagementServiceFactory.getInstance().getAllEngagementSessions("Bearer " + AppControler.getUser().getAccessToken(), engageId, out).enqueue(new Callback<SessionList>() {
            @Override
            public void onResponse(Call<SessionList> call, Response<SessionList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSuccess()) {
                        List<SessionListResult> timeline=response.body().getResult();
                        if (null == timeLineAdapter) { // FIRST CALL
                            timeLineAdapter =new TimelineAdapter(timeline);
                            recyclerView.setAdapter(timeLineAdapter);
                        } else {
                            int previousItemCount = timeLineAdapter.getItemCount();
                            timeLineAdapter.addNewItems(timeline);
                            timeLineAdapter.notifyItemRangeInserted(previousItemCount, timeline.size());
                        }
//                        ds.setTitleText("Success!")
//                                .setConfirmText("OK")
//                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        Components.dismissMessage(ds);
                    } else {
                        ds.setTitleText("opps!").setContentText(response.body().getCustomMessage())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                } else {
                    try {
                        ds.setTitleText("opps!").setContentText(response.errorBody().string())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    } catch (IOException e) {
                        ds.setTitleText("opps!").setContentText(e.getMessage())
                                .setConfirmText("OK")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SessionList> call, Throwable t) {
                ds.setTitleText("opps!").setContentText(t.getMessage())
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }
        });
    }


//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
