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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author pc
 */
public class adminDashboardController implements Initializable{

    @FXML
    private AnchorPane mainAdminDash_form;
    
    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button addProducts_btn;

    @FXML
    private Button clients_btn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_ActiveClients;

    @FXML
    private Label dashboard_IncomeToday;

    @FXML
    private Label dashboard_TotalIncome;

    @FXML
    private AreaChart<?, ?> dashboard_Chart;

    @FXML
    private AnchorPane addProducts_form;

    @FXML
    private TextField addProducts_ProductID;

    @FXML
    private TextField addProducts_BName;

    @FXML
    private TextField addProducts_ProductName;

    @FXML
    private ComboBox<?> addProducts_Status;

    @FXML
    private Button addProducts_addBtn;

    @FXML
    private Button addProducts_updateBtn;

    @FXML
    private Button addProducts_clearBtn;

    @FXML
    private Button addProducts_deleteBtn;

    @FXML
    private TextField addProducts_Price;

    @FXML
    private TextField addProducts_search;

    @FXML
    private TableView<productDB> addProducts_tableView;

    @FXML
    private TableColumn<productDB, String> addProducts_Col_ProductID;

    @FXML
    private TableColumn<productDB, String> addProducts_Col_BName;

    @FXML
    private TableColumn<productDB, String> addProducts_Col_ProductName;

    @FXML
    private TableColumn<productDB, String> addProducts_Col_Status;

    @FXML
    private TableColumn<productDB, String> addProducts_Col_Price;

    @FXML
    private AnchorPane clients_form;

    @FXML
    private TableView<ClientData> Clients_tableView;

    @FXML
    private TableColumn<ClientData,String> Clients_Col_ClientID;

    @FXML
    private TableColumn<ClientData,String> Clients_Col_Password;

    @FXML
    private TableColumn<ClientData,String> Clients_Col_FName;

    @FXML
    private TableColumn<ClientData,String> Clients_Col_LName;

    @FXML
    private TableColumn<ClientData,String> Clients_Col_Gender;

    @FXML
    private TableColumn<ClientData, String> Clients_Col_Date;

    @FXML
    private TextField Clients_ClientID;

    @FXML
    private TextField Clients_Password;

    @FXML
    private TextField Clients_FName;

    @FXML
    private TextField Clients_LName;

    @FXML
    private ComboBox<?> Clients_Gender;

    @FXML
    private Button Clients_ClearBtn;

    @FXML
    private Button Clients_DeleteBtn;

    @FXML
    private Button Clients_UpdateBtn;

    @FXML
    private Button Clients_SaveBtn;
    
    private double x = 0 ;
    private double y = 0 ;
    
    private Connection con;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Statement statement;
    
    public void dashboardDisplayActiveClients(){
        String sql = "SELECT COUNT(id) FROM client";
        
        con = DBConnection.connection();
        
        int countC = 0 ;
        
        try{
            statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                countC = resultSet.getInt("COUNT(id)");
            }
            
            dashboard_ActiveClients.setText(String.valueOf(countC));
        }catch(Exception se){se.printStackTrace();}
    }
    
    
    public void dashboardDisplayIncomeToday(){
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
        String sql = "SELECT SUM(total) FROM customer_receipt WHERE date = '"
                +sqlDate+"'";
        
        
        double sumT = 0;
        con = DBConnection.connection();
        
        try{
           statement = con.createStatement();
           resultSet = statement.executeQuery(sql);
           
           if(resultSet.next()){
               sumT = resultSet.getDouble("SUM(total)");
           }
           
           dashboard_IncomeToday.setText("$"+String.valueOf(sumT));
           
           
        }catch(Exception se){se.printStackTrace();}
    }
    
    
        public void dashboardTotalIncome(){
        
        String sql = "SELECT SUM(total) FROM customer_receipt ";
        
                con = DBConnection.connection();
        
        double sumTI = 0;
        
        try{
           statement = con.createStatement();
           resultSet = statement.executeQuery(sql);
           
           if(resultSet.next()){
               sumTI = resultSet.getDouble("SUM(total)");
           }
           
           dashboard_TotalIncome.setText("$"+String.valueOf(sumTI));
           
           
        }catch(Exception se){se.printStackTrace();}
    }
        
        
        public void dashboardChart(){
            dashboard_Chart.getData().clear();
            
            String sql = "SELECT date, SUM(total) FROM customer_receipt GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 9";
            
            con = DBConnection.connection();
            try{
                XYChart.Series chart = new XYChart.Series();
            
            
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            
            while(resultSet.next()){
                chart.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));
            }
            
            dashboard_Chart.getData().add(chart);
            }catch(Exception se){se.printStackTrace();}
        }
    
    
    
    public void addProductsAdd(){
        
        String insertProducts = "INSERT INTO product " + "(product_id, brand, product_name, status, price)" + "VALUES(?, ?, ?, ?, ?)";
        
        con = DBConnection.connection();
        
        try{
            Alert alert;
            // CHECK IF THE FIELDS ARE EMPTY
            if(addProducts_ProductID.getText().isEmpty() 
                || addProducts_BName.getText().isEmpty() 
                || addProducts_ProductName.getText().isEmpty() 
                || addProducts_Status.getSelectionModel().getSelectedItem() == null 
                || addProducts_Price.getText().isEmpty()){
                
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();
            }else{
            
                String query = "SELECT product_id FROM product WHERE product_id = '" 
                        + addProducts_ProductID.getText()+ "'";
                
                statement = con.createStatement();
                resultSet = statement.executeQuery(query);
                
                if(resultSet.next()){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID : "+ addProducts_ProductID.getText()+ "Was Already Exist!");
                    alert.showAndWait();
                }else{
                
                    preparedStatement = con.prepareStatement(insertProducts);
                    preparedStatement.setString(1, addProducts_ProductID.getText());
                    preparedStatement.setString(2, addProducts_BName.getText());
                    preparedStatement.setString(3, addProducts_ProductName.getText());
                    preparedStatement.setString(4, (String)addProducts_Status.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(5, addProducts_Price.getText());

                    preparedStatement.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    
                    // TO BECOME UPDATE THE TABLEVIEW ONCE YOU INSERT DATA
                    addProductsShowData();
                    // CLEAR THE FIELDS ONCE YOU INSERT SUCCESSFULLY THE DATA
                    addProductsClear();
                }
            }
            
        }catch(Exception se){ se.printStackTrace();}
    }
    
    
    // Update Products
    public void addProductsUpdate(){
        String queryUpdate = "UPDATE product SET brand = '"
                +addProducts_BName.getText()+"', product_name = '"
                +addProducts_ProductName.getText()+"', status = '"
                +addProducts_Status.getSelectionModel().getSelectedItem()+"', price = '"
                +addProducts_Price.getText()+"' WHERE product_id = '"
                +addProducts_ProductID.getText()+"'";
        
        con = DBConnection.connection();
        
        try{
            Alert alert ;
            
            // CHECK IF THE FIELDS ARE EMPTY
            if(addProducts_ProductID.getText().isEmpty() 
                || addProducts_BName.getText().isEmpty() 
                || addProducts_ProductName.getText().isEmpty() 
                || addProducts_Status.getSelectionModel().getSelectedItem() == null 
                || addProducts_Price.getText().isEmpty()){
                
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the blank fields");
            alert.showAndWait();
            
            }else{
                
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are You Sure Do You Want To Update Product ID : "+ addProducts_ProductID.getText() + "?");
                
                Optional<ButtonType> option = alert.showAndWait();
                // IF YOU CLICK OK
                if(option.get().equals(ButtonType.OK)){
                statement = con.createStatement();
                statement.executeUpdate(queryUpdate);
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Update!");
                alert.showAndWait();
                
                // TO BECOME UPDATE THE TABLEVIEW ONCE YOU INSERT DATA
                addProductsShowData();
                // CLEAR THE FIELDS ONCE YOU INSERT SUCCESSFULLY THE DATA
                addProductsClear();
                    
                }else{
                    return ;
                }
            }
            
        }catch(Exception se){se.printStackTrace();}
        
    }
   
    
    // DELETE Products
   public void addProductsDelete(){
       String queryDelete = "DELETE FROM product WHERE product_id = '"
               +addProducts_ProductID.getText()+"'";
       
       con = DBConnection.connection();
       
      try{
            Alert alert ;
            
            // CHECK IF THE FIELDS ARE EMPTY
            if(addProducts_ProductID.getText().isEmpty() 
                || addProducts_BName.getText().isEmpty() 
                || addProducts_ProductName.getText().isEmpty() 
                || addProducts_Status.getSelectionModel().getSelectedItem() == null 
                || addProducts_Price.getText().isEmpty()){
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();

            }else{
                
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are You Sure Do You Want To Delete Product ID : "+ addProducts_ProductID.getText()+"?");
              
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    preparedStatement = con.prepareStatement(queryDelete);
                    preparedStatement.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                    
                    // TO BECOME UPDATE THE TABLEVIEW ONCE YOU INSERT DATA
                    addProductsShowData();
                    // CLEAR THE FIELDS ONCE YOU INSERT SUCCESSFULLY THE DATA
                    addProductsClear();
                    
                }else
                    return ;
            }
           
       }catch(Exception se){ se.printStackTrace();}
   } 



    // Clear Products
    public void addProductsClear(){
        addProducts_ProductID.setText("");
        addProducts_BName.setText("");
        addProducts_ProductName.setText("");
        addProducts_Status.getSelectionModel().clearSelection();
        addProducts_Price.setText("");
    }
    
    
    private String[] statusList = {"Available", "Not Available"};
    public void addProductsStatusList(){
        List<String> listeSta = new ArrayList<>();
        
        for(String data: statusList){
            listeSta.add(data);
        }
        
        ObservableList statusData = FXCollections.observableArrayList(listeSta);
        addProducts_Status.setItems(statusData);
    
    }
    
    // SEARCH product
    public void addProductsSearch(){
        FilteredList<productDB> filter = new FilteredList<>(addProductsList, e -> true);
    
        addProducts_search.textProperty().addListener((Observable, oldValue, newValue) -> {
        
            filter.setPredicate(predicateProductDB -> {
               if(newValue == null || newValue.isEmpty()){
                   return true;
               } 
               
               String searchKey = newValue.toLowerCase();
               
               // IT CANT READ THEM, FIRST WE NEED TO RETURN TRUE. SEE ?
               
               if(predicateProductDB.getProduct_id().toLowerCase().contains(searchKey)){
                   return true;
               }else if(predicateProductDB.getBrand().toLowerCase().contains(searchKey)){
                   return true;
               }else if(predicateProductDB.getStatus().toLowerCase().contains(searchKey)){
                   return true;
               }else if(predicateProductDB.getProduct_name().toLowerCase().contains(searchKey)){
                   return true;
               }else if(predicateProductDB.getPrice().toString().contains(searchKey)){
                   return true;
               }else{
                   return false;
               }
            });
        
        });
        
        SortedList<productDB> sortList = new SortedList<>(filter);
    
        sortList.comparatorProperty().bind(addProducts_tableView.comparatorProperty());
            addProducts_tableView.setItems(sortList);
    
    }
    
    public ObservableList<productDB> addProductsListData(){
        ObservableList<productDB> prodList = FXCollections.observableArrayList();
        
        
        String sql = "SELECT * FROM product";
        
        con = DBConnection.connection();
        try{
        productDB prod;
        
        preparedStatement = con.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
            while(resultSet.next()){
                    prod = new productDB(resultSet.getString("product_id")
                        , resultSet.getString("brand")
                        , resultSet.getString("product_name")
                        , resultSet.getString("status")
                        , resultSet.getDouble("price"));

            prodList.add(prod);
           
            }
        }catch(Exception se){
            se.printStackTrace();
        }
        return prodList;
    }
    
    private ObservableList<productDB> addProductsList;
    
    public void addProductsShowData(){
        addProductsList = addProductsListData();
        
        addProducts_Col_ProductID.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        addProducts_Col_BName.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProducts_Col_ProductName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        addProducts_Col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addProducts_Col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
    
    addProducts_tableView.setItems(addProductsList);
    }
    
    public void addProductsSelect(){
        productDB prod = addProducts_tableView.getSelectionModel().getSelectedItem();
        int num = addProducts_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1){
            return;
        }
        
        addProducts_ProductID.setText(prod.getProduct_id());
        addProducts_BName.setText(prod.getBrand());
        addProducts_ProductName.setText(prod.getProduct_name());
        addProducts_Price.setText(String.valueOf(prod.getPrice()));
        
        
    }
    
    // CLIENT
    
    
    public void ClientsSave(){
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
        String insertClient = "INSERT INTO client " 
                + "(client_id, password, firstname, lastname, gender, date)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        con = DBConnection.connection();
        
        try{
            Alert alert ;
            
            if(Clients_ClientID.getText().isEmpty() 
                || Clients_Password.getText().isEmpty() 
                || Clients_FName.getText().isEmpty() 
                || Clients_LName.getText().isEmpty()
                || Clients_Gender.getSelectionModel().getSelectedItem() == null){
                
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();

            }else{
                
                String checkExist = "SELECT client_id FROM client WHERE client_id =  '"
                        +Clients_ClientID.getText()+"'";
                
                statement = con.createStatement();
                resultSet = statement.executeQuery(checkExist);
                        
                //IF THE CLINET IS ALREADY TAKEN        
                 if(resultSet.next()){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Client ID : "+ Clients_ClientID.getText()+ "Was Already Exist!");
                    alert.showAndWait();
                }else{
                
                    preparedStatement = con.prepareStatement(insertClient);
                    preparedStatement.setString(1, Clients_ClientID.getText());
                    preparedStatement.setString(2, Clients_Password.getText());
                    preparedStatement.setString(3, Clients_FName.getText());
                    preparedStatement.setString(4, Clients_LName.getText());
                    preparedStatement.setString(5,(String) Clients_Gender.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(6, String.valueOf(sqlDate));

                    preparedStatement.executeUpdate();
            
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Saved");
                    alert.showAndWait();
                    
                    // TO BECOME UPDATE THE TABLEVIEW
                    clientsShowListData();
                    // TO CLEAR THE FIELDS 
                    ClientReset();
                 }
            }
        }catch(Exception se){se.printStackTrace();}
    }
    
    private String[] genderList = {"Male", "Female"};
    public void ClientsGender(){
        List<String> listeGen = new ArrayList<>();
        
        for(String data: genderList){
            listeGen.add(data);
        }
        
        ObservableList ListeG = FXCollections.observableArrayList(listeGen);
        Clients_Gender.setItems(ListeG);
    
    }
    
    public void ClientsUpdate(){
        String updateClient = "UPDATE client SET password = '"
                +Clients_Password.getText()+"', firstname = '"
                +Clients_FName.getText()+"', lastname = '"
                +Clients_LName.getText()+"', gender = '"
                +Clients_Gender.getSelectionModel().getSelectedItem()
                +"' WHERE client_id = '" + Clients_ClientID.getText()+"'";
        
        con = DBConnection.connection();
        
        try{
            Alert alert ;
            
            if(Clients_ClientID.getText().isEmpty() 
                || Clients_Password.getText().isEmpty() 
                || Clients_FName.getText().isEmpty() 
                || Clients_LName.getText().isEmpty()
                || Clients_Gender.getSelectionModel().getSelectedItem() == null){
                
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();

            }else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are You Sure Do You Wnat To Update Client ID : "+ Clients_ClientID.getText()+" ?");
                
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(ButtonType.OK)){
                    statement = con.createStatement();
                    statement.executeUpdate(updateClient);
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Update!");
                    alert.showAndWait();
                    
                    // TO BECOME UPDATE THE TABLEVIEW
                    clientsShowListData();
                    // TO CLEAR THE FIELDS 
                    ClientReset();
                    
                }else{
                    return;
                }
            }
        }catch(Exception se){
            se.printStackTrace();
        }
    }
    
    
    // DELETE Products
   public void ClientsDelete(){
       String sqlDelete = "DELETE FROM client WHERE client_id = '"
               +Clients_ClientID.getText()+"'";
       
       con = DBConnection.connection();
       
      try{
            Alert alert ;
            
            // CHECK IF THE FIELDS ARE EMPTY
            if(Clients_ClientID.getText().isEmpty() 
                || Clients_Password.getText().isEmpty() 
                || Clients_FName.getText().isEmpty() 
                || Clients_LName.getText().isEmpty()
                || Clients_Gender.getSelectionModel().getSelectedItem() == null ){ 
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();

            }else{
                
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are You Sure Do You Want To Delete Client ID : "+ Clients_ClientID.getText()+"?");
              
                Optional<ButtonType> option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    preparedStatement = con.prepareStatement(sqlDelete);
                    preparedStatement.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                    
                    // TO BECOME UPDATE THE TABLEVIEW
                    clientsShowListData();
                    // TO CLEAR THE FIELDS 
                    ClientReset();
                    
                }else
                    return ;
            }
           
       }catch(Exception se){ se.printStackTrace();}
   } 
    
    public void ClientReset(){
        Clients_ClientID.setText("");
        Clients_Password.setText("");
        Clients_FName.setText("");
        Clients_LName.setText("");
        Clients_Gender.getSelectionModel().clearSelection();
    }
    
    
    public ObservableList<ClientData> ClientsListData(){
        
        
        ObservableList<ClientData> clnData = FXCollections.observableArrayList();
        String queryClient = "SELECT * FROM client";
        
        con = DBConnection.connection();
        
        try{
            
            ClientData clientDB;
            
            preparedStatement = con.prepareStatement(queryClient);
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
                clientDB = new ClientData(resultSet.getString("client_id")
                        , resultSet.getString("password")
                        , resultSet.getString("firstName")
                        , resultSet.getString("lastName")
                        , resultSet.getString("gender")
                        , resultSet.getDate("date"));

            clnData.add(clientDB);
        }
            
        }catch(Exception se){ se.printStackTrace();}
        return clnData;
    }
    
    
    private ObservableList<ClientData> clientList;
    public void clientsShowListData(){
        clientList = ClientsListData();
        
        Clients_Col_ClientID.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        Clients_Col_Password.setCellValueFactory(new PropertyValueFactory<>("password"));
        Clients_Col_FName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Clients_Col_LName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Clients_Col_Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        Clients_Col_Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        Clients_tableView.setItems(clientList);


    }
    
    
    public void ClientsSelect(){
        ClientData clientDB = Clients_tableView.getSelectionModel().getSelectedItem();
        int num = Clients_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1){
            return;
        }
        
        Clients_ClientID.setText(clientDB.getClient_id());
        Clients_Password.setText(clientDB.getPassword());
        Clients_FName.setText(clientDB.getFirstName());
        Clients_LName.setText(clientDB.getLastName());
        
        
    }
    
    
    public void logout(){
        try{
            Alert alert = new Alert(AlertType.CONFIRMATION);
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
    
    public void displayUsername(){
        username.setText(getData.username);
    }
    
    public void defaultNavBtn(){
        dashboard_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536);");
    }
    
    
    public void switchFrom(ActionEvent event){
       if(event.getSource() == dashboard_btn){
           dashboard_form.setVisible(true);
           addProducts_form.setVisible(false);
           clients_form.setVisible(false);
           
            dashboard_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536);");
           addProducts_btn.setStyle("-fx-background-color: transparent");
           clients_btn.setStyle("-fx-background-color: transparent");
           
           dashboardDisplayActiveClients();
           dashboardDisplayIncomeToday();
           dashboardTotalIncome();
           dashboardChart();
           
       } else if(event.getSource() == addProducts_btn){
           dashboard_form.setVisible(false);
           addProducts_form.setVisible(true);
           clients_form.setVisible(false);
           
           addProducts_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536);");
           dashboard_btn.setStyle("-fx-background-color: transparent");
           clients_btn.setStyle("-fx-background-color: transparent");
           
           addProductsShowData();
           addProductsStatusList();
           addProductsSearch();

           
       }else if(event.getSource() == clients_btn){
           dashboard_form.setVisible(false);
           addProducts_form.setVisible(false);
           clients_form.setVisible(true);
           
           clients_btn.setStyle("-fx-background-color: linear-gradient(to top right, #896b34, #b8a536);");
           addProducts_btn.setStyle("-fx-background-color: transparent");
           dashboard_btn.setStyle("-fx-background-color: transparent");
           
           clientsShowListData();
       }
    }
    
    
    public void close(){
        System.exit(0);
    }
    
    public void minimize(){
        Stage stage = (Stage)mainAdminDash_form.getScene().getWindow();
        stage.setIconified(true);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        displayUsername();
        defaultNavBtn();

        dashboardDisplayActiveClients();
        dashboardDisplayIncomeToday();
        dashboardTotalIncome();
        dashboardChart();
        
        
        //Product Function
        addProductsShowData();
        addProductsStatusList();
        
        // Client Function
        clientsShowListData();
        ClientsGender();
    }
    
}
