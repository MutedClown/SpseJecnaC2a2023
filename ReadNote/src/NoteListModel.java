import javax.swing.AbstractListModel;
import java.util.List;


public class NoteListModel extends AbstractListModel<Note> {


	private List<Note> data;
	
	
	public NoteListModel(List<Note> input) {
	
		data = input;
		data.sort(null);
	}
	
	@Override
	public int getSize() {
	
		return data.size();
	}
	
	@Override
	public Note getElementAt(int index) {
	
		if (data.isEmpty()) {
		
			return null;
		}
		
		return data.get(index);
	}
	
	public boolean isEmpty() {
	
		return data.isEmpty();
	}
	
	public int indexOf(Note n) {
	
		return data.indexOf(n);
	}
	
	public boolean containsName(Note input) {
		
		if (data.contains(input)) {
		
			return true;
		}
		
		return false;
	}
	
	public void add(Note n) {
	
		data.add(n);
		data.sort(null);
	}
	
	public void delete(Note n) {

		data.remove(n);
	}
 
	public void update(Note originalN, Note newN) {

		int index = data.indexOf(originalN);	
		data.set(index, newN);
		data.sort(null);
	}

	public void addElement(Note note) {
		// TODO Auto-generated method stub
		
	}
}