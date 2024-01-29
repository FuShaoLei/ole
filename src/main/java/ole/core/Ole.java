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
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Ole {

    /**
     * 将来会从cmd中读取
     * 现在知识写死
     */
    private static String mRootLocalPath = "";
    private static Map<String, Object> ymlData = new HashMap<>();
    private static List<FileNode> allFileNodeList = new ArrayList<>();


    public void generate(String rootLocalPath) {
        System.err.println("rootLocalPath = " + rootLocalPath);
        mRootLocalPath = rootLocalPath;

        String configUrl = mRootLocalPath + "\\" + Instant.CONFIG_FILE; // TODO 这里的\\我感觉还要在考虑下，感觉兼容性不是很好

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
        List<FileNode> nodeList = OrganizeRootData(mRootLocalPath + Instant.CONTENT_URL);
        allFileNodeList = nodeList;
//        nodeList.forEach(System.err::println);

        ready2Generate();

    }

    private static void ready2Generate() {
        // 清空publish目录
        FolderUtils.deleteFolder(mRootLocalPath + Instant.PUBLISH_URL);
        File newFile = new File(mRootLocalPath + Instant.PUBLISH_URL);
        newFile.mkdir();

        // 初始化freemarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(Ole.class, "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.CHINESE);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

        try {
            Template template = cfg.getTemplate("template.ftl");
            handleMD2Html(template, renderer, parser, allFileNodeList);
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

                    FileInputStream fileInputStream = new FileInputStream(fileNode.getLocalPath());
                    InputStreamReader isr = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

                    Node document = parser.parseReader(new BufferedReader(isr));

                    input.put("title", fileNode.getName());
                    input.put("article", renderer.render(document));

                    FileOutputStream fos = new FileOutputStream(getLocalOutputPath(fileNode.getName()));
                    OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

                    template.process(input, new BufferedWriter(osw));
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

            if (itemFile.isDirectory()) {
                FileNode folder = new FileNode(FileNode.Type.DIRECTORY);
                folder.setName(itemFile.getName());
                folder.setLevel(rootLevel);
                File[] direFiles = itemFile.listFiles();
                if (direFiles != null) {
                    List<FileNode> childrenNodeList = new ArrayList<>();
                    for (File direFile : direFiles) {
                        childrenNodeList.add(getNode(direFile, rootLevel));
                    }
                    folder.setFileNodeList(childrenNodeList);
                }
                rootNodeList.add(folder);
            } else if (itemFile.isFile()) { // 这里其实要做一个判断，判断是否有readme.md这个文件
                if (itemFile.getName().equals(Instant.ROOT_INDEX_FILE)) {
                    rootNodeList.add(getArticleNode(itemFile, "index.html", rootLevel));
                } else {
                    rootNodeList.add(getArticleNode(itemFile, rootLevel));
                }
            }
        }

        rootNodeList.sort(new Comparator<FileNode>() {
            @Override
            public int compare(FileNode o1, FileNode o2) {
                if (o1.isArticle() && o2.isFolder()) {
                    return -1;
                } else if (o2.isArticle() && o1.isFolder()) {
                    return 1;
                }
                return 0;
            }
        });
        return rootNodeList;
    }

    /**
     * 获取文件夹下的数据信息
     */
    private static FileNode getNode(File rootFile, int level) {
        if (rootFile.isFile()) {
            return getArticleNode(rootFile, level + 1);
        } else if (rootFile.isDirectory()) {
            FileNode folder = new FileNode(FileNode.Type.DIRECTORY);
            folder.setName(rootFile.getName());
            folder.setLevel(level + 1);
            File[] direFiles = rootFile.listFiles();
            List<FileNode> childrenNodeList = new ArrayList<>();
            if (direFiles != null) {
                for (File itemFile : direFiles) {
                    childrenNodeList.add(getNode(itemFile, level + 1));
                }
                folder.setFileNodeList(childrenNodeList);
            }

            return folder;
        }
        return null;
    }


    private static FileNode getArticleNode(@NotNull File itemFile, int level) {
        FileNode article = new FileNode(FileNode.Type.FILE);
        article.setName(itemFile.getName());
        article.setLocalPath(itemFile.getAbsolutePath());
        article.setUrl(ymlData.get(Instant.BASE_URL) + getWebUrl(itemFile.getName()));
        article.setLevel(level);
        return article;
    }

    /**
     * @param itemFile
     * @param name     指定html名
     */
    private static FileNode getArticleNode(@NotNull File itemFile, String name, int level) {
        FileNode article = new FileNode(FileNode.Type.FILE);
        article.setName(name);
        article.setLocalPath(itemFile.getAbsolutePath());
        article.setUrl(ymlData.get(Instant.BASE_URL) + getWebUrl(name));
        article.setLevel(level);
        return article;
    }


    private static String getLocalOutputPath(@NotNull String fileName) {

        return getWebUrl(mRootLocalPath + Instant.PUBLISH_URL + "\\" + fileName);
    }

    private static String getWebUrl(@NotNull String fileName) {
        return fileName.replace(" ", "_").replace(".md", ".html");
    }


}