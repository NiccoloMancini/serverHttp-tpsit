package it.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String[] firstline = in.readLine().split(" ");
            String method = firstline[0];
            String resource = firstline[1];
            String version = firstline[2];
            String header;
            do{
                header = in.readLine();
            }while(!header.isEmpty());
            String type;
            if (resource.equals("/")) {
                resource = "/index.html";
            }
            File file = new File("htdocs" + resource);
            if(file.exists()){
                String[] splitResource = resource.split("\\.");
                if (splitResource[1].equals("html")) {
                    type = "text/html";
                }else {
                    type = "text/plain";
                }
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type: " + type + "\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("\n");
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[8192];
                int n;
                while ((n = input.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }
                input.close();
                System.out.println("fine");
            }else{
                out.writeBytes("HTTP/1.1 404 NOT FOUND\n");
                out.writeBytes("Content-Length: 0\n");
                out.writeBytes("\n");
                out.writeBytes("");
            }
            System.out.println("richiesta terminata");
        }
    }
}