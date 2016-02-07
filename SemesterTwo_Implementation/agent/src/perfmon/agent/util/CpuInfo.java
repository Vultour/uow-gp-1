package perfmon.agent.util;

public class CpuInfo{
	private String	model;
	private String	manufacturer;
	private int	cores;
	private double	utilization;


	public CpuInfo(){
		this.model = "Unknown";
		this.manufacturer = "Unknown";
		this.cores = 0;
		this.utilization = 0.0;
	}

	public CpuInfo(String mod, String man, int c, double u){
		this.setModel(mod);
		this.setManufacturer(man);
		this.setCores(c);
		this.setUtilization(u);
	}


	public String getModel()	{ return this.model; }
	public String getManufacturer()	{ return this.manufacturer; }
	public int    getCores()	{ return this.cores; }
	public double getUtilization()	{ return this.utilization; }

	public void setModel(String m)		{ this.model = m; }
	public void setManufacturer(String m)	{ this.manufacturer = m; }
	public void setCores(int c)		{ this.cores = c; }
	public void setUtilization(double u)	{ this.utilization = (u > 100.0)?(100.0):(u); }
}
