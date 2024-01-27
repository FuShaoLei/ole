package ole.core;

import com.sun.istack.internal.NotNull;
import ole.core.entity.FileNode;
import ole.core.exception.OleException;
import ole.core.instant.Instant;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /**
     * 将来会从cmd中读取
     * 现在知识写死
     */
    private static final String testFolderUrl = "F:\\code\\base";
    private static Map<String, Object> ymlData = new HashMap<>();
    private static List<FileNode> fileNodeList = new ArrayList<>();


    public static void main(String[] args) {
        System.out.println("Hello world!");

        String configUrl = testFolderUrl + "\\" + Instant.CONFIG_FILE; // TODO 这里的\\我感觉还要在考虑下，感觉兼容性不是很好

        try (InputStream inputStream = new FileInputStream(configUrl)) {
            Yaml yaml = new Yaml();
            Map<String, Object> dataTmp = yaml.load(inputStream);

            if (!dataTmp.containsKey(Instant.BASE_URL)) {
                throw new OleException("what the hell, no baseUrl !");
            }

            ymlData = dataTmp;

            System.err.println("----- baseUrl = " + ymlData.get(Instant.BASE_URL));
            handleGenerate();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handleGenerate() {
        List<FileNode> result = OrganizeRootData(testFolderUrl + Instant.CONTENT_URL);
        result.forEach(System.err::println);
    }

    private static List<FileNode> OrganizeRootData(String path) {
        List<FileNode> rootNodeList = new ArrayList<>();
        File inputFile = new File(path);

        File[] files = inputFile.listFiles();
        if (files == null) return null;

        for (File itemFile : files) {
            System.err.println("itemFile.getName() = " + itemFile.getName());

            if (itemFile.isDirectory()) {
                FileNode folder = new FileNode(FileNode.Type.DIRECTORY);
                folder.setName(itemFile.getName());
                File[] direFiles = itemFile.listFiles();
                if (direFiles != null) {
                    List<FileNode> childrenNodeList = new ArrayList<>();
                    for (File direFile : direFiles) {
                        childrenNodeList.add(getNode(direFile));
                    }
                    folder.setFileNodeList(childrenNodeList);
                }
                rootNodeList.add(folder);
            } else if (itemFile.isFile()) {
                rootNodeList.add(getArticleNode(itemFile));
            }
        }
        return rootNodeList;
    }

    private static FileNode getNode(File rootFile) {
        if (rootFile.isFile()) {
            return getArticleNode(rootFile);
        } else if (rootFile.isDirectory()) {
            FileNode folder = new FileNode(FileNode.Type.DIRECTORY);
            folder.setName(rootFile.getName());
            File[] direFiles = rootFile.listFiles();
            List<FileNode> childrenNodeList = new ArrayList<>();
            if (direFiles != null) {
                for (File itemFile : direFiles) {
                    childrenNodeList.add(getNode(itemFile));
                }
                folder.setFileNodeList(childrenNodeList);
            }

            return folder;
        }
        return null;
    }


    private static FileNode getArticleNode(@NotNull File itemFile) {
        FileNode article = new FileNode(FileNode.Type.FILE);
        article.setName(itemFile.getName());
        article.setPath(itemFile.getAbsolutePath());
        article.setUrl(ymlData.get(Instant.BASE_URL) + itemFile.getName().replace(".md", ".html"));
//                article.setContent("gugubird test " + itemFile.getName());
        return article;
    }


}