package sk.uniza.fri.kromka.marek.fricords.helper;

import sk.uniza.fri.kromka.marek.fricords.activities.MainActivity;
import sk.uniza.fri.kromka.marek.fricords.data.HostPreferences;

public enum Constants {
    GET_MAPPING("notes/myNotes/?myUserId=");

    private String constant;

    Constants(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }
}
