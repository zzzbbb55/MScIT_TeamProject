package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {
    FileWriter fileWriter;
    String logfile;
    boolean isWriting;
    public Log(boolean isWriting){
        this.isWriting = isWriting;
        logfile = "toptrumps.log";
    }
    public void initialize() throws IOException {
        if(isWriting) {
            File file = new File(logfile);
            fileWriter = new FileWriter(file);
        }
    }
    public void write(String log){
        if(isWriting){
            Date date = new Date();
            try {
                fileWriter.write(date.getTime()+ ": "+log+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public  void close() throws IOException {
        if(isWriting) {
            fileWriter.close();
        }
    }
}
