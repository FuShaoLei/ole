package ole.core;

import com.sun.istack.internal.NotNull;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import ole.core.entity.FileNode;
import ole.core.exception.OleException;
import ole.core.instant.Instant;
import ole.core.utils.FolderUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class Main {

    /**
     * 将来会从cmd中读取
     * 现在知识写死
     */
    private static final String testFolderUrl = "F:\\code\\base";
    private static Map<String, Object> ymlData = new HashMap<>();

    private static List<FileNode> allFileNodeList = new ArrayList<>();

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

            handleGenerate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handleGenerate() {
        List<FileNode> nodeList = OrganizeRootData(testFolderUrl + Instant.CONTENT_URL);

        allFileNodeList = nodeList;

//        nodeList.forEach(System.err::println);

        // 清空publish目录
        FolderUtils.deleteFolder(testFolderUrl + Instant.PUBLISH_URL);
        File newFile = new File(testFolderUrl + Instant.PUBLISH_URL);
        newFile.mkdir();

        // 初始化freemarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(Main.class, "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.CHINESE);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

        try {
            Template template = cfg.getTemplate("template.ftl");
            handleMD2Html(template, renderer, parser, nodeList);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void handleMD2Html(Template template,
                                      HtmlRenderer renderer,
                                      Parser parser, List<FileNode> nodeList) {
        for (FileNode fileNode : nodeList) {
            if (fileNode.isArticle()) {
                try {
                    Map<String, Object> input = new HashMap<>();

                    input.put("nodeList", allFileNodeList);

                    File inputFile = new File(fileNode.getLocalPath());
                    File outputFile = new File(getLocalOutputPath(fileNode.getName()));

                    Node document = parser.parseReader(new FileReader(inputFile));

                    input.put("title", inputFile.getName());
                    input.put("article", renderer.render(document));

                    PrintWriter writer = new PrintWriter(outputFile);
                    template.process(input, writer);
                } catch (Exception e) {

                }

            } else if (fileNode.isFolder()) {
                List<FileNode> folderNodeList = fileNode.getFileNodeList();
                if (folderNodeList != null) {
                    handleMD2Html(template, renderer, parser, folderNodeList);
                }
            }
        }
    }


    /**
     * 组织数据
     */
    private static List<FileNode> OrganizeRootData(String path) {
        List<FileNode> rootNodeList = new ArrayList<>();
        File inputFile = new File(path);

        int rootLevel = 0;

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

                if (itemFile.getName().equals(Instant.ROOT_INDEX_FILE)) {
                    rootNodeList.add(getArticleNode(itemFile, "index.html"));
                } else {
                    rootNodeList.add(getArticleNode(itemFile));
                }
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
        article.setLocalPath(itemFile.getAbsolutePath());
        article.setUrl(ymlData.get(Instant.BASE_URL) + getWebUrl(itemFile.getName()));
        return article;
    }

    /**
     * @param itemFile
     * @param name     指定html名
     */
    private static FileNode getArticleNode(@NotNull File itemFile, String name) {
        FileNode article = new FileNode(FileNode.Type.FILE);
        article.setName(name);
        article.setLocalPath(itemFile.getAbsolutePath());
        article.setUrl(ymlData.get(Instant.BASE_URL) + getWebUrl(name));
        return article;
    }


    private static String getLocalOutputPath(@NotNull String fileName) {

        return getWebUrl(testFolderUrl + Instant.PUBLISH_URL + "\\" + fileName);
    }

    private static String getWebUrl(@NotNull String fileName) {
        return fileName.replace(" ", "_").replace(".md", ".html");
    }


}