package classes;


import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

@RestController
public class WebController {
    static Path path= Path.of("/home/notes.json");
    static HttpURLConnection con;
    static DateTimeFormatter dateF = DateTimeFormatter.ofPattern("EEE MMM dd").localizedBy(Locale.US);



    @GetMapping ("/notes")
    String json (){
        return read();

    }

    @PostMapping("/add")
    String put (@RequestBody String str){
               return write(str);

    }


    @PostMapping("/sms")
    String sms (@RequestBody String str){
        str = URLDecoder.decode(str, StandardCharsets.UTF_8);
        System.out.println("!!!   SMS    !!! : "+ str);

        return sendSMS(str);

    }



    static String write (String str){

        try {
            Files.writeString(path,str);
            return "Заметка записана";
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при записи заметки";
        }

    }

    static String read (){
        String rez="[\"Ошибка при чтении заметок\"]";
   try {
           rez = Files.readString(path);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rez;

    }

    static String sendSMS( String mess) {
        // System.out.println(mess);
        boolean rez;
        var url = "http://77.51.193.189:7077/";
        var urlParameters = "tel=89099197147&mess=shurup.in: " + mess;
        byte[] postData = Base64.getEncoder().encode(urlParameters.getBytes(StandardCharsets.UTF_8));

        try {
            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/html; charset=utf-8");

            try (var wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData, 0, postData.length);
            }

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    content.append(line);
                  // content.append(System.lineSeparator());
                }

                rez =LocalDateTime.now().format(dateF).equals(content.toString().substring(0, 10));
            }


        } catch (Exception e) {
            e.printStackTrace();
                rez=false;


        } finally {
            con.disconnect();
        }
        return String.valueOf(rez) ;
    }

    public static void main(String[] args) {
        System.out.println( sendSMS("КЕККЕКЕьььатвыаыt 1"));
    }

}
