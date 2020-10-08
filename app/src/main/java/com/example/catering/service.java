package com.example.catering;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class service implements Parcelable {
    private String name,id,price,numPerson,profit;
    private int amountSold=0;
    private ArrayList<String> foodList;
    public service(){
    }
    public service createService(String name,String id,ArrayList<String> foodList,String price,String numPerson,String profit){
        service tempoService=new service();
        tempoService.setName(name);
        tempoService.setId(id);
        tempoService.setFoodList(foodList);
        tempoService.setPrice(price);
        tempoService.setNumPerson(numPerson);
        tempoService.setProfit(profit);
        return tempoService;
    }
    public service sales(String name,String id,String profit){
        service tempoService=new service();
        tempoService.setName(name);
        tempoService.setId(id);
        tempoService.setProfit(profit);
        return tempoService;
    }
    public void createSales(){
        this.amountSold++;
    }
    protected service(Parcel in) {
        name = in.readString();
        id = in.readString();
        price = in.readString();
        numPerson = in.readString();
        profit=in.readString();
        foodList = in.createStringArrayList();
    }
    public void addSampleToList(){
        this.foodList=new ArrayList<>();
        this.foodList.add(" ");
    }
    public static final Creator<service> CREATOR = new Creator<service>() {
        @Override
        public service createFromParcel(Parcel in) {
            return new service(in);
        }

        @Override
        public service[] newArray(int size) {
            return new service[size];
        }
    };

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumPerson() {
        return numPerson;
    }

    public void setNumPerson(String numPerson) {
        this.numPerson = numPerson;
    }

    public ArrayList<String> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<String> foodList) {
        this.foodList = foodList;
    }

    public String getColor(){
        return "#464373";
    }
    public int getColorCard(){
        return R.drawable.card;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public int getImage(){
        return 0;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
    public String getAllProfit(){
        return String.valueOf(Integer.parseInt(this.profit)*this.amountSold);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(price);
        dest.writeString(numPerson);
        dest.writeString(profit);
        dest.writeStringList(foodList);
    }

}
