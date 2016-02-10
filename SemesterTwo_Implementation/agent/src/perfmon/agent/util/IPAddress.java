package perfmon.agent.util;

public class IPAddress{
	private int[] bytes;


	public IPAddress(String ip) throws IllegalArgumentException{
		String[] b = ip.split("\\.");
		if (b.length != 4){
			throw new IllegalArgumentException();
		}

		this.bytes = new int[4];
		for (int i = 0; i < 4; i++){
			this.bytes[i] = Integer.parseInt(b[i]);
		}
	}

	public IPAddress(int[] ip) throws IllegalArgumentException{
		if (ip.length != 4){
			throw new IllegalArgumentException();
		}
		
		this.bytes = ip;
	}

	public IPAddress(int b1, int b2, int b3, int b4){
		this.bytes = new int[4];
		this.bytes[0] = b1;
		this.bytes[1] = b2;
		this.bytes[2] = b3;
		this.bytes[3] = b4;
	}


	public String toString(){
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 4; i++){
			if (i > 0){ s.append("."); }
			s.append(this.bytes[i]);
		}
		return s.toString();
	}

	public int[] toArray(){ return this.bytes; }
}
