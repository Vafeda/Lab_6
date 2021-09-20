package Collection;

public class Coordinates {
    private Float x; //Поле не может быть null
    private Double y; //Значение поля должно быть больше -850, Поле не может быть null

    public Float GetX() { return this.x; }

    public boolean SetX(String x) {
        boolean done = true;
        if (x == null) {
            System.out.println("Ошибка ввода данных: Значение координаты X не может быть null");
            done = false;
        }
        else try {
            this.x = Float.parseFloat(x);
        }
        catch (Exception e) {
            System.out.println("Ошибка ввода данных: Некорректный формат координаты X");
            done = false;
        }

        return done;
    }

    public Double GetY() { return this.y; }

    public boolean SetY(String y) {
        boolean done = true;
        if (y == null) {
            System.out.println("Ошибка ввода данных: Значение координаты Y не может быть null");
            done = false;
        }
        else try {
            this.y = Double.parseDouble(y);
        }
        catch (Exception e) {
            System.out.println("Ошибка ввода данных: Некорректный формат координаты Y");
            done = false;
        }
        if (done) {
            if (this.y < -850.0) {
                System.out.println("Ошибка ввода данных: Значение координаты Y должно превышать -850");
                done = false;
            }
        }
        return done;
    }
}
