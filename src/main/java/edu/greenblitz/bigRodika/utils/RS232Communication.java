package edu.greenblitz.bigRodika.utils;

import edu.greenblitz.bigRodika.subsystems.GBSubsystem;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class RS232Communication extends GBSubsystem {

    private static RS232Communication instance;
    private SerialPort channel;
    private static final int BAUD_RATE = 115200;
    private static final int RESPONSE_WAIT_TIME = 5;
    private static final int DEFAULT_TIMEOUT = 20;

    private Random rn = new Random();
    private static final int PING_PAYLOAD = 50;
    private boolean ping = false;

    private byte[] pingReq;
    private byte[] getReq;

    public enum REQUESTS {
        PING(PING_PAYLOAD),
        GET(1 + Double.BYTES*3),
        SET_ALGO(1);

        public final int responseSize;
        REQUESTS(int rSize){
            responseSize = rSize;
        }
    }

    private RS232Communication(){
        super();
        channel = new SerialPort(BAUD_RATE, SerialPort.Port.kMXP);
        channel.disableTermination();

        getReq = new byte[1];
        getReq[0] = (byte) REQUESTS.GET.ordinal();
    }

    public static RS232Communication getInstance(){
        if (instance == null){
            instance = new RS232Communication();
        }
        return instance;
    }

    private byte[] getResponse(int size){
        return getResponse(size, DEFAULT_TIMEOUT);
    }

    private byte[] getResponse(int size, long timeout){
        long tStart = System.currentTimeMillis();
        while (channel.getBytesReceived() < size){
            try {
                Thread.sleep(RESPONSE_WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - tStart > timeout){
                return new byte[0];
            }
        }
        return channel.read(size);
    }

    private byte[] sendRequest(byte[] request){
        channel.write(request, request.length);
        channel.flush();
        REQUESTS requestType = REQUESTS.values()[request[0]];
        return getResponse(requestType.responseSize);
    }

    public boolean ping(){
        return ping;
    }

    public boolean checkConnection(){
        byte[] toSend = new byte[PING_PAYLOAD + 1];
        rn.nextBytes(toSend);
        toSend[0] = (byte) REQUESTS.PING.ordinal();
        byte[] resp = sendRequest(toSend);
        if (resp.length != PING_PAYLOAD) {
            ping = false;
            return false;
        }

        ping = true;

        for (int i = 0; i < resp.length; i++){
            if (resp[i] != toSend[i + 1]){
                return false;
            }
        }
        return true;
    }

    public VisionLocation get(){
        byte[] resp = sendRequest(getReq);
        if (resp.length == 0 || resp[0] == 0){
            return null;
        }

        ByteBuffer buff = ByteBuffer.wrap(resp);
        buff.get();
        double x = buff.getDouble();
        double y = buff.getDouble();
        double z = buff.getDouble();
        return new VisionLocation(x, y, z);
    }

    public boolean setAlgo(VisionMaster.Algorithm algo){
        byte[] algoReq = new byte[2];
        algoReq[0] = (byte) REQUESTS.SET_ALGO.ordinal();
        algoReq[1] = (byte) algo.ordinal();
        byte[] response = sendRequest(algoReq);
        return response.length > 0 && response[0] == 1;
    }

    private long lastPing = 0;
    private static final long BETWEEN_PINGS = 200;

    @Override
    public void periodic() {
        if (System.currentTimeMillis() - lastPing > BETWEEN_PINGS) {
            SmartDashboard.putBoolean("RS232 connection good", checkConnection());
            SmartDashboard.putBoolean("RS232 ping", ping());
            lastPing = System.currentTimeMillis();
        }
    }
}
