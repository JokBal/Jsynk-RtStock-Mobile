package org.jokbal.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Stock
{
    private String time;
    private double price, delta, perc;
    private int vol;

    public Stock(JSONObject jsonObject)
    {
        setStock(jsonObject);
    }

    public void setStock(JSONObject jsonObject)
    {
        try
        {
            price = jsonObject.getDouble("price");
            delta = jsonObject.getDouble("delta");
            perc = jsonObject.getDouble("perc");
            vol = jsonObject.getInt("vol");
            time = jsonObject.getString("time");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public double getDelta() {
        return delta;
    }

    public double getPerc() {
        return perc;
    }

    public int getVol() {
        return vol;
    }
}
