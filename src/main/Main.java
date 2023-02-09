package src.main;

//import com.sun.net.httpserver.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class Main {
    public static void main(String[] args) {

        final var server = new Server();


        server.addHandler("GET", "/index.html", new src.main.Handler() {

            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
                System.out.println("Reguest = " + request);

                try {
                    responseStream.write("Test OK".getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        server.addHandler("POST", "/index.html", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                // TODO: handlers code
                System.out.println("Reguest = " + request);

//                try {
//                    responseStream.write(("HTTP/1.1 200 OK\r\n" + "Connection: close\r\n" +  "\r\n").getBytes());
//                    responseStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
//        server.listen(9999);
        server.start();
    }
}