package apps.veery.com.restInterfaces;

import apps.veery.com.model.ImageUploadResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by sajith on 3/17/17.
 */

public interface AttachmentApiInterface {

    @Multipart
    @POST("DVP/API/1.0.0.0/FileService/File/Upload")
    Call<ImageUploadResponse> sendScannedImage(@Header("Authorization") String token,
                                               @Part("fileCategory") RequestBody fileCategory,
                                               @Part MultipartBody.Part file);


}
