package UUID;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UuidClass {
    private String text;
    public UuidClass(String text){
        this.text = text;
    }
    public String changeTextToUUID(){
        String fileUUID = UUID.nameUUIDFromBytes(text.getBytes(StandardCharsets.UTF_8)).toString();
        return fileUUID;
    }
    public boolean compareUUIDtoText(String uuid){
        return uuid.equals(UUID.nameUUIDFromBytes(text.getBytes(StandardCharsets.UTF_8)).toString());
    }
}
