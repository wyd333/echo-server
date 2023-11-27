package network;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpEchoClient {
    private DatagramSocket socket = null;
    private String serverIP;
    private int serverPort;

    /**
     * 客户端不用显示指定端口
     * 系统自动分配空闲端口
     * 客户端启动需要知道服务器在哪里
     */
    public UdpEchoClient(String serverIP, int serverPort) throws SocketException {
        this.socket = new DatagramSocket();
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        //通过这个客户端可以和服务器进行多次交互
        Scanner reader = new Scanner(System.in);
        while(true) {
            //1-先从控制台读取一个字符串过来
            //  打印一个提示符提示输入
            System.out.print("->");
            String request = reader.next();
            //2-把字符串构造出UDP-packet，并进行发送
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),
                    request.getBytes().length,InetAddress.getByName(serverIP), serverPort);
            socket.send(requestPacket);
            //3-客户端尝试读取服务器返回的响应
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096], 4096);
            socket.receive(responsePacket);
            //4-把响应数据转换成String，显示出来
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.printf("req: %s, resp: %s\n", request, response);
        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient udpEchoClient = new UdpEchoClient("127.0.0.1", 9090);
        udpEchoClient.start();
    }
}
