package mytunes.be;

import java.io.File;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;

//give the media files properties
//@author Dether
public class UserMedia {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty path = new SimpleStringProperty(); //The user-readable path of the media file
    private Media media;
    private double time;
    private String timeString;


    public UserMedia(int id, String title, String artist, String category, String path, Media media, double time) {
        this.id.set(id);
        this.title.set(title);
        this.artist.set(artist);
        this.category.set(category);
        this.path.set(path);
        this.media = media;
        this.time = time;
    }


    public UserMedia() {
    }


    public void createMediaFromPath() throws Exception {
        try {
            File f = new File(path.get());
            this.media = new Media(f.toURI().toString());
        }
        catch (Exception ex) {
            //If the save did not occure on the current machine, an error will occur, and the Media object will no be created
            //The data, hovewer, will not be displayed (but it will appear on the tableView)
            System.out.println(ex.getMessage());
        }
    }


    public String getTitle() {
        return title.get();
    }


    public void setTitle(String value) {
        title.set(value);
    }


    public String getPath() {
        return path.get();
    }


    public void setPath(String value) {
        path.set(value);
    }


    public String getCategory() {
        return category.get();
    }


    public void setCategory(String value) {
        category.set(value);
    }


    public String getArtist() {
        return artist.get();
    }


    public void setArtist(String value) {
        artist.set(value);
    }


    public int getId() {
        return id.get();
    }


    public void setId(int value) {
        id.set(value);
    }


    public void setTime(double time) {
        this.time = time;
    }


    public double getTime() {
        return time;
    }


    public Media getMedia() {
        return this.media;
    }


    public void setMedia(Media media) {
        this.media = media;
    }
    

    public String getTimeString() {
        long timeInLong = new Double(time).longValue();
        int day = (int) TimeUnit.SECONDS.toDays(timeInLong);
        long hours = TimeUnit.SECONDS.toHours(timeInLong) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(timeInLong) - (TimeUnit.SECONDS.toHours(timeInLong) * 60);
        long second = TimeUnit.SECONDS.toSeconds(timeInLong) - (TimeUnit.SECONDS.toMinutes(timeInLong) * 60);
        timeString = String.format("%02d:%02d:%02d", hours, minute, second);

        return timeString;
    }


    @Override
    public String toString() {
        return "Title: " + getTitle() + " Artist: " + getArtist() + " Category: " + getCategory() + " Time: " + getTime();
    }
}
