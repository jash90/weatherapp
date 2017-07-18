package com.example.ideo7.weather.Model;

/**
 * Created by ideo7 on 17.07.2017.
 */

public class Convert {
    public static String convertDegreeToCardinalDirection(Double directionInDegrees){
        String cardinalDirection = "0";
        if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
            cardinalDirection = "N";
        } else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
            cardinalDirection = "NNE";
        } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
            cardinalDirection = "NE";
        } else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
            cardinalDirection = "ENE";
        } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
            cardinalDirection = "E";
        } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
            cardinalDirection = "ESE";
        } else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
            cardinalDirection = "SE";
        } else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
            cardinalDirection = "SSE";
        } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
            cardinalDirection = "S";
        } else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
            cardinalDirection = "SSW";
        } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
            cardinalDirection = "SW";
        } else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
            cardinalDirection = "WSW";
        } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
            cardinalDirection = "W";
        } else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
            cardinalDirection = "WNW";
        } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
            cardinalDirection = "NW";
        } else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
            cardinalDirection = "NNW";
        } else {
            cardinalDirection = "?";
        }

        return cardinalDirection;
    }
    public static String convertWeatherCodeToDescription(int description)
    {
        switch (description)
        {
            case 200: return "thunderstorm with light rain";
            case 201: return "thunderstorm with rain";
            case 202: return "thunderstorm with heavy";
            case 210: return "light thunderstorm";
            case 211: return "thunderstorm";
            case 212: return "heavy thunderstorm";
            case 221: return "ragged thunderstorm";
            case 230: return "thunderstorm with light drizzle";
            case 231: return "thunderstorm with drizzle";
            case 232: return "thunderstorm with heavy drizzle";
            case 300: return "light intensity drizzle";
            case 301: return "drizzle";
            case 302: return "heavy intensity drizzle";
            case 310: return "light intensity drizzle rain";
            case 311: return "drizzle rain";
            case 312: return "heavy intensity drizzle rain";
            case 313: return "shower rain and drizzle";
            case 314: return "heavy shower rain and drizzle";
            case 321: return "shower drizzle";
            case 500: return "light rain";
            case 501: return "moderate rain";
            case 502: return "heavy intensity rain";
            case 503: return "very heavy rain";
            case 504: return "extreme rain";
            case 511: return "freezing rain";
            case 520: return "light intensity shower rain";
            case 521: return "shower rain";
            case 522: return "heavy intensity shower rain";
            case 531: return  "ragged shower rain";
            case 600: return "light snow";
            case 601: return "snow";
            case 602: return "heavy snow";
            case 611: return "sleet";
            case 612: return "shower sleet";
            case 615: return "light rain and snow";
            case 616: return "rain and snow";
            case 620: return "light shower snow";
            case 621: return "shower snow";
            case 622: return "heavy shower snow";
            case 701: return "mist";
            case 711: return "smoke";
            case 721: return "haze";
            case 731: return "sand, dust whirls";
            case 741: return "fog";
            case 751: return "sand";
            case 761: return "dust";
            case 762: return "volcanic ash";
            case 771: return "squalls";
            case 781: return "tornado";
            case 800: return "clear sky";
            case 801: return "few clouds";
            case 802: return "scattered clouds";
            case 803: return "broken clouds";
            case 804: return "overcast clouds";
            case 900: return "tornado";
            case 901: return "tropical storm";
            case 902: return "hurricane";
            case 903: return "cold";
            case 904: return "hot";
            case 905: return "windy";
            case 906: return "hail";
            case 951: return "calm";
            case 952: return "light breeze";
            case 953: return "gentle breeze";
            case 954: return "moderate breeze";
            case 955: return "fresh breeze";
            case 956: return "strong breeze";
            case 957: return "high wind, near gale";
            case 958: return "gale";
            case 959: return "severe gale";
            case 960: return "storm";
            case 961: return "violent storm";
            case 962: return "hurricane";
            default: return "?";
        }
    }
}
