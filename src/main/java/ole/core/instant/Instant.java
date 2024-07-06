package ole.core.instant;

import java.io.File;

public class Instant {
    public static final String SEPARATOR = File.separator;
    public static final String CONFIG_FILE = "config.yml";
    public static final String BASE_URL = "baseUrl";


    /**
     * 原始的md文件的路径
     */
    public static final String CONTENT_URL = SEPARATOR + "content";


    /**
     * 编译过后的html文件路径
     */
    public static final String PUBLISH_URL = SEPARATOR + "publish";


    /**
     * 要编译成index文件的文件名
     */
    public static final String ROOT_INDEX_FILE = "README.md";

}
