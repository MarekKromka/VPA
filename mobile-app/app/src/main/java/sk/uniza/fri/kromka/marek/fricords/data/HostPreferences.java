package sk.uniza.fri.kromka.marek.fricords.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

public class HostPreferences {

    public static final String TOKEN = "token";
    public static final String USER_ID = "userId";
    public static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String PREFERENCES_FILE = "pref";

    public static void saveSharedSetting(Context context, String settingName, String settingValue)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static void test(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();
        String test = sharedPref.getString("userEmail","");
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    public static String readSharedSetting(Context context, String settingName, String defaultValue)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void clearPreferences(Context context)
    {
        context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static boolean preferencesExist(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return (sharedPref != null && sharedPref.contains(TOKEN) && sharedPref.contains(USER_ID));
    }

}
