package com.tananut.travelmateapp;

/**
 * Created by Tananut on 24/3/2560.
 */

public class Singleton {
    private static Tab1Home _tab1 = new Tab1Home();
    private static Tab2Map _tab2 = new Tab2Map();
    private static Tab3Explored _tab3 = new Tab3Explored();
    private static Tab4Setting _tab4 = new Tab4Setting();

    public static Tab1Home Tab1() {
            return _tab1;
    }

    public static Tab2Map Tab2() {
            return _tab2;
    }

    public static Tab3Explored Tab3() {
            return _tab3;
    }

    public static Tab4Setting Tab4() {
            return _tab4;
    }
}
