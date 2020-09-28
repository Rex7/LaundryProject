package com.example.laundryproject.laundryhistory;

public class LaundryHistory {
    private String noOfClothes;
    private String cost;

    LaundryHistory(String noOfClothes, String cost) {
        this.noOfClothes = noOfClothes;
        this.cost = cost;
    }


    public String getNoOfClothes() {
        return noOfClothes;
    }

    public void setNoOfClothes(String noOfClothes) {
        this.noOfClothes = noOfClothes;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
