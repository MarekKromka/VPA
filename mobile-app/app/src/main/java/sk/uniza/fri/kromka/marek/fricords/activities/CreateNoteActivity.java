package sk.uniza.fri.kromka.marek.fricords.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.model.Group;
import sk.uniza.fri.kromka.marek.fricords.model.NoteRequest;
import sk.uniza.fri.kromka.marek.fricords.model.User;
import sk.uniza.fri.kromka.marek.fricords.network.ApiClient;
import sk.uniza.fri.kromka.marek.fricords.network.ApiInterface;

public class CreateNoteActivity extends AppCompatActivity {

    private AutoCompleteTextView target;
    private Spinner group;
    private ArrayAdapter adapterAuto;
    private ArrayAdapter spinnerArrayAdapter;
    private EditText header, text;
    private TextView dateFrom, dateTo;
    private Switch important;

    private int rok;
    private int mesiac;
    private int den;

    List<String> usersNames = new ArrayList<>();
    private int positionInUsersNames = -1;
    List<User> users = new ArrayList<>();
    List<String> groupsNames = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    private int positionInGroupsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        target = findViewById(R.id.selectTarget);
        group = findViewById(R.id.selectGroup);

        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_hint, groupsNames){
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
        group.setAdapter(spinnerArrayAdapter);
        group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionInGroupsNames = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterAuto = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersNames);
        target.setAdapter(adapterAuto);
        target.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionInUsersNames = position;
            }
        });

        header = findViewById(R.id.addHeader);

        dateFrom = findViewById(R.id.datumOd);
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vyberDatum((TextView)v);
            }
        });

        dateTo = findViewById(R.id.datumDo);
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vyberDatum((TextView)v);
            }
        });

        text = findViewById(R.id.addText);
        important = findViewById(R.id.setImportant);



        CardView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        getUsers();
        getGroups();
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
                usersNames.clear();
                users.clear();

                for (User user : response.body()) {

                    String titulPred = (user.getTitleBeforeName() == null)? "": user.getTitleBeforeName();
                    String titulZa = (user.getTitleAfterName() == null)? "": user.getTitleAfterName();
                    usersNames.add(titulPred + user.getFirstName() + " " + user.getLastName() + titulZa);
                    users.add(user);
                }
                adapterAuto.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getGroups() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Group>> call = apiService.getGroups(HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                groupsNames.clear();
                groups.clear();
                groupsNames.add("zvoľte skupinu...");
                groups.add(null);
                for (Group group : response.body()) {

                    groups.add(group);
                    groupsNames.add(group.getName());
                }
                spinnerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                save();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save(){
        NoteRequest note = new NoteRequest();
        note.setHeader(header.getText().toString());
        note.setText(text.getText().toString());
        note.setPriority(important.isChecked()? 1:100);
        if(note.getHeader().isEmpty() || note.getText().isEmpty()){
            Toast.makeText(this, "Vyplň polia hlavička a text", Toast.LENGTH_LONG).show();
            return;
        }
        if(positionInUsersNames == -1 && positionInGroupsNames == 0) {
            Toast.makeText(this, "Zvoľ príjemcu alebo skupinu", Toast.LENGTH_SHORT).show();
            return;
        }
        if(positionInUsersNames != -1 && positionInGroupsNames == 0) note.setTarget(users.get(positionInUsersNames).getUserId());
        note.setSource(HostPreferences.readSharedSetting(this, "userId",""));
        if(positionInGroupsNames != 0) note.setTargetGroup(groups.get(positionInGroupsNames).getGroupId());

        note.setDateOdkedy(dateFrom.getText().toString());
        note.setDateDokedy(dateTo.getText().toString());


        //POSTNI TO a dufaj ze to pojde
        postNote(note);

        finish();
    }

    private void postNote(NoteRequest note) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<NoteRequest> call = apiService.savePost(note, HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<NoteRequest>() {
            @Override
            public void onResponse(Call<NoteRequest> call, Response<NoteRequest> response) {
                if (response.code() == 403) finish();
                Toast.makeText(getApplicationContext(), "Note POSTed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<NoteRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to POST note", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void vyberDatum(TextView view) {
        Calendar mcurrentDate = Calendar.getInstance();
        rok = mcurrentDate.get(Calendar.YEAR);
        mesiac = mcurrentDate.get(Calendar.MONTH);
        den = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(CreateNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                view.setText(sdf.format(myCalendar.getTime()));

                den = selectedday;
                mesiac = selectedmonth;
                rok = selectedyear;
            }
        }, rok, mesiac, den);
        mDatePicker.show();
    }
}
