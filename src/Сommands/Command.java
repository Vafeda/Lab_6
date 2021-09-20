package Сommands;
import Collection.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Command {

    private Collection tm;

    public Command(Collection tm) {
        this.tm = tm;
    }

    public String Execute(String command_line){
        String[] command_args;
        String messageToClient = "Упс, что-то пошло не так...";
        if (command_line != null) {
            command_args = command_line.split(" ");
            switch (command_args[0].toLowerCase()) {
                case "help":
                    messageToClient = this.Help();
                    break;
                case "info":
                    messageToClient = this.Info();
                    break;
                case "show":
                    messageToClient = this.Show();
                    break;
                case "insert":
                    if (command_args.length == 3)  {
                        messageToClient = this.Insert(Long.parseLong(command_args[1]), command_args[2]);
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                case "update":
                    if (command_args.length == 3)  {
                        messageToClient = this.Update(Long.parseLong(command_args[1]), command_args[2]);
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                case "remove_key":
                    if (command_args.length >= 2)  {
                        messageToClient = this.RemoveKey(Long.parseLong(command_args[1]));
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                case "clear":
                    messageToClient = this.Clear();
                    this.Save();
                    break;
                case "histroy":
                    messageToClient = this.History();
                    break;
                case "remove_greater":
                    if (command_args.length == 2)  {
                        messageToClient = this.RemoveGreater(command_args[1]);
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                case "remove_lower":
                    if (command_args.length == 2)  {
                        messageToClient = this.RemoveLower(command_args[1]);
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                case "remove_lower_key":
                    if (command_args.length >= 2)  {
                        messageToClient = this.RemoveLowerKey(Long.parseLong(command_args[1]));
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                case "remove_any_by_type":
                    if (command_args.length >= 2) {
                        messageToClient = this.RemoveAnyByType(command_args[1]);
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                case "group_counting_by_type":
                    messageToClient = this.GroupCountingByType();
                    break;
                case "filter_less_than_annual_turnover":
                    if (command_args.length == 2)  {
                        messageToClient = this.FilterLessThanAnnualTurnover(Float.parseFloat(command_args[1]));
                        this.Save();
                    }
                    else messageToClient = "Недостаточно аргументов";
                    break;
                default:
                    messageToClient = "Команда не найдена. Введите команду help для получения дополнительной информации";
            }
        }
        return messageToClient;
    }

    /**
     *  help - команда выводит все команды который может ввести пользователь в консоль
     */
    private String Help(){
        String message = "help - выводит справку по доступным командам" + "\n" +
            "info - выводит в стандартный поток вывода информацию о коллекции" + "\n" +
            "show - выводит в стандартный поток вывода все элементы коллекции в строковом представлении" + "\n" +
            "insert null {element} - добавляет новый элемент с заданным ключом" + "\n" +
            "update id {element} - обновляет значение элемента коллекции, id которого равен заданному" + "\n" +
            "remove_key null - удаляет элемент из коллекции по его ключу" + "\n" +
            "clear - очищает коллекцию" + "\n" +
            "save - сохраняет коллекцию в файл" + "\n" +
            "exit - завершает программу" + "\n" +
            "remove_greater {element} - удаляет из коллекции все элементы, превышающие заданный" + "\n" +
            "remove_lower {element} - удаляет из коллекции все элементы, меньшие, чем заданный" + "\n" +
            "remove_lower_key null - удаляет из коллекции все элементы, ключ которых меньше, чем заданный" + "\n" +
            "remove_any_by_type type - удаляет из коллекции один элемент, значение поля type которого эквивалентно заданному" + "\n" +
            "group_counting_by_type - сгруппировывает элементы коллекции по значению поля type, вывести количество элементов в каждой группе" + "\n" +
            "filter_less_than_annual_turnover - выводит элементы, значение поля annualTurnover которых меньше заданного";

        return(message);
    }

    /**
     *  info - при вызове этой команды выводиться тип коллекции и количество элементов в ней
     */
    private String Info(){
        String message;
        if (tm.size() > 0)
        {   message = "Тип коллекции: " + tm.get(1L).getClass();
            message += "Количество элементов: " + tm.size();
        }
        else {
            message = "Коллекция пуста";
        }
        return (message);
    }

    /**
     *  show - показывает существующую на данный момент коллекцию 
     */
    private String Show() {
        String message = "";
        Organization org;
        if ( this.tm.size() > 0 ){
            for (Iterator itr = this.tm.keySet().iterator();itr.hasNext();) {
                org = (Organization)this.tm.get(itr.next());
                if ( org != null ) {
                    message += "\n" + org.getInfo();
                }
            }
        }
        else message = "Элементы в коллекции отсутствуют";
        return (message);
    }

    /**
     *  @param key - номер id
     *  insert key - создает элемент коллекции с заданным ключом (key)
     */
    private String Insert(Long key, String element){
        Organization org = new Organization(key,"");
        org = this.GetData(org, element);
        System.out.println(org);
        System.out.println(org.getInfo());
        this.tm.put(key, org);
        this.tm.SetChangeDate();
        return ("Элемент успешно создан");
    }

    /**
     *  @param key - номер id
     *  update key - обновляет значение элемента коллекции
     */
    private String Update(Long key, String element){
        Organization org = new Organization(key,"");
        org = this.GetData(org, element);
        this.tm.put(key, org);
        this.tm.SetChangeDate();
        return "Элемент успешно обновлён";
    }

    /**
     *  @param key - номер id
     *  remove_key key - удаляет элемент из коллекции
     */
    private String RemoveKey(Long key){
        String message;
        Organization org = (Organization) this.tm.get(key);
        if (org != null){
            this.tm.remove(key);
            this.tm.SetChangeDate();
            message = "Элемент коллекции с ключом, равным " + key + " успешно удалён";
        }
        else {
            message = "Элемент коллекции с ключом, равным " + key + " не найден";
        }
        return message;
    }

    /**
     *  сlear - удаляет все элементы из коллекции
     */
    private String Clear() {
        this.tm.clear();
        return "Коллекция очищена";
    }

    private String History(){
        return "привет";
    }

    /**
     *  @throws FileNotFoundException
     *  save - сохраняет коллекцию в файле (data2.csv)
     */
    public String Save() {
        String message;
        try{
            tm.WriteToFile("data.csv");
            message = "Коллекция успешно сохранена";
        } catch (FileNotFoundException e) {
            message = e.getMessage();
        }
        return message;
    }

    private String RemoveGreater(String element){
        String message;
        Organization org = new Organization(0L,"");
        org = this.GetData(org, element);
        if (this.tm.size() > 0) {
            for (Iterator itr = this.tm.keySet().iterator();itr.hasNext();) {
                Organization orgFromCollection = (Organization)this.tm.get(itr.next());
                if (orgFromCollection.hashCode() > org.hashCode()){
                    RemoveKey(orgFromCollection.GetID());
                }
            }
            message = "Элементы, которые больше заданного удаленны";
        } else {
            message = "Коллекция пустая";
        }
        return message;
    }

    private String RemoveLower(String element){
        String message;
        Organization org = new Organization(0L,"");
        org = this.GetData(org, element);
        if (this.tm.size() > 0) {
            for (Iterator itr = this.tm.keySet().iterator();itr.hasNext();) {
                Organization orgFromCollection = (Organization)this.tm.get(itr.next());
                if (orgFromCollection.hashCode() < org.hashCode()){
                    RemoveKey(orgFromCollection.GetID());
                }
            }
            message = "Элементы, которые меньше заданного удаленны";
        } else {
            message = "Коллекция пустая";
        }
        return message;
    }

    private String RemoveLowerKey(Long key){
        Organization org;
        while(this.tm.size() > 0) {
            Long first_key = (Long)this.tm.firstKey();
            System.out.println(first_key);
            if(first_key < key) this.RemoveKey(first_key);
            else break;
        }
        return "Елементы успешно удалены";
    }

    private String RemoveAnyByType (String type_str) {
        String message;
        Organization org;
        OrganizationType type;
        try {
            type = OrganizationType.valueOf(type_str);
            for (Iterator itr = this.tm.keySet().iterator(); itr.hasNext(); ) {
                Long key = (long)itr.next();
                org = (Organization) this.tm.get(key);
                if(org.GetType() == type) {
                    this.RemoveKey(key);
                    break;
                }
            }
            message = "Елементы успешно удалены";
        }
        catch (Exception e) {
            message = "Ошибка ввода. Некорректный тип";
        }
        return message;
    }

    private String GroupCountingByType () {
        String message;
        Organization org;
        int[] counter = new int[4];
        if (this.tm.size() > 0) {
            boolean empty = true;
            for (Iterator itr = this.tm.keySet().iterator(); itr.hasNext(); ) {
                org = (Organization) this.tm.get(itr.next());
                switch (org.GetType()) {
                    case TRUST:
                        counter[0] = counter[0] + 1;
                        break;
                    case GOVERNMENT:
                        counter[1] = counter[1] + 1;
                        break;
                    case COMMERCIAL:
                        counter[2] = counter[2] + 1;
                        break;
                    case PUBLIC:
                        counter[3] = counter[3] + 1;
                        break;
                    default:
                        break;
                }
            }
            message = "TRUST:" + counter[0] +
                    ", GOVERNMENT:" + counter[1] +
                    ", COMMERCIAL:" + counter[2] +
                    ", PUBLIC:" + counter[3];
        }
        else {
            message = "Коллекция пуста";
        }
        return message;
    }

    private String FilterLessThanAnnualTurnover(Float annualTurnover){
        String message = null;
        Organization org;
        Coordinates coord;
        if ( this.tm.size() > 0 ){
            boolean empty = true;
            for (Iterator itr = this.tm.keySet().iterator();itr.hasNext();) {
                org = (Organization)this.tm.get(itr.next());
                if ( org != null ) {
                    if (org.GetAnnualTurnover() < annualTurnover){
                        org.getInfo();
                        empty = false;
                    }
                }
            }
            if(empty) message = "Элементы, удовлетворяющие условию, отсутствуют в коллекции";
            else message = "Элементы успешно отсортированно";
        }

        else message = "Элементы в коллекции отсутствуют";
        return message;
    }

    public Organization GetData(Organization org, String element){
        String field = "Name";
        while(true) {
            switch (field) {
                case "Name":
                    if (org.SetName(element.split(",")[0])) {
                        field = "Coordinate X";
                    }
                    break;
                case "Coordinate X":
                    if (org.GetCoordinates().SetX((element.split(",")[1]))) {
                        field = "Coordinate Y";
                    }
                    break;
                case "Coordinate Y":
                    if (org.GetCoordinates().SetY((element.split(",")[2]))) {
                        field = "Official Address";
                    }
                    break;
                case "Official Address":
                    if (org.GetOfficialAddress().SetZipCode((element.split(",")[3]))) {
                        field = "Type";
                    }
                    break;
                case "Type":
                    if ( org.SetType((element.split(",")[4])) ) {
                        field = "Annual Turnover";
                    }
                    break;
                case "Annual Turnover":
                    if ( org.SetAnnualTurnover((element.split(",")[5])) ){
                        return org;
                    }

            }
        }
    }
}
