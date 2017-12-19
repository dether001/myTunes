package mytunes.be;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

//manages playlists
//@author Dether
public class PlayList {

    private ObservableList<UserMedia> mediaList = FXCollections.observableArrayList();  //The collection of songs
    
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final IntegerProperty count = new SimpleIntegerProperty();
    
    private double totalTimeInSeconds;
    private final StringProperty timeFormattedAsString = new SimpleStringProperty();
    private int currentlyPlayingIndex;
    

    // makes a new playlist
    public PlayList()
    {
        totalTimeInSeconds = 0;
        currentlyPlayingIndex = 0;
        
        mediaList.addListener(new ListChangeListener<UserMedia>()
        {
            @Override
            public void onChanged(ListChangeListener.Change<? extends UserMedia> c)
            {
                count.set(mediaList.size());
            }
        });
    }

    
    public PlayList(int id, String title) {
        this.id.set(id);
        this.title.set(title);
    }


    public StringProperty timeFormattedAsStringProperty()
    {
        return timeFormattedAsString;
    }
    

    private void updateStringTime()
    {
        long timeInLong = new Double(totalTimeInSeconds).longValue();
        
        int day = (int)TimeUnit.SECONDS.toDays(timeInLong);        
        long hours = TimeUnit.SECONDS.toHours(timeInLong) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(timeInLong) - (TimeUnit.SECONDS.toHours(timeInLong)* 60);
        long second = TimeUnit.SECONDS.toSeconds(timeInLong) - (TimeUnit.SECONDS.toMinutes(timeInLong) *60);

        timeFormattedAsString.setValue(String.format("%02d:%02d:%02d", hours, minute, second));
    }
    
    // gets the number of songs in the playlist
    public int getCount()
    {
        return count.get();
    }


    public IntegerProperty countProperty()
    {
        return count;
    }

    // gets the title of a given song
    public String getTitle() {
        return title.get();
    }

    // sets the title of selected playlist
    public void setTitle(String value) {
        title.set(value);
    }

    public int getId() {
        return id.get();
    }

    
    public void setId(int value) {
        id.set(value);
    }

    
    public ObservableList<UserMedia> getMediaList() {
        return mediaList;
    }



    public void addMedia(UserMedia selectedMedia) 
    {
        mediaList.add(selectedMedia);
        totalTimeInSeconds += selectedMedia.getTime();
        updateStringTime();
    }


    public void removeMedia(UserMedia mediaToDelete) {
        Iterator<UserMedia> i = mediaList.iterator();
        
        while(i.hasNext())  //Iterate through the media list
        {
            if (i.next().getId() == mediaToDelete.getId()) //If the media is in the list remove it
            {
                i.remove();
                break;
            }
        }
        
        totalTimeInSeconds -= mediaToDelete.getTime();
        updateStringTime();
    }


    public boolean containsMedia(UserMedia media) {
        for (UserMedia userMedia : mediaList)
        {
            if (userMedia.getId() == media.getId())
            {
                return true;
            }
        }
        return false;
    }


    public boolean isEmpty() {
        return mediaList.isEmpty();
    }


    public void clearMediaList() {
        mediaList.clear();
    }


    public int getIndexOfMedia(UserMedia selected) {
        int i = -1;
        for (UserMedia media : mediaList) {
            i++;
            if (selected.equals(media)) {
                return i;
            }
        }
        return -1;
    }


    public void moveSongUp(int index) 
    {
        UserMedia switchSong = mediaList.get(index - 1);
        mediaList.set(index - 1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }


    public void moveSongDown(int index) 
    {
        UserMedia switchSong = mediaList.get(index + 1);
        mediaList.set(index + 1, mediaList.get(index));
        mediaList.set(index, switchSong);
    }


    public UserMedia getCurrentlyPlaying()
    {
        return mediaList.get(currentlyPlayingIndex);
    }


    public void setNextIndex()
    {
        if (currentlyPlayingIndex < mediaList.size() - 1)
        {
            this.currentlyPlayingIndex++;
        }
        else
        {
            currentlyPlayingIndex = 0;
        }
    }


    public void setPreviousIndex()
    {
        if (currentlyPlayingIndex > 0)
        {
            this.currentlyPlayingIndex--;
        }
        else
        {
            currentlyPlayingIndex = mediaList.size() - 1;
        }
    }
    

    @Override
    public String toString() 
    {
        return "PlayList{ id=" + id + ", title=" + title + '}';
    }
}
