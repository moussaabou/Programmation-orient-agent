package agents;

import java.util.Random;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class Vendeur extends GuiAgent {

	
protected VendeurApp sellerGui;
	
	@Override
	protected void setup() {
		if(getArguments().length == 1) {
			sellerGui = (VendeurApp) getArguments()[0];
			sellerGui.seller = this;
		}
		
		ParallelBehaviour behaviour = new ParallelBehaviour();
		addBehaviour(behaviour);
		
		behaviour.addSubBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				
				DFAgentDescription dfAgentDescription = new DFAgentDescription();
				dfAgentDescription.setName(getAID());
				ServiceDescription serviceDescription = new ServiceDescription();
				serviceDescription.setType("transaction");
				serviceDescription.setName("Sell-BOOK");
				dfAgentDescription.addServices(serviceDescription);
				try {
					DFService.register(myAgent, dfAgentDescription);
				} catch (FIPAException e) {
					e.printStackTrace();
				}
						
				
			}
		});
		
		behaviour.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage aclMessage = receive();
				if(aclMessage != null) {
					sellerGui.Messsages(aclMessage);
					switch (aclMessage.getPerformative()) {
					case ACLMessage.CFP:
						
						ACLMessage reply = aclMessage.createReply();
						reply.setPerformative(ACLMessage.PROPOSE);
						reply.setContent(String.valueOf(200+new Random().nextInt(6000)));
						send(reply);
						break;
						
					case ACLMessage.ACCEPT_PROPOSAL:
						ACLMessage aclMessage2 = aclMessage.createReply();
						aclMessage2.setPerformative(ACLMessage.AGREE);
						send(aclMessage2);
						break;
						
					default:
						break;
					}
					
				}else {
					block();
				}
				
			}
		});
		
		
	}
	
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		
		
	}
	
	@Override
		protected void takeDown() {
			try {
				DFService.deregister(this);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}

}
