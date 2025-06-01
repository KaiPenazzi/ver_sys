package controller;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        Controller controller = new Controller("127.0.0.1:1000", "mynet.json");
        controller.start();
        //controller.stop();
    }
}
