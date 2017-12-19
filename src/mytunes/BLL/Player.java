package mytunes.BLL;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;

//manages the Player
// @author Dether
class Player {
    private MediaPlayer player;
    private PlayList list;
    private final BooleanProperty isPlaying = new SimpleBooleanProperty();
    private final StringProperty currentlyPlayingString = new SimpleStringProperty();
    private UserMedia currentMedia;
    
    public Player(Media media) {
        player = new MediaPlayer(media);
    }
    
    public Player() {
    }
    
    
    public StringProperty currentlyPlayingStringProperty()
    {
        return currentlyPlayingString;
    }
    
    public BooleanProperty isPlayingProperty()
    {
        return isPlaying;
    }

    public void setVolume(double value) {
        if (player != null) {
            player.setVolume(value);
        }
    }

    // plays selected song
    public void play() throws BLLException {
        try {
            player.play();
            isPlaying.set(true);
            setPlayingString(currentMedia);
        }
        catch (Exception ex) {
            throw new BLLException(ex);
        }
    }


    // pauses the curretnly playing song
    public void pause() {
        player.pause();
        currentlyPlayingString.set("PAUSED");
        isPlaying.set(false);
    }

    
    public void setMedia(UserMedia media) throws BLLException {
        try {
            player = new MediaPlayer(media.getMedia());
            currentMedia = media;
        }
        catch (NullPointerException ex) {
            throw new BLLException("You are trying to play a not existing media! Maybe the path of this song is not located on this computer?");
        }
        
        player.setOnEndOfMedia(() ->    //Update the properties, once the song has finished playing
        {
            isPlaying.set(false);
            currentlyPlayingString.set("");
            player.stop();
        });
    }

    
    public void setMedia(PlayList selectedPlayList) throws BLLException
    {
        list = selectedPlayList;
        player = new MediaPlayer(list.getCurrentlyPlaying().getMedia());
        currentMedia = list.getCurrentlyPlaying();
        
        player.setOnEndOfMedia(() ->        //After one song has ended, play the next one
        {
            try
            {
                this.playNextSong();
            } catch (BLLException ex)
            {
                System.out.println(ex.getMessage());
            }
        });
    }

    
    public void playNextSong() throws BLLException
    {
        if (list == null)
        {
            throw new BLLException("No play list");
        }
        
        player.stop();
        list.setNextIndex();
        setMedia(list.getCurrentlyPlaying());
        setPlayingString(list.getCurrentlyPlaying());
        player.play();
        
    }


    public void playPreviousSong() throws BLLException
    {
        if (list == null)
        {
            throw new BLLException("No play list");
        }
        
        player.stop();
        list.setPreviousIndex();
        setMedia(list.getCurrentlyPlaying());
        setPlayingString(list.getCurrentlyPlaying());
        player.play();
    }
    

    private void setPlayingString(UserMedia media)
    {
        currentlyPlayingString.setValue("Currently playing: " +  media.getArtist() + ": " + media.getTitle() );
    } 


    public void setNextSong() throws BLLException
    {
        if (list == null)
        {
            throw new BLLException("No play list");
        }
        
        list.setNextIndex();;
        setMedia(list.getCurrentlyPlaying());
    }
    

    public void setPreviousSong() throws BLLException
    {
        if (list == null)
        {
            throw new BLLException("No play list");
        }
        
        list.setPreviousIndex();
        setMedia(list.getCurrentlyPlaying());
    }
}
