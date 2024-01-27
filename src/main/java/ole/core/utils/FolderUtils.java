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
}
