package jon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class Main extends Application {
	
	
	public static Scanner reader; 
	public static JSONArray components;
	public static JSONObject mainObj;
	
	public Label message;
	
	public TextField ServersControlVersion;
	public TextField VSimControlVersion;
	public TextField AtmelVersion;
	public TextField Server4GVersion;
	public TextField GUIVersion;
	public TextField LogServiceVersion;
	public TextField APConfigurationVersion;
	public TextField OSVersion;
	public TextField OSBaseVersion;
	public TextField OSFilePath;
	
	public TextField PackageVersion;
	public TextField PackageName;
	public TextField ActionString;
	public TextField BucketId;
	public TextField BucketKey;
	
	
	public TextField CaptivePortalVersion;
	public TextField GUIConfigurationVersion;
	public TextField TelephonyDBVersion;
	public TextField APNVersion;
	public TextField WebServerVersion;
	
	
	
	public TextField[] mandatoryFields;
	public TextField[] nonMandatoryFields;
	public TextField[] packageInfoFields;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    public void init() {
    	reader = new Scanner(System.in); 
    	components = new JSONArray();
    	mainObj = new JSONObject();
    	
    	ServersControlVersion = new TextField("1.12.3");
    	VSimControlVersion = new TextField("1.12.1");
    	AtmelVersion = new TextField( "4.4.9.201708301409");
    	Server4GVersion = new TextField("1.3.0");
    	GUIVersion = new TextField("1.8.7");
    	LogServiceVersion = new TextField("1.1.2");
    	APConfigurationVersion = new TextField("1.2.6");
    	OSVersion = new TextField();
    	OSBaseVersion = new TextField();
    	OSFilePath = new TextField();
    	
    	CaptivePortalVersion = new TextField();
    	GUIConfigurationVersion = new TextField();
    	TelephonyDBVersion = new TextField();
    	APNVersion = new TextField();
    	WebServerVersion = new TextField();
    	
    	PackageVersion = new TextField("2.9.0");
    	PackageName = new TextField("2.9.0");
    	ActionString = new TextField("4");
    	BucketId = new TextField("TBD");
    	BucketKey = new TextField("TBD");
    	
    	mandatoryFields = new TextField [] {ServersControlVersion,VSimControlVersion,AtmelVersion,Server4GVersion,GUIVersion,LogServiceVersion,
    			APConfigurationVersion,OSVersion,OSBaseVersion,OSFilePath};
    	nonMandatoryFields = new TextField [] {CaptivePortalVersion,GUIConfigurationVersion,TelephonyDBVersion,APNVersion, WebServerVersion};
    	packageInfoFields = new TextField[] { PackageVersion, PackageName, ActionString, BucketId, BucketKey};
    	
    	message = new Label(" Okay");
    }
    
    @Override
    public void start(Stage primaryStage) {
    	init(); 
        setGrid(primaryStage);    
        primaryStage.show();
    }
	
	
	
	public void generate() throws IOException{
	
		if (!checkFields())
			message.setText("Error");
		else {
			message.setText(" Okay");
			createMandatoryJson();
			try(FileWriter file = new FileWriter("components.json")) { 	
				file.write(mainObj.toString(1));
				System.out.println("Success");
			}
			catch (Exception o) {
				System.out.println("error");
			}
			components = new JSONArray();
	    	mainObj = new JSONObject();
		}

	}
	
	public void generateOS() throws IOException{
		
		if (!checkOSFields()) 
			message.setText("Error");
		else {
			message.setText(" Okay");
			createOS();
			try(FileWriter file = new FileWriter("jon_package.json")) { 	
				file.write(mainObj.toString(1));
				System.out.println("Success");
			}
			catch (Exception o) {
				System.out.println("error");
			}
			components = new JSONArray();
	    	mainObj = new JSONObject();
		}

	}
	private void createMandatoryJson() {
		
		try {
		
			mainObj.put("package version", PackageVersion.getText());
			mainObj.put("version", PackageVersion.getText());
			mainObj.put("name", PackageName.getText());
			mainObj.put("action", Integer.parseInt(ActionString.getText()));
			mainObj.put("bucket_id", BucketId.getText());
			mainObj.put("bucket_key", BucketKey.getText());
			mainObj.put("components", components);
			addObject(0, "ServersControl", ServersControlVersion.getText() , "vph-components/servers-control/ServersService-v", "Server Service", ".apk");
			addObject(1, "VSimControl", VSimControlVersion.getText(),"vph-components/vsim-control/VSimService-v", "Vsim services", ".apk");
			addObject(2, "Atmel", AtmelVersion.getText(),  "vph-components/atmel/VSim-v", "Atmel", ".sgo");
			addObject(3, "Server 4G", Server4GVersion.getText(),  "vph-components/servers4G/Servers4G-v", "Server 4G", ".apk");
			addObject(4, "GUI", GUIVersion.getText(), "vph-components/gui/gui-v", "GUI", ".apk");
			addObject(5, "Log Service", LogServiceVersion.getText(), "vph-components/log-service/LogService-v", "Log Service", ".apk");
			addObject(6, "AP-Configuration", APConfigurationVersion.getText(), "vph-components/config/config-v", "Configurations", ".json");
			addObject(7, "OS", OSVersion.getText(), "vph-components/OS/" + OSFilePath.getText(), "OS", ".zip", OSBaseVersion.getText() );  /////////////////////////////////
			
			if (!CaptivePortalVersion.getText().trim().isEmpty())
				addObject(100, "Captive Portal", CaptivePortalVersion.getText(), "vph-components/captive-portal/mifi-v", "Captive Portal", ".zip");
			if (!GUIConfigurationVersion.getText().trim().isEmpty())
				addObject(101, "Gui-Confiogurations", GUIConfigurationVersion.getText(), "gui/", "Gui-Configuration", ".zip");
			if (!TelephonyDBVersion.getText().trim().isEmpty())
				addObject(102, "Telephony DB", TelephonyDBVersion.getText(), "vph-components/", "", ".db");
			if (!APNVersion.getText().trim().isEmpty())
				addObject(103, "APN", APNVersion.getText(), "vph-components/", "", ".xml");
			if (!WebServerVersion.getText().trim().isEmpty())
				addObject(104, "WEB SERVER", WebServerVersion.getText(), "vph-components/web-server/WebServer-v", "WEB SERVER", ".apk");
		}
		catch (JSONException ex) {
			System.out.println("error");
		}
		
	}
	private void createOS() {
		
		try {
		
			mainObj.put("package version", PackageVersion.getText());
			mainObj.put("version", PackageVersion.getText());
			mainObj.put("name", PackageName.getText());
			mainObj.put("action", Integer.parseInt(ActionString.getText()));
			mainObj.put("bucket_id", BucketId.getText());
			mainObj.put("bucket_key", BucketKey.getText());
			mainObj.put("components", components);
			addObject(7, "OS", OSVersion.getText(), "vph-components/OS/" + OSFilePath.getText(), "OS", ".zip", OSBaseVersion.getText() );  /////////////////////////////////
			
		}
		catch (JSONException ex) {
			System.out.println("error");
		}
		
	}
	private boolean checkFields() {
		
		for(TextField text : mandatoryFields) {
			if ((text.getText().indexOf(',') >= 0) | (text.getText().isEmpty()))
				return false;
		}
		for(TextField text : nonMandatoryFields) {
			if (text.getText().indexOf(',') >= 0)
				return false;
		}
		for(TextField text : packageInfoFields) {
			if ((text.getText().indexOf(',') >= 0) | (text.getText().isEmpty()) )
				return false;
		}
		return true;
	}
	private boolean checkOSFields() {
		TextField[] OSFields = new TextField[] {OSVersion,OSBaseVersion,OSFilePath};
		for(TextField text : OSFields) {
			if ((text.getText().indexOf(',') >= 0) | (text.getText().isEmpty()))
				return false;
		}
		for(TextField text : packageInfoFields) {
			if ((text.getText().indexOf(',') >= 0) | (text.getText().isEmpty()) )
				return false;
		}
		return true;
	}
	
	private static void addObject(int id, String name, String component_version, String filePath, String description, String type) throws JSONException {
		//System.out.println(name + " - enter component version: ");
		//String component_version = reader.nextLine();
		filePath = filePath + component_version + type;
		JSONObject ob = new JSONObject();
		ob.put("id", id);
		ob.put("name",name);
		ob.put("version", component_version);
		ob.put("filepath", filePath);
		ob.put("description", description);
		
		components.put(ob);
		
		
	}
	private static void addObject(int id, String name, String component_version, String filePath, String description, String type, String baseversion) throws JSONException {
	//	System.out.println(name + " - enter component version: ");
	//	String component_version = reader.nextLine();
	//	filePath = filePath + component_version + type;
		JSONObject ob = new JSONObject();
		ob.put("id", id);
		ob.put("name",name);
		ob.put("version", component_version);
		ob.put("filepath", filePath);
		ob.put("description", description);
		ob.put("base_version", baseversion);
		components.put(ob);
		
		
	}
	
	private void setGrid(Stage primaryStage) {
		primaryStage.setTitle("Simgo Package Generator");
        Button btn = new Button();
        btn.setText("Generate JSON");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                try {
					generate();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        Button btn2 = new Button();
        btn2.setText("Clear All");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
  
            @Override
            public void handle(ActionEvent event) {
            	for(TextField text : mandatoryFields) {
        			text.clear();
        		}
        		for(TextField text : nonMandatoryFields) {
        			text.clear();
        		}
        		for(TextField text : packageInfoFields) {
        			text.clear();
        		}
        		message.setText("clear");
            }
        });
        
        Button btn3 = new Button();
        btn3.setText("Generate OS");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
  
            @Override
            public void handle(ActionEvent event) {
            	try {
					generateOS();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
        
		GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));

        mainGrid.add(btn, 1, 3);
        mainGrid.add(btn3, 0, 3);
        mainGrid.add(btn2, 2, 3);
        mainGrid.add(message, 3, 3);
        Scene scene = new Scene(mainGrid, 1200, 500);
        primaryStage.setScene(scene);
        
        // ------------------
        
        GridPane optional_grid = new GridPane();
        optional_grid.setAlignment(Pos.CENTER);
        optional_grid.setHgap(10);
        optional_grid.setVgap(10);
        optional_grid.setPadding(new Insets(25, 25, 25, 25));
        
        mainGrid.add(optional_grid, 2, 1);
        
        Text opttitle = new Text("Optional Components:");
        opttitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        optional_grid.add(opttitle, 0, 0);
        
        Label CaptivePortal = new Label("Captive Portal version:");
        optional_grid.add(CaptivePortal, 0, 1);
        optional_grid.add(CaptivePortalVersion, 1, 1);
        
        Label GuiConfiguration = new Label("GUI Config (name + version):");
        optional_grid.add(GuiConfiguration, 0, 2);
        optional_grid.add(GUIConfigurationVersion, 1, 2);
        
        Label TelephonyDB = new Label("Telephony DB version:");
        optional_grid.add(TelephonyDB, 0, 3);
        optional_grid.add(TelephonyDBVersion, 1, 3);
        
        Label APN = new Label("APN version:");
        optional_grid.add(APN, 0, 4);
        optional_grid.add(APNVersion, 1, 4);
        
        Label WebServer = new Label("Web Server version:");
        optional_grid.add(WebServer, 0, 5);
        optional_grid.add(WebServerVersion, 1, 5);
        
        
        
        // -------------------
        
        GridPane left_grid = new GridPane();
        left_grid.setAlignment(Pos.CENTER);
        left_grid.setHgap(10);
        left_grid.setVgap(10);
        left_grid.setPadding(new Insets(10, 10, 10, 10));
        
        mainGrid.add(left_grid, 0, 1);
        Text leftGridTitle = new Text("Package Info:");
        leftGridTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        left_grid.add(leftGridTitle, 0, 0);
        
        Label PackageVersionLabel = new Label("Package version:");
        left_grid.add(PackageVersionLabel, 0, 1);
        left_grid.add(PackageVersion, 1, 1);
        
        Label PackageNameLabel = new Label("Package name:");
        left_grid.add(PackageNameLabel, 0, 2);
        left_grid.add(PackageName, 1, 2);
        
        Label ActionStringLabel = new Label("Action:");
        left_grid.add(ActionStringLabel, 0, 3);
        left_grid.add(ActionString, 1, 3);
        
        Label BucketIdLabel = new Label("Bucket ID:");
        left_grid.add(BucketIdLabel, 0, 4);
        left_grid.add(BucketId, 1, 4);
        
        Label BucketKeyLabel = new Label("Bucket Key:");
        left_grid.add(BucketKeyLabel, 0, 5);
        left_grid.add(BucketKey, 1, 5);
        
        // -------------------
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        mainGrid.add(grid, 1, 1);
        
        Text scenetitle = new Text("Component Versions:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0);

        int i = 0; int j = 1;
        Label ServersControl = new Label("ServersControl version:");
        grid.add(ServersControl, i, j);
        i++;
        grid.add(ServersControlVersion, i, j);
        i = 0; j++;

        Label VSimControl = new Label("VSimControl version:");
        grid.add(VSimControl, i, j);
        i++;
        grid.add(VSimControlVersion, i, j);
        i = 0; j++;
        
        Label Atmel = new Label("Atmel version:");
        grid.add(Atmel, i, j);
        i++;
        grid.add(AtmelVersion, i, j);
        i = 0; j++;
        
        Label Server4G = new Label("Server 4G version:");
        grid.add(Server4G, i, j);
        i++;
        grid.add(Server4GVersion, i, j);
        i = 0; j++;
        
        Label GUI = new Label("Gui version:");
        grid.add(GUI, i, j);
        i++;
        grid.add(GUIVersion, i, j);
        i = 0; j++;
        
        Label LogService = new Label("Log Service version:");
        grid.add(LogService, i, j);
        i++;
        grid.add(LogServiceVersion, i, j);
        i = 0; j++;
        
        Label APConfiguration = new Label("AP-Configuration version:");
        grid.add(APConfiguration, i, j);
        i++;
        grid.add(APConfigurationVersion, i, j);
        i = 0; j++;
        
        Label OS = new Label("OS version:");
        grid.add(OS, i, j);
        i++;
        grid.add(OSVersion, i, j);
        i = 0; j++;
        
        Label OSBase = new Label("OS Base version:");
        grid.add(OSBase, i, j);
        i++;
        grid.add(OSBaseVersion, i, j);
        i = 0; j++;
        
        Label OSFilePathLabel = new Label("OS file path: (vph-components/OS/...)");
        grid.add(OSFilePathLabel, i, j);
        i++;
        grid.add(OSFilePath, i, j);
        i = 0; j++;
	}

}

