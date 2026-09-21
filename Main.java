import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static class Item {
        public final String name;
        public final String type;
        public final int weightGrams;
        public final int value;

        public Item(String name, String type, int weightGrams, int value) {

            if (name == null || name.strip().isEmpty()) {
                throw new IllegalArgumentException("Имя предмета пусто");
            }

            name = name.strip();

            if (name.length() < 1 || name.length() > 40) {
                throw new IllegalArgumentException(
                        "Имя предмета должно содержать от 1 до 40 символов"
                );
            }

            if (type == null ||
                    (!type.equals("weapon")
                            && !type.equals("armor")
                            && !type.equals("potion"))) {

                throw new IllegalArgumentException(
                        "Тип должен быть weapon, armor или potion"
                );
            }

            if (weightGrams < 1 || weightGrams > 100000) {
                throw new IllegalArgumentException(
                        "Масса должна быть от 1 до 100000 грамм"
                );
            }

            if (value < 0 || value > 1000000) {
                throw new IllegalArgumentException(
                        "Стоимость должна быть от 0 до 1000000 монет"
                );
            }

            this.name = name;
            this.type = type;
            this.weightGrams = weightGrams;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getWeightGrams() {
            return weightGrams;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name
                    + " [" + type + "]"
                    + ", " + weightGrams + " г"
                    + ", " + value + " монет";
        }
    }

    static class Inventory {

        public final Item[] items;
        public int size;
        public final int maxWeightGrams;

        public Inventory(int capacity, int maxWeightGrams) {

            if (capacity < 1 || capacity > 20) {
                throw new IllegalArgumentException(
                        "Количество ячеек должно быть от 1 до 20"
                );
            }

            if (maxWeightGrams < 1 || maxWeightGrams > 100000) {
                throw new IllegalArgumentException(
                        "Лимит массы должен быть от 1 до 100000 грамм"
                );
            }

            this.items = new Item[capacity];
            this.size = 0;
            this.maxWeightGrams = maxWeightGrams;
        }

        public boolean add(Item item) {

            if (item == null) {
                return false;
            }

            if (size >= items.length) {
                return false;
            }

            int newWeight = totalWeightGrams() + item.getWeightGrams();

            if (newWeight > maxWeightGrams) {
                return false;
            }

            items[size] = item;
            size++;

            return true;
        }

        public Item remove(int index) {

            if (index < 0 || index >= size) {
                return null;
            }

            Item removed = items[index];

            for (int i = index; i < size - 1; i++) {
                items[i] = items[i + 1];
            }

            size--;
            items[size] = null;

            return removed;
        }

        public Item get(int index) {

            if (index < 0 || index >= size) {
                return null;
            }

            return items[index];
        }

        public int size() {
            return size;
        }

        public int totalWeightGrams() {

            int sum = 0;

            for (int i = 0; i < size; i++) {
                sum += items[i].getWeightGrams();
            }

            return sum;
        }

        public long totalValue() {

            long sum = 0;

            for (int i = 0; i < size; i++) {
                sum += items[i].getValue();
            }

            return sum;
        }

        public Item[] snapshot() {
            return Arrays.copyOf(items, size);
        }

        public int getMaxWeightGrams() {
            return maxWeightGrams;
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Вариант А — Воин
        Inventory inventory = new Inventory(4, 6000);

        // Три начальных предмета
        Item sword = new Item("Меч", "weapon", 2500, 100);
        Item shield = new Item("Щит", "armor", 3000, 80);
        Item potion = new Item("Зелье", "potion", 500, 20);

        inventory.add(sword);
        inventory.add(shield);
        inventory.add(potion);

        System.out.println("=================================");
        System.out.println("   ИНВЕНТАРЬ ГЕРОЯ");
        System.out.println("=================================");

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("Меню:");
            System.out.println("1 - Добавить предмет");
            System.out.println("2 - Показать инвентарь");
            System.out.println("3 - Удалить предмет");
            System.out.println("4 - Использовать зелье");
            System.out.println("5 - Сводка");
            System.out.println("0 - Выйти");
            System.out.print("Выберите действие: ");

            int choice = readInt(scanner);

            switch (choice) {

                case 1:
                    addItemMenu(scanner, inventory);
                    break;

                case 2:
                    showInventory(inventory);
                    break;

                case 3:
                    removeItemMenu(scanner, inventory);
                    break;

                case 4:
                    usePotionMenu(scanner, inventory);
                    break;

                case 5:
                    showSummary(inventory);
                    break;

                case 0:
                    running = false;
                    System.out.println("Программа завершена.");
                    break;

                default:
                    System.out.println("Ошибка: такого пункта меню нет.");
                    break;
            }
        }

        scanner.close();
    }

    public static void addItemMenu(
            Scanner scanner,
            Inventory inventory
    ) {

        System.out.println();
        System.out.println("=== Добавление предмета ===");

        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите тип (weapon / armor / potion): ");
        String type = scanner.nextLine().strip();

        System.out.print("Введите массу в граммах: ");
        int grams = readInt(scanner);

        System.out.print("Введите стоимость в монетах: ");
        int value = readInt(scanner);

        try {

            Item item = new Item(name, type, grams, value);

            boolean added = inventory.add(item);

            if (added) {
                System.out.println("Предмет успешно добавлен.");
            } else {
                System.out.println("Нет свободной ячейки или превышен лимит массы.");
            }

        } catch (IllegalArgumentException ex) {

            System.out.println(
                    "Данные предмета: " + ex.getMessage()
            );
        }
    }

    public static void showInventory(Inventory inventory) {

        System.out.println();
        System.out.println("=== Инвентарь ===");

        if (inventory.size() == 0) {
            System.out.println("Инвентарь пуст.");
            return;
        }

        for (int i = 0; i < inventory.size(); i++) {

            Item item = inventory.get(i);

            System.out.println(
                    (i + 1) + ". " + item
            );
        }
    }

    public static void removeItemMenu(
            Scanner scanner,
            Inventory inventory
    ) {

        System.out.println();
        System.out.println("=== Удаление предмета ===");

        if (inventory.size() == 0) {
            System.out.println("Инвентарь пуст.");
            return;
        }

        showInventory(inventory);

        System.out.print("Введите номер предмета: ");
        int number = readInt(scanner);

        int index = number - 1;

        Item removed = inventory.remove(index);

        if (removed == null) {
            System.out.println("Неверный номер предмета.");
        } else {
            System.out.println(
                    "Удалён предмет: " + removed.getName()
            );
        }
    }

    public static void usePotionMenu(
            Scanner scanner,
            Inventory inventory
    ) {

        System.out.println();
        System.out.println("=== Использование предмета ===");

        if (inventory.size() == 0) {
            System.out.println("Инвентарь пуст.");
            return;
        }

        showInventory(inventory);

        System.out.print("Введите номер предмета: ");
        int number = readInt(scanner);

        int index = number - 1;

        Item item = inventory.get(index);

        if (item == null) {
            System.out.println("Неверный номер предмета.");
            return;
        }

        if (item.getType().equals("potion")) {

            inventory.remove(index);

            System.out.println("Зелье использовано.");

        } else {

            System.out.println(
                    "Этот предмет не является зельем. Ничего не изменилось."
            );
        }
    }

    public static void showSummary(Inventory inventory) {

        System.out.println();
        System.out.println("=== Сводка ===");

        System.out.println(
                "Ячейки: "
                        + inventory.size()
                        + "/4"
        );

        System.out.println(
                "Масса: "
                        + inventory.totalWeightGrams()
                        + "/"
                        + inventory.getMaxWeightGrams()
                        + " г"
        );

        System.out.println(
                "Стоимость: "
                        + inventory.totalValue()
                        + " монет"
        );
    }

    public static int readInt(Scanner scanner) {

        while (!scanner.hasNextInt()) {

            System.out.println(
                    "Ошибка: необходимо ввести целое число."
            );

            scanner.nextLine();
            System.out.print("Повторите ввод: ");
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        return number;
    }
}
