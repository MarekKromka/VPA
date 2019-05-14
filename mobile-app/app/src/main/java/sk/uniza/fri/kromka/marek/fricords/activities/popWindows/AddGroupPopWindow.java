package sk.uniza.fri.kromka.marek.fricords.activities.popWindows;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.model.Group;
import sk.uniza.fri.kromka.marek.fricords.model.NoteRequest;
import sk.uniza.fri.kromka.marek.fricords.network.ApiClient;
import sk.uniza.fri.kromka.marek.fricords.network.ApiInterface;

public class AddGroupPopWindow extends AppCompatActivity {

    private TextView groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window_add_group);

        DisplayMetrics matrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrix);

        int width = matrix.widthPixels;
        int height = matrix.heightPixels;
        getWindow().setLayout((int)(width * 0.7), (int)(height * 0.2));

        groupName = (TextView) findViewById(R.id.group_name_text);

        CardView addGroup = findViewById(R.id.add_group_btn);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void add() {
        Group group = new Group();
        group.setName(groupName.getText().toString());

        postGroup(group);
        finish();
    }

    private void postGroup(Group group) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Group> call = apiService.saveGroup(group, HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if (response.code() == 403) finish();
                Toast.makeText(getApplicationContext(), "Skupina pridaná", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Nepodarilo sa pridať skupinu", Toast.LENGTH_LONG).show();
            }
        });
    }
}
