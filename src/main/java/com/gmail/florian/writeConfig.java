package com.gmail.florian;

import com.vaadin.flow.component.notification.Notification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class writeConfig  {
    static String pathToConfigFile = "./config/";
    static File configFile = new File(pathToConfigFile + "SQL.conf");
    static File configFileOld = new File(pathToConfigFile + "SQL.old");
    static File directory = new File("./config");

    public static void createConfigFile(String UserInputs)  {
        try {
            if(!directory.exists())  {
                directory.mkdirs();
                Notification.show("Created directory");
            }

            if (configFile.exists())  {

                if(configFileOld.exists())  {
                    Notification.show("Deleted older config File.");
                    configFileOld.delete();
                }

                Notification.show("Configuration exists already, overwrite.");
                configFile.renameTo(configFileOld);
                configFile.delete();
                configFile.createNewFile();
            }
            else  {
                configFile.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        writeToConfigFile(UserInputs);
    }

    public static void writeToConfigFile(String UserInputs)  {
        String inputParts[] = UserInputs.split(";");

        try {
            FileWriter fw = new FileWriter(configFile);
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i = 0; i < inputParts.length; i++)  {
                bw.write(inputParts[i]);
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
