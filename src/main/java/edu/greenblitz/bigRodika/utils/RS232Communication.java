package edu.greenblitz.bigRodika.utils;

import edu.wpi.first.wpilibj.SerialPort;

import java.nio.ByteBuffer;

public class RS232Communication {

    private static RS232Communication instance;
    private SerialPort channel;
    private static final int ROBORIO_MAX_BAUD_RATE = 115200;
    private static final int RASPBERRY_MAX_BAUD_RATE = 15625000;
    private static final int RESPONSE_WAIT_TIME = 5;
    private static final int DEFAULT_TIMEOUT = 20;

    private byte[] pingReq;
    private byte[] getReq;

    public enum REQUESTS {
        PING(1),
        GET(Double.BYTES*3),
        SET_ALGO(1);

        public final int responseSize;
        REQUESTS(int rSize){
            responseSize = rSize;
        }
    }

    private RS232Communication(){
        channel = new SerialPort(Math.min(
                ROBORIO_MAX_BAUD_RATE, RASPBERRY_MAX_BAUD_RATE
        ), SerialPort.Port.kOnboard);
        channel.disableTermination();

        pingReq = new byte[1];
        pingReq[0] = (byte) REQUESTS.PING.ordinal();

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
        return sendRequest(pingReq).length > 0;
    }

    public VisionLocation get(){
        byte[] resp = sendRequest(getReq);
        if (resp.length == 0){
            return null;
        }
        ByteBuffer buff = ByteBuffer.wrap(resp);
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

}
