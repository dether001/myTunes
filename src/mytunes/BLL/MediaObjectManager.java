package mytunes.BLL;

import java.util.ArrayList;
import java.util.List;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DALManager;

// operation handler
// @author Dether
public class MediaObjectManager {

    private List<String> categories;
    private DALManager dalManager;

    // creates "MediaObjectManager" instance
    public MediaObjectManager(DALManager dm) {
        this.dalManager = dm;
    }

    
    // loads the media from the database
    List<UserMedia> getMedia() throws BLLException {
        try {
            List<UserMedia> uMediaList = dalManager.getAllMedia();
            categories = new ArrayList<>();
            for (UserMedia userMedia : uMediaList) //Filter out the categories
            {
                if (!categories.contains(userMedia.getCategory())) {
                    categories.add(userMedia.getCategory());
                }
            }
            return uMediaList;
        } catch (DAException ex) {
            throw new BLLException(ex);
        }
    }


    
    // gets the categories
    public List<String> getCategories() throws BLLException {
        if (categories == null) {
            throw new BLLException("No data has been red in!");
        }
        return this.categories;
    }


    //saves the data from the media to the database
    void addNew(UserMedia selectedSong) throws BLLException {
        try {
            dalManager.saveMedia(selectedSong);
        } catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    // removes selected data from the database
    void remove(UserMedia selected) throws BLLException {
        try {
            dalManager.deleteMedia(selected);
        } catch (DAException ex) {
            throw new BLLException(ex);
        }
    }


    // updates an existing media object
    void updateMedia(UserMedia selectedMedia) throws BLLException {
        try {
            dalManager.editMedia(selectedMedia);
        } catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

}
