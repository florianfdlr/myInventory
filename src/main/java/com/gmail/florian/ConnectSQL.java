package com.gmail.florian;

import com.vaadin.flow.component.notification.Notification;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class ConnectSQL {

    public String[] config = new String[5];
    public String server, port, name, user, pwd;

    String DATABASE_USER, DATABASE_PWD, DATABASE_DRIVER, DATABASE_URL;
    String timeZone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";


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

        DATABASE_USER = user;
        DATABASE_PWD = pwd;
        DATABASE_DRIVER = "com.mysql.jdbc.Driver";
        DATABASE_URL = "jdbc:mysql://" + server + ":" + port + "/" + name + timeZone;

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
        if (DATABASE_USER != null && DATABASE_PWD != null)  {
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
        } else {
            Notification.show("Something went wrong");
        }

        return connection;
    }

    boolean tableExists(String tablename)  {
        connect();
        try  {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, tablename, null);
            while(rs.next())  {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return false;
    }

    public void createTable(String tablename)  {
        connect();
        Statement stmt = null;

        try {

            stmt = connection.createStatement();

            switch (tablename)  {
                case "UserTable":
                    stmt.executeUpdate("CREATE TABLE myInventoryUser (\n" +
                                            "    ID INT AUTO_INCREMENT PRIMARY KEY,\n" +
                                            "    user TEXT NOT NULL,\n" +
                                            "    pass TEXT NOT NULL,\n" +
                                            "    role TEXT NOT NULL\n" +
                                            "    );");

                case "RolesTable":
                    stmt.executeUpdate("CREATE TABLE myInventoryRole (\n" +
                                            "   rolename    VARCHAR(200)    NOT NULL    PRIMARY KEY,\n" +
                                            "   description TEXT            NOT NULL\n" +
                                            "   );");

                default:
                    Notification.show("Unknown task");
            }

        } catch (SQLException e)  {
            Notification.show("Something went wrong");
            e.printStackTrace();
        }
    }

    public void insertSomething(String statement)  {
        connect();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public String getSomething(String selectStatement, String columnLabel)  {
        connect();
        Statement stmt = null;
        StringBuilder sb = new StringBuilder();

        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectStatement);
            while (rs.next())  {
                sb.append(rs.getString(columnLabel));
                if (!rs.isLast())  {
                    sb.append(", ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return sb.toString();
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