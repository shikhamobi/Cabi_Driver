package com.cabi.driver.data;

/**
 * Created by developer on 11/11/16.
 */
public class SearchListData {
    String lat;
    //{"status":"OK","predictions":[{"id":"1a9380e11c829894db5da2e1ed08daba9743b2bd","place_id":"ChIJJZX6649fqDsRCFW_sSfqNac","types":["sublocality_level_1","sublocality","political","geocode"],"matched_substrings":[{"offset":0,"length":12}],"reference":"ClRDAAAAGW8svD3VNoKwmPJMYPaSG1hdqST8NVZcdikQUgn7ZSQRGK6vofUfnTaCrDF3uKs-_EEVraLbfFWKRnQMtAWeGkm-WdE1OyG6O73Mwc6aHcsSEGsZz8nRHvZL1ipmwftEDIIaFKO-s-1Sz9qn4lVHd_vxe9q9QtU8","description":"Maruthamalai, Coimbatore, Tamil Nadu, India","terms":[{"value":"Maruthamalai","offset":0},{"value":"Coimbatore","offset":14},{"value":"Tamil Nadu","offset":26},{"value":"India","offset":38}]},{"id":"46e83c89fcace0e89c3101f7a29b79c9ad51aeee","place_id":"ChIJb4q5NKpZqDsRifCQyEON5g8","types":["establishment"],"matched_substrings":[{"offset":0,"length":12}],"reference":"ClRPAAAA177Xidw_Ucb4gM_VhVkt76V3brZpb2k7A3gMiXqb3UODDmOEpQhAn_N4QLzbbq9sUXsSc-KP9xRRT5Qi774E5xQ7fLhccFImY7DH1Va8mGwSEPd1ydsnZQuWPbLXSPJYudcaFNE0Si11zrPjM6p3u2RuYnSeALhW","description":"Maruthamalaiandavar Tiles & Timber, Coimbatore, Tamil Nadu, India","terms":[{"value":"Maruthamalaiandavar Tiles & Timber","offset":0},{"value":"Coimbatore","offset":36},{"value":"Tamil Nadu","offset":48},{"value":"India","offset":60}]},{"id":"6ab63f2d4df11c286755134016ead3132c23dcee","place_id":"ChIJG27Wg9NYqDsRpyq3ySSlWnE","types":["establishment"],"matched_substrings":[{"offset":0,"length":12}],"reference":"CnRiAAAA_y4uQ_lfqzerq79AWVPnITOjBKBYd91UQYUNRc7k4-rVN4n88drXj_qxgLrSx5b-4ArAnVsMoVXBicuUxaquGF9Baeb8t_g72S9eJUywRvH3iM_23pfoJ1HqaL9eRUi3Ix6XyTGtMid8F20JJh2q3BIQhIMESC7lCoKvG1npXkZhWBoU_01OuPP6enCPBHJcUb-NAuAXYfE","description":"Maruthamalai Fruit Juice, Marudhamalai Road, PN Pudur, Coimbatore, Tamil Nadu, India","terms":[{"value":"Maruthamalai Fruit Juice","offset":0},{"value":"Marudhamalai Road","offset":26},{"value":"PN Pudur","offset":45},{"value":"Coimbatore","offset":55},{"value":"Tamil Nadu","offset":67},{"value":"India","offset":79}]},{"id":"eb69ce66060367254b3165e2796533d74ddb2e1f","place_id":"ChIJ0Ryqii1fqDsRvuhFZcjPmm0","types":["establishment"],"matched_substrings":[{"offset":0,"length":12}],"reference":"ClRMAAAAsCZHi2eKQN5cVGBcjRI4oLMq9nZ6t97OlfI3rjwXdh74296yAHa4nDpsRm_NFC4uF--gK-I0qw-9xQaK1sLWrk27y-Ycsp3ebwdbgY2OcgYSEBBTd8rrSnHtcNg9EmcLogkaFHNhu6c9WhMEEdLBVy7s4h9nCUBP","description":"Maruthamalai Andavar Industries, Coimbatore, Tamil Nadu, India","terms":[{"value":"Maruthamalai Andavar Industries","offset":0},{"value":"Coimbatore","offset":33},{"value":"Tamil Nadu","offset":45},{"value":"India","offset":57}]},{"id":"4cc2e7801fb49566cc3b41fe5a9cd740fbb3b1c9","place_id":"ChIJ07TiNwtZqDsRE9ZZjoCoZjw","types":["establishment"],"matched_substrings":[{"offset":0,"length":12}],"reference":"CmReAAAAFvoslRsFT2HXi6P6LZVwtM8_qiGFh_kAcLZ_Xtr5kNqjEBQpKCLO8UIH0f60rdAZdQ3Tn807Q5nE-thGqiKZgfse3zsJrzc97H7smrmC7WbMp8UWqtrmrMBg1YhhZtkBEhB5FyibP-2nvyDMhvtM7cr2GhTAjgbxKwTiHppHD3roR6xd_cO45A","description":"Maruthamalaian Medicals, Edayar Street, Town Hall, Coimbatore, Tamil Nadu, India","terms":[{"value":"Maruthamalaian Medicals","offset":0},{"value":"Edayar Street","offset":25},{"value":"Town Hall","offset":40},{"value":"Coimbatore","offset":51},{"value":"Tamil Nadu","offset":63},{"value":"India","offset":75}]}]}

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAdd1() {
        return Add1;
    }

    public void setAdd1(String add1) {
        Add1 = add1;
    }

    String lng;
    String Add1;

    public String getAdd2() {
        return Add2;
    }

    public void setAdd2(String add2) {
        Add2 = add2;
    }

    String Add2;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    String place_id;
}
