package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneratePasswordController {
    @FXML
    private Slider sliderBar;
    @FXML
    private TextField textFieldSlider;
    @FXML
    private TextField passwordField;
    @FXML
    private CheckBox cbNumbers;
    @FXML
    private CheckBox cbUppercase;
    @FXML
    private CheckBox cbLowercase;
    @FXML
    private CheckBox cbSymbols;
    @FXML
    private Button generatePasswordButton;
    @FXML
    private Button copyButton;

    @FXML
    public void initialize(){
        // Synchronize slider value with text field
        textFieldSlider.textProperty().bind(sliderBar.valueProperty().asString("%.0f"));
        // Set default password length in the slider
        sliderBar.setValue(12); // Default value, can be changed as needed
    }
    public String onGeneratePsswdBtnPressed(boolean cbNumbers, boolean cbUppercase, boolean cbLowercase, boolean cbSymbols, int length){
        StringBuilder password = new StringBuilder();
        List<Character> selectedSymbols = new ArrayList<>();

        if (cbNumbers)
            selectedSymbols.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        if (cbUppercase)
            selectedSymbols.addAll(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
        if (cbLowercase)
            selectedSymbols.addAll(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
        if (cbSymbols)
            selectedSymbols.addAll(Arrays.asList('~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '{', '}', '[', ']', '|', '\\', ':', ';', '"', '<', ',', '>', '.', '?', '/'));

        Random random = new Random();
        for(int i = 0; i < length; i++){
            if(!selectedSymbols.isEmpty()){
                char randomChar = selectedSymbols.get(random.nextInt(selectedSymbols.size()));
                password.append(randomChar);
            }
        }
        return password.toString();
    }
    @FXML
    protected void onGeneratePasswordClick(){
        int length = (int) sliderBar.getValue();
        boolean includeNumbers = cbNumbers.isSelected();
        boolean includeUppercase = cbUppercase.isSelected();
        boolean includeLowercase = cbLowercase.isSelected();
        boolean includeSymbols = cbSymbols.isSelected();

        String generatedPassword = onGeneratePsswdBtnPressed(includeNumbers, includeUppercase, includeLowercase, includeSymbols, length);
        passwordField.setText(generatedPassword);
    }
    @FXML
    protected void onCopyPasswordClick(){
        final String password = passwordField.getText();
        if (password != null && !password.isEmpty()){
            final javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
            final javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(password);
            clipboard.setContent(content);

        }
    }
}
