package gladiator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class MainFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
      @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane admin_form;

    @FXML
    private TextField admin_username;

    @FXML
    private PasswordField admin_password;

    @FXML
    private Button admin_loginBtn;

    @FXML
    private Hyperlink admin_hyperLink;

    @FXML
    private AnchorPane client_form;

    @FXML
    private TextField client_id;

    @FXML
    private PasswordField client_password;

    @FXML
    private Button client_LoginBtn;

    @FXML
    private Hyperlink client_hyperLink;
    
    private Connection con ;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement; 
    
    private double x = 0 ;
    private double y = 0 ;
    
    
    public void clientLogin(){
        String clientDB = "SELECT * FROM client WHERE client_id = ? and password = ? ";
        
        con = DBConnection.connection();
        
        try{
            Alert alert ;
            
            preparedStatement = con.prepareStatement(clientDB);
            if(client_id.getText().isEmpty() || client_password.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fil all blank field");
                alert.showAndWait();
                
            }else {
                preparedStatement.setString(1,client_id.getText());
                preparedStatement.setString(2, client_password.getText());
                
                resultSet = preparedStatement.executeQuery();
                
                if(resultSet.next()){
                    
                    getData.client_id = client_id.getText();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();

                    client_LoginBtn.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(getClass().getResource("clientDashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x );
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);

                    stage.setScene(scene);
                    stage.show();
                }else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong ClientID/PASSWORD");
                    alert.showAndWait();
                }
            }
        }catch(Exception se){
            se.printStackTrace();
        }
    }
    
    
    
    
    public void adminLogin() {
        String adminDB = "SELECT * FROM admin WHERE username = ? and password = ? ";
        con = DBConnection.connection();
        try{
            Alert alert ;
            preparedStatement = con.prepareStatement(adminDB);
            if(admin_username.getText().isEmpty() || admin_password.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERREEUR MESSAGE");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                
        
        preparedStatement.setString(1,admin_username.getText());
        preparedStatement.setString(2,admin_password.getText());
        
        resultSet = preparedStatement.executeQuery();
            
        if(resultSet.next()){
            
            getData.username = admin_username.getText();

            
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText("Successfully Login!");
            alert.showAndWait();
            admin_loginBtn.getScene().getWindow().hide();
            
            Parent root = FXMLLoader.load(getClass().getResource("adminDashboard.fxml"));
            
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            
            root.setOnMousePressed((MouseEvent event) -> {
             
            x = event.getSceneX();
            y = event.getSceneY();
            });
            
            root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x );
            stage.setY(event.getScreenY() - y );
            });
            
            stage.initStyle(StageStyle.TRANSPARENT);
            
            
            stage.setScene(scene);
            stage.show();
            
        }else{
            alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERREEUR MESSAGE");
                alert.setHeaderText(null);
                alert.setContentText("Wrong Username/Password");
                alert.showAndWait();
        }
            }
       
    }catch(Exception se){
        se.printStackTrace();
    }
    }
    
    
    public void switchFrom(ActionEvent event){
        if(event.getSource() == admin_hyperLink){
            admin_form.setVisible(false);
            client_form.setVisible(true);
        }else if(event.getSource() == client_hyperLink){
            admin_form.setVisible(true);
            client_form.setVisible(false);
        }
    }
    
    public void close(){
        System.exit(0);
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

       
    }    

    
}
