package com.example.catering;

public class order {
    private String id,transactionNum,orderNum,date,address,time,userId,orderStatus,name;
    private service itemOrder;
    public order(){

    }
    public order createOrder(String transactionNum, String orderNum, service itemOrder,String date,String address,String time,String userId){
        this.transactionNum = transactionNum;
        this.orderNum = orderNum;
        this.itemOrder = itemOrder;
        this.date=date;
        this.address=address;
        this.time=time;
        this.userId=userId;
        this.orderStatus="booked";
        return this;
    }
    public order createTempoOrder(String name,String date, String time,String address,String status,String transactionNum){
        this.transactionNum=transactionNum;
        this.date=date;
        this.address=address;
        this.time=time;
        this.orderStatus=status;
        this.name=name;
        return this;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public service getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(service itemOrder) {
        this.itemOrder = itemOrder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
