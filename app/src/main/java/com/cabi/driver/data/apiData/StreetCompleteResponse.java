package com.cabi.driver.data.apiData;

/**
 * Created by developer on 16/11/16.
 */
public class StreetCompleteResponse {


    public String message;
    public Detail detail;
    public String status;


    public class Detail {


            public String fare;
            public String pickup;
            public String trip_id;



//        public String subtotal_fare;
//
//        public String wallet_amount_used;
//
//        public String waiting_cost;
//
//        public String drop;
//
//        public String minutes_traveled;
//
//        public String street_pickup;
//
//        public String passenger_discount;
//
//        public String distance;
//
//        public String tax_amount;
//
//        public String nightfare;
//
//        public Gateway_details[] gateway_details;
//
//        public String trip_fare;
//
//        public String eveningfare;
//
//        public String eveningfare_applicable;
//
//        public String pickup;
//
//        public String credit_card_status;
//
//        public String pass_id;
//
//        public String total_fare;
//
//        public String referdiscount;
//
//        public String waiting_per_hour;
//
//        public String trip_id;
//
//        public String company_tax;
//
//        public String base_fare;
//
//        public String metric;
//
//        public String minutes_fare;
//
//        public String roundtrip;
//
//        public String promo_discount_per;
//
//        public String waiting_time;
//
//        public String nightfare_applicable;
//
//        public String promodiscount_amount;
    }

    public class Gateway_details {
        public String pay_mod_id;

        public String pay_mod_default;

        public String pay_mod_name;
    }
}
