/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.model;

/**
 *
 * @author leehoa
 */
public class CustomerObject {
    
    String name,phone,address,gender,email,phone1,id,birthday, reg;
    
    public CustomerObject(String m){
        phone = m;
    }

    public CustomerObject() {
    }
    
    public void setName(String n){
        name = n;
    }
    public String getName(){
        return name;
    }

    public void setPhone(String m){
        phone = m;
    }
    public String getPhone(){
        return phone;
    }    
    
    public void setAddress(String a){
        address = a;
    }
    public String getAddress(){
        return address;
    }    

    public void setGender(String g){
        gender = g;
    }
    public String getGender(){
        return gender;
    }    

    public void setEmail(String e){
        email = e;
    }
    public String getEmail(){
        return email;
    }
    
    public void setPhone1(String p){
        phone1 = p;
    }
    public String getPhone1(){
        return phone1;
    }   
    
    public void setId(String i){
        id = i;
    }
    public String getId(){
        return id;
    }    
    public void setBirth(String date){
        birthday = date;
    }
    public String getBirth(){
        return birthday;
    }    
    public void setReg(String re){
        reg = re;
    }
    public String getReg(){
        return reg;
    }    
}
