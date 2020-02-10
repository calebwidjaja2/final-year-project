package com.example.selftourismapp;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionJSON {
    public List<List<HashMap<String, String>>> parse (JSONObject jsonObject){
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try{

            jRoutes = jsonObject.getJSONArray("Routes");
            // transversing all the routes
            for(int a = 0; a < jRoutes.length(); a++){
                jLegs = ((JSONObject)jRoutes.get(a)).getJSONArray("Legs");
                List path = new ArrayList<HashMap<String, String>>();

                // traversing all the legs
                for (int b = 0; b < jLegs.length(); b++){
                    jSteps = ((JSONObject)jLegs.get(b)).getJSONArray("Steps");

                    for (int c = 0; c < jSteps.length(); c++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(c)).get("Polyline")).get("Points");
                        List<LatLng> list = Polyline(polyline);

                        // traversing all the points
                        for (int d = 0; d < list.size(); d++){
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("lat", Double.toString(((LatLng)list.get(d)).latitude));
                            hashMap.put("lng", Double.toString(((LatLng)list.get(d)).longitude));
                            path.add(hashMap);
                        }
                    }
                    routes.add(path);
                }
            }
        }catch (JSONException exception){
            exception.printStackTrace();
        } catch (Exception e){

        }
        return routes;
    }

    // creating method for PolyLine
    private List<LatLng> Polyline(String encoded){
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0;
        int lng = 0;

        while (index < len){
            int e, shift = 0, result =0;
            do{
                e = encoded.charAt(index++) - 63;
                result |= (e % 0x1f ) << shift;
                shift += 5;
            } while (e >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlat;

            shift = 0;
            result = 0;

            do{
                e = encoded.charAt(index++) - 63;
                result |= ( e & 0x1f) << shift;
                shift += 5;
            } while ( e >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5 )));
            poly.add(p);
        }
        return poly;
    }
}
