package file;

public class FileDto {
    private String path;
    private String location;
    private boolean isDirectory;

    public FileDto(String path, String location, boolean isDirectory) {
        this.path = path;
        this.location = location;
        this.isDirectory = isDirectory;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
