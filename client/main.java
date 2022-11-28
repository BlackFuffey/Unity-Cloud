import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Main {
    static Socket socketClient;
    static InputStream inputStream;
    static OutputStream outputStream;
    public static void main(String[] args){
        try {
            System.out.println("Running Unity Cloud Client");
            System.out.print("Starting up...");
            Socket client = new Socket();
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader bw;
            String str;
            File fl;
            System.out.println("Done");
            System.out.print("Connecting...");
            client.connect(new InetSocketAddress("demo",55), 60000);
            System.out.println("Connected");
            final InputStream is = client.getInputStream();
            final OutputStream os = client.getOutputStream();
            socketClient = client;
            inputStream = is;
            outputStream = os;
            for (;;){
                System.out.println("Login");
                System.out.print("Username: ");
                String username = keyboard.readLine();
                System.out.print("Password: ");
                String password = keyboard.readLine();
                System.out.print("Logging in...");
                byte[] passcode = MessageDigest.getInstance("SHA3-256").digest(password.getBytes());
                os.write(username.getBytes());
                os.write(passcode);
                os.write(MessageDigest.getInstance("SHA3-256").digest("".getBytes()));
                int user = is.read();
                if (user == 0){
                    System.out.println("error");
                    System.out.println("Access denied: Your client is outdated, ask the owner for the newest version");
                } if (user == 1){
                    System.out.println("error");
                    System.out.println("Access denied: Username or password is incorrect");
                } else {
                    System.out.println("done");
                    System.out.println("Welcome, " + username);
                    fr:for (;;) {
                        System.out.println("Main menu");
                        System.out.println("=============================");
                        System.out.println("1.Upload a file");
                        System.out.println("2.Download a file");
                        System.out.println("3.Delete a file");
                        System.out.println("4.Share a file");
                        System.out.println("5.Check your receive box");
                        System.out.println("6.About Unity Cloud");
                        System.out.println("7.Exit Unity Cloud");
                        System.out.print("Option:");
                        sw:switch (keyboard.readLine()) {
                            case "1":
                                System.out.println("Which file do you want to upload?");
                                System.out.println("Type \"exit\" to return to the main menu");
                                for (;;){
                                    System.out.print("File Path:");
                                    String path = keyboard.readLine();
                                    if (path.equals("exit")){
                                        break sw;
                                    } else {
                                        fl = new File(path);
                                    System.out.print("Verifying file path...");
                                    if (fl.exists()) {
                                        if (fl.isFile()) {
                                            if (fl.canRead()){
                                                System.out.println("done");
                                                System.out.print("Uploading file....");
                                                os.write(1);
                                                upload(fl);
                                            } else {
                                                System.out.println("error");
                                                System.out.println("This file is not readable, try again with another file");
                                            }
                                        } else {
                                            System.out.println("error");
                                            System.out.println("Folder upload is not supported yet, try again with a file");
                                        }
                                        } else {
                                            System.out.println("error");
                                            System.out.println("This path does not exist, try again with the right path");
                                        }
                                    }
                                }
                            case "2":

                                break;
                            case "3":

                                break;
                            case "4":
                                System.out.println("Share and receive is still in development");
                                break;
                            case "5":
                                System.out.println("Share and receive is still in development");
                                break;
                            case "6":
                                System.out.println("\n===============================================================================================================");
                                System.out.println("Unity Cloud Client");
                                System.out.println("Version: a1.0");
                                System.out.println("Author: BlackFuffey");
                                System.out.println("Description: Unity Cloud is an open-source cloud drive program designed for top security, \neverything here is completely encrypted to prevent anyone from stealing your data.");
                                System.out.println("===============================================================================================================\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Some thing went wrong");
            e.printStackTrace();
        }
    }

    public static void upload(File file) throws Exception {
        FileInputStream fis;
        DataOutputStream dos;
        fis = new FileInputStream(file);
        dos = new DataOutputStream(outputStream);
        dos.writeUTF(file.getName());
        dos.flush();
        dos.writeLong(file.length());
        dos.flush();
        byte[] bytes = new byte[1024];
        int length = 0;
        long progress = 0;
        while((length = fis.read(bytes, 0, bytes.length)) != -1) {
            dos.write(bytes, 0, length);
            dos.flush();
            progress += length;
            if (100*progress/file.length() <= 10){
                System.out.print("\b\b" + (100*progress/file.length()) + "%");
            } else {
                System.out.print("\b\b\b" + (100*progress/file.length()) + "%");
            }
        }
        System.out.println("\b\b\b\bdone");
    }
}
