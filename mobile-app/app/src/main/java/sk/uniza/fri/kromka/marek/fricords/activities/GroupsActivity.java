package sk.uniza.fri.kromka.marek.fricords.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.activities.popWindows.AddGroupPopWindow;
import sk.uniza.fri.kromka.marek.fricords.adapter.GroupsAdapter;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.helper.DividerItemDecoration;
import sk.uniza.fri.kromka.marek.fricords.model.Group;
import sk.uniza.fri.kromka.marek.fricords.network.ApiClient;
import sk.uniza.fri.kromka.marek.fricords.network.ApiInterface;

public class GroupsActivity extends AppCompatActivity implements GroupsAdapter.GroupsAdapterListener {
    private List<Group> groups = new ArrayList<>();
    private RecyclerView recyclerView;
    private GroupsAdapter gAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        recyclerView = (RecyclerView) findViewById(R.id.recycleGroups);
        gAdapter = new GroupsAdapter(this, groups, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(gAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Zoznam skupín");
        getGroups();
    }

    private void getGroups() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Group>> call = apiService.getGroups(HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                // clear the inbox
                groups.clear();

                for (Group group : response.body()) {
                    groups.add(group);
                }
                gAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteGroup(String id){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.deleteGroup(id,
                HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(GroupsActivity.this, "Skupina bola vymazaná", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(GroupsActivity.this, "Skupina nebola vymazaná", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGroupRowClicked(int position) {
        Group group = groups.get(position);
        Intent intent = new Intent(GroupsActivity.this, GroupsDetails.class);
        intent.putExtra("iName", group.getName());
        intent.putExtra("iGroup", group);
        startActivity(intent);
    }

    @Override
    public void onIconDeleteClicked(int position) {
        Group group = groups.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Potvrďte zmazanie")
                .setMessage("Naozaj chcete zmazať skupinu - " + group.getName() +"?")
                .setIcon(R.drawable.ic_delete_button_group_member)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteGroup(group.getGroupId());
                        gAdapter.removeData(position);
                        gAdapter.notifyDataSetChanged();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.groups_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_group:
                Intent intent = new Intent(GroupsActivity.this, AddGroupPopWindow.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroups();
    }
}
