package sk.uniza.fri.kromka.marek.fricords.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import sk.uniza.fri.kromka.marek.fricords.R;

public class NoteDetails extends AppCompatActivity {

    TextView header, source, textOfNote, dateCreated, groupName, timeTo;
    ImageView iconImp, iconGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        ActionBar actionBar = getSupportActionBar();

        header = findViewById(R.id.header);
        source = findViewById(R.id.source);
        groupName = findViewById(R.id.groupName);
        groupName.setVisibility(View.GONE);
        textOfNote = findViewById(R.id.textOfNote);
        dateCreated = findViewById(R.id.dateCreated);
        iconImp = findViewById(R.id.imageStar);
        iconGroup = findViewById(R.id.imageGroup);
        timeTo = findViewById(R.id.time_to_end);

        Intent intent = getIntent();
        String headerText = intent.getStringExtra("iHeader");
        String sourceText = intent.getStringExtra("iSource");
        String dateText = intent.getStringExtra("iDateCreated");
        String text = intent.getStringExtra("iText");
        String dateTo = intent.getStringExtra("iDateTo");

        actionBar.setTitle(headerText);

        header.setText(headerText);
        source.setText(sourceText);
        textOfNote.setText(text);
        dateCreated.setText(dateText);



        if(!dateTo.isEmpty()){
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate d1 = LocalDate.parse(dateTo, inputFormat);
            LocalDate d2 = LocalDate.now();
            Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());
            long diffDays = diff.toDays();

            timeTo.setText("Zostávajúci počet dní: " + ((Long.signum(diffDays) == 1)? diffDays: "0"));
        }


        if(intent.getBooleanExtra("iImportant",false)){
            iconImp.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp));
            iconImp.setColorFilter(ContextCompat.getColor(this, R.color.icon_tint_selected));
        }
        else {
            iconImp.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp));
            iconImp.setColorFilter(ContextCompat.getColor(this, R.color.icon_tint_normal));
        }

        if(intent.getBooleanExtra("iGroup", false)){
            iconGroup.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_group_icon));
            iconGroup.setColorFilter(ContextCompat.getColor(this, R.color.groupMessage));
            groupName.setText("Pre skupinu: " + intent.getStringExtra("iGroupName"));
            groupName.setVisibility(View.VISIBLE);
        }

    }

}
