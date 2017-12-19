package mytunes.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.be.UserMedia;

// operation handler
public class MediaDBManager {

    private ConnectionManager cm = new ConnectionManager();

    public List<UserMedia> getAll() throws DAException {
        List<UserMedia> mediaList = new ArrayList();
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("SELECT * FROM Music");
            ResultSet result = pstatement.executeQuery();
            while (result.next()) {
                UserMedia tempMedia = new UserMedia();
                tempMedia.setId(result.getInt("id"));
                tempMedia.setTitle(result.getString("title"));
                tempMedia.setArtist(result.getString("artist"));
                tempMedia.setCategory(result.getString("category"));
                tempMedia.setTime(result.getInt("time"));
                tempMedia.setPath(result.getString("path"));
                tempMedia.createMediaFromPath();
                mediaList.add(tempMedia);
            }
        } catch (Exception e) {
            throw new DAException(e.getMessage());
        }
        return mediaList;
    }
    
    public void save(UserMedia media) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("INSERT INTO Music(title, artist, category, time, path)"
                    + "VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstatement.setString(1, media.getTitle());
            pstatement.setString(2, media.getArtist());
            pstatement.setString(3, media.getCategory());
            pstatement.setDouble(4, media.getTime());
            pstatement.setString(5, media.getPath());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Media could not be added!");
            }
            ResultSet rs = pstatement.getGeneratedKeys();
            if (rs.next()) {
                media.setId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }
    
    public void edit(UserMedia media) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("UPDATE Music SET title=?, artist=?, category=?, time=?, path=? WHERE id=?");
            pstatement.setString(1, media.getTitle());
            pstatement.setString(2, media.getArtist());
            pstatement.setString(3, media.getCategory());
            pstatement.setDouble(4, media.getTime());
            pstatement.setString(5, media.getPath());
            pstatement.setInt(6, media.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Media could not be edited!");
            }
        } catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }
    
    public void delete(UserMedia media) throws DAException {
        try (Connection con = cm.getConnection()) {
            PreparedStatement pstatement = con.prepareStatement("DELETE FROM Music WHERE id=?");
            pstatement.setInt(1, media.getId());
            int affected = pstatement.executeUpdate();
            if (affected < 1) {
                throw new DAException("Media could not be deleted!");
            }
        } catch (Exception e) {
            throw new DAException(e.getMessage());
        }
    }
}
