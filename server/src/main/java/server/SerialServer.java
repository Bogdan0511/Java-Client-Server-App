package server;

import interfaces.IService;
import networking.object_protocol.ClientObjectWorker;
import networking.utils.ConcurrentServer;

import java.net.Socket;

public class SerialServer extends ConcurrentServer {
    private IService server;

    public SerialServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("SerialServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(server, client);
        return new Thread(worker);
    }
}
