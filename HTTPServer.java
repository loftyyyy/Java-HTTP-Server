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
        String EOL = "\r\n\r\n";
        String OK = "HTTP/1.1 200 Ok" + EOL;
        String ERROR = "HTTP/1.1 404 Error Not Found" + EOL;


        ServerSocket serverSocket = new ServerSocket(8080);
        serverSocket.setReuseAddress(true);
        Socket clientSocket = serverSocket.accept();
        System.out.println("Server Connected!");

        List<String> headers = getHeaders(clientSocket.getInputStream());
        String[] request = headers.get(0).split(" ");
        if(request[0].equals("GET")){
            if(request[1].equals("/")){
                System.out.println("200 OK NO ERROR");
                clientSocket.getOutputStream().write(OK.getBytes());
                clientSocket.getOutputStream().write("200 OK\r\n".getBytes());

            }else{
               System.out.println("404 ERROR NOT FOUND");
               clientSocket.getOutputStream().write(ERROR.getBytes());
               clientSocket.getOutputStream().write("404 ERROR NOT FOUND \r\n".getBytes());
            }
            clientSocket.close();
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
