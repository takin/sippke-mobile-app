package almujahidin.sippke.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mtakin on 08/11/16.
 */

public class VehicleDataModel {

    public String engine;
    public String horn;
    public String ping;
    public String power;

    public Map<String, Object> parking = new HashMap<>();
    public Map<String, Object> perimeter = new HashMap<>();
    public Map<String, Object> altitude = new HashMap<>();
    public Map<String, Object> position = new HashMap<>();

    public VehicleDataModel(){}

    @Exclude
    public Map<String, Object> parking(){
        return new HashMap<>();
    }
}
