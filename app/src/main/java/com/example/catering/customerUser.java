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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMembership() {
        return membership;
    }

    public void setMembership(boolean membership) {
        this.membership = membership;
    }

    public ArrayList<order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<order> orderHistory) {
        this.orderHistory = orderHistory;
    }
    public void addOrder(order order){
        this.orderHistory.add(order);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
