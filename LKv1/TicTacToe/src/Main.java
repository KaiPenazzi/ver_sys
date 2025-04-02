import java.util.Scanner;
public class Main {
    public static void main(String[] args) {


        Scanner s = new Scanner(System.in);

//        System.out.println("gib einen Username ein");
//        String nickname = s.nextLine();

//        System.out.println("gib deinen Port an");
//        int port = s.nextInt();
String nickname = "Leon";

        //UDP_Com.recv_UDP();
        //UDP_Com.send_UDP();
        Board b = new Board();
        Game g = new Game(b, null);


    MessageData t = new ActionMessageData(1,2,"alarm");
        Message m = new Message("action",t );

    g.getGameBoard().printBoardTest();
        System.out.println("gib x an: ");
        int x = s.nextInt();
        System.out.println("gib y an: ");
        int y = s.nextInt();
    //3 wegen fieldsize
        while(x < b.FIELDSIZE && y < b.FIELDSIZE){
        g.move(x, y, nickname);
        System.out.println("****************************************************");
        g.getGameBoard().printBoardTest();
        System.out.println("gib x an: ");
        x = s.nextInt();
        System.out.println("gib y an: ");
        y = s.nextInt();
    }

    }
}