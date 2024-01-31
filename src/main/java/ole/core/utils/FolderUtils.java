package ole.core.utils;

import java.io.File;

public class FolderUtils {
    public static void deleteFolder(String path) {
        File publicFile = new File(path);
        File[] publicFiles = publicFile.listFiles();
        if (publicFiles == null) {
            publicFile.delete();
            return;
        }
        for (File item : publicFiles) {
            item.delete();
        }
        publicFile.delete();
    }

    public static boolean createDirectoryIfNotExists(String path) {
        File directory = new File(path);

        // 判断路径是否存在
        if (!directory.exists()) {
            // 如果路径不存在，则创建路径

            return directory.mkdirs();

        } else {
            return true;
        }
    }
}
