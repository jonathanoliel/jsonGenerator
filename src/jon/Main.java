package jon;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

import org.controlsfx.control.CheckListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.cell.*;

public class Main extends Application {
	
	
	public static Scanner reader; 
	public static JSONArray components;
	public static JSONObject mainObj;
	public Gson gson = new Gson();
	
	public Label message;
	
	public TextField ServersControlVersion,VSimControlVersion,AtmelVersion,Server4GVersion,GUIVersion,LogServiceVersion,APConfigurationVersion,OSVersion,OSBaseVersion,OSFilePath;
	public TextField PackageVersion,PackageName,ActionString,BucketId,BucketKey;		
	public TextField CaptivePortalVersion,GUIConfigurationVersion,TelephonyDBVersion,APNVersion,WebServerVersion;
	public TextField[] mandatoryFields,nonMandatoryFields,packageInfoFields;	
	public Button btn,btn2,btn3, openButton;
	public CheckListView<String> list;
	
    public ObservableList<TableEntry1> packageInfo; 
    public ObservableList<TableEntry2> componentInfo; 
    public ObservableList<TableEntry3> optionalInfo;
    public FileChooser fileChooser;
	public TableView<TableEntry1> table1;
	public TableView<TableEntry2> table2;
	public TableView<TableEntry3> table3;
	public TextField GenerateOSName, GenerateJSONName;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    public void initFields() {
    	
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
    			APConfigurationVersion,OSVersion,OSFilePath};
    	nonMandatoryFields = new TextField [] {OSBaseVersion, CaptivePortalVersion,GUIConfigurationVersion,TelephonyDBVersion,APNVersion, WebServerVersion};
    	packageInfoFields = new TextField[] { PackageVersion, PackageName, ActionString, BucketId, BucketKey};
    	
    	message = new Label(" Okay");
    	btn = new Button();
        btn2 = new Button();
        btn3 = new Button();
        openButton = new Button();
        table1 = new TableView<TableEntry1>();
        table2 = new TableView<TableEntry2>();
        table3 = new TableView<TableEntry3>();
        list = new CheckListView<>();
        packageInfo = FXCollections.observableArrayList();
        componentInfo = FXCollections.observableArrayList();
        optionalInfo = FXCollections.observableArrayList();
        fileChooser = new FileChooser();
        
        GenerateOSName = new TextField();
        GenerateJSONName = new TextField();
        		
        
    }
    
    @Override
    public void start(Stage primaryStage) {
    	initFields(); 
    	primaryStage.getIcons().add(new Image("file:logo.png"));
       // setGrid(primaryStage);   
    	File file = new File("src/configurations.json");
        try {
			openFile(new FileReader(file),true);
		} catch (FileNotFoundException e1) {	
			e1.printStackTrace();
		}
        setGrid(primaryStage);
        primaryStage.show();
    }

	
	public void generate() throws IOException{
	
		if (!checkFields())
			message.setText("Error");
		else {
			message.setText(" Okay");
			createMandatoryJson();
			try(FileWriter file = new FileWriter(GenerateJSONName.getText())) { 	
				file.write(mainObj.toString(1));
				System.out.println("Success");
			}
			catch (Exception o) {
				System.out.println("error");
				message.setText("error");
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
			try(FileWriter file = new FileWriter(GenerateOSName.getText())) { 	
				file.write(mainObj.toString(1));
				System.out.println("Success");
			}
			catch (Exception o) {
				System.out.println("error");
				message.setText("error");
			}
			components = new JSONArray();
	    	mainObj = new JSONObject();
		}

	}
	private void createMandatoryJson() {
		
		try {
			for (int i = 0; i < packageInfo.size(); i ++) {
				if (packageInfo.get(i).getKeyName().equals("action"))
					mainObj.put(packageInfo.get(i).getKeyName(), Integer.parseInt(packageInfo.get(i).getValueName()));
				else
					mainObj.put(packageInfo.get(i).getKeyName(), packageInfo.get(i).getValueName());
			}
			mainObj.put("components", components);
			for (int i = 0; i < componentInfo.size(); i ++) {
				String version = componentInfo.get(i).getVersionName();
				switch(componentInfo.get(i).getIdValue()) {
				case(0): 
					addObject(0, "ServersControl", 	version , 	"vph-components/servers-control/ServersService-v", "Server Service", 	".apk");
					break;
				case(1):
					addObject(1, "VSimControl", 	version,	"vph-components/vsim-control/VSimService-v", 		"Vsim services", 	".apk");
					break;
				case(2):
					addObject(2, "Atmel", 			version,  	"vph-components/atmel/VSim-v", 						"Atmel", 			".sgo");
					break;
				case(3):
					addObject(3, "Server 4G", 		version,  	"vph-components/servers4g/Servers4G-v", 			"Server 4G", 		".apk");
					break;
				case(4):
					addObject(4, "GUI", 			version,	"vph-components/gui/Gui-v", 						"GUI", 				".apk");
					break;
				case(5):
					addObject(5, "Log Service", 	version,	"vph-components/log-service/LogService-v", 			"Log Service", 		".apk");
					break;
				case(6):
					addObject(6, "AP-Configuration",version, 	"vph-components/config/config-v", 					"Configurations", 	".json");
					break;
				case(7):
					addObject(7, "OS",				version, 	 OSFilePath.getText()					, "OS", ".zip", OSBaseVersion.getText() );  
					break;
				}	
			}
			for (int i = 0; i < optionalInfo.size(); i ++) {
				if (!optionalInfo.get(i).getActive())
					continue;
				String version = optionalInfo.get(i).getVersionName();
				switch(optionalInfo.get(i).getIdValue()) {
				case(100): 
					addObject(100, "Captive Portal", 		version, 	"vph-components/captive-portal/mifi-v", 	"Captive Portal", 	".zip");
					break;
				case(101):
					addObject(101, "Gui-Configurations", 	version, 	"gui/", 									"Gui-Configuration",".zip");
					break;
				case(102):
					addObject(102, "Telephony DB", 			version, 	"vph-components/", 							"", 				".db");
					break;
				case(103):
					addObject(103, "APN", 					version, 	"vph-components/", 							"",					".xml");
					break;
				case(104):
					addObject(104, "WEB SERVER",			 version, 	"vph-components/web-server/WebServers-v",	"WEB SERVER", 		".apk");
					break;
				}	
			}
		/*
			mainObj.put("package_version", PackageVersion.getText());
			mainObj.put("version", PackageVersion.getText());
			mainObj.put("name", PackageName.getText());
			mainObj.put("action", Integer.parseInt(ActionString.getText()));
			if (!BucketId.getText().trim().isEmpty())
				mainObj.put("bucket_id", BucketId.getText());
			if (!BucketKey.getText().trim().isEmpty())
				mainObj.put("bucket_key", BucketKey.getText());
			mainObj.put("components", components);
			addObject(0, "ServersControl", 	ServersControlVersion.getText() , 	"vph-components/servers-control/ServersService-v", "Server Service", 	".apk");
			addObject(1, "VSimControl", 	VSimControlVersion.getText(),		"vph-components/vsim-control/VSimService-v", 		"Vsim services", 	".apk");
			addObject(2, "Atmel", 			AtmelVersion.getText(),  			"vph-components/atmel/VSim-v", 						"Atmel", 			".sgo");
			addObject(3, "Server 4G", 		Server4GVersion.getText(),  		"vph-components/servers4g/Servers4G-v", 			"Server 4G", 		".apk");
			addObject(4, "GUI", 			GUIVersion.getText(), 				"vph-components/gui/Gui-v", 						"GUI", 				".apk");
			addObject(5, "Log Service", 	LogServiceVersion.getText(), 		"vph-components/log-service/LogService-v", 			"Log Service", 		".apk");
			addObject(6, "AP-Configuration",APConfigurationVersion.getText(), 	"vph-components/config/config-v", 					"Configurations", 	".json");
			addObject(7, "OS", 				OSVersion.getText(), 				"vph-components/OS/" + OSFilePath.getText(), 		"OS", 				".zip",		OSBaseVersion.getText());  
			
			if (!CaptivePortalVersion.getText().trim().isEmpty())
				addObject(100, "Captive Portal", 		CaptivePortalVersion.getText(), 	"vph-components/captive-portal/mifi-v", 	"Captive Portal", 	".zip");
			if (!GUIConfigurationVersion.getText().trim().isEmpty())
				addObject(101, "Gui-Configurations", 	GUIConfigurationVersion.getText(), 	"gui/", 									"Gui-Configuration",".zip");
			if (!TelephonyDBVersion.getText().trim().isEmpty())
				addObject(102, "Telephony DB", 			TelephonyDBVersion.getText(), 		"vph-components/", 							"", 				".db");
			if (!APNVersion.getText().trim().isEmpty())
				addObject(103, "APN", 					APNVersion.getText(), 				"vph-components/", 							"",					".xml");
			if (!WebServerVersion.getText().trim().isEmpty())
				addObject(104, "WEB SERVER",			 WebServerVersion.getText(), 		"vph-components/web-server/WebServers-v",	"WEB SERVER", 		".apk");
	*/
		}
		catch (JSONException ex) {
			System.out.println("error");
		}
		
	}
	private void createOS() {
		
		try {
			for (int i = 0; i < packageInfo.size(); i ++) {
				if (packageInfo.get(i).getKeyName().equals("action"))
					mainObj.put(packageInfo.get(i).getKeyName(), Integer.parseInt(packageInfo.get(i).getValueName()));
				else
					mainObj.put(packageInfo.get(i).getKeyName(), packageInfo.get(i).getValueName());
			}
			mainObj.put("components", components);
			for (int i = 0; i < componentInfo.size(); i ++) {
				switch(componentInfo.get(i).getCompName()) {
				case("OS"):
					addObject(7, "OS",	componentInfo.get(i).getVersionName(),  OSFilePath.getText(), "OS", ".zip", OSBaseVersion.getText() );  
					break;
				}	
			}
		}
		catch (JSONException ex) {
			System.out.println("error");
		}
		
	}
	private boolean checkFields() {
		if (!checkOSFields())
			return false;
		if (componentInfo.size() < 7)
			return false;
		for (int i = 0; i < componentInfo.size(); i ++) {
			if (componentInfo.get(i).getVersionName().isEmpty() | componentInfo.get(i).getVersionName().indexOf(',') >= 0)
				return false;
		}
		for (int i = 0; i < optionalInfo.size(); i ++) {
			if (optionalInfo.get(i).getActive() && (optionalInfo.get(i).getVersionName().isEmpty() | optionalInfo.get(i).getVersionName().indexOf(',') >= 0))
				return false;
		}
	/*	for(TextField text : mandatoryFields) {
			if ((text.getText().indexOf(',') >= 0) | (text.getText().isEmpty()))
				return false;
		}
		for(TextField text : nonMandatoryFields) {
			if (text.getText().indexOf(',') >= 0)
				return false;
		}
		for(TextField text : packageInfoFields) {
			if ((text.getText().indexOf(',') >= 0))
				return false;
		}
	*/
		return true;
	}
	private boolean checkOSFields() {
		for (int i = 0; i < packageInfo.size(); i ++) {
			if (packageInfo.get(i).getValueName().isEmpty() | packageInfo.get(i).getValueName().indexOf(',') >= 0)
				return false;
		}
		for (int i = 0; i < componentInfo.size(); i ++) {
			if (componentInfo.get(i).getIdValue() == 7) {
				if (componentInfo.get(i).getVersionName().isEmpty() | componentInfo.get(i).getVersionName().indexOf(',') >= 0 )
					return false;
				if ((OSBaseVersion.getText().indexOf(',') >= 0) | (OSBaseVersion.getText().isEmpty() ))
					return false;
			}
		}
		/*	TextField[] OSFields = new TextField[] {OSVersion,OSFilePath};
		for(TextField text : OSFields) {
			if ((text.getText().indexOf(',') >= 0) | (text.getText().isEmpty()))
				return false;
		}
		for(TextField text : packageInfoFields) {
			if ((text.getText().indexOf(',') >= 0) )
				return false;
		}
		*/
		return true;
	
	}
	
	private static void addObject(int id, String name, String component_version, String filePath, String description, String type) throws JSONException {
		
		filePath = (id == 101) ?  filePath + component_version : filePath + component_version + type;
		JSONObject ob = new JSONObject();
		ob.put("id", id);
		ob.put("name",name);
		ob.put("version", component_version);
		ob.put("filepath", filePath);
		ob.put("description", description);
		components.put(ob);		
	}
	//	For OS 
	private static void addObject(int id, String name, String component_version, String filePath, String description, String type, String baseversion) throws JSONException {
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
		FileChooser fileChooser = new FileChooser();
        
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
        
        openButton.setText("Open JSON");
        openButton.setOnAction( new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        
                        if (file != null) {
                            try {
								openFile(new FileReader(file),false);
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
                        }
                    }
                });
        
		GridPane mainGrid = new GridPane();
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);
        mainGrid.setPadding(new Insets(25, 25, 25, 25));

        final HBox hb1 = new HBox();
        hb1.getChildren().addAll(btn3,GenerateOSName);
        hb1.setSpacing(3);
        GenerateOSName.setText("jon_package.json");
        final HBox hb2 = new HBox();
        hb2.getChildren().addAll(btn,GenerateJSONName);
        hb2.setSpacing(3);
        GenerateJSONName.setText("components.json");
        mainGrid.add(openButton, 0, 0);
        mainGrid.add(hb2, 1, 3);
        mainGrid.add(hb1, 0, 3);
    //    mainGrid.add(btn2, 2, 3);
        mainGrid.add(message, 2, 3);
        Scene scene = new Scene(mainGrid, 1200, 500);
        primaryStage.setScene(scene);
        setLeft(mainGrid);
        setCenter(mainGrid);
        setRight(mainGrid,primaryStage);
        // ------------------
       /* {
        GridPane right_grid = new GridPane();
        right_grid.setAlignment(Pos.TOP_CENTER);
        right_grid.setHgap(10);
        right_grid.setVgap(10);
        right_grid.setPadding(new Insets(25, 25, 25, 25));
        
        mainGrid.add(right_grid, 2, 1);
        
        Text opttitle = new Text("Optional Components:");
        opttitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        right_grid.add(opttitle, 0, 0);
        
        Label CaptivePortal = new Label("Captive Portal version:");
        right_grid.add(CaptivePortal, 0, 1);
        right_grid.add(CaptivePortalVersion, 1, 1);
        
        Label GuiConfiguration = new Label("GUI Config name (gui/...):");
        right_grid.add(GuiConfiguration, 0, 2);
        right_grid.add(GUIConfigurationVersion, 1, 2);
        
        Label TelephonyDB = new Label("Telephony DB version:");
        right_grid.add(TelephonyDB, 0, 3);
        right_grid.add(TelephonyDBVersion, 1, 3);
        
        Label APN = new Label("APN version:");
        right_grid.add(APN, 0, 4);
        right_grid.add(APNVersion, 1, 4);
        
        Label WebServer = new Label("Web Server version:");
        right_grid.add(WebServer, 0, 5);
        right_grid.add(WebServerVersion, 1, 5);
        
        
        
        // -------------------
        
        GridPane left_grid = new GridPane();
        left_grid.setAlignment(Pos.TOP_CENTER);
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
        
        GridPane center_grid = new GridPane();
        center_grid.setAlignment(Pos.TOP_CENTER);
        center_grid.setHgap(10);
        center_grid.setVgap(10);
        center_grid.setPadding(new Insets(10, 10, 10, 10));
        
        mainGrid.add(center_grid, 1, 1);
        
        Text scenetitle = new Text("Component Versions:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        center_grid.add(scenetitle, 0, 0);

        Label ServersControl = new Label("ServersControl version:");
        center_grid.add(ServersControl, 0, 1);
        center_grid.add(ServersControlVersion, 1, 1);

        Label VSimControl = new Label("VSimControl version:");
        center_grid.add(VSimControl, 0, 2);
        center_grid.add(VSimControlVersion, 1, 2);
        
        Label Atmel = new Label("Atmel version:");
        center_grid.add(Atmel, 0, 3);
        center_grid.add(AtmelVersion, 1, 3);
        
        Label Server4G = new Label("Server 4G version:");
        center_grid.add(Server4G, 0, 4);
        center_grid.add(Server4GVersion, 1, 4);
        
        Label GUI = new Label("Gui version:");
        center_grid.add(GUI, 0, 5);
        center_grid.add(GUIVersion, 1, 5);
        
        Label LogService = new Label("Log Service version:");
        center_grid.add(LogService, 0, 6);
        center_grid.add(LogServiceVersion, 1, 6);
        
        Label APConfiguration = new Label("AP-Configuration version:");
        center_grid.add(APConfiguration, 0, 7);
        center_grid.add(APConfigurationVersion, 1, 7);
        
        Label OS = new Label("OS version:");
        center_grid.add(OS, 0, 8);
        center_grid.add(OSVersion, 1, 8);
        
        Label OSBase = new Label("OS Base version:");
        center_grid.add(OSBase, 0, 9);
        center_grid.add(OSBaseVersion, 1, 9);
        
        Label OSFilePathLabel = new Label("OS file name: (vph-components/OS/...)");
        center_grid.add(OSFilePathLabel, 0, 10);
        center_grid.add(OSFilePath, 1, 10);
        }
	*/
    }
	private void openFile(FileReader file, boolean first) {	
		try {
			
			JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(file);
            packageInfo.clear();
            componentInfo.clear();
      //      optionalInfo.clear();
            addElements(jsonElement.getAsJsonObject(), first);
          //  readJson(jsonElement.getAsJsonObject());
        } catch (Exception ex ) {
            ex.printStackTrace();
        }
    }
	private void addElements(JsonObject object, boolean first) throws JSONException{
		JsonArray array = object.getAsJsonArray("components");;
		for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
			if (!entry.getKey().equals("components")) {
				packageInfo.add(new TableEntry1( entry.getKey(), entry.getValue().getAsString()));
			}	
		}
		for (JsonElement el : array) {
			String name = el.getAsJsonObject().get("name").getAsString();
			String version = el.getAsJsonObject().get("version").getAsString();
			int id =  el.getAsJsonObject().get("id").getAsInt();
			if (  el.getAsJsonObject().get("id").getAsInt() < 100) {
				componentInfo.add(new TableEntry2( name , version, id));
				if (el.getAsJsonObject().get("id").getAsInt()  == 7) {
					OSBaseVersion.setText((el.getAsJsonObject()).get("base_version").getAsString());
					OSFilePath.setText((el.getAsJsonObject()).get("filepath").getAsString());
				}
			}
			else {
				if (first)
					optionalInfo.add(new TableEntry3( name , version, id, false));
				else {
					for (int i = 0; i < optionalInfo.size(); i ++) {
						if (optionalInfo.get(i).getIdValue() == id) {
							optionalInfo.remove(i);
							optionalInfo.add(new TableEntry3( name , version, id, true));
						}
					}
				}
			}
		}
	}
/*	private void readJson(JsonObject obj) throws JSONException{
		btn2.fire();
		PackageVersion.setText(obj.get("package_version").getAsString());
		PackageName.setText(obj.get("name").getAsString());
		ActionString.setText(obj.get("action").getAsString());
		if (obj.has("bucket_id"))
			BucketId.setText(obj.get("bucket_id").getAsString());
		if (obj.has("bucket_key"))
			BucketKey.setText(obj.get("bucket_key").getAsString());
		JsonArray array = obj.getAsJsonArray("components");
		for (int i = 0; i < array.size(); i++) {
			JsonElement el = array.get(i);
			System.out.println( (el.getAsJsonObject()).get("id").getAsInt());
			switch( (el.getAsJsonObject()).get("id").getAsInt() ) {
			case(0):
				ServersControlVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(1):
				VSimControlVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(2):
				AtmelVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(3):
				Server4GVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(4):
				GUIVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(5):
				LogServiceVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(6):
				APConfigurationVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(7):
				OSVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				if (el.getAsJsonObject().has("base_version"))
					OSBaseVersion.setText((el.getAsJsonObject()).get("base_version").getAsString());
				OSFilePath.setText((el.getAsJsonObject()).get("filepath").getAsString().substring(18));
				break;
			case(100):
				CaptivePortalVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(101):
				GUIConfigurationVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(102):
				TelephonyDBVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(103):
				APNVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			case(104):
				WebServerVersion.setText((el.getAsJsonObject()).get("version").getAsString());
				break;
			}
		}
	} */
	private void setLeft(GridPane mainGrid) {
		TextField addKey,addVal;
		Button addButton,removeButton;
		addKey = new TextField();
    	addVal = new TextField();
    	addButton = new Button("Add");
    	removeButton = new Button("Remove");
    	
		Text title = new Text("Package Info:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
	   
        table1.setEditable(true);
        TableColumn keyCol = new TableColumn("Key");
        keyCol.setMinWidth(100);
        keyCol.setCellValueFactory(new PropertyValueFactory<TableEntry1,String>("keyName"));
        TableColumn valCol = new TableColumn("Value");
        valCol.setMinWidth(200);
        valCol.setCellValueFactory(new PropertyValueFactory<TableEntry1,String>("valueName"));

        table1.setItems(packageInfo);
        table1.getColumns().addAll(keyCol, valCol);
    
        addKey.setPromptText("Add Key");
        addKey.setMaxWidth(keyCol.getPrefWidth());
        addVal.setMaxWidth(valCol.getPrefWidth());
        addVal.setPromptText("Add Value");
        
        valCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valCol.setOnEditCommit(
            new EventHandler<CellEditEvent<TableEntry1, String>>() {
                @Override
                public void handle(CellEditEvent<TableEntry1, String> t) {
                    if (!t.getNewValue().isEmpty())
                    	((TableEntry1) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValueName(t.getNewValue());
                }
            }
        );
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if (!addKey.getText().isEmpty() & !addVal.getText().isEmpty() )
            		packageInfo.add(new TableEntry1( addKey.getText(),addVal.getText()));
        		addKey.clear();
                addVal.clear();
            }
        });
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	packageInfo.remove(table1.getSelectionModel().getSelectedItem());
        		addKey.clear();
                addVal.clear();
            }
        });
       
        final HBox hb = new HBox();
        hb.getChildren().addAll(addKey, addVal,addButton, removeButton);
        hb.setSpacing(3);
        
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(title, table1,hb);
       
        mainGrid.add(vbox, 0, 1);
     
 
        
	}
	private void setCenter(GridPane mainGrid) {
		
		TextField addKey,addVal;
		Button addButton,removeButton;
		addKey = new TextField();
    	addVal = new TextField();
    	addButton = new Button("Add");
    	removeButton = new Button("Remove");
		Text title = new Text("Components Info:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
	   
        table2.setEditable(true);
        TableColumn<TableEntry2, String> keyCol = new TableColumn<>("Component");
        keyCol.setMinWidth(130);
        keyCol.setCellValueFactory(new PropertyValueFactory<TableEntry2,String>("compName"));
        TableColumn<TableEntry2, String> valCol = new TableColumn<>("Version");
        valCol.setMinWidth(250);
        valCol.setCellValueFactory(new PropertyValueFactory<TableEntry2,String>("versionName"));
       
        table2.setItems(componentInfo);
        table2.getColumns().addAll(keyCol, valCol);
    
        addKey.setPromptText("Add Key");
        addKey.setMaxWidth(keyCol.getPrefWidth());
        addVal.setMaxWidth(valCol.getPrefWidth());
        addVal.setPromptText("Add Value");
        
        valCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valCol.setOnEditCommit(
            new EventHandler<CellEditEvent<TableEntry2, String>>() {
                @Override
                public void handle(CellEditEvent<TableEntry2, String> t) {
                	if (!t.getNewValue().isEmpty())
                		((TableEntry2) t.getTableView().getItems().get( t.getTablePosition().getRow())).setVersionName(t.getNewValue());
                }
            }
        );
     /*   
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if (!addKey.getText().isEmpty() & !addVal.getText().isEmpty() )
            		componentInfo.add(new TableEntry2( addKey.getText(),addVal.getText()));
        		addKey.clear();
                addVal.clear();
            }
        });
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	componentInfo.remove(table2.getSelectionModel().getSelectedItem());
        		addKey.clear();
                addVal.clear();
            }
        });
       
        final HBox hb = new HBox();
        hb.getChildren().addAll(addKey, addVal,addButton, removeButton);
        hb.setSpacing(3);
        
       */
        Label OSBaseLabel = new Label("OS Base Version: ");
        Label OSFilePathLabel = new Label("OS Filepath: ");
        final HBox hb1 = new HBox();
        hb1.getChildren().addAll(OSBaseLabel, OSBaseVersion);
        hb1.setHgrow(OSBaseLabel, Priority.ALWAYS);
        hb1.setHgrow(OSBaseVersion, Priority.ALWAYS);
        hb1.setSpacing(3);
        final HBox hb2 = new HBox();
        hb2.getChildren().addAll(OSFilePathLabel, OSFilePath);
        hb1.setHgrow(OSFilePathLabel, Priority.ALWAYS);
        hb1.setHgrow(OSFilePath, Priority.ALWAYS);
        hb2.setSpacing(3);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(title, table2, hb1, hb2);
       
        mainGrid.add(vbox, 1, 1);

	}
	private void setRight(GridPane mainGrid,Stage primaryStage) {

		Button addButton;		
    	addButton = new Button("Add/Remove Component (Mandatory or Optional)");
    	
		Text title = new Text("Optional:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		
		table3.setEditable(true);
        TableColumn<TableEntry3,String> keyCol = new TableColumn<>("Component");
        keyCol.setMinWidth(130);
        keyCol.setCellValueFactory(new PropertyValueFactory<TableEntry3,String>("compName"));
        TableColumn<TableEntry3,String> valCol = new TableColumn<>("Version");
        valCol.setMinWidth(150);
        valCol.setCellValueFactory(new PropertyValueFactory<TableEntry3,String>("versionName"));
        TableColumn<TableEntry3,Boolean> activeCol = new TableColumn<>("Include?");
        activeCol.setMinWidth(50);
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
       
        table3.setItems(optionalInfo);
        table3.getColumns().addAll(keyCol, valCol, activeCol);
    
        
 
        valCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valCol.setOnEditCommit(
            new EventHandler<CellEditEvent<TableEntry3, String>>() {
                @Override
                public void handle(CellEditEvent<TableEntry3, String> t) {
                	if (!t.getNewValue().isEmpty())
                		((TableEntry3) t.getTableView().getItems().get( t.getTablePosition().getRow()) ).setVersionName(t.getNewValue());
                }
            }
        );
        
        activeCol.setCellValueFactory(new Callback<CellDataFeatures<TableEntry3, Boolean>, ObservableValue<Boolean>>()  {  	 
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<TableEntry3, Boolean> param) {
            	TableEntry3 entry = param.getValue();
 
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(entry.getActive());
                booleanProp.addListener(new ChangeListener<Boolean>() {
 
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                    	entry.setActive(newValue);
                    }
                });
                return booleanProp;
            }
        });
 
        activeCol.setCellFactory(new Callback<TableColumn<TableEntry3, Boolean>, //
        TableCell<TableEntry3, Boolean>>() {
            @Override
            public TableCell<TableEntry3, Boolean> call(TableColumn<TableEntry3, Boolean> p) {
                CheckBoxTableCell<TableEntry3, Boolean> cell = new CheckBoxTableCell<TableEntry3, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        
        
        
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	Stage addRemove = new Stage(); // new stage
            	addRemove.initModality(Modality.APPLICATION_MODAL);
                // Defines a modal window that blocks events from being
                // delivered to any other application window.
            	addRemove.initOwner(primaryStage);
            	setUpAddRemove(addRemove);
                
                addRemove.show();
                
            }
        });
       
        final HBox hb = new HBox();
        hb.getChildren().addAll(addButton);
        hb.setSpacing(3);
        hb.setHgrow(addButton, Priority.ALWAYS);
        hb.setAlignment(Pos.CENTER);
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(title, table3, hb);
       
        mainGrid.add(vbox, 2, 1);
	}
	public void setUpAddRemove(Stage addRemove) {
		addRemove.setTitle("Add and Remove Components");
		
		Text title = new Text("To be completed...");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		
		
		VBox vb = new VBox(20);

        Button okay = new Button("Okay");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	addRemove.close();
                
            }
        });
        vb.getChildren().addAll(title,okay);
        vb.setAlignment(Pos.CENTER);
        Scene addRemoveScene = new Scene(vb, 300, 200);
        addRemove.setScene(addRemoveScene);
	}
		
	
}

