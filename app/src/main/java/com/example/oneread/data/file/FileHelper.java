package com.example.oneread.data.file;

import android.content.Context;
import com.example.oneread.di.anotation.ApplicationContext;
import com.example.oneread.di.anotation.CachedThreadPool;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Singleton
public class FileHelper {

    private ExecutorService executor;
    private Context context;

    @Inject
    public FileHelper(@ApplicationContext Context context, @CachedThreadPool ExecutorService executor) {
        this.context = context;
        this.executor = executor;
    }

    public List<String> downloadImages(List<String> images, String startPath) {
        try {
            List<DownloadImageTask> tasks = new ArrayList<>(images.size());
            tasks.add(new DownloadImageTask(context, images, startPath));
            List<Future<List<String>>> results = executor.invokeAll(tasks);
            for (Future<List<String>> ff : results) {
                if (ff.get() != null && ff.get().size() > 0) return ff.get();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String downloadImage(String image, String startPath, String fileName) {
        try {
            List<DownloadImageTask> tasks = new ArrayList<>();
            tasks.add(new DownloadImageTask(context, Arrays.asList(image), startPath));
            List<Future<List<String>>> results = executor.invokeAll(tasks);
            for (Future<List<String>> ff : results) {
                if (ff.get() != null && ff.get().size() > 0) return ff.get().get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

//    private String downloadImage(String url, String path, String filename){
//        try{
//            Uri downloadUri = Uri.parse(url);
//            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                    .setAllowedOverRoaming(false)
//                    .setTitle(filename)
//                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
//                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,path + File.separator + filename + ".jpg");
//            downloadManager.enqueue(request);
//            return
//        } catch (Exception ignored){
//            return null;
//        }
//    }

    public List<String> downloadTexts(List<String> texts, String startPath){
        try {
            List<DownloadTextTask> tasks = new ArrayList<>(texts.size());
            tasks.add(new DownloadTextTask(context, texts, startPath));
            List<Future<List<String>>> results = executor.invokeAll(tasks);
            for (Future<List<String>> ff : results) {
                if (ff.get() != null && ff.get().size() > 0) return ff.get();
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }


    /* Java isn't able to delete folders with data in it.
        You have to delete all files before deleting the folder. */
    public void deleteDirectory(File file) {
        System.out.println(file.getAbsolutePath());
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }

}
