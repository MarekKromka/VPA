package sk.uniza.fri.kromka.marek.fricords.activities;

import android.app.ActivityGroup;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.Constraints;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.uniza.fri.kromka.marek.fricords.C;
import sk.uniza.fri.kromka.marek.fricords.MyService;
import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.adapter.MessagesAdapter;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;
import sk.uniza.fri.kromka.marek.fricords.helper.DividerItemDecoration;
import sk.uniza.fri.kromka.marek.fricords.model.Note;
import sk.uniza.fri.kromka.marek.fricords.network.ApiClient;
import sk.uniza.fri.kromka.marek.fricords.network.ApiInterface;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener,
        MessagesAdapter.MessageAdapterListener {

    private List<Note> notes = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private SearchView searchView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Observable fetchWeatherInterval;
    Observer displayWeatherInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!HostPreferences.preferencesExist(this)) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        hideFloatingActionButton(fab);
        String test = HostPreferences.readSharedSetting(this,"teacher","");
        if(test.equals("true")) {
            showFloatingActionButton(fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, CreateNoteActivity.class));
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new MessagesAdapter(this, notes, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        actionModeCallback = new ActionModeCallback();


        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox(0);
                    }
                }
        );

    }

    private void startMyService() {
//        Observable.interval(5, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::boom, this::onError);

        Intent startIntent = new Intent(getApplicationContext(), MyService.class);
        startIntent.setAction(C.ACTION_START_SERVICE);
        startIntent.putExtra("iNumber",notes.size());
        startService(startIntent);
    }

    private void onError(Throwable throwable) {
        Toast.makeText(this, "EROOOOR", Toast.LENGTH_SHORT).show();
    }

    public void boom(Long aLong) {
        Toast.makeText(this, "Boom", Toast.LENGTH_SHORT).show();
    }


    private void doStuff() {
        fetchWeatherInterval = Observable.interval(10, TimeUnit.SECONDS)
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return getInbox(0);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        displayWeatherInterval = new Observer<String>() {

            @Override
            public void onError(Throwable e) {
                Log.e("Throwable ERROR", e.getMessage());
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(String value) {
                Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
            }
        };

    }

    /**
     * Fetches mail messages by making HTTP request
     * url: https://api.androidhive.info/json/inbox.json
     */
    private String getInbox(int filter) {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Note>> call = apiService.getNotes(HostPreferences.readSharedSetting(this,"userId",""),
                HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                // clear the inbox
                notes.clear();
                List<Note> filteredList = new ArrayList<>();
                if (response.code() == 403) logout();
                // add all the messages
                // messages.addAll(response.body());

                // TODO - avoid looping always thru every element
                // the loop was performed to add colors to each message
                for (Note note : response.body()) {
                    // generate a random color
                    String test = "do";
                    if(note.getDateTo() != null && note.getDateTo().before(new Date())) {
                        deleteNote(note.getNoteId());
                        continue;
                    }
                    note.setColor(getRandomMaterialColor("400"));
                    note.setImportanceByPriority();
                    switch (filter){
                        case 0:
                            filteredList.add(note);
                            break;
                        case 1:
                            if (note.isImportant())  filteredList.add(note);
                            break;
                        case 2:
                            if (note.getTargetGroup()!= null) filteredList.add(note);
                            break;
                    }

                }

                notes.addAll(filteredList);
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return "getInbox called";
    }

    private void deleteNote(String id){
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.deleteNote(id,
                HostPreferences.readSharedSetting(this, "token",""));
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, "Poznámky boli vymazané", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Neboli vymazané", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        getInbox(0);
    }

    @Override
    public void onIconClicked(int position) {
        if(HostPreferences.readSharedSetting(this,"teacher","").equals("true")) {
            if (actionMode == null) {
                actionMode = startSupportActionMode(actionModeCallback);
            }

            toggleSelection(position);
        }
    }

    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        Note note = notes.get(position);
        note.setImportant(!note.isImportant());
        notes.set(position, note);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Note note = notes.get(position);
            //note.setRead(true);
            notes.set(position, note);
            mAdapter.notifyDataSetChanged();

            String header = note.getHeader();
            String source = note.getSource().getFirstName() + " " + note.getSource().getLastName();
            SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
            String dateCreated = simpleDate.format(note.getDateCreated());
            String textOfNote = note.getText();
            String dateTo = "";
            simpleDate = new SimpleDateFormat("dd/MM/yyyy");
            if(note.getDateTo() != null) dateTo = simpleDate.format(note.getDateTo());

            Intent intent = new Intent(this,NoteDetails.class);
            intent.putExtra("iHeader", header);
            intent.putExtra("iSource", source);
            intent.putExtra("iDateCreated", dateCreated);
            intent.putExtra("iDateTo", dateTo);
            intent.putExtra("iText", textOfNote);
            intent.putExtra("iImportant", note.isImportant());
            if(note.getTargetGroup() != null) {
                intent.putExtra("iGroupName", note.getTargetGroup().getName());
                intent.putExtra("iGroup", note.getTargetGroup() != null ? true : false);
            }
            startActivity(intent);
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        if(HostPreferences.readSharedSetting(this,"teacher","").equals("true")) {
            enableActionMode(position);
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            deleteNote(notes.get(selectedItemPositions.get(i)).getNoteId());
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if(query.isEmpty()) getInbox(0);
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if(query.isEmpty()) getInbox(0);
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        TextView status = (TextView) findViewById(R.id.text_user_level);
        if(HostPreferences.readSharedSetting(this,"teacher","").equals("true")) status.setText("Učiteľský profil");
        else status.setText("Študentský profil");

        TextView userEmail = findViewById(R.id.userEmail);
        userEmail.setText(HostPreferences.readSharedSetting(this, "userEmail", ""));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        } else if (id == R.id.action_logout) {
            logout();
        } else if (id == R.id.action_set_groups) {
            Intent groups = new Intent(MainActivity.this,GroupsActivity.class);
            startActivity(groups);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            setTitle(R.string.all_notes);
            getInbox(0);
        } else if (id == R.id.nav_favorites) {
            setTitle(R.string.favorite_notes);
            getInbox(1);
        } else if (id == R.id.nav_settings_groups) {
            Intent groups = new Intent(MainActivity.this,GroupsActivity.class);
            startActivity(groups);
        } else if (id == R.id.nav_groups) {
            setTitle(R.string.groupTitle);
            getInbox(2);
        } else if (id == R.id.nav_contact) {
            Intent contacts = new Intent(MainActivity.this,ContactsActivity.class);
            startActivity(contacts);
        } else if (id == R.id.nav_contact_stud) {
            Intent contacts = new Intent(MainActivity.this,ContactsActivity.class);
            contacts.putExtra("iTeacher",false);
            startActivity(contacts);
        } else if (id == R.id.nav_settings) {
            return true;
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        HostPreferences.clearPreferences(this);
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void hideFloatingActionButton(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(false);
        }

        fab.hide();
    }

    private void showFloatingActionButton(FloatingActionButton fab) {
        fab.show();
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(true);
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInbox(0);
        Intent startIntent = new Intent(getApplicationContext(), MyService.class);
        startIntent.setAction("STOP");
        startService(startIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        startMyService();
    }
}
