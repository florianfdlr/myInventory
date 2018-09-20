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

@Route("Step1")
@PageTitle("Setup Step 1")
@HtmlImport("frontend://styles/shared-styles.html")
public class SetupStep1 extends VerticalLayout {

    public SetupStep1() {
        initPageFramework();
        initUIStep1();
    }

    public void initPageFramework()  {
        //reset preset
        setPadding(false);          setWidth("100%");
        setSpacing(false);          setHeight("100%");
        setMargin(false);

        //init Components
        Label setupLabel = new Label("Setup");
        Label setupStepLabel = new Label();
        setupStepLabel.setText("(Step 1/5)");

        //customize Components
        setupLabel.setSizeUndefined();
        setHorizontalComponentAlignment(Alignment.CENTER, setupLabel, setupStepLabel);
        setupLabel.addClassName("setupLabel");
        setupStepLabel.addClassName("setupStepLabel");

        //add Components
        add(setupLabel, setupStepLabel);

    }

    //Line 1
    private TextField databaseURLTextfield = new TextField("Database URL");      /* --> */       public boolean field1;
    private TextField databasePortTextfield = new TextField("Database Port");    /* --> */       public boolean field2;
    private TextField databaseNameTextfield = new TextField("Database Name");    /* --> */       public boolean field3;

    //Line 2
    private TextField databaseUserTextfield = new TextField("Database User");    /* --> */       public boolean field4;
    private PasswordField databasePwdTextfield = new PasswordField("Password");  /* --> */       public boolean field5;

    public String getDatabaseUrlText()  {
        return databaseURLTextfield.getValue();
    }
    public String getDatabasePortText()  {
                return databasePortTextfield.getValue();
    }
    public String getDatabaseNameText()  {
        return databaseNameTextfield.getValue();
    }
    public String getDatabaseUserText()  {
        return databaseUserTextfield.getValue();
    }
    public String getDatabasePwdText()  {
        return databasePwdTextfield.getValue();
    }

    //Line 3
    Button submitBtn = new Button("Test connection and save Information", e -> submit());

    //Line 4
    ProgressBar setupProgressBar = new ProgressBar();


    public void initUIStep1()  {
        VerticalLayout setupBox = new VerticalLayout();
        HorizontalLayout information = new HorizontalLayout();
        HorizontalLayout setupLine_1 = new HorizontalLayout();
        HorizontalLayout setupLine_2 = new HorizontalLayout();
        HorizontalLayout setupLine_3 = new HorizontalLayout();
        HorizontalLayout setupLine_4 = new HorizontalLayout();

        add(setupBox);
        setupBox.add(information, setupLine_1, setupLine_2, setupLine_3, setupLine_4);

        setupBox.setClassName("setupBox");
        setHorizontalComponentAlignment(Alignment.CENTER, setupBox);
        setupBox.setWidth("40vw");

        //add information Line
        Label dbInformationLabel = new Label("You have to setup a SQL-Server to use this product. " +
                "Please fill all fields to establish a connection to your SQL-Server. It's recommended to use MySQL as Database-Server.");

        information.add(dbInformationLabel);

        //Add components of Line 1
        setupLine_1.setFlexGrow(1, databaseURLTextfield, databaseNameTextfield);
        setupLine_1.setClassName("setupLine_1");
        setupLine_1.add(databaseURLTextfield, databasePortTextfield, databaseNameTextfield);

        //Add components of Line 2
        setupLine_2.setFlexGrow(1, databaseUserTextfield, databasePwdTextfield);
        setupLine_2.setClassName("setupLine_2");
        setupLine_2.add(databaseUserTextfield, databasePwdTextfield);

        //Add components of Line 3

        submitBtn.setClassName("SetupSubmitBtn");
        setupLine_3.add(submitBtn);

        //Line 4
        setupLine_4.add(setupProgressBar);
        setupProgressBar.setValue(0.0);
        setupProgressBar.addClassName("setupProgress");

        //Set all Fields Required
        databaseURLTextfield.isRequired();
        databasePortTextfield.isRequired();
        databaseNameTextfield.isRequired();
        databaseUserTextfield.isRequired();
        databasePwdTextfield.isRequired();

        //Add event listeners to all fields to enable submit button if all fields are filled
        databaseURLTextfield.addBlurListener(e ->   {if(!databaseURLTextfield.getValue().isEmpty())         {field1 = true; checkFields();}     else {field1 = false; checkFields();}});
        databasePortTextfield.addBlurListener(e->   {if(!databasePortTextfield.getValue().isEmpty())        {field2 = true; checkFields();}     else {field2 = false; checkFields();}});
        databaseNameTextfield.addBlurListener(e->   {if(!databaseNameTextfield.getValue().isEmpty())        {field3 = true; checkFields();}     else {field3 = false; checkFields();}});
        databaseUserTextfield.addBlurListener(e->   {if(!databaseUserTextfield.getValue().isEmpty())        {field4 = true; checkFields();}     else {field4 = false; checkFields();}});
        databasePwdTextfield.addBlurListener(e->    {if(!databasePwdTextfield.getValue().isEmpty())         {field5 = true; checkFields();}     else {field5 = false; checkFields();}});

        //Set standard values for fields
        databasePortTextfield.setPlaceholder("3306");
        databaseNameTextfield.setValue("myInventory");
    }

    public boolean checkFields()  {
        if (!field1 && !field2 && !field3 && !field4 && !field5)  {
            setupProgressBar.setValue(0.0);
        }

        if(field1 || field2 || field3 || field4 || field5)  {
            setupProgressBar.setValue(0.04);
        }

        if(field1 && field2 || field3 || field4 || field5)  {
            setupProgressBar.setValue(0.08);
        }

        if(field1 && field2 && field3 || field4 || field5)  {
            setupProgressBar.setValue(0.12);
        }

        if(field1 && field2 && field3 && field4 || field5)  {
            setupProgressBar.setValue(0.16);
        }

        if(field1 && field2 && field3 && field4 && field5)  {
            submitBtn.setEnabled(true);
            setupProgressBar.setValue(0.20);
            return true;
        } else  {
            submitBtn.setEnabled(false);
            return false;
        }
    }

    public void submit()  {
        TestDBConnection testDBConnection = new TestDBConnection();

        testDBConnection.setParam(
                UserInputs.dbUrl(this),
                UserInputs.dbPort(this),
                UserInputs.dbName(this),
                UserInputs.dbUser(this),
                UserInputs.dbPwd(this)
        );

        testDBConnection.test();

        if (testDBConnection.sqltestsuccessfull)  {
            writeConfig.createConfigFile(UserInputs.toString(this));
            this.getUI().ifPresent(ui -> ui.navigate("Step2"));
        }
    }
}
