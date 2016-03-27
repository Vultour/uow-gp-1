package perfmon.util;

public class Config{
	public static final boolean	AGENT_DEBUG		= true;
	public static final boolean	AGENT_DRY_RUN		= false;
	public static final int		AGENT_CYCLE_PAUSE	= 500;
	public static final boolean	AGENT_DEBUG_EXPAND_HDD	= false;
	public static final boolean	AGENT_DEBUG_EXPAND_NET	= false;
	public static final boolean	AGENT_DEBUG_EXPAND_PS	= false;

	public static final String[]	AGENT_FILTER_HDD_DIR	= {};
	public static final String[]	AGENT_FILTER_HDD_FS	= {
									"tmpfs"
								};

	public static final boolean	AGENT_LOG_PRINT	= true;
	public static final boolean	AGENT_LOG_FILE	= true;
	public static final boolean	AGENT_LOG_DB	= false;

	public static final String	DATABASE_HOST	= "localhost";
	public static final int		DATABASE_PORT	= 3310;
	public static final String	DATABASE_NAME	= "nydus_sdgp";
	public static final String	DATABASE_USER	= "nydus_sdgp";
	public static final String	DATABASE_PASS	= "JwkB9v9hFf";
}
