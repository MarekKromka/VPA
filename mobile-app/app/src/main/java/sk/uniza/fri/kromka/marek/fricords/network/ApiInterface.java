package sk.uniza.fri.kromka.marek.fricords.network;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.model.Group;
import sk.uniza.fri.kromka.marek.fricords.model.Note;
import sk.uniza.fri.kromka.marek.fricords.model.NoteRequest;
import sk.uniza.fri.kromka.marek.fricords.model.Subject;
import sk.uniza.fri.kromka.marek.fricords.model.User;

public interface ApiInterface {
    @GET("notes/myNotes")
    Call<List<Note>> getNotes(@Query("myUserId") String userId, @Header("Authorization") String token);
    @GET("users")
    Call<List<User>> getUsers(@Query("limit") int limit, @Header("Authorization") String token);
    @GET("groups")
    Call<List<Group>> getGroups(@Header("Authorization") String token);
    @GET("subjects")
    Call<List<Subject>> getSubjects(@Header("Authorization") String token);
    @POST("notes")
    Call<NoteRequest> savePost(@Body NoteRequest note, @Header("Authorization") String token);
    @DELETE("notes/{id}")
    Call<ResponseBody> deleteNote(@Path("id") String id, @Header("Authorization") String token);
    @POST("groups")
    Call<Group> saveGroup(@Body Group group, @Header("Authorization") String token);
    @DELETE("groups/{id}")
    Call<ResponseBody> deleteGroup(@Path("id") String id, @Header("Authorization") String token);
    @DELETE("groups/{idGroup}/users/{idUser}")
    Call<ResponseBody> deleteFromGroup(@Path("idGroup") String idGroup, @Path("idUser") String idUser, @Header("Authorization") String token);
    @POST("users/filter")
    Call<List<User>> getFilteredUsers(@Body User user, @Header("Authorization") String token);
    @PUT("groups/{idGroup}/users/{idUser}")
    Call<Group> addUserToGroup(@Path("idGroup") String idGroup, @Path("idUser") String idUser, @Header("Authorization") String token);
    @GET("notes/myNotes")
    Observable<List<Note>> getNotesObservable(@Query("myUserId") String userId, @Header("Authorization") String token);

}
