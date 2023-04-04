/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gladiator;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author pc
 */
public class ClientDashboardController implements Initializable{
    
     @FXML
    private AnchorPane credit_Main_Form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label credit_ClientId;

    @FXML
    private Button logout;

    @FXML
    private TextField credit_BrandName;

    @FXML
    private ComboBox<?> credit_ProductName;

    @FXML
    private Spinner<Integer> credit_quantity;

    @FXML
    private Button credit_addBtn;

    @FXML
    private Button credit_BuyBtn;

    @FXML
    private Button credit_ReceiptBtn;

    @FXML
    private Label credit_Total;
    
    @FXML
    private TableView<customerData> credit_tableView;

    @FXML
    private TableColumn<customerData, String> credit_Col_BName;

    @FXML
    private TableColumn<customerData, String> credit_Col_PName;

    @FXML
    private TableColumn<customerData, String> credit_Col_Quantity;

    @FXML
    private TableColumn<customerData, String> credit_Col_Price;
    
    // DATABASE TOOLS
    private Connection con;
    private PreparedStatement preparedStatement;
    private Statement statement ;
    private ResultSet resultSet ;
    
    
     public void creditAdd(){
         creditCustomerId();
         creditSpinnerValue();
         creditGetPrice();
        
        String insertProd = "INSERT INTO customer " 
                + "(customer_id, brand, productName, quantity, date, price)" 
                + "VALUES(?, ?, ?, ?, ?, ?)";
        
        con = DBConnection.connection();
        
        try{
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            
            Alert alert;
            // CHECK IF THE FIELDS ARE EMPTY
            if(credit_BrandName.getText().isEmpty()
                || credit_ProductName.getSelectionModel().getSelectedItem() == null
                || Qty == 0){
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur Message");
                alert.setHeaderText(null);
                alert.setContentText("Please Choose product/s first");
                alert.showAndWait();
            }else{
      
                preparedStatement = con.prepareStatement(insertProd);
                preparedStatement.setString(1, String.valueOf(customerId));
                preparedStatement.setString(2, credit_BrandName.getText());
                preparedStatement.setString(3, (String)credit_ProductName.getSelectionModel().getSelectedItem());
                preparedStatement.setString(4, String.valueOf(Qty));
                preparedStatement.setString(5, String.valueOf(sqlDate));
                
                totalPrice = (Qty * price);
                preparedStatement.setString(6, String.valueOf(totalPrice));

                preparedStatement.executeUpdate();
                
                // TO UPDATE THE TABLEVIEW
                customersShowListData();
                // Aficher SUM
                creditDisplayTotal();
            }
        }catch(Exception se){ se.printStackTrace();}
    }
     
     
     public void creditReset(){
         creditCustomerId();
       String resetData = "DELETE FROM customer WHERE customer_id = '"
               +customerId+"'";
       
       con = DBConnection.connection();
       
      try{
            Alert alert ;
                
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are You Sure Do You Want To Reset? The Product List including To Reset");
              
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    statement = con.createStatement();
                    statement.executeUpdate(resetData);
                    
                    credit_BrandName.setText("");
                    credit_ProductName.getSelectionModel().clearSelection();
                    creditSpinner();
                    credit_Total.setText("$0.0");
                    
                    customersShowListData();
                    
                }else
                    return ;
            }catch(Exception se){ se.printStackTrace();}
   } 

     
     public void creditBuy(){
         creditCustomerId();
         creditDisplayTotal();
         String sqlBuy = "INSERT INTO customer_receipt (customer_id, total, date)"
                 +"VALUES (?, ?, ?)";
         
         con = DBConnection.connection();
         
         try{
             Date date = new Date();
             java.sql.Date sqlDate = new java.sql.Date(date.getTime());
             Alert alert ;
             if(credit_tableView.getItems().isEmpty()){
                 alert = new Alert(AlertType.ERROR);
                 alert.setTitle("Erreur Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Please Choose The Product First!");
                 alert.showAndWait();
             }else{
                 alert = new Alert(AlertType.CONFIRMATION);
                 alert.setTitle("Confirmation Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Are You Sure!");
                 
                 Optional<ButtonType> option = alert.showAndWait();
                 
                 if(option.get().equals(ButtonType.OK)){
                    preparedStatement = con.prepareStatement(sqlBuy);
                    preparedStatement.setString(1,String.valueOf(customerId));
                    preparedStatement.setString(2, String.valueOf(totalP));
                    preparedStatement.setString(3, String.valueOf(sqlDate));
                    
                    preparedStatement.executeUpdate();
                    
                 alert = new Alert(AlertType.INFORMATION);
                 alert.setTitle("Information Message");
                 alert.setHeaderText(null);
                 alert.setContentText("Successfully!");
                 alert.showAndWait();
                    
                 }else
                     return;
                 
                }
        }catch(Exception se){se.printStackTrace();}
     }
     
     
     public void creditReceipt(){
         
         HashMap hash = new HashMap();
         
         // PARAMETRE NAME, id 
         hash.put("creditP",customerId);
         
         
         
         try{
             
             Alert alert ;
             if(totalP == 0 ){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid : 3!");
                alert.showAndWait();
             }else{
                JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\pc\\Documents\\NetBeansProjects\\Gladiator\\src\\gladiator\\report.jrxml");
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, hash, con);
                
                JasperViewer.viewReport(jPrint, false);
                 
             }

         }catch(Exception se){se.printStackTrace();}
     }
     
     //AFICHER TOTAL
    private double totalP = 0;
    public void creditDisplayTotal(){
        creditCustomerId();
        String sqlTotal = "SELECT SUM(price) FROM customer WHERE customer_id = '"
                +customerId+"'";
        
        con = DBConnection.connection();
        
        try{
            statement = con.createStatement();
            resultSet = statement.executeQuery(sqlTotal);
            
            if(resultSet.next()){
                totalP = resultSet.getDouble("SUM(price)");
            }
            
            credit_Total.setText("$"+String.valueOf(totalP));
            
                    
        }catch(Exception se){se.printStackTrace();}
    }
     
    
     private double price = 0 ;
     private double totalPrice = 0 ;
     public void creditGetPrice(){
        String gPrice = "SELECT price FROM product WHERE product_name = '"
                +credit_ProductName.getSelectionModel().getSelectedItem()+"'";
        
        con = DBConnection.connection();
        
        try{
            statement = con.createStatement();
            resultSet = statement.executeQuery(gPrice);
            
            if(resultSet.next()){
                price = resultSet.getDouble("price");
            }
        }catch(Exception se){se.printStackTrace();}

     }
    
    public void creditSearchBrand(){
        
        String searchB = "SELECT * FROM product WHERE brand = '"
                +credit_BrandName.getText()+"' and status = 'Available'";
        
        con = DBConnection.connection();
        
        try{
            
            preparedStatement = con.prepareStatement(searchB);
            resultSet = preparedStatement.executeQuery();
            ObservableList ListProduct = FXCollections.observableArrayList();
            
            while(resultSet.next()){
                ListProduct.add(resultSet.getString("product_name"));
            }
            
            
            
            credit_ProductName.setItems(ListProduct);

            
        }catch(Exception se ){se.printStackTrace();}
        
    }
    
    private SpinnerValueFactory spinner ;
    public void creditSpinner(){                                   // MIN, MAX, DISPLAY NUM
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
    
    credit_quantity.setValueFactory(spinner);
    
    }
    
    private int Qty ;
    public void creditSpinnerValue(){
        Qty = credit_quantity.getValue();
        
       // System.out.println(Qty);
    }
    
    
    
    public ObservableList<customerData> customersListData(){
        
        creditCustomerId();
        ObservableList<customerData> customerList = FXCollections.observableArrayList();
        
        String sqlcust = "SELECT * FROM customer WHERE customer_id = '" + customerId + "'";
        
        con = DBConnection.connection();
        try{
        customerData custDB;

            preparedStatement = con.prepareStatement(sqlcust);
            resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                        custDB = new customerData(resultSet.getInt("customer_id")
                            , resultSet.getString("brand")
                            , resultSet.getString("productName")
                            , resultSet.getInt("quantity")
                            , resultSet.getDouble("price")
                            , resultSet.getDate("date"));

                customerList.add(custDB);
           
            }
        }catch(Exception se){
            se.printStackTrace();
        }
        return customerList;
    }
    
    
    private int customerId;
    public void creditCustomerId(){
        String cID = "SELECT customer_id FROM customer";
        
        con = DBConnection.connection();
        
        try{
            preparedStatement = con.prepareStatement(cID);
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
                // FINAL ROW OF CUSTOMER ID
                customerId = resultSet.getInt("customer_id");
            }
                int checkNum = 0;
                
                String checkCustomerId = "SELECT customer_id FROM customer_receipt";
                
                statement = con.createStatement();
                resultSet = statement.executeQuery(checkCustomerId);
                
                while(resultSet.next()){
                    checkNum = resultSet.getInt("customer_id");
                }
                
                if(customerId == 0){
                    customerId += 1 ; 
                }else if(checkNum == customerId){
                    customerId += 1 ;
                }
                
            
            
            
            
        }catch(Exception se){se.printStackTrace();}
    }
    
    public void displayClient(){
        credit_ClientId.setText(getData.client_id);
    }
    
    private double x = 0;
    private double y = 0;
    
    private ObservableList<customerData> customersList;
    
    public void customersShowListData(){
        customersList = customersListData();
        
        credit_Col_BName.setCellValueFactory(new PropertyValueFactory<>("brand"));
        credit_Col_PName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        credit_Col_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        credit_Col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
    
        credit_tableView.setItems(customersList);
    }
    public void logout(){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confimation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are You Sure Do You Want To LogOut?");
            
            Optional<ButtonType> option = alert.showAndWait();
             
            if(option.get().equals((ButtonType.OK))){
                
                logout.getScene().getWindow().hide();
                // Back To Login Form
                Parent root = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
                Stage window = new Stage();
                Scene scene = new Scene(root);
                
                root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    window.setX(event.getScreenX() - x);
                    window.setY((event.getScreenY() - y));

                    window.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                window.setOpacity(1);
                });

                window.initStyle(StageStyle.TRANSPARENT);
                
                window.setScene(scene);
                window.show();
            }else
                return ;
        }catch(Exception se){
            se.printStackTrace();
        }
    }
    
    
    
    

    public void close(){
        System.exit(0);
    }
    
    public void minimize(){
        Stage stage = (Stage)credit_Main_Form.getScene().getWindow();
        stage.setIconified(true);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        displayClient();
        
        customersShowListData();
        creditSpinner();
        creditDisplayTotal();       
    }
    
}
