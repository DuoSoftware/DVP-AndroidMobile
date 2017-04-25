package apps.veery.com.service;

import apps.veery.com.model.Break;
import apps.veery.com.model.NewTicketResponse;
import apps.veery.com.model.Ticket;
import apps.veery.com.model.TicketResponse;
import apps.veery.com.model.count.Count;
import apps.veery.com.model.ticketviews.TicketViews;
import apps.veery.com.requestmodel.resource.Resourcereq;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TicketViewServiceGenarator {

    @GET("TicketViews")
    Call<TicketViews> getTicketView(@Header("Authorization")String authorization);

    @GET("TicketView/{id}/TicketCount")
    Call<Count> getTicketViewCount(@Path("id") String path,@Header("Authorization")String authorization);

    @GET("TicketView/{titleId}/Tickets")
    Call<TicketResponse> getTicketViewToTitle(@Path("titleId") String path, @Header("Authorization")String authorization);

    @PUT("Ticket/{tid}/AssignUser/{uid}")
    Call<NewTicketResponse> getAssignToUser(@Header("Authorization")String authorization, @Path("tid") String tid, @Path("uid") String uid);

    @GET("Tickets/Requester/{requesterid}/{ticketsAmount}/{ticketsPage}")
    Call<TicketResponse> getEngagementTickets(@Header("authorization")String authorization,@Path("requesterid") String resid,@Path("ticketsAmount") int ticketsAmount,@Path("ticketsPage") int ticketsPage);


    class TicketViewServiceFactory{
        private static TicketViewServiceGenarator service;
        public static TicketViewServiceGenarator getInstance(){
            String BASE_URL="http://liteticket.app.veery.cloud/DVP/API/1.0.0.0/";
            if(service==null){
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service=retrofit.create(TicketViewServiceGenarator.class);
                return service;
            }else {
                return service;
            }
        }
    }
}
