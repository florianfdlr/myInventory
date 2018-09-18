package com.gmail.florian;

import com.vaadin.flow.component.notification.Notification;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Properties;

public class ConnectSQL {

    public String[] config = new String[5];
    public String server;
    public String port;
    public String name;
    public String user;
    public String pwd;


    public ConnectSQL()  {

        readConfig();

        System.out.println("**************************************************************************************************");
        System.out.println("**************************************************************************************************");
        System.out.println("Found following configuration:");
        System.out.println("Server: " +     server);
        System.out.println("Port: " +       port);
        System.out.println("Name: " +       name);
        System.out.println("User: " +       user);
        System.out.println("Password " +    pwd);
        System.out.println("**************************************************************************************************");
        System.out.println("**************************************************************************************************");

    }

    void readConfig()  {
        try {
            String pathToConfigFile = "./config/";
            File configFile = new File(pathToConfigFile + "SQL.conf");
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            
            for (int i = 0; i <= 4; i++)  {
                config[i] = br.readLine();
            }

            br.close();

            //------------------------------------------------//
            server  = config[0].split("=")[1];
            port    = config[1].split("=")[1];
            name    = config[2].split("=")[1];
            user    = config[3].split("=")[1];
            pwd     = config[4].split("=")[1];
            //------------------------------------------------//
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Notification.show("Couldn't find config file. Please check in Vaadin directory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String timeZone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    String DATABASE_URL = "jdbc:mysql://" + server + ":" + port + "/" + name + timeZone;
    String DATABASE_USER = user;
    String DATABASE_PWD = pwd;


    private Connection connection;
    private Properties properties;

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", DATABASE_USER);
            properties.setProperty("password", DATABASE_PWD);
        }

        return properties;
    }

    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());

            } catch (ClassNotFoundException e) {
                Notification.show("MySQL Driver not found");
            } catch (SQLSyntaxErrorException e) {
                Notification.show(e.getLocalizedMessage());

            } catch (SQLException e) {
                Notification.show("Other SQL Problems");
                e.printStackTrace();
            }
        }

        return connection;
    }

    public void executeQuery(String statement)  {
        try {
            connect();
            connection.createStatement().executeQuery(statement);
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}