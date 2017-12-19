package mytunes.gui.Model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import mytunes.BLL.BLLException;
import mytunes.BLL.BLLManager;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.gui.Controller.Mode;

/**
 * @author atilka19
 */
public class MediaPlayerModel {

    private ObservableList<UserMedia> allMedia = FXCollections.observableArrayList();   //Contains all the songs
    private ObservableList<UserMedia> filteredList = FXCollections.observableArrayList();   //Contains the songs that match the current filter (if there is one)
    private ObservableList<PlayList> playlists = FXCollections.observableArrayList();   //Contains the play lists
    private ObservableList<String> categories = FXCollections.observableArrayList();    //Contains the categories

    private final BLLManager bllManager = new BLLManager();
    private static MediaPlayerModel instance;

    private Mode mediaMode;
    private Mode playListMode;

    public MediaPlayerModel()
    {
        allMedia.addListener(new ListChangeListener<UserMedia>()
        {
            @Override
            public void onChanged(ListChangeListener.Change<? extends UserMedia> c)
            {
                filteredList.clear();
                filteredList.addAll(allMedia);
            }
        });
    }

    public static MediaPlayerModel getInstance() {
        if (instance == null) {
            instance = new MediaPlayerModel();
        }

        return instance;
    }

    public void loadDataFromDB() throws ModelException {
        try {
            allMedia.addAll(bllManager.loadMedia());    //Load the songs
            playlists.addAll(bllManager.loadPlayLists());   //Load the play lists
            categories.addAll(bllManager.getCategories());  //Load the categories
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void addNewCategory(String category) throws ModelException {
        category = category.trim(); //Remove tailing and leading whitespaces

        if (category.isEmpty()) //Do not accept an empty string
        {
            throw new ModelException("Nothing to add");
        }

        if (categories.contains(category)) //Do not allow duplicate entries
        {
            throw new ModelException("Category is already in the list!");
        }

        categories.add(category);
    }

    public void createNewPlayList(String title) throws ModelException {
        if (title.isEmpty()) //Do not create a playlist with an empty titly
        {
            throw new ModelException("Empty title!");
        }

        PlayList newPlayList = new PlayList();
        newPlayList.setTitle(title);

        try {
            bllManager.saveNewPlayList(newPlayList);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        playlists.add(newPlayList);
    }

    public void addNewMedia(UserMedia selectedSong) throws ModelException {
        try {
            bllManager.addNewMedia(selectedSong);
            allMedia.add(selectedSong);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws ModelException {
        if (selectedMedia == null || selectedPlayList == null) {
            throw new ModelException("Please select a song or a play list!");
        }
        
        for (UserMedia userMedia : selectedPlayList.getMediaList()) {
            if (userMedia.getArtist().equalsIgnoreCase(selectedMedia.getArtist()) && userMedia.getTitle().equalsIgnoreCase(selectedMedia.getTitle())) {
                throw new ModelException("Media is already in the list!");
            }
        }

        try {
            bllManager.addMediaToPlayList(selectedMedia, selectedPlayList);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        selectedPlayList.addMedia(selectedMedia);
    }

    public void updatePlayList(PlayList selectedPlayList) throws ModelException {
        try {
            bllManager.updatePlayList(selectedPlayList);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void updateMedia(UserMedia editMedia) throws ModelException {
        try {
            bllManager.updateMedia(editMedia);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
                    
        for (PlayList list : playlists)
        {
            if (list.containsMedia(editMedia))
            {
                list.removeMedia(editMedia);
                list.addMedia(editMedia);
            }
        }
    }

    public void removeMedia(UserMedia selected) throws ModelException {
        for (PlayList list : playlists) {
            if (list.containsMedia(selected)) {
                removeMediaFromPlayList(selected, list);
            }
        }

        try {
            bllManager.deleteMedia(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        allMedia.remove(selected);
    }

    public void removeMediaFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws ModelException {
        if (!selectedPlayList.containsMedia(selectedMedia)) //ilyen nem lesz
        {
            throw new ModelException("This playlist does not contain the selected media!");
        }

        try {
            bllManager.removeMediaFromPlayList(selectedMedia, selectedPlayList);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        selectedPlayList.removeMedia(selectedMedia);
    }

    public void removePlayList(PlayList selected) throws ModelException {
        if (selected != null) {
            List<UserMedia> inList = new ArrayList<>();

            inList.addAll(selected.getMediaList());

            for (UserMedia userMedia : inList)
            {
                removeMediaFromPlayList(userMedia, selected);
            }
        }

        try {
            bllManager.deletePlayList(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }

        playlists.remove(selected);
    }

    public ObservableList<UserMedia> getMedia() {
        return this.filteredList;
    }

    public ObservableList<PlayList> getPlayLists() {
        return this.playlists;
    }

    public ObservableList<String> getCategories() {
        return this.categories;
    }

    public Mode getMediaMode() {
        return mediaMode;
    }

    public Mode getPlayListMode() {
        return playListMode;
    }

    public void setMediaMode(Mode mediaMode) {
        this.mediaMode = mediaMode;
    }

    public void setPlayListMode(Mode playListMode) {
        this.playListMode = playListMode;
    }

    public void setSelectedPlayList(PlayList selected) throws ModelException {
        try {
            bllManager.setSelectedPLayList(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void setSelectedMedia(UserMedia selected) throws ModelException {
        try {
            bllManager.setSelectedPMedia(selected);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public PlayList getSelectedPlayList() throws ModelException {
        try {
            return bllManager.getSelectedPlayList();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public UserMedia getSelectedMedia() throws ModelException {
        try {
            return bllManager.getSelectedMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public BooleanProperty isPlaying()
    {
        return bllManager.isPlaying();
    }

    public StringProperty getCurrentlyPlayingString()
    {
        return bllManager.getPlayingString();
    }

    public void searchString(String search) {
        filteredList.clear();

        if (search.isEmpty())
        {
            filteredList.addAll(allMedia);
            return;
        }

        search = search.toLowerCase();

        for (UserMedia userMedia : allMedia) {
            if (userMedia.getArtist().toLowerCase().contains(search) || userMedia.getTitle().toLowerCase().contains(search)) //If the artis's name or the title of the song contains the string, treat it as a match
            {
                filteredList.add(userMedia);
            }
        }
    }

    public UserMedia getMetaData(URI path) throws ModelException {
        try {
            return bllManager.getMetaData(path);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void moveSongUp(UserMedia selected, PlayList list) throws ModelException {
        if (selected == null) {
            throw new ModelException("No song selected!");
        }

        if (list == null) {
            throw new ModelException("No play list selected!");
        }

        int index = list.getIndexOfMedia(selected);

        if (index == 0) {
            throw new ModelException("Media is alredy on the top of the list!");
        }

        list.moveSongUp(index);
    }

    public void moveSongDown(UserMedia selected, PlayList list) throws ModelException {
        if (selected == null) {
            throw new ModelException("No song selected!");
        }

        if (list == null) {
            throw new ModelException("No play list selected!");
        }

        int index = list.getIndexOfMedia(selected);

        if (index == list.getCount() - 1) {
            throw new ModelException("Media is alredy on the bottom of the list!");
        }

        list.moveSongDown(index);
    }

    public void playMedia() throws ModelException {
        try {
             bllManager.playMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void setMedia(UserMedia media) throws ModelException {
        try {
            bllManager.setMedia(media);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void setMedia(PlayList selectedPlayList) throws ModelException
    {
        try 
        {
            bllManager.setMedia(selectedPlayList);
        }
        catch (BLLException ex)
        {
            throw new ModelException(ex);
        }
    }

    public void pauseMedia() {
        bllManager.pauseMedia();
    }

    public void setVolume(double vol) {
        bllManager.setVolume(vol);
    }

    public void setNextMedia() throws ModelException {
        try 
        {
            bllManager.nextMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public void setPreviousMedia() throws ModelException {
        try {
            bllManager.previousMedia();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }
}
