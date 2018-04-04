package com.css.common.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件工具类
 *
 */
public class FileUtil {

    /**
     * 得到所有视频文件
     *
     * @param rootpath
     * @param relpath
     * @return
     * @author maoyl
     * @date Mar 5, 2013 2:53:20 PM
     */
    public static List<Map<String, Object>> getVideoFiles(String rootpath, String relpath) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        File file = new File(rootpath);
        if (!file.exists()) {
            return list;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].exists()) {
                        String thisRelPath = relpath + "/" + files[i].getName();
                        if (files[i].isFile()) {
                            String path = files[i].getPath();
                            if (path.endsWith(".swf") || path.endsWith(".flv") || path.endsWith(".wmv")) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("file", files[i]);
                                map.put("path", files[i].getPath());
                                map.put("name", files[i].getName());
                                map.put("relpath", thisRelPath);

                                list.add(map);
                            }
                        }
                        if (files[i].isDirectory()) {
                            List<Map<String, Object>> childFiles = getVideoFiles(files[i].getPath(), thisRelPath);
                            list.addAll(childFiles);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 删除文件
     * @param path
     */
    public static void delete(String path) {
        if (path == null || path.length() == 0) {
            return;
        }
        delete(new File(path));
    }

    /**
     * 删除文件
     * @param file
     */
    public static void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                file.delete();
            }
            for (File temp : files) {
                delete(temp);
            }
        }
    }

}
