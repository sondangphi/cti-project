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
    
    String name,mobile,address,gender,email,phone1;
    
    public CustomerObject(String m){
        mobile = m;
    }

    public CustomerObject() {
    }
    
    public void setName(String n){
        name = n;
    }
    public String getName(){
        return name;
    }

    public void setMobile(String m){
        mobile = m;
    }
    public String getMobile(){
        return mobile;
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
    
}
