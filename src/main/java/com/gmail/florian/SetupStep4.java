package com.gmail.florian;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Setup Step 4")
@Route("Step4")
@HtmlImport("frontend://styles/shared-styles.html")
public class SetupStep4 extends VerticalLayout {
    public SetupStep4()  {
        initPageFramework();
        initUIStep3();
    }

    void initPageFramework()  {
        //reset preset
        setPadding(false);          setWidth("100%");
        setSpacing(false);          setHeight("100%");
        setMargin(false);

        //init Components
        Label setupLabel = new Label("Setup");
        Label setupStepLabel = new Label();
        setupStepLabel.setText("(Step 4/5)");

        //customize Components
        setupLabel.setSizeUndefined();
        setHorizontalComponentAlignment(Alignment.CENTER, setupLabel, setupStepLabel);
        setupLabel.addClassName("setupLabel");
        setupStepLabel.addClassName("setupStepLabel");

        //add Components
        add(setupLabel, setupStepLabel);
    }

    //Control Buttons
    Button backBtn = new Button("<-", e -> this.getUI().ifPresent(ui -> ui.navigate("Step3")));           //Back Button
    Span span = new Span("");                                                                                      //Placeholder
    Button nextBtn = new Button("->", e -> this.getUI().ifPresent(ui -> ui.navigate("Step5")));           //Next Button

    //Inputfields for new User
    TextField username = new TextField("Name");
    PasswordField password = new PasswordField("Password");
    Button submitBtn = new Button("Submit");

    //Selectionbox
    ComboBox<String> selectRole = new ComboBox<>("Role");

    //Progressbar
    ProgressBar setupProgress = new ProgressBar();

    void initUIStep3()  {
        VerticalLayout setupBox = new VerticalLayout();
        VerticalLayout information = new VerticalLayout();
        HorizontalLayout setupLine_1 = new HorizontalLayout();
        HorizontalLayout setupLine_2 = new HorizontalLayout();

        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, setupBox);
        setupBox.setWidth("50vw");
        setupBox.addClassName("setupBox");

        add(setupBox);
        setupBox.add(information, setupProgress, setupLine_1, setupLine_2);

        setupProgress.getStyle().set("width", "calc(50vw - 40px");
        setupProgress.setValue(0.6);

        //add information Line_1
        Label dbInformationLabel = new Label("Please add minimum 1 user to a role.");
        information.add(dbInformationLabel);

        //define selectRole-ComboBox
        selectRole.setItems(getRoles());

        //add components to setupLine_1
        setupLine_1.add(selectRole, username, password, submitBtn);
        setupLine_1.setVerticalComponentAlignment(Alignment.END, submitBtn);
        setupLine_1.getStyle().set("width", "calc(50vw - 40px");
        setupLine_1.setFlexGrow(1, selectRole, username, password);
        submitBtn.setSizeUndefined();

        //add buttons to setupLine_2
        setupLine_2.add(backBtn, span, nextBtn);
        setupLine_2.getStyle().set("width", "calc(50vw - 40px)");
        setupLine_2.setFlexGrow(1, span);
        backBtn.setSizeUndefined();
        nextBtn.setSizeUndefined();
    }

    boolean roleTableExists()  {
        ConnectSQL connectSQL = new ConnectSQL();

        if(connectSQL.tableExists("myInventoryRole"))  {
            return true;
        } else  {
            Notification.show("Roles Table does not exist in DB, please go back to Step 3");
            return false;
        }
    }

    String[] getRoles()  {
        ConnectSQL connectSQL = new ConnectSQL();
        String[] noRoles = {"No roles. Please repeat Step 3"};

        if(roleTableExists())  {
            String roles = connectSQL.getSomething("SELECT * FROM myInventoryRole", "rolename");
            if(roles.isEmpty())  {
                return noRoles;
            }
            if (roles.contains(","))  {
                return roles.split(",");
            } else  {
                String[] role = {roles};
                return role;
            }
        } else  {
            return noRoles;
        }
    }
}

