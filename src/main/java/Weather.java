import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Weather {

    //https://api.openweathermap.org/data/2.5/weather?q=%D0%9A%D0%B8%D1%97%D0%B2&appid=2cf5f4a4b6c576926ae7177032a3d282&units=metric&lang=ru
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&appid=2cf5f4a4b6c576926ae7177032a3d282&units=metric&lang=ru");
        DateFormat dateFormat = new SimpleDateFormat("DD.MM.YYYY hh:mm:ss ");
        Date date = new Date();

        Scanner sc = new Scanner((InputStream) url.getContent());
        String result = "";
        while (sc.hasNext()) {
            result += sc.nextLine();
        }


        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));
        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setMaxTemp(main.getDouble("temp_max"));
        model.setMinTemp(main.getDouble("temp_min"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }


        return "➖ Місто: "+model.getName()+" \uD83C\uDFD9\n"+
                "➖ Температура: "+model.getTemp()+" ℃ \uD83C\uDF21\n"+
                "➖ Максимальна температура: "+model.getMaxTemp()+" ℃ \uD83C\uDF21\n"+
                "➖ Мінімальна температура: "+model.getMinTemp()+" ℃ \uD83C\uDF21\n"+
                "➖ Вологість: "+model.getHumidity()+" % \uD83D\uDCA6\n"+
                "➖ Дата: "+date+" \uD83D\uDCC5\n"+
                "➖ http://openweathermap.org/img/w/"+model.getIcon()+".png";
    }
}

