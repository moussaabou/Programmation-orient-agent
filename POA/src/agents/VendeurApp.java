package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VendeurApp extends Application {

	protected Vendeur seller;
	ObservableList<String> observableList;
	AgentContainer agentContainer;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		startContainer();
		primaryStage.setTitle("Seller");
		
		HBox box = new HBox();
		box.setPadding(new Insets(10));
		box.setSpacing(10);
		
		Label label = new Label("Agent name");
		TextField textField = new TextField();
		Button deploy = new Button("Deploy");
		
		box.getChildren().addAll(label,textField,deploy);
		
		BorderPane borderPane = new BorderPane();
		VBox vBox = new VBox();
		
		observableList=FXCollections.observableArrayList();
		ListView<String> listView = new ListView<String>(observableList);
		vBox.getChildren().add(listView);
		borderPane.setTop(box);
		borderPane.setCenter(vBox);
		Scene scene = new Scene(borderPane,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		deploy.setOnAction((e)->{
			try {
				AgentController agentController = agentContainer.createNewAgent
						(textField.getText(), "agents.Seller", new Object[] {this});
				agentController.start();
			} catch (StaleProxyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
	}


	private void startContainer() throws Exception {
		Runtime runtime = Runtime.instance();
		ExtendedProperties extendedProperties = new ExtendedProperties();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		agentContainer = runtime.createAgentContainer(profileImpl);
		agentContainer.start();
		
	}
	
	public void Messsages(ACLMessage aclMessage) {
		Platform.runLater(()->{
			observableList.add(aclMessage.getContent()
					+",   "+aclMessage.getSender().getName());
					
		});
	
		
	}
	
	

}
