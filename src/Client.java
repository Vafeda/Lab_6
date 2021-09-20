import Serialization.Serialization;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Клиент
 */
public class Client implements Serializable{

    private static Socket clientSocket; // сокет для общения
    private static BufferedReader reader; // ридер читающий с консоли
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static final int port = 4004; // порт для подключения
    private static final Serialization serialization = new Serialization(); // сериализатор/десериализатор
    private static final String serialisedDate = "SerialisationDate.txt"; //Файл где храниться сериализованное сообщение

    /**
     * Это main)
     */
    public static void main(String[] args){
        try {
            connection( true);
            while (true){
                String message = write();
                read();
                if (message.equals("exit")){
                    connection(false);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Невозможно установить соединение с сервером");
        }
    }

    /**
     * Модуль отправки запросов на сервер
     *
     * @return - введённая команда
     * @throws IOException - ошибка чтения
     */
    public static String write() throws IOException {
        System.out.print("\n" + ": ");
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String message = reader.readLine();
        if (((message.split(" ")[0].equals("update") | message.split(" ")[0].equals("insert")) & message.split(" ").length == 2) | ((message.split(" ")[0].equals("remove_greater") | message.split(" ")[0].equals("remove_lower")) & message.split(" ").length == 1)){
            message += " " + elementPreparation();
        }
        serialization.serializeObject(message,serialisedDate);
        out.write("\n");
        out.flush();
        return message;
    }

    /**
     * Модуль принятия сообщений от сервера
     *
     * @throws IOException - ошибка принятия сообщений
     */
    public static void read() throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String serverWord = in.readLine();
        serverWord = serialization.deserializeObject(serialisedDate);
        System.out.println(serverWord);
    }

    /**
     * Модуль соединения с сервером
     *
     * @param connect - режим работы (отключиться/подключиться)
     * @throws IOException - ошибка подключения
     */
    public static void connection(boolean connect) throws IOException {
        if (connect) {
            clientSocket = new Socket("localhost", port);
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Соединение с сервером установлено");
        }
        if (!connect) {
            System.out.println("Клиент был закрыт...");
            clientSocket.close();
            in.close();
            out.close();
        }
    }

    /**
     * Подготавливает элемент к отправке на сервер
     *
     * @return сформированный эленмент
     */
    public static String elementPreparation(){
        Scanner sc = new Scanner(System.in);
        String field = "Name";
        String element = null;
        System.out.println("Введите данные организации");
        while(true) {
            switch (field) {
                case "Name":
                    System.out.print("Наименование: ");
                    element = sc.nextLine();
                    field = "Coordinate X";
                    break;
                case "Coordinate X":
                    System.out.print("Координата X: ");
                    element += "," + sc.nextLine();
                    field = "Coordinate Y";
                    break;
                case "Coordinate Y":
                    System.out.print("Координата Y: ");
                    element += "," + sc.nextLine();
                    field = "Official Address";
                    break;
                case "Official Address":
                    System.out.print("Адрес (Индекс): ");
                    element += "," + sc.nextLine();
                    field = "Type";
                    break;
                case "Type":
                    System.out.print("Тип [1-COMMERCIAL, 2-PUBLIC, 3-GOVERNMENT, 4-TRUST]: ");
                    element += "," + sc.nextLine();
                    field = "Annual Turnover";
                    break;
                case "Annual Turnover":
                    System.out.print("Годовой оборот: ");
                    element += "," + sc.nextLine();
                    return element;
            }
        }
    }
}