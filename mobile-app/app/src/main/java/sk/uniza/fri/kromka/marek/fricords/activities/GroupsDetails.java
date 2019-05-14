package sk.uniza.fri.kromka.marek.fricords.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.activities.popWindows.AddGroupPopWindow;
import sk.uniza.fri.kromka.marek.fricords.activities.popWindows.AddToGroupPopWindow;
import sk.uniza.fri.kromka.marek.fricords.adapter.GroupDetailsAdapter;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.model.Group;
import sk.uniza.fri.kromka.marek.fricords.model.User;
import sk.uniza.fri.kromka.marek.fricords.network.ApiClient;
import sk.uniza.fri.kromka.marek.fricords.network.ApiInterface;

public class GroupsDetails extends AppCompatActivity implements GroupDetailsAdapter.GroupDetailsAdapterListener {
    private Group group;
    private GroupDetailsAdapter adapter;
    private List<User> list = new ArrayList<User>();

    public static final int REQUEST_CODE = 7;
    public static final int RESULT_OK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("iName"));

        //generate list

        group = getIntent().getParcelableExtra("iGroup");
        list = group.getUserList();

        //instantiate custom adapter
        adapter = new GroupDetailsAdapter(list, this, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.my_listview);
        lView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_to_group_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_group:
                Intent intent = new Intent(GroupsDetails.this, AddToGroupPopWindow.class);
                intent.putExtra("iGroup", group);
                startActivityForResult(intent, REQUEST_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            group = data.getParcelableExtra("iGroupAfter");
            list.clear();
            list.addAll(group.getUserList());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteClicked(int position) {
        User user = group.getUserList().get(position);
        new AlertDialog.Builder(this)
                .setTitle("Potvrďte zmazanie")
                .setMessage("Naozaj chcete odobrať používateľa - " + user.getFirstName() + " " + user.getLastName() +
                        " zo skupiny - " + group.getName() + "?")
                .setIcon(R.drawable.ic_delete_button_group_member)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteUserFromGroup(group.getGroupId(), user.getUserId());
                        adapter.removeData(position);
                        adapter.notifyDataSetChanged();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteUserFromGroup(String groupId, String userId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.deleteFromGroup(groupId, userId,
                HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(GroupsDetails.this, "Používateľ odstránený zo skupiny", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(GroupsDetails.this, "Používateľ neodstránený.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
