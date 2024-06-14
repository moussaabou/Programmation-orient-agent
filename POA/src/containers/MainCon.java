package containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;


public class MainCon {

	public static void main(String[] args) throws Exception {
		Runtime runtime =  Runtime.instance();
		Properties properties = new ExtendedProperties();
		properties.setProperty("gui", "true");
		ProfileImpl profileImpl = new ProfileImpl(properties);
		AgentContainer mainContainer = runtime.createMainContainer(profileImpl);
		mainContainer.start();
	}

}