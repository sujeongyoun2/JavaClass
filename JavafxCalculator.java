/*
    This is to practice how to use JavaFx for building Java application 
*/
package javafxcalculator;

import javafx.scene.input.MouseEvent;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
/**
 *
 * @author suyoun
 */
public class JavafxCalculator extends Application {

    final int MAX_INPUT_LENGTH = 20;	//최대한 입력 가능한 길이를 제한하고
	
     //각 모드별로 index를 부여
    final int INPUT_MODE = 0;
    final int RESULT_MODE = 1;
    final int ERROR_MODE = 2;
    int displayMode;
    
    // Button Styles
    final static String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
    final static String HOVERED_BUTTON_STYLE = "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;";

    boolean clearOnNextDigit; 	//화면에 표시될 숫자를 지울지 말지 결정하는 녀석

    double lastNumber;		//마지막에 기억될 수
    String lastOperator;		//마지막에 누른 연산자를 기억.
	
    private GridPane gridPane;
    private Scene masterPanel;
    private Label output;				 //숫자가 표시될 공간
    private Label preOutput;
    private Button eraseBtn, ceBtn, cBtn, oneBtn, twoBtn, threeBtn,
                    fourBtn, fiveBtn, sixBtn, sevenBtn, eightBtn, nineBtn,
                    zeroBtn, divBtn, multiplyBtn, subtractBtn, plusBtn,
                    sqrBtn, oneOverXBtn, pctBtn, equalBtn, plusMinusBtn, dotBtn;    
    
    private HBox ctrlBox, numBoxLn1, numBoxLn2, numBoxLn3, numBoxLn4;
    
    private void handleButtonClick(MouseEvent event){
        double result = 0;
        
        // 버튼이 눌렸을때 그 버튼 값을 여기로 저장합니다.
        Object temp = event.getSource();
        
        /* 저는 우선 클릭된 버튼이 어떤버튼인지 확인하기 위해 if문으로 길게 길게 썼습니다. 
        많이 귀찮을 수도 있지만, 제 수준에선 이렇게 밖에 생각이 안나더라구요...호호 */
        if(temp == oneBtn || temp == twoBtn || temp == threeBtn ||
                temp == fourBtn || temp == fiveBtn || temp == sixBtn ||
                temp == sevenBtn || temp == eightBtn || temp == nineBtn ||
                temp == zeroBtn){
                    addToDisplay(Integer.parseInt(((Button)temp).getText()));
        } else if(temp == plusMinusBtn) {
                processSingChange();
        } else if(temp == dotBtn) {
                addPoint();
        } else if(temp == equalBtn) {
                processEquals();
        } else if(temp == divBtn) {
                processOperator("/");
        } else if(temp == multiplyBtn) {
                processOperator("*");
        } else if(temp == plusBtn) {
                processOperator("+");
        } else if(temp == subtractBtn) {
                processOperator("-");
        } else if(temp == sqrBtn) {
                if (displayMode != ERROR_MODE){
                    try{
			if (getDisplayString().indexOf("-") == 0)
                            displayError("Invalid input for function!");
                        result = Math.sqrt(getNumberInDisplay());
                        displayResult(result);
                    }
                    catch(Exception ex){
			displayError("Invalid input for function!");
			displayMode = ERROR_MODE;
                    }
                }
        } else if(temp == subtractBtn) {
                if (displayMode != ERROR_MODE){
                    try{
			if (getNumberInDisplay() == 0)
                            displayError("Invalid input for function!");
                        result = 1 / getNumberInDisplay();
                        displayResult(result);
                    }
                    catch(Exception ex){
			displayError("Invalid input for function!");
			displayMode = ERROR_MODE;
                    }
                }
        } else if(temp == pctBtn) {
                if (displayMode != ERROR_MODE){
                    try{
			result = getNumberInDisplay() / 100;
                        displayResult(result);
                    }
                    catch(Exception ex){
			displayError("Invalid input for function!");
			displayMode = ERROR_MODE;
                    }
                }
        } else if(temp == pctBtn) {
                backspace();
        } else if(temp == ceBtn) {
                setDisplayString("0");
		clearOnNextDigit = true;
		displayMode = INPUT_MODE;
        } else if(temp == cBtn) {
                clearAll();
        }
        
    }
    
    // 여기서부터 start메소드 전까지는 전에 swing으로 구현한 기능과 동일합니다.
    
    private void clearAll() {
		setDisplayString("0");
		lastOperator = "0";
		lastNumber = 0;
		displayMode = INPUT_MODE;
		clearOnNextDigit = true;
    }
    
    private void backspace(){
		if (displayMode != ERROR_MODE){
			setDisplayString(getDisplayString().substring(0,
					  getDisplayString().length() - 1));
			
			if (getDisplayString().length() < 1)
				setDisplayString("0");
		}
    }
    
    private void processOperator(String string) {
		if (displayMode != ERROR_MODE){
			double numberInDisplay = getNumberInDisplay();

			if (!lastOperator.equals("0")){
				try	{
					double result = processLastOperator();
					displayResult(result);
					lastNumber = result;
				}catch(Exception e){}
			
			}else{
				lastNumber = numberInDisplay;
			}
			
			clearOnNextDigit = true;
			lastOperator = string;
		}
    }
    
    private void processEquals() {
		double result = 0;

		if (displayMode != ERROR_MODE){
			try{
				result = processLastOperator();
				displayResult(result);
			}catch (Exception e){
				displayError("0으로 나눌 수 없습니다.");
			}
			lastOperator = "0";
		}
    }
    
    private void displayError(String error) {
		setDisplayString(error);
		lastNumber = 0;
		displayMode = ERROR_MODE;
		clearOnNextDigit = true;
    }
    
    private double processLastOperator() throws Exception{
		double result = 0;
		double numberInDisplay = getNumberInDisplay();

		if (lastOperator.equals("/")){
			if (numberInDisplay == 0)
				throw (new Exception());

			result = lastNumber / numberInDisplay;
		}
			
		if (lastOperator.equals("*")){
			result = lastNumber * numberInDisplay;
		}
		if (lastOperator.equals("-")){
			result = lastNumber - numberInDisplay;
		}
		if (lastOperator.equals("+")){
			result = lastNumber + numberInDisplay;
		}
		
		return result;
    }
    
    private void addPoint() {
		displayMode = INPUT_MODE;

		if (clearOnNextDigit)
			setDisplayString("");

		String inputString = getDisplayString();
	
		// 이미 점이 찍혀있으면 찍지 않는다.
		if (inputString.indexOf(".") < 0)
			setDisplayString(new String(inputString + "."));
    }
    
    private void processSingChange() {
		if (displayMode == INPUT_MODE){
			String input = getDisplayString();
			if (input.length() > 0 && !input.equals("0")){
				if (input.indexOf("-") == 0)
					setDisplayString(input.substring(1));
				else
					setDisplayString("-" + input);
			}
		}else if (displayMode == RESULT_MODE){
			double numberInDisplay = getNumberInDisplay();
		
			if (numberInDisplay != 0)
				displayResult(-numberInDisplay);
		}
    }
    
    private void displayResult(double result) {
		setDisplayString(Double.toString(result));
		lastNumber = result;
		displayMode = RESULT_MODE;
		clearOnNextDigit = true;
    }
    
    private double getNumberInDisplay() {
		String input = output.getText();
		return Double.parseDouble(input);
    }
    
    private void addToDisplay(int i) {
	if (clearOnNextDigit)
            setDisplayString("");
		
        String inputString = getDisplayString();
	
	if(inputString.indexOf("0") == 0){
            inputString = inputString.substring(1);
	}
	
        if(( !inputString.equals("0") || i>0 ) && inputString.length() < MAX_INPUT_LENGTH){
            setDisplayString(inputString + i);
	}
		
	displayMode = INPUT_MODE;
	clearOnNextDigit = false;
    }
    
    private void setDisplayString(String string) {
	output.setText(string);
    }
    
    private String getDisplayString() {
	return output.getText();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        ctrlBox = new HBox();
        numBoxLn1 = new HBox();
        numBoxLn2 = new HBox();
        numBoxLn3 = new HBox();
        numBoxLn4 = new HBox();
        
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
//        gridPane.setHgap(5);
//        gridPane.setVgap(5);
        gridPane.setPrefSize(300, 300);
        gridPane.setMaxSize(300, 300);
        
        preOutput = new Label ("");
        preOutput.setAlignment(Pos.CENTER_LEFT);
        output = new Label ("0");
        output.setAlignment(Pos.CENTER_LEFT);
        
        /* 저는 버튼을 만들면서 이벤트 핸들링을 할 수 있는 기능을 그 때 그 때 지정해주었습니다. */
        
        //컨트롤 버튼
        eraseBtn = new Button ("←");
        eraseBtn.setOnMouseClicked(event -> handleButtonClick(event));
        ceBtn = new Button ("CE");
        ceBtn.setOnMouseClicked(event -> handleButtonClick(event));
        cBtn = new Button ("C");
        cBtn.setOnMouseClicked(event -> handleButtonClick(event));
        
        //숫자 버튼
        oneBtn = new Button ("1");
        oneBtn.setOnMouseClicked(event -> handleButtonClick(event));
        twoBtn = new Button ("2");
        twoBtn.setOnMouseClicked(event -> handleButtonClick(event));
        threeBtn = new Button ("3");
        threeBtn.setOnMouseClicked(event -> handleButtonClick(event));
        fourBtn = new Button ("4");
        fourBtn.setOnMouseClicked(event -> handleButtonClick(event));
        fiveBtn = new Button ("5");
        fiveBtn.setOnMouseClicked(event -> handleButtonClick(event));
        sixBtn = new Button ("6");
        sixBtn.setOnMouseClicked(event -> handleButtonClick(event));
        sevenBtn = new Button ("7");
        sevenBtn.setOnMouseClicked(event -> handleButtonClick(event));
        eightBtn = new Button ("8");
        eightBtn.setOnMouseClicked(event -> handleButtonClick(event));
        nineBtn = new Button ("9");
        nineBtn.setOnMouseClicked(event -> handleButtonClick(event));
        zeroBtn = new Button ("0");
        zeroBtn.setOnMouseClicked(event -> handleButtonClick(event));
        
        //연산 버튼
        plusMinusBtn = new Button ("±");
        plusMinusBtn.setOnMouseClicked(event -> handleButtonClick(event));
        dotBtn = new Button (".");
        dotBtn.setOnMouseClicked(event -> handleButtonClick(event));
        divBtn = new Button ("/");
        divBtn.setOnMouseClicked(event -> handleButtonClick(event));
        multiplyBtn = new Button ("*");
        multiplyBtn.setOnMouseClicked(event -> handleButtonClick(event));
        subtractBtn = new Button ("-");
        subtractBtn.setOnMouseClicked(event -> handleButtonClick(event));
        plusBtn = new Button ("+");
        plusBtn.setOnMouseClicked(event -> handleButtonClick(event));
        sqrBtn = new Button ("√");
        sqrBtn.setOnMouseClicked(event -> handleButtonClick(event));
        oneOverXBtn = new Button ("1/x");
        oneOverXBtn.setOnMouseClicked(event -> handleButtonClick(event));
        pctBtn = new Button ("%");
        pctBtn.setOnMouseClicked(event -> handleButtonClick(event));
        equalBtn = new Button ("=");  
        equalBtn.setOnMouseClicked(event -> handleButtonClick(event));
        
        clearAll();
        
        //HBox에 버튼들 넣어주기
        ctrlBox.getChildren().addAll(eraseBtn, ceBtn, cBtn);
        eraseBtn.prefWidthProperty().bind(ctrlBox.widthProperty().divide(ctrlBox.getChildren().size()));
        eraseBtn.prefHeightProperty().bind(ctrlBox.heightProperty());
        ceBtn.prefWidthProperty().bind(ctrlBox.widthProperty().divide(ctrlBox.getChildren().size()));
        ceBtn.prefHeightProperty().bind(ctrlBox.heightProperty());
        cBtn.prefWidthProperty().bind(ctrlBox.widthProperty().divide(ctrlBox.getChildren().size()));
        cBtn.prefHeightProperty().bind(ctrlBox.heightProperty());
        
        numBoxLn1.getChildren().addAll(sevenBtn, eightBtn, nineBtn, divBtn, sqrBtn);
        sevenBtn.prefWidthProperty().bind(numBoxLn1.widthProperty().divide(numBoxLn1.getChildren().size()));
        sevenBtn.prefHeightProperty().bind(numBoxLn1.heightProperty());
        eightBtn.prefWidthProperty().bind(numBoxLn1.widthProperty().divide(numBoxLn1.getChildren().size()));
        eightBtn.prefHeightProperty().bind(numBoxLn1.heightProperty());
        nineBtn.prefWidthProperty().bind(numBoxLn1.widthProperty().divide(numBoxLn1.getChildren().size()));
        nineBtn.prefHeightProperty().bind(numBoxLn1.heightProperty());
        divBtn.prefWidthProperty().bind(numBoxLn1.widthProperty().divide(numBoxLn1.getChildren().size()));
        divBtn.prefHeightProperty().bind(numBoxLn1.heightProperty());
        sqrBtn.prefWidthProperty().bind(numBoxLn1.widthProperty().divide(numBoxLn1.getChildren().size()));
        sqrBtn.prefHeightProperty().bind(numBoxLn1.heightProperty());
        
        numBoxLn2.getChildren().addAll(fourBtn, fiveBtn, sixBtn, multiplyBtn, oneOverXBtn);
        fourBtn.prefWidthProperty().bind(numBoxLn2.widthProperty().divide(numBoxLn2.getChildren().size()));
        fourBtn.prefHeightProperty().bind(numBoxLn2.heightProperty());
        fiveBtn.prefWidthProperty().bind(numBoxLn2.widthProperty().divide(numBoxLn2.getChildren().size()));
        fiveBtn.prefHeightProperty().bind(numBoxLn2.heightProperty());
        sixBtn.prefWidthProperty().bind(numBoxLn2.widthProperty().divide(numBoxLn2.getChildren().size()));
        sixBtn.prefHeightProperty().bind(numBoxLn2.heightProperty());
        multiplyBtn.prefWidthProperty().bind(numBoxLn2.widthProperty().divide(numBoxLn2.getChildren().size()));
        multiplyBtn.prefHeightProperty().bind(numBoxLn2.heightProperty());
        oneOverXBtn.prefWidthProperty().bind(numBoxLn2.widthProperty().divide(numBoxLn2.getChildren().size()));
        oneOverXBtn.prefHeightProperty().bind(numBoxLn2.heightProperty());
        
        numBoxLn3.getChildren().addAll(oneBtn, twoBtn, threeBtn, subtractBtn, pctBtn);
        oneBtn.prefWidthProperty().bind(numBoxLn3.widthProperty().divide(numBoxLn3.getChildren().size()));
        oneBtn.prefHeightProperty().bind(numBoxLn3.heightProperty());
        twoBtn.prefWidthProperty().bind(numBoxLn3.widthProperty().divide(numBoxLn3.getChildren().size()));   
        twoBtn.prefHeightProperty().bind(numBoxLn3.heightProperty());
        threeBtn.prefWidthProperty().bind(numBoxLn3.widthProperty().divide(numBoxLn3.getChildren().size()));
        threeBtn.prefHeightProperty().bind(numBoxLn3.heightProperty());
        subtractBtn.prefWidthProperty().bind(numBoxLn3.widthProperty().divide(numBoxLn3.getChildren().size()));
        subtractBtn.prefHeightProperty().bind(numBoxLn3.heightProperty());
        pctBtn.prefWidthProperty().bind(numBoxLn3.widthProperty().divide(numBoxLn3.getChildren().size()));
        pctBtn.prefHeightProperty().bind(numBoxLn3.heightProperty());
        
        numBoxLn4.getChildren().addAll(zeroBtn, plusMinusBtn, dotBtn, plusBtn, equalBtn); 
        zeroBtn.prefWidthProperty().bind(numBoxLn4.widthProperty().divide(numBoxLn4.getChildren().size()));
        zeroBtn.prefHeightProperty().bind(numBoxLn4.heightProperty());
        plusMinusBtn.prefWidthProperty().bind(numBoxLn4.widthProperty().divide(numBoxLn4.getChildren().size()));
        plusMinusBtn.prefHeightProperty().bind(numBoxLn4.heightProperty());
        dotBtn.prefWidthProperty().bind(numBoxLn4.widthProperty().divide(numBoxLn4.getChildren().size()));
        dotBtn.prefHeightProperty().bind(numBoxLn4.heightProperty());
        plusBtn.prefWidthProperty().bind(numBoxLn4.widthProperty().divide(numBoxLn4.getChildren().size()));
        plusBtn.prefHeightProperty().bind(numBoxLn4.heightProperty());
        equalBtn.prefWidthProperty().bind(numBoxLn4.widthProperty().divide(numBoxLn4.getChildren().size()));
        equalBtn.prefHeightProperty().bind(numBoxLn4.heightProperty());
        
        ctrlBox.setMaxWidth(300);
        ctrlBox.setMinWidth(300);
        
        gridPane.add(preOutput, 0, 0);
        gridPane.add(output, 0, 1);
        gridPane.add(ctrlBox, 0, 2);
        gridPane.add(numBoxLn1, 0, 3);
        gridPane.add(numBoxLn2, 0, 4);
        gridPane.add(numBoxLn3, 0, 5);
        gridPane.add(numBoxLn4, 0, 6);
        
        preOutput.prefHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getChildren().size()));
        output.prefHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getChildren().size()));
        ctrlBox.prefHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getChildren().size()));
        numBoxLn1.prefHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getChildren().size()));
        numBoxLn2.prefHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getChildren().size()));
        numBoxLn3.prefHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getChildren().size()));
        numBoxLn4.prefHeightProperty().bind(gridPane.heightProperty().divide(gridPane.getChildren().size()));
                
        GridPane.setHalignment(output,HPos.RIGHT);
        
//        ctrlBox.setStyle("-fx-background-color:red");
        
        masterPanel = new Scene(gridPane, 300, 300);
        
        primaryStage.setTitle("JavaFx Calculator");
        primaryStage.setScene(masterPanel);
        primaryStage.show();
        
    }
    
    public static void main(String[] args) {
        
        launch(args);
    
    }
    
}
