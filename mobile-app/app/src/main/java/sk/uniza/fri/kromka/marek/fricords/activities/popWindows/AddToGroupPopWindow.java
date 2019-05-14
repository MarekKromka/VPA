package sk.uniza.fri.kromka.marek.fricords.activities.popWindows;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.activities.GroupsDetails;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.model.Group;
import sk.uniza.fri.kromka.marek.fricords.model.Subject;
import sk.uniza.fri.kromka.marek.fricords.model.User;
import sk.uniza.fri.kromka.marek.fricords.network.ApiClient;
import sk.uniza.fri.kromka.marek.fricords.network.ApiInterface;

public class AddToGroupPopWindow extends AppCompatActivity {

    private EditText meno, priezvisko, skupina;
    private Spinner predmety;
    private ArrayAdapter spinnerArrayAdapter;

    private Group group;
    List<String> subjectsNames = new ArrayList<>();
    List<Subject> subjects = new ArrayList<>();
    private int positionInSubjectsNames = 0;
    List<User> filteredUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group_pop_window);

        DisplayMetrics matrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrix);

        int width = matrix.widthPixels;
        int height = matrix.heightPixels;
        getWindow().setLayout((int)(width * 0.65), (int)(height * 0.5));

        group = getIntent().getParcelableExtra("iGroup");
        meno = (EditText) findViewById(R.id.filter_first_name);
        priezvisko = (EditText) findViewById(R.id.filter_last_name);
        skupina = (EditText) findViewById(R.id.filter_group);
        predmety = (Spinner) findViewById(R.id.filter_subjects);

        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_hint, subjectsNames){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Prvy je len napoveda
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // prvy sa neda zobrat je to napoveda nastavime na sivo
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_hint);
        predmety.setAdapter(spinnerArrayAdapter);
        predmety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionInSubjectsNames = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getSubjects();

        CardView addToGroup = findViewById(R.id.add_to_group_btn);
        addToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void addUser() {
        User user = new User();
        user.setFirstName(meno.getText().toString());
        user.setLastName(priezvisko.getText().toString());
        user.setRoom(skupina.getText().toString());
        if(positionInSubjectsNames != 0){
            List<Subject> subjectList = new ArrayList<Subject>();
            subjectList.add(subjects.get(positionInSubjectsNames));
            user.setSubjectList(subjectList);
        }

        filtre(user);
    }

    private void AddToGroup(User u) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Group> call = apiService.addUserToGroup(group.getGroupId(), u.getUserId(),
                HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<Group>(){
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                Toast.makeText(getApplicationContext(), "Používateľ pridaný do skupiny", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Používateľ nebol pridaný.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtre(User user) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<User>> call = apiService.getFilteredUsers(user, HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                filteredUsers.clear();
                for (User user : response.body()) {
                    filteredUsers.add(user);
                }
                if(!filteredUsers.isEmpty()) addUsersToGroup();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void addUsersToGroup() {
        for (User u : filteredUsers) {
            AddToGroup(u);
            if(!group.getUserList().contains(u)){
                group.getUserList().add(u);
            }
        }
        Intent intent = getIntent().putExtra("iGroupAfter", group);
        setResult(1, intent);
        finish();
    }

    private void getSubjects() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Subject>> call = apiService.getSubjects(HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                subjectsNames.clear();
                subjects.clear();
                subjectsNames.add("Zvoľte predmet...");
                subjects.add(null);
                for (Subject subject : response.body()) {

                    subjects.add(subject);
                    subjectsNames.add(subject.getName());
                }
                spinnerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
