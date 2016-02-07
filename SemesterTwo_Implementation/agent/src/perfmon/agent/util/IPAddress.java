package perfmon.agent.util;

public class IPAddress{
	private byte[] bytes;


	public IPAddress(String ip) throws IllegalArgumentException{
		String[] b = ip.split(".");
		if (b.length != 4){
			throw new IllegalArgumentException();
		}

		this.bytes = new byte[4];
		for (int i = 0; i < 4; i++){
			this.bytes[i] = Byte.parseByte(b[i]);
		}
	}

	public IPAddress(byte[] ip) throws IllegalArgumentException{
		if (ip.length != 4){
			throw new IllegalArgumentException();
		}
		
		this.bytes = new byte[4];
		this.bytes = ip;
	}

	public IPAddress(byte b1, byte b2, byte b3, byte b4){
		this.bytes = new byte[4];
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

	public byte[] toArray(){ return this.bytes; }
}
