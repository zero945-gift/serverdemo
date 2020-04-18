import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);

        System.out.println("服务器已就绪");
        System.out.println("服务端端信息"+ "ip" + serverSocket.getInetAddress() + "port" + serverSocket.getLocalPort() );


        for (;;){
            Socket client = serverSocket.accept();
            ClientHandle clientHandle = new ClientHandle(client);
            clientHandle.start();
        }

    }

    private static class ClientHandle extends Thread{
        private Socket socket;
        private boolean flag = true;

        ClientHandle(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接" + "ip" + socket.getInetAddress() + "port"+ socket.getPort());
            try{
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                BufferedReader socketInput = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                do {
                    String str = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(str)){
                        flag = false;
                        socketOutput.println("bye");
                    }else{
                        System.out.println(str);
                        socketOutput.println("回送" + str.length());
                    }

                }while(flag);
                socketInput.close();
                socketOutput.close();

            }catch(Exception e){
                System.out.println("连接断开异常");

            }finally {
                try{
                    socket.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            System.out.println("客户端退出" + "ip" + socket.getInetAddress() + "port" + socket.getPort());


        }



    }
}
