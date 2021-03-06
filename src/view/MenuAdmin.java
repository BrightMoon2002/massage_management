package view;

import control.*;
import login.Account;
import login.AccountFactory;
import login.AccountType;
import model.pay.Bill;
import model.idol.Idol;
import model.idol.IdolFactory;
import model.idol.IdolType;
import model.room.Room;
import model.room.RoomFactory;
import model.room.RoomType;
import storage.*;


import java.time.LocalTime;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MenuAdmin {
    public MenuAdmin() {
    }

    private static MenuLogin menuLogin = new MenuLogin();

    public static void menuAdmin() {
        Pattern pattern = Pattern.compile("\\d{1,3}[-]\\d{1,3}[-]\\d{1,3}");
        Pattern pattern1 =  Pattern.compile("^([a-zA-Z0-9]{4,8})$");
        IdolManager idolManager = new IdolManager();
        RoomManager roomManager = new RoomManager();
        CustomerManager customerManager = new CustomerManager();
        BillManager billManager = new BillManager();
        AccountManager userManager = new AccountManager();

        LoginManager loginManager = new LoginManager();
        FileManagerLogin fileManagerLogin = FileManagerLogin.getInstance();
        loginManager.setFileManagerLogin(fileManagerLogin);
        List<Account> loginList = fileManagerLogin.readList();
        loginManager.setAccountList(loginList);

        FileManagerBill fileManagerBill = FileManagerBill.getInstance();
        billManager.setFileManagerBill(fileManagerBill);
        List<Bill> billList = fileManagerBill.readList();
        billManager.setBillList(billList);

        FileManagerAccount fileManagerUser = FileManagerAccount.getInstance();
        userManager.setFileManagerUser(fileManagerUser);
        List<Account> accountList = fileManagerUser.readList();
        userManager.setUserList(accountList);


        FileManagerIdol fileManagerIdol = FileManagerIdol.getInstance();
        idolManager.setFileManagerIdol(fileManagerIdol);
        List<Idol> idolList = fileManagerIdol.readList();
        idolManager.setIdolList(idolList);

        FileManagerRoom fileManagerRoom = FileManagerRoom.getInstance();
        roomManager.setFileManagerRoom(fileManagerRoom);
        List<Room> roomList = fileManagerRoom.readList();
        roomManager.setRoomList(roomList);


        while (true) {
            Account account = userManager.searchByName(loginManager.getAccountList().get(0).getUsername());
            System.out.println("|-----------------------------------|");
            System.out.println("|            Menu of Admin:         |");
            System.out.println("|1.         Change password:        |");
            System.out.println("|2.   Change information of account |");
            System.out.println("|3.          Add new Idol           |");
            System.out.println("|4.          Add new Room           |");
            System.out.println("|5.        Display list idol        |");
            System.out.println("|6.        Display list room        |");
            System.out.println("|7.        Display list Bill        |");
            System.out.println("|8.           Check Bill            |");
            System.out.println("|9.       Remove User By Id         |");
            System.out.println("|10.      Remove Idol by Id         |");
            System.out.println("|11.      Remove Room by Id         |");
            System.out.println("|12.      Creat new Member          |");
            System.out.println("|13.      Show list A idol          |");
            System.out.println("|14.      Show list B idol          |");
            System.out.println("|15.      Show list C idol          |");
            System.out.println("|0.             Back                |");
            System.out.println("|-----------------------------------|");
            Scanner inputChoice = new Scanner(System.in);
            int choice = inputChoice.nextInt();
            switch (choice) {
                case 1:
                    changePasswordOfAccount(userManager, accountList, account);
                    break;
                case 2:
                    userManager.updateByIndex(accountList.indexOf(account), creatNewAccount(userManager,pattern1));
                    break;
                case 3:
                    creatNewIdol(idolManager, pattern);

                    break;
                case 4:
                    creatNewRoom(roomManager);
                    break;
                case 5:
                    idolManager.showAllList();
                    break;
                case 6:
                    roomManager.showAllList();
                    break;
                case 7:
                    billManager.showAllList();
                    break;
                case 8:
                    checkoutBillByID(idolManager, roomManager, billManager, userManager, billList);
                    break;
                case 9:
                    removeById(userManager);
                    break;
                case 10:
                    removeIdolById(idolManager, userManager);
                    break;
                case 11:
                    removeRoomById(userManager);
                    break;
                case 12:
                    userManager.saveList(creatNewAccount(userManager, pattern1));
                    break;
                case 13:
                    idolManager.showAIdolList(idolList);
                    break;
                case 14:
                    idolManager.showBIdolList(idolList);
                    break;
                case 15:
                    idolManager.showCIdolList(idolList);
                    break;
                case 0:
                    menuLogin.loginMenu();

            }
        }

    }

    private static void changePasswordOfAccount(AccountManager userManager, List<Account> accountList, Account account) {
        System.out.println("Enter the password you want to change:");
        Scanner inputPassword = new Scanner(System.in);
        String password = inputPassword.nextLine();
        userManager.changePassword(accountList.indexOf(account), password);
    }

    private static void removeRoomById(AccountManager userManager) {
        System.out.println("Enter the code of room");
        Scanner inputCode1 = new Scanner(System.in);
        String newCode = inputCode1.nextLine();
        int indexRoom = userManager.searchIndexById(newCode);
        if (indexRoom != -1) {
            userManager.removeByIndex(indexRoom);
        } else {
            System.err.println("No room have this code");
        }
    }

    private static void removeIdolById(IdolManager idolManager, AccountManager userManager) {
        System.out.println("Enter the id of idol");
        Scanner newInputId = new Scanner(System.in);
        String newId1 = newInputId.nextLine();
        int index1 = idolManager.searchIndexById(newId1);
        if (index1 != -1) {
            idolManager.removeByIndex(index1);
        } else {
            System.err.println("No idol have this id");
        }
    }

    private static void removeById(AccountManager userManager) {
        System.out.println("Enter the id of user");
        Scanner inputID = new Scanner(System.in);
        String newId = inputID.nextLine();
        int index = userManager.searchIndexById(newId);
        if (index != -1) {
            userManager.removeByIndex(index);
        } else {
            System.err.println("No user have this id");
        }
    }

    private static void checkoutBillByID(IdolManager idolManager, RoomManager roomManager, BillManager billManager, AccountManager userManager, List<Bill> billList) {
        System.out.println("Enter the id of Customer you want to check bill to pay today:");
        Scanner inputIdOfCustomer = new Scanner(System.in);
        String idOfCustomer = inputIdOfCustomer.nextLine();
        Bill bill = billManager.searchByName(userManager.searchById(idOfCustomer).getUsername());
        if (bill != null) {
            bill.setCheckout(LocalTime.now());
            System.out.println("Enter the code of Bill");
            Scanner inputCode1 = new Scanner(System.in);
            String code1 = inputCode1.nextLine();
            bill.setCode(code1);
            billManager.updateByIndex(billList.indexOf(bill), bill);
            System.out.println("the money  need to pay is: " + billManager.getBillList().get(billManager.getBillList().indexOf(bill)).getMoneyToPay());
            Idol idol1 = bill.getIdol();
            int index = idolManager.searchIndexByIdol(idol1);
            idol1.setStatus(false);
            idol1.setCountChecker(idol1.getCountChecker()+1);
            if (index != -1) {
                idolManager.updateByIndex(index, idol1);
            } else {
                System.err.println("cant find this idol, index =-1");
            }
            Room room1 = bill.getRoom();
            int index1 = roomManager.searchIndexByRoom(room1);
            room1.setStatus(false);
            if (index != -1) {
                roomManager.updateByIndex(index1, room1);
            } else {
                System.err.println("cant find this idol, index =-1");
            }
        }
    }

    private static void creatNewRoom(RoomManager roomManager) {
        System.out.println("Enter the type of Room(VIP, DELUXE, KING");
        Scanner inputTypeRoom = new Scanner(System.in);
        String typeRoom = inputTypeRoom.nextLine();
        System.out.println("Enter the code of Room");
        String code;
        do {
            Scanner inputCode = new Scanner(System.in);
            code = inputCode.nextLine();
        } while (roomManager.searchById(code) != null);
        Room room = new Room();
        switch (typeRoom) {
            case "VIP":
                room = RoomFactory.getRoom(RoomType.VIPROOM, code);
                roomManager.saveList(room);

                break;
            case "DELUXE":
                room = RoomFactory.getRoom(RoomType.DELUXERROOM, code);
                roomManager.saveList(room);
                break;
            case "KING":
                room = RoomFactory.getRoom(RoomType.KINGROOM, code);
                roomManager.saveList(room);
                break;
            default:
                System.out.println("NO TYPE ROOM LIKE THAT");
                break;
        }
    }

    private static void creatNewIdol(IdolManager idolManager, Pattern pattern) {
        System.out.println("Enter the type of Idol (A, B or C):");
        Scanner inputType = new Scanner(System.in);
        String type = inputType.nextLine();
        System.out.println("Enter the stage name of idol:");
        Scanner inputStageName = new Scanner(System.in);
        String stageName = inputStageName.nextLine();
        String id;
        do {
            System.out.println("Enter the id of idol:");
            Scanner inputId = new Scanner(System.in);
            id = inputId.nextLine();
        } while (idolManager.searchById(id) != null);

        System.out.println("Enter the dob of idol:");
        Scanner inputDOB = new Scanner(System.in);
        String dOB = inputDOB.nextLine();
        System.out.println("Enter the idol height");
        double height = checkHeight(0, 200);

        String body;
        do {
            System.out.println("Enter the Body Measurements");
            Scanner inputBody = new Scanner(System.in);
            body = inputBody.nextLine();
        } while(!pattern.matcher(body).matches());
        Idol idol;
        if (type.equals("A")) {
            idol = IdolFactory.getIdol(IdolType.IDOLA, stageName, dOB, height, body, id);
            idolManager.saveList(idol);
        } else if (type.equals("B")) {
            idol = IdolFactory.getIdol(IdolType.IDOLB, stageName, dOB, height, body, id);
            idolManager.saveList(idol);
        } else if (type.equals("C")) {
            idol = IdolFactory.getIdol(IdolType.IDOLC, stageName, dOB, height, body, id);
            idolManager.saveList(idol);
        } else {
            System.err.println("No type of Idol is support");
        }
    }

    private static Account creatNewAccount(AccountManager accountManager, Pattern pattern) {
        String userName;
        do {
            System.out.println("Enter the new username: ");
            Scanner inputNewUserName = new Scanner(System.in);
            userName = inputNewUserName.nextLine();
        } while (accountManager.searchById(userName) != null || !pattern.matcher(userName).matches() || accountManager.searchByName(userName) != null);
        String newPassword;
        do {
            System.out.println("Enter the new password: ");
            Scanner inputNewPassword = new Scanner(System.in);
            newPassword = inputNewPassword.nextLine();
        } while(!pattern.matcher(newPassword).matches());

        String newId;
        do {
            System.out.println("Enter the new id: ");
            Scanner inputNewId = new Scanner(System.in);
            newId = inputNewId.nextLine();
        } while (accountManager.searchById(newId) != null);
        System.out.println("Enter the new fullName");
        Scanner inputNewName = new Scanner(System.in);
        String fullName = inputNewName.nextLine();
        Account newUser = AccountFactory.getAccount(AccountType.CUSTOMER, userName, newPassword, newId, fullName);
        return newUser;
    }


    public static double checkHeight(double min, double max) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                double n = Double.parseDouble(sc.nextLine().trim());
                if (n < min || n > max) {
                    throw new NumberFormatException();
                }
                return n;
            } catch (NumberFormatException e) {
                System.err.println("Please input a integer in blank");
            }
        }
    }
}