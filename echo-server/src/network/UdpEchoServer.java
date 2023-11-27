package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpEchoServer {
    /**
     * 1-需要先定义一个socket对象
     * 2-通过网络通信，必须使用socket对象
     */
    private DatagramSocket socket = null;

    /**
     * 绑定一个端口不一定能成功（如该端口已被其它进程占用）
     * 失败了则会抛出SocketException异常
     * 同一主机的同一端口，同时只能被一个进程绑定
     */
     public UdpEchoServer(int port) throws SocketException {
        //构造socket的同时，指定要绑定的端口
        socket = new DatagramSocket(port);
    }

    /**
     * 启动服务器的主逻辑
     */
    public void start() throws IOException {
        System.out.println("服务器启动！");
        while(true) {
            //每次循环要做的三件事情
            //1-读取请求并解析
            //  构造空饭盒
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096], 4096);
            //  食堂大妈往饭盒里盛饭
            //  饭从网卡上来
            socket.receive(requestPacket);
            //  为了方便处理请求，把数据报转成String
            String request = new String(requestPacket.getData(), 0, requestPacket.getLength());
            //2-根据请求计算响应
            String response = process(request);
            //3-把响应结果写回响应
            //  根据response字符串构造一个 DatagramPacket
            //  和请求packet不同，此处构造响应的时候需要指定这个包要发给谁
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),
                    response.getBytes().length,
                    //requestPacket是从客户端收来的，所以getSocketAddress()会得到客户端的ip和端口号
                    requestPacket.getSocketAddress());
            socket.send(responsePacket);
            System.out.printf("[%s:%d] req: %s, resp: %s\n", requestPacket.getAddress().toString(),
                    requestPacket.getPort(),request,response);
        }
    }

    /**
     * 根据请求计算响应
     * 后续有具体业务了，可以在process中写具体业务的实现代码，根据需要重新构造响应
     */
    private String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        UdpEchoServer udpEchoServer = new UdpEchoServer(9090);
        udpEchoServer.start();
    }
}
