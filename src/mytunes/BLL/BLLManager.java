package mytunes.BLL;

import java.net.URI;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import mytunes.be.PlayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DALManager;

// @author Dether
public class BLLManager {

    private PlayList selectedPlayList;
    private UserMedia selectedMedia;
    private DALManager dalManger = new DALManager();
    private MediaObjectManager mediaObjectManager = new MediaObjectManager(dalManger); 
    private PlayListManager playListManager = new PlayListManager(dalManger);
    private Player player = new Player();

    // loads the information from the database
    public List<UserMedia> loadMedia() throws BLLException {
        return mediaObjectManager.getMedia();
    }

    // loads the playlists from the database
    public List<PlayList> loadPlayLists() throws BLLException {
        return playListManager.loadPlayLists();
    }

    // loads the categories from the database
    public List<String> getCategories() throws BLLException {
        return mediaObjectManager.getCategories();
    }

    // adds a new song to the database
    public void addNewMedia(UserMedia newMedia) throws BLLException {
        try {
            mediaObjectManager.addNew(newMedia);
        }
        catch (BLLException ex) {
            Logger.getLogger(BLLManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new BLLException(ex);
        }
    }

    // stores a playlist to the database
    public void saveNewPlayList(PlayList newPlayList) throws BLLException {
        if (newPlayList == null) {
            throw new BLLException("Play list does not exists!");
        }
        playListManager.saveNewPlayList(newPlayList);
    }

    // stores a song to the database
    public void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No media selected");
        }
        if (selectedPlayList == null) {
            throw new BLLException("No play list selected!");
        }
        playListManager.addMediaToPlayList(selectedMedia, selectedPlayList);
    }

    
    
    public void updateMedia(UserMedia selectedMedia) throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No media selected!");
        }
        mediaObjectManager.updateMedia(selectedMedia);
    }


    
    public void updatePlayList(PlayList selectedPlayList) throws BLLException {
        if (selectedPlayList == null) {
            throw new BLLException("No playlist selected");
        }
        playListManager.updatePlayList(selectedPlayList);
    }


    
    
    
    public void deleteMedia(UserMedia selectedMedia) throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No song selected song!");
        }
        mediaObjectManager.remove(selectedMedia);
    }


    
    
    public void removeMediaFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        if (selectedPlayList == null) {
            throw new BLLException("No play list selected!");
        }
        playListManager.removeSongFromPlayList(selectedMedia, selectedPlayList);
    }


    
    
    public void deletePlayList(PlayList selected) throws BLLException {
        if (selected == null) {
            throw new BLLException("No playlist selected!");
        }
        if (!selected.isEmpty()) {  
            for (UserMedia media : selected.getMediaList()) {   //Remove the song found in the play list firs
                removeMediaFromPlayList(media, selected);
            }
            selected.clearMediaList();
        }
        playListManager.removePlayList(selected);
    }


    
    
    public void setSelectedPLayList(PlayList selected) throws BLLException {
        if (selected == null) {
            throw new BLLException("No playlist selected!");
        }
        this.selectedPlayList = selected;
    }


    
    
    public void setSelectedPMedia(UserMedia selected) throws BLLException {
        if (selected == null) {
            throw new BLLException("No song  selected!");
        }
        this.selectedMedia = selected;
    }


    
    
    public PlayList getSelectedPlayList() throws BLLException {
        if (selectedPlayList == null) {
            throw new BLLException("No playlist selected!");
        }
        return selectedPlayList;
    }


    
    
    public UserMedia getSelectedMedia() throws BLLException {
        if (selectedMedia == null) {
            throw new BLLException("No media selected!");
        }
        return this.selectedMedia;
    }


    
    
    public BooleanProperty isPlaying()
    {
        return player.isPlayingProperty();
    }
    

    
    
    public StringProperty getPlayingString()
    {
        return player.currentlyPlayingStringProperty();
    }

    
    
    
    public void playMedia() throws BLLException {
         player.play();
    }

    
    
    public void setMedia(UserMedia media) throws BLLException {
        if (media == null)
        {
            throw new BLLException("No media selected");
        }
        
        player.setMedia(media);
    }


    
    public void setMedia(PlayList selectedPlayList) throws BLLException
    {
        if (selectedPlayList == null)
        {
            throw new BLLException("No playlist selected!");
        }
        
        player.setMedia(selectedPlayList);
    }

    
    
    public void setVolume(double vol) {
        player.setVolume(vol);
    }


    
    public void pauseMedia() {
        player.pause();
    }


    
    
    public UserMedia getMetaData(URI path) throws BLLException {
        try {
            return dalManger.getMetaData(path);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }


    
    
    public void nextMedia() throws BLLException 
    {
        if (player.isPlayingProperty().get())
        {
             player.playNextSong(); //Play the next song in the list
        }
        else
        {
            player.setNextSong(); //Switch to the next song, but don't play it
        }
    }


    
    
    public void previousMedia() throws BLLException {
        if (player.isPlayingProperty().get())
        {
             player.playPreviousSong(); //Play the previous song
        }
        else
        {
            player.setPreviousSong(); //Switch to the previous song, but don't play it
        }
    }
}
