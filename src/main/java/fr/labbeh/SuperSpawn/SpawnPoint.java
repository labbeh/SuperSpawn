package fr.labbeh.SuperSpawn;

/**
 * Cette classe représente un point de spawn
 * @author labbeh
 * @version 2019-08-09, v1.0
 * */
public class SpawnPoint {
	/**
	 * UUID nom du monde dans lequel se trouve le spawn
	 * */
	private String worldName;
	
	private double x;
	private double y;
	private double z;
	
	SpawnPoint(String worldName, double x, double y, double z) {
		super();
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public String getWorld() {
		return worldName;
	}

	@Override
	public String toString() {
		return worldName+ ";" +x+ ";" +y+ ";" +z;
	}

	
	
	
}
