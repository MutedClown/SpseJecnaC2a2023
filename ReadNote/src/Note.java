public class Note implements Comparable<Note> {

//
	private String name;
	private String text;

	public Note() {
	}
	
	public Note(String n, String t) {
	
		name = n;
		text = t;
	}
	
	public void setName(String n) {
	
		name = n;
	}
	public String getName() {
	
		return name;
	}
	
	public void setText(String t) {
	
		text = t;
	}
	public String getText() {
	
		return text;
	}

	@Override
	public String toString() {
	
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
	
		if ((obj instanceof Note) &&
				(((Note) obj).getName().equals(name))) {
		
			return true;
		}
		
		return false;
	}
	
	@Override
	public int compareTo(Note other) {
	
		return this.name.compareTo(other.getName());
	}
}