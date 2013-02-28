package gs.wallet;

public class Name {
	private long id;
	private String name;
	
	public Name(String name) {
		this.name = name;
	}
	
	public Name(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
