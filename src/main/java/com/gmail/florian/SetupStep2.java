package com.gmail.florian;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
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

    void addPerson()  {
        ConnectSQL connectSQL = new ConnectSQL();
        connectSQL.executeQuery("SELECT * FROM 'Users'");
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

    void initUIStep2()  {
        VerticalLayout setupBox = new VerticalLayout();
        HorizontalLayout information = new HorizontalLayout();
        HorizontalLayout setupLine_1 = new HorizontalLayout();

        setHorizontalComponentAlignment(Alignment.CENTER, setupBox);
        setupBox.setWidth("40vw");
        setupBox.addClassName("setupBox");

        add(setupBox);
        setupBox.add(information, setupLine_1);

        ProgressBar setupProgress = new ProgressBar();
        setupProgress.addClassName("setupProgress");
        setupProgress.setValue(0.2);


        //add information Line
        Label dbInformationLabel = new Label("Please add minimum 1 user with admin rights.");
        information.add(dbInformationLabel);

        //add components of line 1
        TextField username = new TextField("Username");                                                           //Username Field
        PasswordField password = new PasswordField("Password");                                                   //PWD Field
        PasswordField passwordRepeat = new PasswordField("Repeat Password");                                      //Repeat PWD Field
        Button submitUser = new Button("+", e -> addPerson());                                                     //Submit Button

        setupLine_1.add(username, password, passwordRepeat, submitUser);
        setupLine_1.setFlexGrow(1, username, password, passwordRepeat);
        setupLine_1.setVerticalComponentAlignment(Alignment.END, submitUser);
        setupLine_1.setClassName("setupLine_1");
        submitUser.setSizeUndefined();
        submitUser.setClassName("submitUser");

        setupBox.add(setupProgress);
    }
}
