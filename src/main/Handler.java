package src.main;

//import com.sun.net.httpserver.Request;

import java.io.BufferedOutputStream;


public interface Handler {
    void handle(Request request, BufferedOutputStream outputStream);
}
