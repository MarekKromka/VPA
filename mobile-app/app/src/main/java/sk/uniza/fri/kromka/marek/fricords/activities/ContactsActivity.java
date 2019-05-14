package sk.uniza.fri.kromka.marek.fricords.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.activities.popWindows.ContactPopWindow;
import sk.uniza.fri.kromka.marek.fricords.adapter.ContactsAdapter;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.helper.DividerItemDecoration;
import sk.uniza.fri.kromka.marek.fricords.model.User;
import sk.uniza.fri.kromka.marek.fricords.network.ApiClient;
import sk.uniza.fri.kromka.marek.fricords.network.ApiInterface;

public class ContactsActivity extends AppCompatActivity implements ContactsAdapter.UsersAdapterListener {
    private List<User> users = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter cAdapter;

    private boolean teachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = (RecyclerView) findViewById(R.id.recycleContacts);
        cAdapter = new ContactsAdapter(this, users, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(cAdapter);

        ActionBar actionBar = getSupportActionBar();
        teachers = getIntent().getBooleanExtra("iTeacher",true);
        if(teachers) actionBar.setTitle("Zoznam vyučujúcich");
        else actionBar.setTitle("Zoznam študentov");
        getUsers();
    }

    private void getUsers() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> call = apiService.getUsers(5000,
                HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // clear the inbox
                users.clear();

                for (User user : response.body()) {
                    if(teachers) {
                        if (user.isTeacher()) users.add(user);
                    }
                    else {
                        if (!user.isTeacher()){
                            user.setRoom("-");
                            users.add(user);
                        }

                    }
                }
                cAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onIconClicked(int position) {

    }

    @Override
    public void onUserRowClicked(int position) {
        User user = users.get(position);
        Intent intent = new Intent(ContactsActivity.this, ContactPopWindow.class);
        intent.putExtra("user", user);
        intent.putExtra("iTeacher",teachers);
        startActivity(intent);
    }
}
