package com.gmail.florian;

import com.vaadin.flow.component.notification.Notification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Properties;

public class TestDBConnection {

    public boolean sqltestsuccessfull = false;

    String DATABASE_DRIVER;
    String DATABASE_URL;
    String DATABASE_PWD;
    String DATABASE_USER;

    public void setParam(String server, String port, String name, String user, String pwd)  {

        String timeZone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        DATABASE_DRIVER = "com.mysql.jdbc.Driver";
        DATABASE_URL = "jdbc:mysql://" + server + ":" + port + "/" + name + timeZone;
        DATABASE_USER = user;
        DATABASE_PWD = pwd;
    }


    private Connection testConnection;
    private Properties testProperties;

    private Properties getProperties()  {
        if(testProperties == null)  {
            testProperties = new Properties();
            testProperties.setProperty("user", DATABASE_USER);
            testProperties.setProperty("password", DATABASE_PWD);

        }

        return testProperties;
    }

    public Connection connect()  {
        if(testConnection == null)  {
            try  {
                Class.forName(DATABASE_DRIVER);
                testConnection = DriverManager.getConnection(DATABASE_URL, getProperties());

                sqltestsuccessfull = true;
            } catch (ClassNotFoundException e) {
                Notification.show("MySQL Driver not found");
            } catch (SQLSyntaxErrorException e)  {
                Notification.show(e.getLocalizedMessage());

            } catch (SQLException e) {
                Notification.show("Other SQL Problems");
                e.printStackTrace();
            }
        }

        return testConnection;
    }

    public void disconnect()  {
        if(testConnection != null)  {
            try {
                testConnection.close();
                testConnection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void test()  {

        try {
            connect();
            Notification.show("Connected to Database");

            disconnect();
            Notification.show("Disconnected from Database");
        } catch (Exception e)  {
            e.printStackTrace();
        }
    }
}
