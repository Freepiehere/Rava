package com.umbrellaProtocol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Controller.java acts as a central hub which connects feature classes to Sender.java
public class Controller {

    private String weather_report;

    /**
     * Controller object interacts with DarkSkies object to acquire weather prediction data. Controller object
     * composes weather report to be sent to user containing daily summary, a possible umbrella warning, and a
     * potential jacket warning. Weather report is assigned to a Message obejct field, and sent to the user
     * within the Sender object.
     * This method always activates the send_report(String) method.
     *
     */
    public Controller() {
        this.weather_report = "\n";
        DarkSkies ds = new DarkSkies();
        Sender sender = new Sender();

        write_report(ds.getSummary());
        read_data(ds.getWeather());
        sender.setMessage("Weather report", this.weather_report);
        send_report(sender);
    }

    /**
     * Activating the message release protocol.
     * @param sender the object interacting with SMTP
     */
    public void send_report(Sender sender)   {
        sender.sendMessage();
    }

    /**
     *
     * @param summary
     */
    public void write_report(String summary)   {
        this.weather_report += summary+"\n";
    }

    /**
     *
     * @param weather_data
     */
    public void read_data(double[][] weather_data)  {
        boolean umbrella = false;
        boolean jacket = false;
        for (int i=0; i<weather_data.length; i++)   {
            if (weather_data[i][0]>0.3 && !umbrella) {
                umbrella = true;
                write_umbrella_warning();
            }
            if (weather_data[i][1]<50 && !jacket)   {
                jacket = true;
                write_jacket_warning();
            }
        }
    }

    /**
     *
     */
    public void write_umbrella_warning()    {
        this.weather_report += "Bring an umbrella.\n";
    }

    /**
     *
     */
    public void write_jacket_warning()   {
        this.weather_report += "It's jacket-appropriate weather.\n";
    }

    /**
     *
     * @param args
     */
    public static void main(String args[])  {
        Controller controller = new Controller();;
    }
}
