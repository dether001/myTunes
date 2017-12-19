package mytunes.gui.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mytunes.be.UserMedia;
import mytunes.gui.Model.MediaPlayerModel;
import mytunes.gui.Model.ModelException;


// controller for "NewSong" window
public class NewSongController implements Initializable {

    @FXML
    private TextField titleOfSongField;
    @FXML
    private TextField songArtistField;
    @FXML
    private ComboBox<String> chooseCategoryComboBox;
    @FXML
    private TextField songTimeField;
    @FXML
    private TextField songPathField;
    @FXML
    private Button cancelNewSongButton;
    
    private MediaPlayerModel model;
    private UserMedia workingUserMedia;
    
    private Mode mode; //Checks wich mode was selected
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            model = MediaPlayerModel.getInstance();            
            chooseCategoryComboBox.setItems(model.getCategories());
            if (model.getMediaMode() == mytunes.gui.Controller.Mode.EDIT)
            {
                mode = Mode.EDIT;
                workingUserMedia = model.getSelectedMedia();
                fillData();
            }
            else
            {
                mode = Mode.NEW;
                workingUserMedia = new UserMedia();
            }
        } catch (ModelException ex)
        {
            Logger.getLogger(NewSongController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    
    @FXML
    private void chooseFileClicked(ActionEvent event)
    {
        FileChooser fc = new FileChooser();
        URI uri = fc.showOpenDialog(new ContextMenu()).toURI();
        
        workingUserMedia = getMetaData(uri);    //Get the meta data of the selected file
        Media media = new Media(uri.toString()); //Create a Media object from the selected file
        workingUserMedia.setMedia(media);   
        fillData(); //Fill out the text fields using the data previously retrieved
    }


    // opens a window, with the purpose of creating new categories
    @FXML
    private void addNewCategoryClicked(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/View/CategoryAdd.fxml"));
            
            Parent root1 = (Parent) fxmlLoader.load();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
    
    
    @FXML
    private void saveSongClicked(ActionEvent event)
    {
        saveDataFromTextFields();
        closeWindow();
    }
    
    @FXML
    private void onKeyPressed(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            saveDataFromTextFields();
            closeWindow();
        }
    }

    
    

    private UserMedia getMetaData(URI uri)
    {
        UserMedia m = new UserMedia();
        try
        {
            m = model.getMetaData(uri);
        }
        catch(ModelException ex)
        {
            showAlert(ex);
        }
        m.setPath(uri.getPath());
        return m;
    }
    
    @FXML
    private void cancelNewSongClicked(ActionEvent event) 
    {
        closeWindow();
    }
    
    private void showAlert(Exception ex)
    {
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.show();
    }

    private void closeWindow()
    {
        Stage stage = (Stage) cancelNewSongButton.getScene().getWindow();
        stage.close();
    }
    
    private void fillData()
    {
        songArtistField.setText(workingUserMedia.getArtist().isEmpty() ? "Unknown" : workingUserMedia.getArtist());
        titleOfSongField.setText(workingUserMedia.getTitle().isEmpty() ? "Unknown" : workingUserMedia.getTitle());
        songTimeField.setText(workingUserMedia.getTimeString());
        songPathField.setText(workingUserMedia.getPath());
        
        chooseCategoryComboBox.setValue(workingUserMedia.getCategory().isEmpty() ? "Unkown" : workingUserMedia.getCategory());
    }
    
    private void saveDataFromTextFields()
    {
        try
        {
            String artist = songArtistField.getText();
            String title = titleOfSongField.getText();
            String path = songPathField.getText();
            String category = chooseCategoryComboBox.getValue();
            
            workingUserMedia.setArtist(artist);
            workingUserMedia.setTitle(title);
            workingUserMedia.setCategory(category);
            workingUserMedia.setPath(path);
            
            if (mode == Mode.NEW)
            {
                model.addNewMedia(workingUserMedia);
            }
            else
            {
                model.updateMedia(workingUserMedia);
            }
        } 
        catch (ModelException ex)
        {
            Logger.getLogger(NewSongController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(ex);
        }
    }
}
