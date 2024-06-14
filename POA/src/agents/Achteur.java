package agents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.scene.control.ListView;

public class Achteur extends GuiAgent {

	protected AchteurApp buyerGui;
	protected AID[] sellers;
	
	@Override
	protected void setup() {
		if(getArguments().length == 1) {
			buyerGui = (AchteurApp) getArguments()[0];
			buyerGui.buyer = this;
		}
		
		ParallelBehaviour behaviour = new ParallelBehaviour();
		addBehaviour(behaviour);
		
		behaviour.addSubBehaviour(new TickerBehaviour(this, 5000) {
			
			@Override
			protected void onTick() {
				
				DFAgentDescription dfAgentDescription = new DFAgentDescription();
				ServiceDescription serviceDescription = new ServiceDescription();
				serviceDescription.setType("transaction");
				serviceDescription.setName("Sell-BOOK");
				dfAgentDescription.addServices(serviceDescription);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, dfAgentDescription);
					sellers = new AID[result.length];
					for (int i = 0; i<sellers.length; i++) {
						sellers[i]= result[i].getName();
					}
				
				} catch (FIPAException e) {
					
					e.printStackTrace();
				}
			}
		});
		
		behaviour.addSubBehaviour(new CyclicBehaviour() {
			
			private int counter =0;
			private List<ACLMessage> replies = new ArrayList<ACLMessage>();
			
			@Override
			public void action() {
				MessageTemplate messageTemplate = 
						MessageTemplate.or(
								MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
								MessageTemplate.or(
										MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
										MessageTemplate.or(
												MessageTemplate.MatchPerformative(ACLMessage.AGREE),
												MessageTemplate.MatchPerformative(ACLMessage.REFUSE))));
				
				ACLMessage aclMessage = receive(messageTemplate);
				if(aclMessage != null) {
					
					switch (aclMessage.getPerformative()) {
					case ACLMessage.REQUEST:
						
						ACLMessage aclMessage2 = new ACLMessage(ACLMessage.CFP);
						aclMessage2.setContent(aclMessage.getContent());
						for(AID aid:sellers) {
							aclMessage2.addReceiver(aid);
						}
						
						send(aclMessage2);
						break;
						
					case ACLMessage.PROPOSE:
					
						++counter;
						replies.add(aclMessage);
						if(counter == sellers.length) {
							ACLMessage bestOffer = replies.get(0);
							double min = Double.parseDouble(replies.get(0).getContent());
							for( ACLMessage offer:replies) {
								double price = Double.parseDouble(offer.getContent());
								if (price <min) {
									bestOffer= offer;
									min = price;
								}
							}
							
							ACLMessage accepted = bestOffer.createReply(); 
							accepted.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							
							send(accepted);
							
						}
						
						break;
						
					case ACLMessage.AGREE:
						
						ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
						response.addReceiver(new AID("Consumer",AID.ISLOCALNAME));
						response.setContent(aclMessage.getContent());
						send(response);
						
						break;
					case ACLMessage.REFUSE:
						
						
						
						break;
						
					default:
						break;
					}
					
					String livre = aclMessage.getContent();
					buyerGui.Messsages(aclMessage);
					ACLMessage reply = aclMessage.createReply();
					reply.setContent("" +aclMessage.getContent());
					send(reply);
					ACLMessage message = new ACLMessage(ACLMessage.CFP);
					message.setContent(livre);
					message.addReceiver(new AID("Seller",AID.ISLOCALNAME));
					send(message);
				}else {
					block();
				}
				
			}
		});
		
	}
	
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		
		
	}
	

}
