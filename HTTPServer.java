import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HTTPServer {

    public static void main(String[] args) throws IOException{

        HTTPServer main = new HTTPServer();
        main.Server();
    }

    public void Server() throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        try{

            serverSocket.setReuseAddress(true);
            System.out.println("Server Connected!");

            while(true){
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                serverSocket.close();
            }

        }
    }

    public void handleClient(Socket socket) throws IOException{
        Socket clientSocket = socket;

        String EOL = "\r\n\r\n";
        String OK = "HTTP/1.1 200 Ok" + EOL;
        String ERROR = "HTTP/1.1 404 Error Not Found" + EOL;

        List<String> headers = getHeaders(clientSocket.getInputStream());
        if(headers.size() == 0){

        }else{

            String[] request = headers.get(0).split(" ");
            if(request[0].equals("GET")){
                if(request[1].equals("/")){
                    System.out.println("200 OK NO ERROR");
                    clientSocket.getOutputStream().write(OK.getBytes());
                    clientSocket.getOutputStream().write("200 OK\r\n".getBytes());

                }else if(request[1].startsWith("/echo")){
                    String headerPath = request[1].replace("/echo/", "");
                    int pathLength = headerPath.length();

                    clientSocket.getOutputStream().write(OK.getBytes());
                    clientSocket.getOutputStream().write("HTTP/1.1 200 OK \r\n".getBytes());
                    clientSocket.getOutputStream().write("Content-Type: text/plain\r\n".getBytes());
                    clientSocket.getOutputStream().write(("Content-Length: " + pathLength + "\r\n").getBytes());
                    clientSocket.getOutputStream().write((headerPath + "\r\n").getBytes());

                }else if(request[1].startsWith("/user-agent")){
                    String userAgent = headers.get(2).replace("User-Agent: ", "");
                    int agentLength = userAgent.length();

                    clientSocket.getOutputStream().write(OK.getBytes());
                    clientSocket.getOutputStream().write("HTTP/1.1 200 OK\r\n".getBytes());
                    clientSocket.getOutputStream().write("Content-Type: text/plain\r\n".getBytes());
                    clientSocket.getOutputStream().write(("Content-Length: " + agentLength + "\r\n").getBytes());
                    clientSocket.getOutputStream().write((userAgent + "\r\n").getBytes());


                }else{
                    System.out.println("404 ERROR NOT FOUND");
                    clientSocket.getOutputStream().write(ERROR.getBytes());
                    clientSocket.getOutputStream().write("404 ERROR NOT FOUND \r\n".getBytes());
                }
                clientSocket.close();
            }
        }
    }

    public List<String> getHeaders(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        List<String> userHeader = new ArrayList<>();

        while(reader.ready()){
            String line = reader.readLine();
            if(!"".equals(line)){
                userHeader.add(line);
            }

        }
        return userHeader;
    }
}
