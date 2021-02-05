import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<String> readTextFromFile(String fileName){
        List<String> fromFile = null;
        Path path = Paths.get(fileName);
        try {
            fromFile = Files.readAllLines(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fromFile;
    }

    public static List<String[]> splitList(List<String> stringList){
        List<String[]> splitList = new ArrayList<>();
        for (String line : stringList) {
             String[] splitLine = line.split("/");
             splitList.add(splitLine);
        }
        return splitList;
    }

    public static void writeObjectToFile(String fileName, Object object) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Object readObjectFromFile(String fileName) {
        Object object = null;
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            object = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

}
