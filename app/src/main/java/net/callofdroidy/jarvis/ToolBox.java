package net.callofdroidy.jarvis;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by admin on 29/10/15.
 */
public class ToolBox {
    private static ToolBox instance;

    public static synchronized ToolBox getInstance(){
        if(instance == null)
            instance = new ToolBox();
        return instance;
    }

    // target IP broadcast address and MAC address, used to do "Wake On Lan"
    public void wakeOnLan(String ipBroadcast, String macAddress){
        new AsyncTask<String, String, String>(){
            @Override
            protected String doInBackground(String...params){
                new WakeOnLan().wakeTargetUp(params[0], params[1]);
                return null;
            }
        }.execute(ipBroadcast, macAddress);
    }

    private class WakeOnLan{
        private final int PORT = 9; //port 9 is the default port for computers receiving wake up packet

        //send a UDP Magic Packet request to wake target computer up
        public void wakeTargetUp(String ipBroadcast, String macAddress){
            try {
                byte[] macBytes = convertMacInBytes(macAddress);
                byte[] bytes = new byte[6 + 16 * macBytes.length];
                for (int i = 0; i < 6; i++) {
                    bytes[i] = (byte) 0xff;
                }
                for (int i = 6; i < bytes.length; i += macBytes.length)
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.length);

                InetAddress address = InetAddress.getByName(ipBroadcast);
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.close();
                Log.e("WakeOnLAN packet sent", "success");
            }
            catch (Exception e) {
                Log.e("WakeOnLAN packet sent", "failed, error: " + e.toString());
            }
        }

        private byte[] convertMacInBytes(String macStr) throws IllegalArgumentException {
            byte[] bytes = new byte[6];
            String[] hex = macStr.split("(\\:|\\-)");
            if (hex.length != 6)
                throw new IllegalArgumentException("Invalid MAC address.");
            try {
                for (int i = 0; i < 6; i++)
                    bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid hex digit in MAC address.");
            }
            return bytes;
        }
    }
}
