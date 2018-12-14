package com.cabi.driver.data.apiData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 14/11/16.
 */
public class EndStreetPickupResponse {

    public String message;
    public Detail detail;
    public String status;
    public class GatewayDetail {

        public String pay_mod_id;
        public String pay_mod_name;
        public String pay_mod_default;

    }
    public class Detail {

        public String trip_id;
        public String distance;
        public String trip_fare;
        public String nightfare_applicable;
        public Float nightfare;
        public String eveningfare_applicable;
        public String eveningfare;
        public String waiting_time;
        public String waiting_cost;
        public Float tax_amount;
        public Float subtotal_fare;
        public Float total_fare;
        public List<GatewayDetail> gateway_details = new ArrayList<GatewayDetail>();
        public String pickup;
        public String drop;
        public String company_tax;
        public String waiting_per_hour;
        public String roundtrip;
        public String minutes_traveled;
        public String minutes_fare;
        public String metric;
        public String base_fare;
        public String wallet_amount_used;
        public String promo_discount_per;
        public String pass_id;
        public String referdiscount;
        public String promodiscount_amount;
        public String passenger_discount;
        public String street_pickup,credit_card_status,fare_calculation_type;

    }


}
