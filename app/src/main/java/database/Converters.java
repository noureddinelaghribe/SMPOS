package database;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null) {
            return null;
        }
        return new Gson().toJson(list);  // Convert list to JSON string
    }

    @TypeConverter
    public static List<String> toList(String data) {
        if (data == null) {
            return null;
        }
        // Convert JSON string back to list
        return new Gson().fromJson(data, new TypeToken<List<String>>(){}.getType());
    }


}
