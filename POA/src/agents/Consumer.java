package agents;


import containers.ConsumerCon;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class Consumer extends GuiAgent {
	
	private  transient ConsumerCon guiConsumerContainer;
	
	@Override
	protected void setup() {
		
		if(getArguments().length == 1) {
			guiConsumerContainer =(ConsumerCon) getArguments()[0];
			guiConsumerContainer.setConsumerAgent(this);
		} 
		 
		ParallelBehaviour parallel = new ParallelBehaviour();
		
		parallel.addSubBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage aclMessage = receive();
				if(aclMessage!=null) {
					switch (aclMessage.getPerformative()) {
					case ACLMessage.CONFIRM:
						
						guiConsumerContainer.Messsage(aclMessage);
						
						break;

					default:
						break;
					}
				}else {
					block();
				}
				
			}
		});
		
		
		addBehaviour(parallel);
		
		
	}
	
	@Override
	protected void beforeMove() {
		System.out.println("%%%%%%%%%%%");
		System.out.println("Avant ::");
		System.out.println("%%%%%%%%%%%");
	}
	
	@Override
	protected void afterMove() {
		System.out.println("%%%%%%%%%%%");
		System.out.println("Aprés ::");
		System.out.println("%%%%%%%%%%%");
	} 
	
	@Override
	protected void takeDown() {
		System.out.println("%%%%%%%%%%%");
		System.out.println("entrain de mourir");
		System.out.println("%%%%%%%%%%%");
	}

	
	@Override
	public void onGuiEvent(GuiEvent params) {
		
		if(params.getType()==1) {
			String livre = params.getParameter(0).toString();
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			aclMessage.setContent(livre);
			aclMessage.addReceiver(new AID("Buyer",AID.ISLOCALNAME));
			send(aclMessage);
		}
		
	}
	

}
