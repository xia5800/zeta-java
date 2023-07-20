package org.zetaframework.extra.file.model;

/**
 * 文件删除参数
 *
 * @author gcc
 */
public class FileDeleteParam {

    /** 文件相对路径 */
    private String path;

    /** 将来其它的OSS要用到新的字段可以加在这里.. */


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
