package mytunes.dal;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import mytunes.be.UserMedia;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

// reads meta data
public class MetaReader {

    public UserMedia getMetaData(URI path) throws DAException {
        UserMedia tempmedia = new UserMedia();
        try {
            File file = new File(path);
            AudioFile audiofile = AudioFileIO.read(file);
            AudioHeader header = audiofile.getAudioHeader();
            if (path.toString().endsWith(".ogg")) {
                VorbisCommentTag tag = (VorbisCommentTag) audiofile.getTag();

                tempmedia.setTitle(tag.getFirst(FieldKey.TITLE));
                tempmedia.setArtist(tag.getFirst(FieldKey.ARTIST));
                tempmedia.setCategory(tag.getFirst(FieldKey.GENRE));
                tempmedia.setTime(header.getPreciseTrackLength());
            }
            if (path.toString().endsWith(".flac")) {
                FlacTag tag = (FlacTag) audiofile.getTag();

                tempmedia.setTitle(tag.getFirst(FieldKey.TITLE));
                tempmedia.setArtist(tag.getFirst(FieldKey.ARTIST));
                tempmedia.setCategory(tag.getFirst(FieldKey.GENRE));
                tempmedia.setTime(header.getPreciseTrackLength());
            } else {
                Tag tag = audiofile.getTag();
                
                tempmedia.setTitle(tag.getFirst(FieldKey.TITLE));
                tempmedia.setArtist(tag.getFirst(FieldKey.ARTIST));
                tempmedia.setCategory(tag.getFirst(FieldKey.GENRE));
                tempmedia.setTime(header.getPreciseTrackLength());
            }
        }

        catch (CannotReadException | IOException | org.jaudiotagger.tag.TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
            throw new DAException(ex.getMessage());
        }
        return tempmedia;
    }

}
