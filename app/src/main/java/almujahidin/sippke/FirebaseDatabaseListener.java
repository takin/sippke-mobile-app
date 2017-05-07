package almujahidin.sippke;

import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by syamsul on 5/1/17.
 */

public interface FirebaseDatabaseListener {
    public void onPower(String powerStatus);
    public void onPing(String response);
    public void onEngine(String engineStatus);
    public void onPositionChange(DataSnapshot position);
}
