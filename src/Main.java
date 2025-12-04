import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class Main {
    public static void main(String[] args) {
        String fileURL = "https://upload.wikimedia.org/wikipedia/commons/2/2c/Battle_of_Gaugamela%2C_331_BC_-_Opening_movements.png";
        String saveDir = "/Users/oleg/Documents/программирование/системное программирование/10 лаба (запуск и скачивание)";
        try {
            createDirectoryIfNotExists(saveDir);
            downloadFileNIO(fileURL, saveDir);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void downloadFileNIO(String fileURL, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        String fileName = getFileNameFromURL(url);
        String result = fileName.substring(0, fileName.length() - 4);
        String filePath = saveDir + File.separator + fileName;
        System.out.println("Загрузка файла: " + fileName);
        System.out.println("Сохраняем в: " + filePath);
        // Создаем HttpURLConnection для установки заголовков
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Устанавливаем User-Agent для обхода 403 ошибки
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        connection.setRequestProperty("Referer", "https://commons.wikimedia.org/");
        // Проверяем код ответа
        int responseCode = connection.getResponseCode();
        System.out.println("HTTP код ответа: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream in = connection.getInputStream()) {
                Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("Файл успешно загружен");
        } else {
            throw new IOException("Сервер вернул HTTP код: " + responseCode);
        }
    }
    public static void downloadFileIO(String fileURL, String photo, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        String fileName = getFileNameFromURL(url);
        String filePath = saveDir + File.separator + fileName;
        System.out.println("Загрузка файла (IO метод): " + fileName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        connection.setRequestProperty("Referer", "https://commons.wikimedia.org/");
        int responseCode = connection.getResponseCode();
        System.out.println("HTTP код ответа: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("Файл успешно загружен!");
        } else {
            throw new IOException("Сервер вернул HTTP код: " + responseCode);
        }
    }
    private static String getFileNameFromURL(URL url) {
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
    private static void createDirectoryIfNotExists(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Создана директория: " + dirPath);
        }
    }
}