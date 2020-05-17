/*Class Family*/
public class Family {
	private int members;
	private Person flist;

	/* Constructor Family */
	public Family(int members, Person flist) {
		this.members = members;
		this.flist = flist;
	}

	/* Setter and Getter methods to set and get the data of family class */
	public int getMembers() {
		return members;
	}

	public void setMembers(int members) {
		this.members = members;
	}

	public Person getFlist() {
		return flist;
	}

	public void setFlist(Person flist) {
		this.flist = flist;
	}

	/*
	 * equals method overide the object o to compare the objects of Family Class
	 */
	public boolean equals(Object o) {
		return this.getFlist().getTicket().equals(((Family) o).getFlist().getTicket());
	}
}
