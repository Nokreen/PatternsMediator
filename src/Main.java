import java.util.ArrayList;
import java.util.List;

//  Интерфейс Посредника  (вышка)
interface Mediator {
    void registerAircraft(Aircraft aircraft);
    void sendMessage(String message, Aircraft sender);
}

//  Интерфейс коллеги (Самолет)
interface Aircraft {
    String getName();
    void send(String message);
    void receive(String message);
}

//  Конкретный посредник (Конкретная вышка)
class ControlTower implements Mediator {
    private List<Aircraft> aircrafts = new ArrayList<>();

    @Override
    public void registerAircraft(Aircraft aircraft) {
        aircrafts.add(aircraft);
    }

    @Override
    public void sendMessage(String message, Aircraft sender) {
        for (Aircraft aircraft : aircrafts) {
            // Сообщение получают все, кроме того, кто его отправил
            if (aircraft != sender) {
                aircraft.receive(sender.getName() + ": " + message);
            }
        }
    }
}

//  Конкретный коллкга (Конкретный самолет)
class Airplane implements Aircraft {
    private String name;
    private Mediator mediator;

    public Airplane(String name, Mediator mediator) {
        this.name = name;
        this.mediator = mediator;
        this.mediator.registerAircraft(this); // Самолет регистрируется на вышке
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void send(String message) {
        System.out.println("--- " + name + " передает сообщение: " + message);
        mediator.sendMessage(message, this); // Отправляем через посредника!
    }

    @Override
    public void receive(String message) {
        System.out.println(name + " получил сообщение -> " + message);
    }
}

//  Тест
public class Main {
    public static void main(String[] args) {
        // Создаем диспетчерскую вышку
        Mediator tower = new ControlTower();

        // Создаем самолеты и сразу привязываем их к вышке
        Aircraft boeing = new Airplane("Boeing 737", tower);
        Aircraft airbus = new Airplane("Airbus A320", tower);
        Aircraft su = new Airplane("Superjet 100", tower);

        System.out.println("=== Начинаем переговоры ===");

        // Боинг отправляет собщение. Его получат все, кроме него самого
        boeing.send("Запрашиваю разрешение на взлет!");

        System.out.println();

        // Суперджет отправляет сообщение
        su.send("Всем привет, иду на посадку!");
    }
}