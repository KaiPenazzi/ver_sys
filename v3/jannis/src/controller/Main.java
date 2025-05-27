package controller;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        Controller controller = new Controller("127.0.0.1:1000", "C:\\Semester 6 Git Repos\\Verteilte Systeme\\ver_sys\\v3\\jannis\\src\\controller\\mynet.json");
        controller.getConfigNodes();
    }
}
