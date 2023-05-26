package kozmikoda.passwordspace;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;


public class GUIController {
    // SQL variables
    PSQLConnection database = new PSQLConnection();
    static MainUserAccount user;

    // GUI variables
    double offsetX, offsetY;
    private Stage stage;

    @FXML
    private ImageView image;
    @FXML
    private Stage window;
    @FXML
    private Pane passwdForgetNamePane, passwdForgetPhonePane, passwdForgetNewPasswdPane, signUpPane, signupFailPane,
            servicesPane, addServicePane, deleteServicePane, openinInfoPane, serviceInsidePane;
    @FXML
    private JFXButton loginButton, deleteServiceButton;
    static JFXButton serviceButton;
    @FXML
    private JFXCheckBox signinShowPassword, serviceShowPassword, addServiceShowPassword;
    @FXML
    private TextField passwdVerifName, signUpNameField, signUpSurnameField, signUpUsernameField, signUpPhoneField,
            signUpMailField, signUpPasswordField, addServiceNameField, addServiceUserField, passwdShowText,
            serviceInsideShowPassword, loginUsernameField, serviceInsideName, addServiceShowPasswdText;
    @FXML
    private Hyperlink forgotPasswdLink, signUpLink;
    @FXML
    private PasswordField passwdVerifCode, passwdNewPasswd, addServicePasswdField;
    @FXML
    private VBox serviceVbox;
    @FXML
    private Label failLoginLabel, passwdUserNotFoundLabel, deleteServiceName, serviceNameShower, welcomeLabel, verifCodeErrorLabel;
    @FXML
    private PasswordField loginPasswordField, serviceInsidePassword;
    @FXML
    private ScrollPane serviceScrollPane;



    // Default constructor with empty body
    public GUIController() throws SQLException {}

    /**
     * Drag window setter
     */
    @FXML
    void dragWindow(MouseEvent event) {
        window.setX(event.getScreenX() - offsetX);
        window.setY(event.getScreenY() - offsetY);
    }

    /**
     * Set window according to the drag operation
     */
    @FXML
    public void setWindowOffset(MouseEvent event) {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    }

    /**
     * Close button
     */
    @FXML
    protected void closeButton(ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    /** Minimize button
     *
     */
    @FXML
    void minimizeButton() {
        window.setIconified(true);
    }


    // FORGOT PASSWORD PART
    @FXML
    void forgetPasswdButton() {
        // GUI stuff below
        passwdForgetNamePane.setVisible(true);
        passwdUserNotFoundLabel.setVisible(false);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    /** Back button1
     *
     */
    @FXML
    void forgetPasswdBackButton() {
        // GUI stuff below
        passwdForgetNamePane.setVisible(false);
        passwdUserNotFoundLabel.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);
        passwdVerifName.clear();
    }

    /** Back button2
     *
     */
    @FXML
    void forgetPasswdBackButton2() {
        // GUI stuff below
        passwdForgetPhonePane.setVisible(false);
        passwdForgetNamePane.setVisible(true);
        passwdUserNotFoundLabel.setVisible(false);
        passwdVerifCode.clear();
    }

    /** Verification page
     *
     */
    int verifCode;
    @FXML
    void sendVerificationButton() {
        try {
            passwdUserNotFoundLabel.setVisible(false);
            user = new MainUserAccount(database, passwdVerifName.getText());

            passwdForgetNamePane.setVisible(false);
            passwdForgetPhonePane.setVisible(true);

            // SEND VERIFICATION CODE
            verifCode = Utility.generateResetCode();
            ResetCodeSender.sendViaSMS(Integer.toString(verifCode) ,user.getPhoneNumber());


        } catch (Exception e) {
            passwdUserNotFoundLabel.setVisible(true);
        }

    }

    /** Actual reset password page
     *
     */
    @FXML
    void verifyButton() {
        if (Utility.validateResetCode(verifCode, Integer.parseInt(passwdVerifCode.getText()))) {
            passwdForgetPhonePane.setVisible(false);
            passwdForgetNewPasswdPane.setVisible(true);
            verifCodeErrorLabel.setVisible(false);
        }
        else {
            verifCodeErrorLabel.setVisible(true);
        }
    }

    /** After password reset button clicked
     *
     * @throws SQLException random sql exception
     */
    @FXML
    void resetPasswdButton() throws SQLException {
        // Updating SQL password
        user.updatePassword(passwdNewPasswd.getText());

        // GUI stuff below
        passwdForgetNewPasswdPane.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);
        passwdNewPasswd.clear();
    }


    // SIGN UP PART
    @FXML
    void signUpLinkAction() {
        // Open the panes
        signUpPane.setVisible(true);
        signupFailPane.setVisible(false);
        loginButton.setDisable(true);
        forgotPasswdLink.setDisable(true);
        signUpLink.setDisable(true);
    }

    // Sign up screen back button
    // GUI stuff below
    @FXML
    void signUpBackButton() {
        // Close the panes
        signUpPane.setVisible(false);
        signupFailPane.setVisible(false);
        loginButton.setDisable(false);
        forgotPasswdLink.setDisable(false);
        signUpLink.setDisable(false);

        // clear the text-fields
        signUpNameField.clear();
        signUpSurnameField.clear();
        signUpUsernameField.clear();
        signUpPhoneField.clear();
        signUpMailField.clear();
        signUpPasswordField.clear();
    }

    /** Sign up the user to the database
     *
     */
    @FXML
    void signUpButtonAction() {
        try {
            // New SQL connection
            database = new PSQLConnection();

            // Registering to the database according to signup info
            user = new MainUserAccount(database, signUpUsernameField.getText(), signUpPasswordField.getText(),
                    signUpNameField.getText(), signUpMailField.getText(), signUpPhoneField.getText());

            // Close the pane. And other GUI stuff below
            signupFailPane.setVisible(false);
            signUpPane.setVisible(false);
            signupFailPane.setVisible(false);
            loginButton.setDisable(false);
            forgotPasswdLink.setDisable(false);
            signUpLink.setDisable(false);

            // Clear previous values
            signUpNameField.clear();
            signUpSurnameField.clear();
            signUpUsernameField.clear();
            signUpPhoneField.clear();
            signUpMailField.clear();
            signUpPasswordField.clear();

        } catch (Exception e) {
            signupFailPane.setVisible(true);
            e.printStackTrace();
        }
    }


    // LOG IN (SIGN IN) PART
    JFXButton lastClickedService;
    static boolean lastClicked = false;


    @FXML
    void signinButtonAction() {
        String[][] services = new String[serviceVbox.getChildren().size()][3];

        // Check the show password flag
        if (showPasswdFlag) {
            loginPasswordField.setText(passwdShowText.getText());
        }

        try {
            // Check the username and password
            UserValidator.validateUser(database, loginUsernameField.getText(), loginPasswordField.getText());

            // GUI stuff below
            servicesPane.setVisible(true);
            failLoginLabel.setVisible(false);

            // Connect the user's database
            user = new MainUserAccount(database, loginUsernameField.getText());

            // Get service info from the database
            final int[] loginIT = {0};
            user.getServices().getHashMap().forEach((serviceName, credentials) -> {
                serviceButton = (JFXButton) serviceVbox.getChildren().get(loginIT[0]);
                serviceButton.setText(serviceName);
                serviceButton.setVisible(true);
                services[loginIT[0]][1] = credentials.getKey();
                services[loginIT[0]][2] = credentials.getValue();
                loginIT[0]++;
            });

            // Set service info to text-fields and labels
            // TL;DR: Lots of GUI stuff below
            for (int k = 0; k < serviceVbox.getChildren().size(); k++) {
                int finalK = k;
                ((JFXButton) serviceVbox.getChildren().get(k)).setOnAction(e -> {

                    serviceShowPassword.setSelected(false);
                    serviceShowFlag = false;
                    serviceInsideShowPassword.setVisible(false);
                    lastClicked = true;

                    // Set service info
                    serviceInsideName.setText(services[finalK][1]);
                    serviceInsidePassword.setText(services[finalK][2]);

                    // Set GUI according to the last clicked service
                    lastClickedService =  ((JFXButton) serviceVbox.getChildren().get(finalK));
                    if(lastClickedService.getText().equals("")) {
                        serviceInsidePane.setVisible(false);
                        deleteServiceButton.setVisible(false);
                        serviceNameShower.setVisible(false);
                    }
                    else {
                        serviceNameShower.setVisible(true);
                        serviceNameShower.setText(lastClickedService.getText());
                        deleteServiceButton.setVisible(true);
                        serviceInsidePane.setVisible(true);
                        serviceInsidePassword.setVisible(true);
                        openinInfoPane.setVisible(false);
                        welcomeLabel.setVisible(false);
                    }

                });
            }

            // Another GUI stuff below
            openinInfoPane.setVisible(true);
            welcomeLabel.setVisible(true);
            deleteServiceButton.setVisible(false);

        } catch (Exception e) {
            failLoginLabel.setVisible(true);
        }
    }

    /**
     * Show password button in the log in (sign in) part
     */
    static boolean showPasswdFlag = false;
    @FXML
    void signinShowPasswordButton() {

        showPasswdFlag = !showPasswdFlag;
        if(showPasswdFlag) {
            loginPasswordField.setVisible(false);
            passwdShowText.setText(loginPasswordField.getText());
            passwdShowText.setVisible(true);
        }
        else {
            loginPasswordField.setVisible(true);
            passwdShowText.setVisible(false);
            loginPasswordField.setText(passwdShowText.getText());
        }
    }

    /** Service show password
     *
     */
    boolean serviceShowFlag = false;
    @FXML
    void serviceShowPasswordButton() {
        serviceShowFlag = !serviceShowFlag;
        serviceInsideShowPassword.setText(serviceInsidePassword.getText());
        if (serviceShowFlag) {
            serviceInsidePassword.setVisible(false);
            serviceInsideShowPassword.setVisible(true);
        }
        else {
            serviceInsidePassword.setVisible(true);
            serviceInsideShowPassword.setVisible(false);
        }

    }

    /** Inside services back button
     *
     */
    @FXML
    void servicesBackButton() {
        // GUI stuff below
        addServiceShowPassword.setSelected(false);
        showPasswdFlag2 = false;
        addServicePane.setVisible(false);
        addServiceNameField.clear();
        addServicePasswdField.clear();
        addServiceShowPasswdText.clear();
        addServiceUserField.clear();
        servicesPane.setDisable(false);
    }

    /** Open add service pane
     *
     */
    @FXML
    void addServicePaneOpenerButton() {
        // GUI stuff below
        addServiceShowPassword.setSelected(false);
        showPasswdFlag2 = false;
        addServicePasswdField.setVisible(true);
        addServiceShowPasswdText.setVisible(false);
        addServicePane.setVisible(true);
        servicesPane.setDisable(true);
    }


    /** Adding services to the database
     *
     * @throws SQLException a random sql exception
     */
    @FXML
    void addServiceButton() throws SQLException {
        if (addServiceNameField.getText().trim().length() > 0) {
            if (showPasswdFlag2) {
                addServicePasswdField.setText(addServiceShowPasswdText.getText());
            }

            // Add service to the database
            user.addNewService(addServiceNameField.getText(), addServiceUserField.getText(), addServicePasswdField.getText());

            // Update the GUI for service names
            final int[] serviceIT = {0};
            user.getServices().getHashMap().forEach((serviceName, credentials) -> {
                serviceButton = (JFXButton) serviceVbox.getChildren().get(serviceIT[0]);
                serviceButton.setText(serviceName);
                serviceButton.setVisible(true);
                serviceIT[0]++;
            });

            signinButtonAction();

            // GUI stuff below
            if (lastClicked) {
                welcomeLabel.setVisible(false);
                openinInfoPane.setVisible(false);
                deleteServiceButton.setVisible(true);

            } else {
                welcomeLabel.setVisible(true);
                openinInfoPane.setVisible(true);
                deleteServiceButton.setVisible(false);
            }

            addServiceNameField.clear();
            addServicePasswdField.clear();
            addServiceShowPasswdText.clear();
            addServiceUserField.clear();

        }
    }

    /**
     * Add service show password button
     */
    static boolean showPasswdFlag2 = false;
    @FXML
    void addServiceShowPasswordButton() {
        showPasswdFlag2 = !showPasswdFlag2;
        if(showPasswdFlag2) {
            addServicePasswdField.setVisible(false);
            addServiceShowPasswdText.setText(addServicePasswdField.getText());
            addServiceShowPasswdText.setVisible(true);
        }
        else {
            addServicePasswdField.setVisible(true);
            addServiceShowPasswdText.setVisible(false);
            addServicePasswdField.setText(addServiceShowPasswdText.getText());
        }

    }


    // Delete services part

    /** Delete service yes no pane opener
     *
     */
    @FXML
    void deleteServicePaneOpenerButton() {
        deleteServicePane.setVisible(true);
        servicesPane.setDisable(true);
        deleteServiceName.setText("\"" + lastClickedService.getText() + "\"");
    }

    // Delete service no button
    // TL;DR: GUI stuff below
    @FXML
    void deleteServiceNoButton() {
        deleteServicePane.setVisible(false);
        servicesPane.setDisable(false);
        deleteServiceName.setText("");
    }

    /** Delete service yes button
     *
     */
    @FXML
    void deleteServiceYesButton() throws SQLException {
        // Remove service from database according to the last clicked service
        user.removeService(lastClickedService.getText());

        // GUI stuff below
        lastClickedService.setText(" ");
        deleteServiceButton.setVisible(false);
        servicesPane.setDisable(false);
        serviceInsidePane.setVisible(false);
        deleteServicePane.setVisible(false);
        serviceNameShower.setVisible(false);
        openinInfoPane.setVisible(true);

        // Clear the buttons
        for(int i = 0; i < serviceVbox.getChildren().size(); i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            serviceButton.setText("");
        }

        // Set back to login position
        signinButtonAction();
        for(int i = 0; i < serviceVbox.getChildren().size(); i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            if (serviceButton.getText().equals("")) {
                serviceButton.setVisible(false);
            }
        }
        welcomeLabel.setVisible(false);
    }


    /** Sign out part
     *
     */
    @FXML
    void signOutButton(ActionEvent event) throws IOException {
        // GUI stuff below
        servicesPane.setVisible(false);
        lastClicked = false;
        serviceInsideName.setText("");
        serviceInsidePassword.setText("");
        loginUsernameField.clear();
        loginPasswordField.clear();
        passwdShowText.clear();
        serviceNameShower.setVisible(false);
        serviceInsidePane.setVisible(false);
        serviceScrollPane.setVvalue(0.0);

        // Clear the buttons
        for(int i = 0; i < serviceVbox.getChildren().size(); i++) {
            serviceButton = (JFXButton) serviceVbox.getChildren().get(i);
            serviceButton.setText("");
        }
    }

    // GUI stuff for cool things (not cool btw)
    @FXML
    void servicesBigButton() {
        deleteServiceButton.setVisible(false);
        serviceInsidePane.setVisible(false);
        openinInfoPane.setVisible(true);
        serviceNameShower.setVisible(false);

    }
}
