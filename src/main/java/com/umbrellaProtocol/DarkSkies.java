package com.umbrellaProtocol;

import java.io.*;
import java.util.Date;
import java.text.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class DarkSkies {
    private String summary;
    private String[] metrics;
    private double[][] weather;
    private String[] time;


    public DarkSkies()  {
        JSONParser jsonParser = new JSONParser();
        String lon = null;
        String lat = null;
        String key = null;
        try {
            FileReader reader = new FileReader("umbrellaConfigs.json");
            JSONObject configs = (JSONObject)jsonParser.parse(reader);
            lon = (String)configs.get("LONGITUDE");
            lat = (String)configs.get("LATITUDE");
            key = (String)configs.get("ACCESS_KEY");

        }   catch (FileNotFoundException e) {
            e.printStackTrace();
        }   catch (IOException e)   {
            e.printStackTrace();
        }   catch (ParseException e)    {
            e.printStackTrace();
        }
        String[] fields = {"precipProbability", "temperature"};
        HttpURLConnection conn = connectDarkSky(lon,lat,key);
        JSONObject hourly = parseHourly(conn);

        String weather_summary = weather_summary(hourly);
        double[][] weather_data = weather_fields(weather_data(hourly),fields);
        String[] time_array = weather_time(hourly);

        this.metrics = fields;
        this.summary = weather_summary;
        this.weather = weather_data;
        this.time = time_array;
    }

    private HttpURLConnection connectDarkSky(String lon, String lat, String key) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://api.darksky.net/forecast/" + key + "/" + lon + "," + lat);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            return conn;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private JSONObject parseHourly(HttpURLConnection conn)  {
        JSONObject hourly = null;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(
                    new InputStreamReader(conn.getErrorStream(), "UTF-8"));

            hourly = (JSONObject)jsonObject.get("hourly");
        }   catch (UnsupportedEncodingException e)  {
            e.printStackTrace();
        }   catch (ParseException e)   {
            e.printStackTrace();
        }   catch (IOException e)   {
            e.printStackTrace();
        }
        return hourly;
    }

    private String weather_summary(JSONObject o) {
        String summary = o.get("summary").toString();
        return summary;
    }

    private JSONArray weather_data(JSONObject o) {
        JSONArray objData = (JSONArray)o.get("data");
        return objData;
    }

    private double[][] weather_fields(JSONArray data, String[] fields) {
        double[][] args = new double[data.size()][fields.length];

        JSONObject report;
        for (int i=0; i<data.size(); i++)   {
            report = (JSONObject)data.get(i);
            for (int j=0; j<fields.length; j++) {
                args[i][j] = (Double)report.get(fields[j]);
            }
        }
        return args;
    }

    private String[] weather_time(JSONObject o)    {
        String[] time_array = new String[o.size()];
        long time;
        for (int i=0; i<o.size(); i++)  {
            time = (Long)o.get("time")*1000;
            Date date = new Date(time);
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy/ HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            String formatted_time = format.format(date);
            time_array[i] = formatted_time;
        }
        return time_array;
    }

    public String getSummary() {
        return this.summary;
    }

    public String[] getMetrics()    {
        return this.metrics;
    }

    public double[][] getWeather()  {
        return weather;
    }

    public String[] getTime()   {
        return time;
    }

    /**
    public static void main(String[] args)  {
        DarkSkies ds = new DarkSkies();
        System.exit(0);

        try {                                                           //replace with JSON access
            URL url = new URL("https://api.darksky.net/forecast/393c3c68c626da225db4e6fd2af2b560/38.8462,-77.3064");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200)  {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));

            JSONObject hourly = (JSONObject)jsonObject.get("hourly");

            String hourly_summary = hourly.get("summary").toString();
            System.out.println(hourly_summary);

            JSONArray hourly_data = (JSONArray)hourly.get("data");
            JSONObject hourly_report;

            for (int i = 0; i <hourly_data.size(); i++) {
                hourly_report = (JSONObject)hourly_data.get(i);

                double precProb;
                try {
                    precProb = (Double)hourly_report.get("precipProbability");
                }   catch (ClassCastException e) {
                    precProb = 0.0;
                }

                long time = (Long)hourly_report.get("time")*1000;
                Date date = new Date(time);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy/ HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                String formatted = format.format(date);

                double temp = (Double)hourly_report.get("temperature");

                System.out.print(formatted);
                System.out.print(" ");
                System.out.println(precProb);

                System.out.println(temp);
            }



            conn.disconnect();

            FileWriter file = new FileWriter("weatherdata.json");
            file.write(hourly.toJSONString());
            file.flush();

        }   catch (MalformedURLException e) {
            e.printStackTrace();
        }   catch (IOException e)   {
            e.printStackTrace();
        }   catch (ParseException e)    {
            e.printStackTrace();
        }


    }
     */
}
