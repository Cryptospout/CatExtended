/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Cryptospout
 */
public class UserHomeTest {

    public static void main(String args[]) {
        String s = System.getProperty("user.home");
        System.out.println(s);
       // return s;
    }
    
    public String getLocation(){
        return System.getProperty("user.home");
    }
}
