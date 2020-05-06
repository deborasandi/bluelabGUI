package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MainPanelCtrl {
    
    @FXML
    private HBox mainPanel;
    
    @FXML
    private ToggleButton btnClient;

    @FXML
    private ToggleGroup menu;

    @FXML
    private ToggleButton btnTables;

    @FXML
    private ToggleButton btnJobs;
    
    private AnchorPane clientPanel;
    
    private AnchorPane tablePanel;
    
    public void initialize() {
        try {
           clientPanel = FXMLLoader.load(getClass().getResource("../client/Client.fxml"));
           tablePanel = FXMLLoader.load(getClass().getResource("../jobprice/JobPrice.fxml"));
           
           HBox.setHgrow(clientPanel, Priority.ALWAYS);
           HBox.setHgrow(tablePanel, Priority.ALWAYS);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    @FXML
    void loadClientPanel() {
        if(mainPanel.getChildren().size() > 1)
            mainPanel.getChildren().remove(1);
        
        mainPanel.getChildren().add(clientPanel);
        
        if(!btnClient.isSelected()) 
            btnClient.setSelected(true);
        
    }

    @FXML
    void loadJobPanel() {
        if(mainPanel.getChildren().size() > 1)
            mainPanel.getChildren().remove(1);
        
        if(!btnJobs.isSelected()) 
            btnJobs.setSelected(true);
    }

    @FXML
    void loadTablesPanel() {
        if(mainPanel.getChildren().size() > 1)
            mainPanel.getChildren().remove(1);
        
        mainPanel.getChildren().add(tablePanel);
        
        if(!btnTables.isSelected()) 
            btnTables.setSelected(true);
    }
}
