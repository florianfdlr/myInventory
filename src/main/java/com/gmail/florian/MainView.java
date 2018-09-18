package com.gmail.florian;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * The main view contains a button and a click listener.
 */
@Route
@PageTitle("myInventory")
@HtmlImport("frontend://styles/shared-styles.html")
public class MainView extends VerticalLayout {

    public MainView() {

    }

}

