package sk.uniza.fri.kromka.marek.fricords.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Group implements Parcelable {
    private int id;
    private String groupId;
    private String name;
    private List<User> userList;

    public Group() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(groupId);
        dest.writeString(name);
        dest.writeList(userList);
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Group(Parcel in) {
        id = in.readInt();
        groupId = in.readString();
        name = in.readString();
        userList = new ArrayList<User>();
        in.readList(userList, User.class.getClassLoader());
    }

}
