package containers;


import agents.Consumer;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

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

public class ConsumerCon extends Application {
	
	protected Consumer consumerAgent;
	ObservableList<String> observableList;
	
	
	public static void main(String[] args) {
		
		launch(args);
		
		
	}
	
	

	public void startContainer() throws Exception {
		Runtime runtime = Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
		AgentController agentController = agentContainer.createNewAgent
				("Consumer", "agents.Consumer", new Object[] {this} );
		agentController.start();
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		startContainer();
		primaryStage.setTitle("CONSUMER");
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10));
		hbox.setSpacing(10);
		Label label = new Label("Book: ");
		TextField textFieldLivre = new TextField(); 
		Button buttonBuy = new Button("Buy");
		
		hbox.getChildren().addAll(label,textFieldLivre,buttonBuy);
		
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10));
		
		 observableList= FXCollections.observableArrayList();
		
		ListView<String> listViewMessages = new ListView<String>(observableList);
		vBox.getChildren().add(listViewMessages);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(hbox);
		borderPane.setCenter(vBox);
		Scene scene = new Scene(borderPane,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonBuy.setOnAction(evt->{
			String livre = textFieldLivre.getText();
			//observableList.add(livre);
			GuiEvent guiEvent = new GuiEvent(this, 1);
			guiEvent.addParameter(livre);
			consumerAgent.onGuiEvent(guiEvent);
		});
		
	}
	
	public Consumer getConsumerAgent() {
		return consumerAgent;
	}

	public void setConsumerAgent(Consumer consumerAgent) {
		this.consumerAgent = consumerAgent;
	}
	
	public void Messsage(ACLMessage aclMessage) {
		Platform.runLater(()->{
			observableList.add(aclMessage.getContent()
					+",   "+aclMessage.getSender().getName());
					
		});
	}

}
