package mytunes.BLL;

import java.util.List;
import mytunes.be.PlayList;
import mytunes.be.UserMedia;
import mytunes.dal.DAException;
import mytunes.dal.DALManager;

//operation handler
//author @atilka19
public class PlayListManager {

    private DALManager dalManager;

    public PlayListManager(DALManager dm) {
        this.dalManager = dm;
    }

    List<PlayList> loadPlayLists() throws BLLException {
        try {
            return dalManager.getAllPlayList();
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    void saveNewPlayList(PlayList newPlayList) throws BLLException {
        try {
            dalManager.savePlayList(newPlayList);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }
    
    void updatePlayList(PlayList selectedPlayList) throws BLLException {
        try {
            dalManager.editList(selectedPlayList);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    void removePlayList(PlayList selected) throws BLLException {
        try {
            dalManager.deletePlayList(selected);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    void addMediaToPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        try {
            dalManager.addMediaToList(selectedPlayList, selectedMedia);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }

    void removeSongFromPlayList(UserMedia selectedMedia, PlayList selectedPlayList) throws BLLException {
        try {
            dalManager.deleteMediaFromList(selectedPlayList, selectedMedia);
        }
        catch (DAException ex) {
            throw new BLLException(ex);
        }
    }
}
