//********************************************************************
//  MinesweeperFX.java
//
//  Author: David J. Gardner
//  Date: 4/29/18
//
//  GUI driver for MinesweeperFX.
//********************************************************************

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIDriver extends Application {
    // primary UI stage
    private Stage primaryStage;
    // root object used by the scene
    private VBox root;

    // game level enumeration
    private GameLevel gameLevel;

    //-----------------------------------------------------------------
    //  JavaFX application start method.
    //-----------------------------------------------------------------
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        setup();
    }

    //-----------------------------------------------------------------
    //  Sets up the game display and shows the stage (window).
    //-----------------------------------------------------------------
    private void setup() {
        this.primaryStage.setTitle(Constants.APP_NAME);

        this.root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(getMenuBar());

        Scene scene = new Scene(root);
        this.primaryStage.setScene(scene);

        // set the default game level.
        setGameLevel(GameLevel.BEGINNER);

        // fix the size of the game window.
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }

    //-----------------------------------------------------------------
    //  Start the game with the currently selected level option.
    //-----------------------------------------------------------------
    private void startGame() {
        minesweeper ms;
        int timeout; // game timer, in minutes

        switch (this.gameLevel) {

            case INTERMEDIATE:
                ms = new minesweeper(16, 16);
                timeout = 10;
                break;
            case EXPERT:
                ms = new minesweeper(24, 24);
                timeout = 15;
                break;
            default:
                ms = new minesweeper(9, 9);
                timeout = 5;
        }

        MinesweeperFX msFx = new MinesweeperFX(ms, timeout);
        Parent gameUI = msFx.getGameUI();

        int children = this.root.getChildren().size();
        // check child count.  if greater than '1'
        // then MinesweeperFX parent has been added previously.
        if (children > 1) {
            this.root.getChildren().set(1, gameUI);
        } else {
            this.root.getChildren().add(gameUI);
        }
    }

    //-----------------------------------------------------------------
    //  Quit game.  Closes the stage, exits application.
    //-----------------------------------------------------------------
    private void quitGame() {
        this.primaryStage.close();
    }

    //-----------------------------------------------------------------
    //  Create the menu bar.
    //-----------------------------------------------------------------
    private MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();
        // setup File menu
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(
                getMenuItem("New", event -> startGame()),
                getMenuItem("Quit", event -> quitGame())
        );

        // setup Level menu
        Menu levelMenu = new Menu("Level");

        // for radio menu items, ensures only
        // one is selected at a time.
        ToggleGroup group = new ToggleGroup();

        levelMenu.getItems().addAll(
                getRadioMenuItem(
                        "Beginner - 9x9",
                        true,
                        group,
                        event -> setGameLevel(GameLevel.BEGINNER)
                ),

                getRadioMenuItem(
                        "Intermediate - 16x16",
                        false,
                        group,
                        event -> setGameLevel(GameLevel.INTERMEDIATE)
                ),

                getRadioMenuItem(
                        "Expert - 24x24",
                        false,
                        group,
                        event -> setGameLevel(GameLevel.EXPERT))
        );

        menuBar.getMenus().addAll(fileMenu, levelMenu);

        return menuBar;
    }

    //-----------------------------------------------------------------
    //  Sets game level and adjusts the stage dimensions.
    //-----------------------------------------------------------------
    private void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;

        int width, height;

        switch (this.gameLevel) {
            case INTERMEDIATE:
                width = 800;
                height = 680;
                break;
            case EXPERT:
                width = 920;
                height = 960;
                break;
            default:
                width = 500;
                height = 450;
                break;
        }

        // adjust the stage size to fit the game grid
        this.primaryStage.setWidth(width);
        this.primaryStage.setHeight(height);
    }

    //-----------------------------------------------------------------
    //  Create new JavaFX menu item with the provided parameters.
    //-----------------------------------------------------------------
    private MenuItem getMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem mi = new MenuItem(text);
        mi.setOnAction(handler);

        return mi;
    }

    //-----------------------------------------------------------------
    //  Create new JavaFX radio menu item with the provided parameters.
    //-----------------------------------------------------------------
    private RadioMenuItem getRadioMenuItem(String text,
                                           boolean isSelected,
                                           ToggleGroup toggleGroup,
                                           EventHandler<ActionEvent> handler) {
        RadioMenuItem mi = new RadioMenuItem(text);
        mi.setSelected(isSelected);
        mi.setOnAction(handler);
        mi.setToggleGroup(toggleGroup);

        return mi;
    }
}