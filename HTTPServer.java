public class HTTPServer {

    public static void main(String[] args){

        HTTPServer main = new HTTPServer();
        main.Server();
    }

    public void Server(){
        String EOL = "\r\n";
        String OK = "HTTP/1.1 200 Ok" + EOL;
        String ERROR = "HTTP/1.1 404 Error Not Found" + EOL;



    }
}
