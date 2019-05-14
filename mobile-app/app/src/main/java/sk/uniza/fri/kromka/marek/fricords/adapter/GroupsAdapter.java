package sk.uniza.fri.kromka.marek.fricords.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sk.uniza.fri.kromka.marek.fricords.R;
import sk.uniza.fri.kromka.marek.fricords.model.Group;
import sk.uniza.fri.kromka.marek.fricords.model.User;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Group> groups;
    private GroupsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView meno, info;
        public LinearLayout groupContainer;
        public ImageView iconGroup, iconDelete;

        public MyViewHolder(View view) {
            super(view);
            meno = (TextView) view.findViewById(R.id.name_of_group);
            info = (TextView) view.findViewById(R.id.information_of_group);
            groupContainer = (LinearLayout) view.findViewById(R.id.group_container);
            iconGroup = (ImageView) view.findViewById(R.id.icon_group);
            iconDelete = (ImageView) view.findViewById(R.id.deleteicon_group_delete);
        }
    }

    public GroupsAdapter(Context mContext, List<Group> groups, GroupsAdapterListener listener) {
        this.mContext = mContext;
        this.groups = groups;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Group group = groups.get(position);

        // displaying text view data
        holder.meno.setText(group.getName());
        holder.info.setText("Počet členov: " + group.getUserList().size());

        // apply click events
        applyClickEvents(holder, position);
        applyIcon(holder);
    }

    private void applyIcon(MyViewHolder holder) {
        holder.iconGroup.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.group_icon));
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.groupContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGroupRowClicked(position);
            }
        });
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconDeleteClicked(position);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return groups.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public interface GroupsAdapterListener {
        void onGroupRowClicked(int position);
        void onIconDeleteClicked(int position);
    }

    public void removeData(int position) {
        groups.remove(position);
    }

}
