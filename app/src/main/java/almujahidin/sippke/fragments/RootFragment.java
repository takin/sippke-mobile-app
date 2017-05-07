package almujahidin.sippke.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import almujahidin.sippke.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RootFragment extends Fragment {

    private static final String DB_REF = "DR3559KE";
    protected DatabaseReference firebaseDatabseRef;

    public static class VehicleState {
        public static final String ON = "on";
        public static final String OFF = "off";
        public static final String IGNITE = "ignite";
        public static final String PING = "ping";
        public static final String ONLINE = "online";
        public static final String ASK = "ask";
        public static final String OFFLINE = "offline";
    }

    private static Map<String,Object> FirebasePowerChild = new HashMap<>(1);
    private static Map<String,Object> FirebaseEngineChild = new HashMap<>(1);
    private static Map<String,Object> FirebasePingChild = new HashMap<>(1);

    public RootFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabseRef = FirebaseDatabase.getInstance().getReference(DB_REF);
    }

    protected void changePowerState(String newState){
        FirebasePowerChild.put("power", newState);
        FirebaseEngineChild.put("engine", VehicleState.OFF);
        firebaseDatabseRef.updateChildren(FirebasePowerChild);
        firebaseDatabseRef.updateChildren(FirebaseEngineChild);
    }

    protected void igniteEngine(){
        FirebaseEngineChild.put("engine","ignite");
        firebaseDatabseRef.updateChildren(FirebaseEngineChild);
    }

    protected void pingVehicle(){
        FirebasePingChild.put("ping","ask");
        firebaseDatabseRef.updateChildren(FirebasePingChild);
    }

    protected void getLastPosition(ValueEventListener listener){
        firebaseDatabseRef.child("position").addValueEventListener(listener);
    }

}
