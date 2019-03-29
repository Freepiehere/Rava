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

import javax.validation.constraints.Null;


public class DarkSkies {
    private String summary;
    private String[] metrics;
    private double[][] weather;
    private String[] time;


    public static void main(String args[])  {

    }
    /**
     *
     */
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

    /**
     *
     * @param lon
     * @param lat
     * @param key
     * @return
     */
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


    /**
     *
     * @param conn
     * @return
     */
    private JSONObject parseHourly(HttpURLConnection conn)  {
        JSONObject hourly = null;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

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


    /**
     *
     * @param o
     * @return
     */
    private String weather_summary(JSONObject o) {
        String summary = o.get("summary").toString();
        return summary;
    }

    /**
     *
     * @param o
     * @return
     */
    private JSONArray weather_data(JSONObject o) {
        JSONArray objData = (JSONArray)o.get("data");
        return objData;
    }


    /**
     *
     * @param data
     * @param fields
     * @return
     */
    private double[][] weather_fields(JSONArray data, String[] fields) {
        double[][] args = new double[data.size()][fields.length];
        JSONObject report;


        for (int i=0; i<data.size(); i++)   {
            report = (JSONObject)data.get(i);
            for (int j=0; j<fields.length; j++) {
                try {
                    args[i][j] = (Double) report.get(fields[j]);
                }   catch (ClassCastException e) {
                    args[i][j] = 0.0;
                }   catch (NullPointerException e)  {
                    e.printStackTrace();
                }
            }
        }
        return args;
    }


    /**
     *
     * @param o
     * @return
     */
    private String[] weather_time(JSONObject o)    {
        JSONArray data = (JSONArray)o.get("data");
        String[] time_array = new String[data.size()];
        long time;
        for (int i=0; i<data.size(); i++)  {
            JSONObject hour_data = (JSONObject)data.get(i);
            time = (Long)hour_data.get("time")*1000;
            Date date = new Date(time);
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy/ HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            String formatted_time = format.format(date);
            time_array[i] = formatted_time;
        }
        return time_array;
    }


    /**
     *
     * @return
     */
    public String getSummary() {
        return this.summary;
    }


    /**
     *
     * @return
     */
    public String[] getMetrics()    {
        return this.metrics;
    }


    /**
     *
     * @return
     */
    public double[][] getWeather()  {
        return weather;
    }


    /**
     *
     * @return
     */
    public String[] getTime()   {
        return time;
    }

}
