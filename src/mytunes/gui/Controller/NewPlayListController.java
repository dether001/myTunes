package mytunes.gui.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mytunes.be.PlayList;
import mytunes.gui.Model.MediaPlayerModel;
import mytunes.gui.Model.ModelException;


//controller for "NewPlayListW windows
public class NewPlayListController implements Initializable
{

    @FXML
    private TextField txtFieldName;

    private PlayList list;
    private MediaPlayerModel model;
    
    private Mode mode; //Depends on whether the "New" or the "Edit" button was clicked
   
    
    // initializes controller class
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        txtFieldName.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (event.getCode().equals(KeyCode.ENTER))
                {
                    saveData();
                }
            }
        });
        
        model = MediaPlayerModel.getInstance();     //Get the mode in which the window was opened
        
        try
        {
            if (model.getPlayListMode() == Mode.EDIT)
            {
                mode = Mode.EDIT;
                list = model.getSelectedPlayList();
                setText(list);
            }
            else
            {
                mode = Mode.NEW;
                list = new PlayList();
            }
        }
        catch(ModelException ex)
        {
            Logger.getLogger(NewPlayListController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        } 
        catch (Exception ex)
        {
            Logger.getLogger(NewPlayListController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }    

    // tries to save data
    @FXML
    private void btnSaveClick(ActionEvent event)
    {
        saveData();
    }

    
    // closes the window
    @FXML
    private void btnCancelClick(ActionEvent event)
    {
        closeWindow();
    }
    
    private void saveData()
    {
        try
        {
            String playListName = txtFieldName.getText();
            if (mode == Mode.NEW)   //If the selectedPlayList is null, we are creating a new play list
            {
                model.createNewPlayList(playListName);
            }
            else
            {
                list.setTitle(playListName);
                model.updatePlayList(list);
            }
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(NewPlayListController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
       
        closeWindow();
    }
    
    private void closeWindow()
    {
        Stage stage = (Stage) txtFieldName.getScene().getWindow();
        stage.close();
    }

    public void setText(PlayList list) throws Exception
    {
        try 
        {
            txtFieldName.setText(list.getTitle());
        }
        catch (NullPointerException ex)
        {
            throw new Exception("No list selected!");
        }
    }
    
    private void showAlert(Exception ex)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }
}
