package com.gcs;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class Main {
    public static void main(String[] args) {
        // GCSのプロジェクトID
        String projectId = "projectId";

        // バケット名
        String bucketName = "bucketName";

        // 認証情報ファイルのパス
        String credentialsPath = "credentialsPath";


        try {
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));

            // GCSクライアントを初期化
            Storage storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build()
                    .getService();


            // バケット内のオブジェクト一覧を取得
            Bucket bucket = storage.get(bucketName);

            bucket.list().iterateAll().forEach(blob -> {
                System.out.println(blob.getName());
            });

            // ファイルのアップロード
            // アップロード先のオブジェクト名
            String objectName = "path/file_name.csv";
            // アップロード対象のファイル名
            String uploadFilePath = "local_path/upload_upload_filesample.txt";

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();
            storage.createFrom(blobInfo, new FileInputStream(uploadFilePath));
            
            // ファイルのダウンロード
            // ダウンロード対象のオブジェクト名
            String downloadObjectName = "path/file_name.csv";
            // ダウンロード対象の保存先
            String saveFilePath = "local_path/file_name.csv";
            Blob blob = bucket.get(downloadObjectName);
            blob.downloadTo(Paths.get(saveFilePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}