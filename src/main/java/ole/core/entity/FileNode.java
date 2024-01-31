package ole.core.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件或者文件夹
 */
public class FileNode implements Serializable {

    public static final int TYPE_FILE = 1;
    public static final int TYPE_DIRECTORY = 2;

     public enum Type {
        FILE,
        DIRECTORY
    }

    /**
     * 1=文件
     * 2=文件夹
     */
    private Type type;

    private String name;

    //--------------文件专属属性-------------
    /**
     * 文件的网页url
     */
    private String url;
    /**
     * 文件的本地绝对路径
     */
    private String localPath;

    private String outputLocalPath;

    /**
     * 文件的内容
     */
    private String content;

    /**
     * 层级
     */
    private int level;

    /**
     * 文件夹的里的东西
     */
    private List<FileNode> fileNodeList = new ArrayList<>();

    public FileNode(Type type) {
        this.type = type;
    }

    public boolean isArticle() {
        return type == Type.FILE;
    }

    public boolean isFolder() {
        return type == Type.DIRECTORY;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOutputLocalPath() {
        return outputLocalPath;
    }

    public void setOutputLocalPath(String outputLocalPath) {
        this.outputLocalPath = outputLocalPath;
    }

    public List<FileNode> getFileNodeList() {
        return fileNodeList;
    }

    public void setFileNodeList(List<FileNode> fileNodeList) {
        this.fileNodeList = fileNodeList;
    }

    @Override
    public String toString() {
        return "FileNode{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", localPath='" + localPath + '\'' +
                ", outputLocalPath='" + outputLocalPath + '\'' +
                ", content='" + content + '\'' +
                ", level=" + level +
                ", fileNodeList=" + fileNodeList +
                '}';
    }
}
