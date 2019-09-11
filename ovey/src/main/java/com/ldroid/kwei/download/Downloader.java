package com.ldroid.kwei.download;


import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by ngliaxl on 2018/4/18.
 */
public interface Downloader {

    File download(final FileParam file) throws IOException;

    List<File> download(final List<FileParam> files) throws IOException;

    class FileParam {
        String remote;
        File localFile;

        public FileParam(@NonNull String remote, @NonNull File localFile) {
            this.remote = remote;
            this.localFile = localFile;
        }
    }

}
