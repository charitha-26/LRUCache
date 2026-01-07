import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class LRUCacheApp {
    private static LRUCache cache;

    public static void main(String[] args) throws IOException {
        cache = new LRUCache(5); // Initialize with capacity of 5
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // GET /get?key=
        server.createContext("/get", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String key = extractParam(query, "key");
                
                int value = cache.get(Integer.parseInt(key));
                String response = "{\"value\":" + value + "}";
                
                sendResponse(exchange, response);
            }
        });
        
        server.createContext("/put", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String keyStr = extractParam(query, "key");
                String valueStr = extractParam(query, "value");
                
                int key = Integer.parseInt(keyStr);
                int value = Integer.parseInt(valueStr);
                
                cache.put(key, value);
                String response = "{\"status\":\"success\",\"message\":\"Key-value pair added\"}";
                
                sendResponse(exchange, response);
            }
        });
        server.createContext("/display", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = cache.display();
                sendResponse(exchange, response);
            }
        });
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on http://localhost:8080");
    }
    
    private static String extractParam(String query, String paramName) {
        if (query == null) return "";
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals(paramName)) {
                return keyValue.length > 1 ? keyValue[1] : "";
            }
        }
        return "";
    }
    
    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
