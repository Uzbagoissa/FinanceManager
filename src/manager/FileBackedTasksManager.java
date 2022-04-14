package manager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager, Serializable {

    public static FileBackedTasksManager loadFromFile(File file) throws IOException, ClassNotFoundException {
        FileBackedTasksManager fileBackedTasksManager = null;
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        if (!file.exists()) {
            fileBackedTasksManager = new FileBackedTasksManager();
        }
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream("backend.txt");
                objectInputStream = new ObjectInputStream(fileInputStream);
                fileBackedTasksManager = (FileBackedTasksManager) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                objectInputStream.close();
                fileInputStream.close();
            }
        }
        return fileBackedTasksManager;
    }

    public void save(File file, String dir, FileBackedTasksManager fileBackedTasksManager) throws IOException{
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (!file.exists()) {
                Files.createFile(Paths.get(dir, "backend.txt"));
                fileOutputStream = new FileOutputStream("backend.txt");
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(fileBackedTasksManager);
            } else {
                fileOutputStream = new FileOutputStream("backend.txt");
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(fileBackedTasksManager);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            objectOutputStream.close();
            fileOutputStream.close();
        }
    }
}
