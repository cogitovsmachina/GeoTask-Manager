package org.androidtitlan.geotaskmanager;

import com.google.android.maps.GeoPoint;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

public class GeoHelper {

    public static List<GeoPoint> findMarker(GeoPoint topleft, GeoPoint bottomright, int zoomlevel) {
        List<GeoPoint> marker = new LinkedList<GeoPoint>();
        if (zoomlevel == 0 || zoomlevel == 1)
            marker.addAll(Arrays.asList(geopoint));
        else {
            if (topleft.getLongitudeE6() > bottomright.getLongitudeE6()) {
                for (int i = 0; i < geopoint.length; i++) {
                    GeoPoint p = geopoint[i];
                    if ((p.getLongitudeE6() > topleft.getLongitudeE6() || p.getLongitudeE6() < bottomright.getLongitudeE6())
                            && p.getLatitudeE6() < topleft.getLatitudeE6() && p.getLatitudeE6() > bottomright.getLatitudeE6()) {
                        marker.add(p);
                    }
                }
            } else {
                for (int i = 0; i < geopoint.length; i++) {
                    GeoPoint p = geopoint[i];
                    if (p.getLongitudeE6() > topleft.getLongitudeE6() && p.getLatitudeE6() < topleft.getLatitudeE6()
                            && p.getLongitudeE6() < bottomright.getLongitudeE6() && p.getLatitudeE6() > bottomright.getLatitudeE6()) {
                        marker.add(p);
                    }
                }
            }
        }
        return marker;
    }

    public static GeoPoint[] geopoint = {
            new GeoPoint(-30822124, 144329188),
            new GeoPoint(-22669843, 130266688),
            new GeoPoint(58588261, 123235440),
            new GeoPoint(40651417, 120071378),
            new GeoPoint(48169700, 101790128),
            new GeoPoint(26985661, 95813565),
            new GeoPoint(46259597, 71907316),
            new GeoPoint(34818255, 58899503),
            new GeoPoint(62555356, 52922943),
            new GeoPoint(23488374, 45891693),
            new GeoPoint(52859138, 44837006),
            new GeoPoint(34238995, 41321381),
            new GeoPoint(5183884, 33235443),
            new GeoPoint(56707481, 28665131),
            new GeoPoint(19895822, 27962006),
            new GeoPoint(1323664, 20227631),
            new GeoPoint(-8488742, 13899506),
            new GeoPoint(21539890, 8274506),
            new GeoPoint(48403631, 7571381),
            new GeoPoint(38207916, -6842681),
            new GeoPoint(19564899, -9655181),
            new GeoPoint(42750994, -35670806),
            new GeoPoint(71498757, -41998928),
            new GeoPoint(-8836293, -44459867),
            new GeoPoint(-19720236, -47272367),
            new GeoPoint(66620274, -47623928),
            new GeoPoint(8673280, -63444240),
            new GeoPoint(-13662068, -65202054),
            new GeoPoint(-38612632, -66256741),
            new GeoPoint(-28376961, -66959866),
            new GeoPoint(-2893736, -70123929),
            new GeoPoint(53907531, -70827053),
            new GeoPoint(69628397, -76100490),
            new GeoPoint(40115835, -80670802),
            new GeoPoint(51566782, -84537990),
            new GeoPoint(42492301, -90866114),
            new GeoPoint(37931145, -97194239),
            new GeoPoint(20884409, -100709865),
            new GeoPoint(51127615, -105983302),
            new GeoPoint(32477270, -108444239),
            new GeoPoint(44028320, -118639552),
            new GeoPoint(56319544, -120748927)

    };

}

