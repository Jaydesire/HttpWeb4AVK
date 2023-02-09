package src.main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
//    private final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private final ExecutorService executorService = Executors.newFixedThreadPool(64);
    private static final int PORT = 9999;
    private static final int LIMIT_CONNECTIONS = 64;
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();

    public Server() {

    }

    public void start() {
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                final var socket = serverSocket.accept();
                executorService.submit(() -> connect(socket));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void connect(Socket socket) {
        try (
                socket;
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            final String[] parts = getResponsePartsFromRequest(in);

            //Метод - 1-ая часть запроса
            final var method = parts[0];

            //Query - 2-ая часть запроса
            final var query = parts[1];

            //Путь - всё, что до "?"
            final String path;
            if (query.contains("?")) {
                path = query.substring(0,
                        query.indexOf('?'));
            } else {
                path = query;
            }

            //Ищем в handlers по ключу наш метод
            var methodMap = handlers.get(method);
            if (methodMap == null) {
                System.out.println("404: methodMap == null");
                return;
            }

            //Ищем в methodMap по ключу наш путь
            var handler = methodMap.get(path);
            if (handler == null) {
                System.out.println("404: handler == null");
                return;
            }

            //TODO - если путь нашли, вызываем handler.handle();



//            out.write((
//                    "HTTP/1.1 200 OK\r\n" +
//                            "Content-Length: 0\r\n" +
//                            "Connection: close\r\n" +
//                            "\r\n"
//            ).getBytes());
//            out.flush();
//
//            if (parts.length != 3) {
//                // just close socket
//            } else if (!validPaths.contains(path)) {
//                errorResponse(out);
//            } else {
//                getTrueResponse(out, path);
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getResponsePartsFromRequest(BufferedReader in) throws IOException {
        final var requestLine = in.readLine();
        final var parts = requestLine.split(" ");
        return parts;
    }
//
//    private void errorResponse(BufferedOutputStream out) throws IOException {
//        String response = "HTTP/1.1 404 Not Found\r\n" +
//                "Content-Length: 0\r\n" +
//                "Connection: close\r\n" +
//                "\r\n";
//        out.write(response.getBytes());
//        out.flush();
//    }
//
//    private String okResponse(String mimeType, long length) {
//        return "HTTP/1.1 200 OK\r\n" +
//                "Content-Length: " + length + "\r\n" +
//                "Connection: close\r\n" +
//                "\r\n";
//
//    }
//
//    private void getTrueResponse(BufferedOutputStream out, String path) throws IOException {
//        var filePath = Path.of(".", "public", path);
//        var mimeType = Files.probeContentType(filePath);
//        // special case for classic
//        if (path.equals("/classic.html")) {
//            final var template = Files.readString(filePath);
//            final var content = template.replace(
//                    "{time}",
//                    LocalDateTime.now().toString()
//            ).getBytes();
//            out.write(okResponse(mimeType, content.length).getBytes());
//            out.write(content);
//        } else {
//            final var length = Files.size(filePath);
//            out.write(okResponse(mimeType, length).getBytes());
//            Files.copy(filePath, out);
//        }
//        out.flush();
//    }
//
//    public void listen(int i) {
//    }

    public static void addHandler(String method, String path, src.main.Handler handler) {
        handlers.putIfAbsent(method, new ConcurrentHashMap<>());

        var methodMap = handlers.get(method);
        methodMap.put(path, handler);
    }

}