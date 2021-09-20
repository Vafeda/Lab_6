import Collection.*;
import Serialization.Serialization;
import Сommands.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.TreeSet;

public class Server {
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static final int port = 4004; // порт для подключения
    private static final Serialization serialization = new Serialization(); // сериализатор/десериализатор
    private static final String serialisedDate = "SerialisationDate.txt"; //Файл где храниться сериализованное сообщение
   // private static TreeMap<Organization, > collection;

    public static void main(String[] args) throws IOException, InterruptedException {
        Collection tm = new Collection();
        Command cmd = new Command(tm);
        boolean work;

        if (args.length > 0) {
            tm.CreateFromFile(args[0]);
        }
        while (true) {
            work = true;
            connection(true, "non");
            try {
                while (work) {
                    String message = read();
                    if (message.equals("exit")) {
                        connection(false, "non");
                        work = false;
                    } else {
                        write(execution(message, cmd));
                    }
                }
            } catch (IOException | InterruptedException e) {
                connection(false, "non");
                System.out.println("Соединение с клиентом разорванно");
            }
        }
    }

    /**
     * Модуль приёма подключений
     *
     * @param connect - режим работы (отключиться/подключиться)
     * @param close - звкрытие сервера
     * @throws IOException - ошибка подключения
     */
    public static void connection(boolean connect, String close) throws IOException {
        if (connect) {
            server = new ServerSocket(port);
            System.out.println("Ожидание подключения...");
            clientSocket = server.accept();
            System.out.println("Соединение с клиентом установлено");
        }
        if (!connect) {
            System.out.println("Соединение с клиентом разорвано");
            clientSocket.close();
            server.close();
            if (close.equals("close server")){
                System.out.println("Сервер закрыт!");
                System.exit(0);
            }
        }
    }

    /**
     * Модуль чтения запроса
     *
     * @return - возвращает десериализованную команду
     * @throws IOException - ошибка чтения запроса
     */
    public static String read() throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String message = in.readLine();
        message = serialization.deserializeObject(serialisedDate);
        System.out.println("Сервер принял команду: " + message);
        return message;
    }

    /**
     * Модуль выполнения команд
     *
     * @param command - подаваемая команда
     * @param cmd - отвечает за выполнение команд
     * @return
     */
    public static String execution(String command, Command cmd) {
        return cmd.Execute(command);
    }


    /**
     * Модуль отправки ответов
     *
     * @param messageToClient - сообщение клиенту
     * @throws IOException
     * @throws InterruptedException
     */
    public static void write(String messageToClient) throws IOException, InterruptedException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        serialization.serializeObject(messageToClient, serialisedDate);
        out.write("\n");
        out.flush();
    }
}
