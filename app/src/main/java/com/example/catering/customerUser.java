package com.example.catering;

import java.util.ArrayList;

public class customerUser {
    private String name,id;
    private boolean membership;
    private ArrayList<order> orderHistory;

    public customerUser(String name,String id) {
        this.name = name;
        this.membership=false;
        ArrayList<service> tempoList=new ArrayList<>();
        service tempoService=new service();
        tempoList.add(tempoService);
        order sampleSave=new order();
        this.id=id;
        this.orderHistory=new ArrayList<>();
        orderHistory.add(sampleSave);
    }

    //get name
    public String getName() {
        return name;
    }

    //set name
    public void setName(String name) {
        this.name = name;
    }

    //get membership
    public boolean isMembership() {
        return membership;
    }

    //set membership
    public void setMembership(boolean membership) {
        this.membership = membership;
    }

    //get order history
    public ArrayList<order> getOrderHistory() {
        return orderHistory;
    }

    //set order history
    public void setOrderHistory(ArrayList<order> orderHistory) {
        this.orderHistory = orderHistory;
    }
    
    //add order
    public void addOrder(order order){
        this.orderHistory.add(order);
    }

    //get id
    public String getId() {
        return id;
    }

    //set id
    public void setId(String id) {
        this.id = id;
    }
}
