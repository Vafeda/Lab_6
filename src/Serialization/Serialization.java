package Serialization;
import java.io.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Класс, проводящий сериализацию и десериализацию объектов
 */
public class Serialization {

    /**
     * Сериализует объект
     *
     * @param <T> тип объекта
     * @return массив байтов
     */
    public <T> byte[] serializeObject(T input, String file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            T object = input;
            oos.writeObject(object);
        } catch (IOException e) {
            System.out.println("Ошибка сериализации");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Десериализует объект
     *
     * @param <T> тип объекта
     * @return объект
     */
    public <T> T deserializeObject(String file) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            return (T) ois.readObject();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
