/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gladiator;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author pc
 */
public class DBConnection {
    
    public static Connection connection(){
        
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3325/credit","mohammed","same1999*/@");
            return connect;
        }catch(Exception se){
            se.printStackTrace();
        }
        return null ;
        }
    
}
