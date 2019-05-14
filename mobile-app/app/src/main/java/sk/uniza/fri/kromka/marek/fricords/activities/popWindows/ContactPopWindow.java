package sk.uniza.fri.kromka.marek.fricords.activities.popWindows;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.model.Subject;
import sk.uniza.fri.kromka.marek.fricords.model.User;

public class ContactPopWindow extends AppCompatActivity {

    private TextView meno, email, cislo, miestnost, predmety, vyskum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        DisplayMetrics matrix = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrix);

        int width = matrix.widthPixels;
        int height = matrix.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.5));

        Intent i = getIntent();
        User user = (User) i.getParcelableExtra("user");
        boolean isTeacher = i.getBooleanExtra("iTeacher",true);
        user.setPhoneNumber("+421911534445");
        user.setRoom("RA327");

        meno = (TextView) findViewById(R.id.meno);
        email = (TextView) findViewById(R.id.email);
        cislo = (TextView) findViewById(R.id.cislo);
        miestnost = (TextView) findViewById(R.id.miestnost);
        predmety = (TextView) findViewById(R.id.predmety);
        vyskum = (TextView) findViewById(R.id.vyskum);

        String titulPred = (user.getTitleBeforeName() == null)? "": user.getTitleBeforeName();
        String titulZa = (user.getTitleAfterName() == null)? "": user.getTitleAfterName();
        meno.setText(titulPred + user.getFirstName() +" "+ user.getLastName() + titulZa);
        email.setText(user.getEmail());
        cislo.setText("Tel.č: " + user.getPhoneNumber());
        String predmetyText = "";
        for (Subject subject : user.getSubjectList()) {
            predmetyText += subject.toString() + " ";
        }

        if(isTeacher) {
            predmety.setText("Vyučuje: " + predmetyText);
            miestnost.setText("Kancelária: " + user.getRoom());
            vyskum.setText("Výskum: " + user.getResearch());
        }
        else{
            predmety.setText("Predmety: " + predmetyText);
            miestnost.setText("Skupina: " + user.getRoom());
            vyskum.setText("Dodatočné informácie: " + user.getResearch());
        }

        CardView mailBtn = findViewById(R.id.mail_button);
        mailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",user.getEmail(), null));
                startActivity(emailIntent);
            }
        });

        CardView dialBtn = findViewById(R.id.dial_button);
        dialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + user.getPhoneNumber()));
                startActivity(intent);
            }
        });
    }
}
