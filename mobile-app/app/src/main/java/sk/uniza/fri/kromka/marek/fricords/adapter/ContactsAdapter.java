package sk.uniza.fri.kromka.marek.fricords.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.model.User;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private Context mContext;
    private List<User> users;
    private UsersAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView meno, email, miestnost;
        public LinearLayout userContainer;
        public ImageView iconStatus, iconCharacter;

        public MyViewHolder(View view) {
            super(view);
            meno = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            miestnost = (TextView) view.findViewById(R.id.miestnost);
            userContainer = (LinearLayout) view.findViewById(R.id.users_container);
            iconStatus = (ImageView) view.findViewById(R.id.icon_status);
            iconCharacter = (ImageView) view.findViewById(R.id.icon_char);
        }
    }

    public ContactsAdapter(Context mContext, List<User> users, UsersAdapterListener listener) {
        this.mContext = mContext;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        User user = users.get(position);

        // displaying text view data
        String titulPred = (user.getTitleBeforeName() == null)? "": user.getTitleBeforeName();
        String titulZa = (user.getTitleAfterName() == null)? "": user.getTitleAfterName();
        holder.meno.setText(titulPred + user.getFirstName() +" "+ user.getLastName() + titulZa);

        holder.email.setText(user.getEmail());
        holder.miestnost.setText("Miestnosť: " + (user.getRoom() == null ? "":user.getRoom()));

        // apply click events
        applyClickEvents(holder, position);
        applyIcon(holder, user);
    }

    private void applyIcon(MyViewHolder holder, User user) {
        if(user.isMan()){
            holder.iconCharacter.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.teacher_man));
        }
        else{
            holder.iconCharacter.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.teacher_woman));
        }
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.iconStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });

        holder.userContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUserRowClicked(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface UsersAdapterListener {
        void onIconClicked(int position);
        void onUserRowClicked(int position);
    }

}
