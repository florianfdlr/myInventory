package com.gmail.florian;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Setup Step 3")
@Route("Step3")
@HtmlImport("frontend://styles/shared-styles.html")

public class SetupStep3 extends VerticalLayout {
    public SetupStep3()  {
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
        setupStepLabel.setText("(Step 3/5)");

        //customize Components
        setupLabel.setSizeUndefined();
        setHorizontalComponentAlignment(Alignment.CENTER, setupLabel, setupStepLabel);
        setupLabel.addClassName("setupLabel");
        setupStepLabel.addClassName("setupStepLabel");

        //add Components
        add(setupLabel, setupStepLabel);
    }

    //Textfields and submit button
    TextField roleTextfield = new TextField("Add new Role");                                                      //Role name Field
    TextField roleDescriptionTextfield = new TextField("Description", e -> submitRole());                         //Description Field
    Button submitRoleBtn = new Button("Submit");                                                                   //Submit Button
    Button backBtn = new Button("<-", e -> this.getUI().ifPresent(ui -> ui.navigate("Step1")));           //Back Button
    Span span = new Span("");                                                                                      //Placeholder
    Button nextBtn = new Button("->", e -> this.getUI().ifPresent(ui -> ui.navigate("Step3")));           //Next Button


    //Progressbar
    ProgressBar setupProgress = new ProgressBar();

    void initUIStep3()  {
        VerticalLayout setupBox = new VerticalLayout();
        VerticalLayout information = new VerticalLayout();
        HorizontalLayout information_2 = new HorizontalLayout();
        HorizontalLayout setupLine_1 = new HorizontalLayout();
        HorizontalLayout setupLine_2 = new HorizontalLayout();

        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, setupBox);
        setupBox.setWidth("40vw");
        setupBox.addClassName("setupBox");

        add(setupBox);
        setupBox.add(information, information_2, setupLine_1, setupProgress, setupLine_2);

        setupProgress.addClassName("setupProgress");
        setupProgress.setValue(0.4);

        //add information Line_1 & 2
        Label dbInformationLabel = new Label("Please add minimum 1 user role and a short description.");
        Span existingRolesSpan = new Span();
        Span existingRoles = new Span();

        existingRolesSpan.setText("Existing roles: ");
        existingRolesSpan.getStyle().set("font-weight", "bold");

        existingRoles.setText(getExistingRoles());
        existingRoles.getStyle().set("font-style", "italic");

        information.add(dbInformationLabel);
        information_2.add(existingRolesSpan, existingRoles);

        //add textfields and submit button
        setupLine_1.add(roleTextfield, roleDescriptionTextfield, submitRoleBtn);
        setupLine_1.setFlexGrow(1, roleTextfield, roleDescriptionTextfield);
        setupLine_1.setVerticalComponentAlignment(FlexComponent.Alignment.END, submitRoleBtn);
        setupLine_1.setClassName("setupLine_1");
        submitRoleBtn.setSizeUndefined();
        submitRoleBtn.setClassName("submitUser");
        submitRoleBtn.setEnabled(false);

        //add buttons to setupLine_2
        setupLine_2.add(backBtn, span, nextBtn);
        setupLine_2.setFlexGrow(1, span);
        setupLine_2.addClassName("setupLine_1");
        backBtn.setSizeUndefined();
        nextBtn.setSizeUndefined();

        //add Keylisteners
        roleTextfield.addKeyDownListener(e -> checkInput());
        roleDescriptionTextfield.addKeyDownListener(e -> checkInput());
    }

    void checkInput()  {
        if(!roleTextfield.isEmpty() && !roleDescriptionTextfield.isEmpty())  {
            submitRoleBtn.setEnabled(true);
        }
    }

    void submitRole()  {
        ConnectSQL connectSQL = new ConnectSQL();

        if(roleTableExists())  {
            connectSQL.insertSomething("INSERT INTO myInventoryRole (rolename, description) " +
                    "VALUES ('" + roleTextfield.getValue() + "', '"+ roleDescriptionTextfield.getValue() +"')");
        } else {
            createRoleTable();
            connectSQL.insertSomething("INSERT INTO myInventoryRole (rolename, description) " +
                    "VALUES ('" + roleTextfield.getValue() + "', '"+ roleDescriptionTextfield.getValue() +"')");
        }
    }

    boolean roleTableExists()  {
        ConnectSQL connectSQL = new ConnectSQL();

        if(connectSQL.tableExists("myInventoryRole"))  {
            return true;
        } else  {
            Notification.show("Roles Table does not exist in DB");
            return false;
        }
    }

    void createRoleTable()  {
        ConnectSQL connectSQL = new ConnectSQL();
        connectSQL.createTable("RolesTable");
    }

    String getExistingRoles()  {
        ConnectSQL connectSQL = new ConnectSQL();

        if(!roleTableExists())  {
            createRoleTable();
            nextBtn.setEnabled(false);
            roleTextfield.setPlaceholder("required");
            roleDescriptionTextfield.setPlaceholder("required");
            return "-";
        } else  {
            String exRo = connectSQL.getSomething("SELECT * FROM myInventoryRole", "rolename");
            if(exRo.length() > 0)  {
                nextBtn.setEnabled(true);
                roleTextfield.setPlaceholder("optional");
                roleDescriptionTextfield.setPlaceholder("optional");
                setupProgress.setValue(0.6);
            } else  {
                nextBtn.setEnabled(false);
                roleTextfield.setPlaceholder("required");
                roleDescriptionTextfield.setPlaceholder("required");
                setupProgress.setValue(0.4);
            }
            return exRo;
        }
    }
}
