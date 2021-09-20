package Collection;

public class Address {
    private String zipCode; //Длина строки не должна быть больше 28, Поле может быть null

    public String GetZipCode(){ return this.zipCode; }

    public boolean SetZipCode(String zipCode){
        boolean done = true;
        if (zipCode.length() > 28 ) {
            System.out.println("Ошибка ввода данных: Значение индекса не должно превышать 28 символов");
            done = false;
        }
        if(done) this.zipCode = zipCode;
        return done;
    }
}
