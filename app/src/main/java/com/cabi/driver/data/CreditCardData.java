package com.cabi.driver.data;

//Getter/setter class to hold the credit card details.
public class CreditCardData {
    public String id, type, month, year, card, cvv;

    public CreditCardData(String _id, String _type, String _month, String _year, String _card, String _cvv) {

        id = _id;
        type = _type;
        month = _month;
        year = _year;
        card = _card;
        cvv = _cvv;
    }

    public String getCard() {

        return card;
    }

    public void setCard(String card) {

        this.card = card;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getMonth() {

        return month;
    }

    public void setMonth(String month) {

        this.month = month;
    }

    public String getYear() {

        return year;
    }

    public void setYear(String year) {

        this.year = year;
    }

    public String getCvv() {

        return cvv;
    }

    public void setCvv(String cvv) {

        this.cvv = cvv;
    }
}
