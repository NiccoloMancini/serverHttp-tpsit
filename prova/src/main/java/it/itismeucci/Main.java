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
import java.net.URLDecoder;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String[] firstline = in.readLine().split(" ");
            String resource = firstline[1];
            String header;
            do{
                header = in.readLine();
            }while(!header.isEmpty());
            resource = URLDecoder.decode(resource, "UTF-8");
            if (resource.equals("/")) {
                resource = "/index.html";
            }
            
            File file = new File("progetto_personale" + resource);
            if(file.exists()){
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type: " + getContentType(resource) + "\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("\n");
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[65536];
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
            s.close();
        }
    }

    private static String getContentType(String resource){
        String[] splitResource = resource.split("\\.");
        switch (splitResource[1]) {
            case "html":
            case "htm":
                return "text/html";    
            case "png":
            case "PNG":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";    
            case "css":
                return "text/css";
            case "js":
                return "application/js";
            case "svg":
                return "image/svg";
            case "webp":
                return "image/webp";
            default:
                return "";
            
    
        }        
    }
}