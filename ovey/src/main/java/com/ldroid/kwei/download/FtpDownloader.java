package com.ldroid.kwei.download;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by ngliaxl on 2018/4/9.
 */
public class FtpDownloader implements Downloader{

    private static int DEAFULT_REMOTE_PORT = 21;
    private static String DEAFULT_REMOTE_CHARSET = "UTF-8";

    private String server;
    private int portNumber;
    private String user;
    private String password;

    public FtpDownloader() {
    }

    public FtpDownloader(String server, int portNumber, String user, String password) {
        this.server = server;
        this.portNumber = portNumber;
        this.user = user;
        this.password = password;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public File download(@NonNull final Downloader.FileParam file) throws IOException {
        return execute(new FtpClientCallback<File>() {
            @Override
            public File doTransfer(FTPClient ftp) {
                ftp.enterLocalPassiveMode();
                if (retrieveFile(ftp, file)) {
                    return file.localFile;
                } else {
                }
                return null;
            }
        });
    }


    /**
     * @param files
     * @return
     * @throws IOException
     */
    @Override
    public List<File> download(@NonNull final List<Downloader.FileParam> files) throws IOException {
        return execute(new FtpClientCallback<List<File>>() {
            @Override
            public List<File> doTransfer(FTPClient ftp) {
                ftp.enterLocalPassiveMode();
                List<File> successList = new ArrayList<>();
                for (Downloader.FileParam file : files) {
                    if (retrieveFile(ftp, file)) {
                        successList.add(file.localFile);
                    } else {
                    }
                }
                return successList;
            }
        });
    }

    private boolean retrieveFile(FTPClient ftp, Downloader.FileParam file) {
        OutputStream outputStream = null;
        boolean success = false;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file
                    .localFile));
            success = ftp.retrieveFile(file.remote, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    public <T> T execute(FtpClientCallback<T> callback) throws IOException {
        FTPClient ftp = new FTPClient();
        try {
            ftp.setDataTimeout(7200);
            ftp.setControlEncoding(DEAFULT_REMOTE_CHARSET);
            ftp.setDefaultPort(DEAFULT_REMOTE_PORT);
            if (portNumber != 0) {
                ftp.connect(server, portNumber);
            } else {
                ftp.connect(server);
            }

            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
            if (!ftp.login(user, password)) {
                ftp.quit();
                ftp.disconnect();
                throw new IOException("Cant Authentificate to FTP-Server");
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            return callback.doTransfer(ftp);
        } finally {
            if (ftp != null) {
                ftp.logout();
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            }
        }
    }

    interface FtpClientCallback<T> {

        T doTransfer(FTPClient ftp) throws IOException;

    }

}
