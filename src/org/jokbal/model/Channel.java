package org.jokbal.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Channel
{
    private String name;
    private Stock stock;
    private boolean checkd;
    private boolean changed;

    public Channel(String data)
    {
        JSONObject jsonObject,streamJsonObject;
        try
        {
            jsonObject = new JSONObject(data);
            streamJsonObject = jsonObject.getJSONObject("stream");
            this.name = jsonObject.getString("ticker");
            if(stock != null)
            {
                stock.setStock(streamJsonObject);
            }
            else
            {
                stock = new Stock(streamJsonObject);
            }
        }
        catch (JSONException e)
        {
            this.name = data;
        }
        this.checkd = true;
        this.changed = false;
    }

    public void setStock(String data)
    {
        JSONObject jsonObject;
        JSONObject streamJsonObject;
        try
        {
            jsonObject = new JSONObject(data);
            streamJsonObject = jsonObject.getJSONObject("stream");
            stock = new Stock(streamJsonObject);
        }
        catch (JSONException e)
        {
        }
    }

    public String getName()
    {
        return name;
    }

    public Stock getStock()
    {
        return stock;
    }

    public boolean isCheckd()
    {
        return checkd;
    }

    public void setCheckd(boolean checkd)
    {
        this.checkd = checkd;
    }

    public boolean isChanged()
    {
        return changed;
    }

    public void setChanged(boolean changed)
    {
        this.changed = changed;
    }
}
