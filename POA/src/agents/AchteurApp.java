package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AchteurApp extends Application {

	protected Achteur buyer; 
	ObservableList<String> observableList;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		startContainer();
		primaryStage.setTitle("BUYER");
		BorderPane borderPane = new BorderPane();
		VBox vBox = new VBox();
		
		observableList=FXCollections.observableArrayList();
		ListView<String> listView = new ListView<String>(observableList);
		vBox.getChildren().add(listView);
		borderPane.setCenter(vBox);
		Scene scene = new Scene(borderPane,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}


	private void startContainer() throws Exception {
		Runtime runtime = Runtime.instance();
		
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
		AgentController agentController = agentContainer.createNewAgent
				("Buyer", "agents.Buyer", new Object[] {this});
		
		agentController.start();
		
	}
	
	public void Messsages(ACLMessage aclMessage) {
		Platform.runLater(()->{
			observableList.add(aclMessage.getContent()
					+",   "+aclMessage.getSender().getName());
					
		});
	
		
	}
	
	

}
