package apps.veery.com.restInterfaces;

import apps.veery.com.model.Attachment;
import apps.veery.com.model.CommentResponse;
import apps.veery.com.model.NewComment;
import apps.veery.com.model.NewTicketResponse;
import apps.veery.com.model.OneTicketResponse;
import apps.veery.com.model.Ticket;
import apps.veery.com.model.TicketResponse;
import apps.veery.com.model.TicketStatus;
import apps.veery.com.model.TicketTagResponse;
import apps.veery.com.model.ticketviews.TicketStatusResponse;
import apps.veery.com.requestmodel.resource.NewTicket;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TicketApiInterface {

    @Headers({
            "Content-Type: application/json",
            "companyinfo: 1:103"
    })
    @GET("DVP/API/1.0/MyTickets/{ticketsAmount}/{ticketsPage}")
    Call<TicketResponse> getMyTickets(@Header("authorization")String authorization,@Path("ticketsAmount") int ticketsAmount,@Path("ticketsPage") int ticketsPage);

    @GET("DVP/API/1.0/MyAllGroupTickets/{ticketsAmount}/{ticketsPage}")
    Call<TicketResponse> getMyGroupTickets(@Header("authorization")String authorization,@Path("ticketsAmount") int ticketsAmount,@Path("ticketsPage") int ticketsPage);

    @GET("DVP/API/1.0/Tickets/{ticketsAmount}/{ticketsPage}")
    Call<TicketResponse> getTickets(@Header("authorization")String authorization,@Path("ticketsAmount") int ticketsAmount,@Path("ticketsPage") int ticketsPage);

    @GET("DVP/API/1.0.0.0/TicketSearch/{searchText}/{ticketsAmount}/{ticketsPage}")
    Call<TicketResponse> getSearchedTickets(@Header("authorization")String authorization, @Path("searchText") String searchText, @Path("ticketsAmount") int ticketsAmount, @Path("ticketsPage") int ticketsPage);

    @GET("DVP/API/1.0/TagCategoriesWithoutData")
    Call<TicketTagResponse> getTicketCategories(@Header("authorization")String authorization);

    @GET("DVP/API/1.0/TagsWithoutData")
    Call<TicketTagResponse> getAllTicketTags(@Header("authorization")String authorization);

    @POST("DVP/API/1.0.0.0/Ticket")
    Call<NewTicketResponse> createNewTicket(@Header("authorization")String authorization,@Body NewTicket newTicket);

    @POST("DVP/API/1.0/Ticket/{ticketID}/SubTicket")
    Call<NewTicketResponse> createNewSubTicket(@Header("authorization")String authorization,@Path("ticketID") String ticketId,@Body NewTicket newTicket);

    @GET("DVP/API/1.0/Ticket/{ticketID}/Details")
    Call<OneTicketResponse> getTicketDetail(@Header("authorization")String authorization,@Path("ticketID") String ticketId);

    @PUT("DVP/API/1.0/Ticket/{ticketID}/Comment")
    Call<CommentResponse> sendComment(@Header("authorization")String authorization,@Path("ticketID") String ticketId,@Body NewComment newComment);

    @PUT("DVP/API/1.0.0.0/Ticket/{tid}")
    Call<NewTicketResponse> getUpdateTicket(@Header("Authorization")String authorization, @Body Ticket newTicket, @Path("tid") String tid);

    @PUT("DVP/API/1.0/Ticket/{tid}/pick")
    Call<CommentResponse> pickTicket(@Header("Authorization")String authorization,@Path("tid") String tid); //used comment res as generic data are same

    @PUT("DVP/API/1.0/Ticket/{tid}/AssignUser/{user}")
    Call<CommentResponse> assignTicketToUser(@Header("Authorization")String authorization,@Path("tid") String tid,@Path("user") String user); //used comment res as generic data are same

    @PUT("DVP/API/1.0/Ticket/{tid}/AssignGroup/{group}")
    Call<CommentResponse> assignTicketToGroup(@Header("Authorization")String authorization,@Path("tid") String tid,@Path("group") String group); //used comment res as generic data are same

    @GET("DVP/API/1.0.0.0/TicketStatusFlow/NextAvailableStatus/{type}/{currentStatus}")
    Call<TicketStatusResponse> getNextTicketStatus(@Header("Authorization")String authorization, @Path("type") String type, @Path("currentStatus") String currentStatus);

    @PUT("DVP/API/1.0/Ticket/{tid}/Status")
    Call<CommentResponse> changeTicketStatus(@Header("Authorization")String authorization, @Path("tid")String tid,@Body TicketStatus newStatus);

    @PUT("DVP/API/1.0.0.0/Ticket/{tid}/Attachment")
    Call<CommentResponse> addAttachmentToTicket(@Header("Authorization")String authorization, @Path("tid") String tid, @Body Attachment attachment);

}
