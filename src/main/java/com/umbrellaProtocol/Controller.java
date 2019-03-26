package com.umbrellaProtocol;

import com.emailSender.Sender;
import com.umbrellaProtocol.DarkSkies;

// Controller.java acts as a central hub which connects feature classes to Sender.java
public class Controller {

    private String weather_report;

    public Controller() {
        this.weather_report = "\n";
        DarkSkies ds = new DarkSkies();
        Sender sender = new Sender();
        write_report(ds.getSummary());
        read_data(ds.getWeather());
        //System.out.println(this.weather_report);
        sender.setMessage("Weather report", this.weather_report);
        send_report(sender);
    }

    public void send_report(Sender sender)   {
        sender.sendMessage();
    }

    public void write_report(String summary)   {
        this.weather_report += summary+"\n";
    }

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

    public void write_umbrella_warning()    {
        this.weather_report += " Bring an umbrella.\n";
    }

    public void write_jacket_warning()   {
        this.weather_report += "It's jacket-appropriate weather.\n";
    }

    public static void main(String args[])  {
        Controller controller = new Controller();

    }

}
