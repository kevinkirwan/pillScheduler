package com.kevinkirwansoftware.capsule;

/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */
public class WeatherData{
    public int lon;
    public int lat;
}
/*
public class Sys{
    public String country;
    public int sunrise;
    public int sunset;
}

public class Weather{
    public int id;
    public String main;
    public String description;
    public String icon;
}

public class Main{
    public double temp;
    public int humidity;
    public int pressure;
    public double temp_min;
    public double temp_max;
}

public class Wind{
    public double speed;
    public double deg;
}

public class Rain{
    @JsonProperty("3h")
    public int _3h;
}

public class Clouds{
    public int all;
}

public class Root{
    public Coord coord;
    public Sys sys;
    public List<Weather> weather;
    public Main main;
    public Wind wind;
    public Rain rain;
    public Clouds clouds;
    public int dt;
    public int id;
    public String name;
    public int cod;
}




 */
