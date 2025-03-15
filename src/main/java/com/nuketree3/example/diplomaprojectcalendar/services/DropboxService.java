package com.nuketree3.example.diplomaprojectcalendar.services;

import com.dropbox.core.DbxException;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DropboxService {

    @Autowired
    private DbxClientV2 client;

    private static final String DROPBOX_FOLDER = "/images/";

    /**
     * uploadFile - метод, который загружает файл в облако
     * @param file - файл, который нужно загрузить в облако
     */
    public void uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            String dropboxPath = DROPBOX_FOLDER + fileName;
            FileMetadata fileMetadata = client.files().uploadBuilder(dropboxPath).uploadAndFinish(inputStream);
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * downloadFile - метод, который загружает из облака фото по пути
     * @param dropboxPath - путь файла в облаке
     * @return - возвращает массив байтов, представляющий файл
     */
    public byte[] downloadFile(String dropboxPath) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            client.files().download(DROPBOX_FOLDER + dropboxPath).download(outputStream);
            return outputStream.toByteArray();
        } catch (IOException | DbxException e) {
            return new byte[0];
        }
    }

    /**
     * deleteFile - метод, который по пути удаляет файл в облаке
     * @param dropboxPath - путь файла, который нужно удалить
     */
    public void deleteFile(String dropboxPath) {
        try{
            client.files().deleteV2(DROPBOX_FOLDER + dropboxPath);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
