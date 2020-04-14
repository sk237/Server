package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void serverSetting() {
        try {
            serverSocket = new ServerSocket(8888);
            clientSocket = serverSocket.accept();
            System.out.println("client connect");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAll() {
        try {
            serverSocket.close();
            clientSocket.close();
            dataInputStream.close();
            dataOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void streamSetting() {
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dataRecv() {
        new Thread(new Runnable() {
            boolean isThread = true;

            @Override
            public void run() {
                while (isThread) {
                    try {
                        String recvData = dataInputStream.readUTF();
                        if (recvData.equals("/quit")) {
                            isThread = false;
                        } else {
                            System.out.println("client : " + recvData);
                        }
                    } catch (Exception e) {
                    }
                }
                closeAll();
                System.out.println("close all");
            }
        }).start();
    }

    public void dataSend() {
        new Thread(new Runnable() {
            Scanner in = new Scanner(System.in);
            boolean isThread = true;
            @Override
            public void run() {
                while (isThread) {
                    try {
                        String sendData = in.nextLine();
                        if (sendData.equals("/quit")) isThread = false;
                        else dataOutputStream.writeUTF(sendData);

                    } catch (Exception e) {
                    }
                }

            }
        }).start();
    }

    public MainServer() {
        serverSetting();
        streamSetting();
        dataRecv();
        dataSend();
    }

    public static void main(String[] args) {
        new MainServer();
    }
}
