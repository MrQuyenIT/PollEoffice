package quyennv.becamex.voteeoffice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.commons.Constant;


public class Settings {
    private static SharedPreferences settings;
    Context myContext;
    private static final String SOCIAL_NETWORK_KEY = "SOCIAL_NETWORK_KEY";
    private static final String USER_NAME_KEY = "USER_NAME_KEY";
    public Settings(Context context) {
        myContext = context;

        settings = PreferenceManager.getDefaultSharedPreferences(myContext);
    }
    public String getSocialNetworkURLKey() {
        String value = "";
        if (Constant.EOFFICE_IS_UAT)
            value = settings.getString(SOCIAL_NETWORK_KEY, myContext.getString(R.string.host_eoffice_api_uat));
        else
            value = settings.getString(SOCIAL_NETWORK_KEY, myContext.getString(R.string.host_eoffice_api));
        return value;
    }

    public String getUserNameKey() {
        String value = settings.getString(USER_NAME_KEY, "ky.nt");
        return value;
    }

    public void setUserNameKey(String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_NAME_KEY, value);
        editor.commit();
    }
}
