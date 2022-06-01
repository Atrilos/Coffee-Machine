package machine;

import java.util.Scanner;

enum MachineState {
    PENDING, CHOICE, BUY, REFILL, COLLECTION, REMAINING, EXIT
}

class CoffeeMachine {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CoffeeMachine ESPRESSO = new CoffeeMachine(new int[]{250, 0, 16, 1, 4});
    private static final CoffeeMachine LATTE = new CoffeeMachine(new int[]{350, 75, 20, 1, 7});
    private static final CoffeeMachine CAPPUCCINO = new CoffeeMachine(new int[]{200, 100, 12, 1, 6});
    private int water, milk, beans, cups, money;
    private int choice;
    private MachineState state = MachineState.PENDING;

    public CoffeeMachine(int[] stats) {
        this.water = stats[0];
        this.milk = stats[1];
        this.beans = stats[2];
        this.cups = stats[3];
        this.money = stats[4];
    }

    public void appRun() {
        while (!state.equals(MachineState.EXIT)) {
            switch (state) {
                case CHOICE:
                    this.coffeeChoice();
                    break;
                case BUY:
                    this.makeCoffee();
                    break;
                case REFILL:
                    this.coffeeFill();
                    break;
                case PENDING:
                    this.userInput();
                    break;
                case REMAINING:
                    this.displayState();
                    break;
                case COLLECTION:
                    this.takeMoney();
                    break;
            }
        }
    }

    private void userInput() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
        String command = scanner.next();
        switch (command) {
            case "buy":
                state = MachineState.CHOICE;
                break;
            case "fill":
                state = MachineState.REFILL;
                break;
            case "take":
                state = MachineState.COLLECTION;
                break;
            case "remaining":
                state = MachineState.REMAINING;
                break;
            case "exit":
                state = MachineState.EXIT;
                break;
            default:
                break;
        }
    }

    private void coffeeChoice() {
        System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
        switch (scanner.next()) {
            case "1":
                state = MachineState.BUY;
                choice = 1;
                break;
            case "2":
                state = MachineState.BUY;
                choice = 2;
                break;
            case "3":
                state = MachineState.BUY;
                choice = 3;
                break;
            case "back":
            default:
                state = MachineState.PENDING;
                break;
        }
    }

    private void makeCoffee() {
        CoffeeMachine temp = new CoffeeMachine(new int[]{0, 0, 0, 0, 0});
        switch (choice) {
            case 1:
                temp = ESPRESSO;
                break;
            case 2:
                temp = LATTE;
                break;
            case 3:
                temp = CAPPUCCINO;
                break;
        }
        if (checkPrintError(checkRemaining(temp))) {
            this.water -= temp.water;
            this.milk -= temp.milk;
            this.beans -= temp.beans;
            this.cups -= temp.cups;
            this.money += temp.money;
        }
    }

    private int checkRemaining(CoffeeMachine coffee) {
        if (this.water - coffee.water < 0) return 1;
        if (this.milk - coffee.milk < 0) return 2;
        if (this.beans - coffee.beans < 0) return 3;
        if (this.cups - coffee.cups < 0) return 4;
        return 0;
    }

    private boolean checkPrintError(int errCode) {
        state = MachineState.PENDING;
        switch (errCode) {
            case 1:
                System.out.println("Sorry, not enough water!\n");
                return false;
            case 2:
                System.out.println("Sorry, not enough milk!\n");
                return false;
            case 3:
                System.out.println("Sorry, not enough coffee beans!\n");
                return false;
            case 4:
                System.out.println("Sorry, not enough disposable cups!\n");
                return false;
            case 0:
                System.out.println("I have enough resources, making you a coffee!\n");
                return true;
        }
        return true;
    }

    private void coffeeFill() {
        System.out.println("\nWrite how many ml of water you want to add:");
        this.water += scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add:");
        this.milk += scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add:");
        this.beans += scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee you want to add:");
        this.cups += scanner.nextInt();
        System.out.println();
        state = MachineState.PENDING;
    }

    private void takeMoney() {
        System.out.printf("%nI gave you $%d%n%n", this.money);
        this.money = 0;
        state = MachineState.PENDING;
    }

    private void displayState() {
        System.out.println("\nThe coffee machine has:");
        System.out.printf("%d ml of water%n", this.water);
        System.out.printf("%d ml of milk%n", this.milk);
        System.out.printf("%d g of coffee beans%n", this.beans);
        System.out.printf("%d disposable cups%n", this.cups);
        System.out.printf("$%d of money%n%n", this.money);
        state = MachineState.PENDING;
    }
}

class CoffeeMachineInterface {

    public static void main(String[] args) {
        var coffeeMachine = new CoffeeMachine(new int[]{400, 540, 120, 9, 550});
        coffeeMachine.appRun();
    }

}
