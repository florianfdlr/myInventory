package com.gmail.florian;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.awt.*;

@Route("Step2")
@PageTitle("Setup Step 2")
@HtmlImport("frontend://styles/shared-styles.html")

public class SetupStep2 extends VerticalLayout {
    public SetupStep2()  {
        initPageFramework();
        initUIStep2();

    }

    void initPageFramework()  {
        //reset preset
        setPadding(false);          setWidth("100%");
        setSpacing(false);          setHeight("100%");
        setMargin(false);

        //init Components
        Label setupLabel = new Label("Setup");
        Label setupStepLabel = new Label();
        setupStepLabel.setText("(Step 2/5)");

        //customize Components
        setupLabel.setSizeUndefined();
        setHorizontalComponentAlignment(Alignment.CENTER, setupLabel, setupStepLabel);
        setupLabel.addClassName("setupLabel");
        setupStepLabel.addClassName("setupStepLabel");

        //add Components
        add(setupLabel, setupStepLabel);
    }

    void addPerson(String user, String password, String passwordRepeat)  {
        ConnectSQL connectSQL = new ConnectSQL();

        if(validateInputs(user, password, passwordRepeat))  {
            if(!userTableExists())  {
                Dialog dialog = new Dialog();
                HorizontalLayout dialogLine1 = new HorizontalLayout();
                HorizontalLayout dialogLine2 = new HorizontalLayout();

                Label dialogLabel = new Label("Missing User-Table in Database.");
                Button dialogBtn = new Button("CREATE", e -> {     connectSQL.createTable("UserTable");
                                                                        connectSQL.insertSomething("INSERT INTO myInventoryUser (user, pass, role) " +
                                                                                "VALUES ('" + user + "','" + password + "', 'admin')");
                                                                        dialog.close();
                                                                    });

                dialogBtn.setWidth("300px");

                dialogLine1.add(dialogLabel);
                dialogLine2.add(dialogBtn);

                dialog.add(dialogLine1, dialogLine2);

                dialog.setHeight("150px");
                dialog.setWidth("300px");

                dialog.open();
            } else  {
                connectSQL.insertSomething("INSERT INTO myInventoryUser (user, pass) VALUES ('" + user + "','" + password + "')");
            }

            Notification.show("Added " + user);
            clearFields();
            nextBtn.setEnabled(true);
        }
    }

    //Textfields and submit button
    TextField username = new TextField("Username");                                                                                     //Username Field
    PasswordField password = new PasswordField("Password");                                                                             //PWD Field
    PasswordField passwordRepeat = new PasswordField("Repeat Password");                                                                //Repeat PWD Field
    Button submitUser = new Button("+", e -> addPerson(username.getValue(), password.getValue(), passwordRepeat.getValue()));            //Submit Button
    Button backBtn = new Button("<-", e -> this.getUI().ifPresent(ui -> ui.navigate("Step1")));                                  //Back Button
    Span span = new Span("");                                                                                                            //Placeholder
    Button nextBtn = new Button("->", e -> this.getUI().ifPresent(ui -> ui.navigate("Step3")));                                  //Next Button


    //Progressbar
    ProgressBar setupProgress = new ProgressBar();

    void initUIStep2()  {
        VerticalLayout setupBox = new VerticalLayout();
        HorizontalLayout information = new HorizontalLayout();
        HorizontalLayout information_2 = new HorizontalLayout();
        HorizontalLayout setupLine_1 = new HorizontalLayout();
        HorizontalLayout setupLine_2 = new HorizontalLayout();

        setHorizontalComponentAlignment(Alignment.CENTER, setupBox);
        setupBox.setWidth("40vw");
        setupBox.addClassName("setupBox");

        add(setupBox);
        setupBox.add(information, information_2, setupLine_1, setupProgress, setupLine_2);

        setupProgress.addClassName("setupProgress");
        setupProgress.setValue(0.2);

        //add information Line_1 & Line_2
        Label dbInformationLabel = new Label("Please add minimum 1 user with admin rights.");
        information.add(dbInformationLabel);

        Span existingUsersSpan = new Span();
        existingUsersSpan.setText("Existing users: ");
        existingUsersSpan.getStyle().set("font-weight", "bold");

        Span existingUsers = new Span();
        existingUsers.setText(getExistingUsers());
        existingUsers.getStyle().set("font-style", "italic");

        information_2.add(existingUsersSpan, existingUsers);

        //add textfields and submit button
        setupLine_1.add(username, password, passwordRepeat, submitUser);
        setupLine_1.setFlexGrow(1, username, password, passwordRepeat);
        setupLine_1.setVerticalComponentAlignment(Alignment.END, submitUser);
        setupLine_1.setClassName("setupLine_1");
        submitUser.setSizeUndefined();
        submitUser.setClassName("submitUser");
        submitUser.setEnabled(false);

        //add listeners to textfields
        username.addBlurListener(e-> checkFields());
        password.addBlurListener(e -> checkFields());
        passwordRepeat.addBlurListener(e -> checkFields());

        //add buttons to setupLine_2
        setupLine_2.add(backBtn, span, nextBtn);
        setupLine_2.setFlexGrow(1, span);
        setupLine_2.addClassName("setupLine_1");
        backBtn.setSizeUndefined();
        nextBtn.setSizeUndefined();
    }

    String getExistingUsers()  {
        ConnectSQL connectSQL = new ConnectSQL();

        if(userTableExists()) {
            String existingUsers = connectSQL.getSomething("SELECT * FROM myInventoryUser", "user");

            if(existingUsers.length() < 3)  {
                nextBtn.setEnabled(false);
                username.setPlaceholder("required");
                password.setPlaceholder("required");
                passwordRepeat.setPlaceholder("required");
                return "-";
            } else  {
                nextBtn.setEnabled(true);
                username.setPlaceholder("optional");
                password.setPlaceholder("optional");
                passwordRepeat.setPlaceholder("optional");
                setupProgress.setValue(0.4);

                if(existingUsers.contains(","))  {
                    String[] exUsArr = existingUsers.split(",");

                    if(exUsArr.length > 5)  {
                        StringBuilder sb = new StringBuilder();
                        sb.append(exUsArr[0]).append(",")
                                .append(exUsArr[1]).append(",")
                                .append(exUsArr[2]).append(",")
                                .append(exUsArr[3]).append(",")
                                .append(exUsArr[4]).append(" and [" + (exUsArr.length-5) + "] more");
                        return sb.toString();
                    } else {
                        return existingUsers;
                    }
                } else {
                    return existingUsers;
                }
            }
        } else  {
            return "-";
        }
    }

    boolean userTableExists()  {
        ConnectSQL connectSQL = new ConnectSQL();

        if(connectSQL.tableExists("myInventoryUser"))  {
            return true;
        } else  {
            return false;
        }
    }

    void checkFields()  {
        if (username.getValue().isEmpty() || password.getValue().isEmpty() || passwordRepeat.getValue().isEmpty())  {
            submitUser.setEnabled(false);
            setupProgress.setValue(0.2);
        } else  {
            submitUser.setEnabled(true);
            setupProgress.setValue(0.4);
        }
    }

    boolean validateInputs(String user, String password, String passwordRepeat)  {
        if(!user.isEmpty())  {
            if (password.equals(passwordRepeat))  {
                return true;
            } else  {
                Notification.show("Passwords don't match. Please verify");
                return false;
            }
        } else  {
            Notification.show("Please fill all fields");
            return false;
        }
    }

    void clearFields()  {
        username.setValue("");
        password.setValue("");
        passwordRepeat.setValue("");
    }
}
