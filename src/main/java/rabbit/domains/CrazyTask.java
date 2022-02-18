package rabbit.domains;

import java.io.Serializable;

public class CrazyTask implements Serializable {

	private String id;
	
	private String fromServer;
	
	
	

	public CrazyTask() {
		super();
	}

	public CrazyTask(String id, String fromServer) {
		super();
		this.id = id;
		this.fromServer = fromServer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromServer() {
		return fromServer;
	}

	public void setFromServer(String fromServer) {
		this.fromServer = fromServer;
	}

	
	
	
}
