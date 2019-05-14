package sk.uniza.fri.kromka.marek.fricords.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.model.User;

public class GroupDetailsAdapter extends BaseAdapter implements ListAdapter {
    private List<User> list;
    private Context context;
    private GroupDetailsAdapterListener listener;
    private ImageView deleteBtn;

    public GroupDetailsAdapter(List<User> list, Context context,GroupDetailsAdapterListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.user_row_groups, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        User user = list.get(position);
        String statut = user.isTeacher()? "uč. - ":"št. - ";
        String titulPred = (user.getTitleBeforeName() == null)? "": user.getTitleBeforeName();
        String titulZa = (user.getTitleAfterName() == null)? "": user.getTitleAfterName();
        listItemText.setText(statut + titulPred + user.getFirstName() +" "+ user.getLastName() + titulZa);

        //Handle buttons and add onClickListeners
        deleteBtn = (ImageView)view.findViewById(R.id.deleteicon);

        applyClickEvents(position);
        return view;
    }

    private void applyClickEvents(final int position) {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClicked(position);
            }
        });

    }

    public void removeData(int position) {
        list.remove(position);
    }

    public interface GroupDetailsAdapterListener {
        void onDeleteClicked(int position);
    }
}
