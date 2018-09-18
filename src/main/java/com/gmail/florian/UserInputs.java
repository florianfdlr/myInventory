package com.gmail.florian;

import com.vaadin.flow.component.notification.Notification;

class UserInputs {

    public static String dbUrl(SetupStep1 setupStep1)  {
        return setupStep1.getDatabaseUrlText();
    }

    public static String dbPort(SetupStep1 setupStep1)  {
        return setupStep1.getDatabasePortText();
    }

    public static String dbName(SetupStep1 setupStep1)  {
        return setupStep1.getDatabaseNameText();
    }

    public static String dbUser(SetupStep1 setupStep1)  {
        return setupStep1.getDatabaseUserText();
    }

    public static String dbPwd(SetupStep1 setupStep1)  {
        return setupStep1.getDatabasePwdText();
    }

    public static String toString(SetupStep1 setupStep1) {

        String url = setupStep1.getDatabaseUrlText();
        String port = setupStep1.getDatabasePortText();
        String dbName = setupStep1.getDatabaseNameText();
        String dbUser = setupStep1.getDatabaseUserText();
        String dbPwd = setupStep1.getDatabasePwdText();

        StringBuilder sb = new StringBuilder();

        sb.append("Server=").append(url).append(';')
                .append("Port=").append(port).append(';')
                .append("Database=").append(dbName).append(';')
                .append("User=").append(dbUser).append(';')
                .append("Pwd=").append(dbPwd).append(';');

        Notification.show(sb.toString());

        return sb.toString();
    }
}