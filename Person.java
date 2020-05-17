/*Person Class*/
public class Person implements Comparable<Person> {
	private String survival;
	private String pcClass;
	private String lastname;
	private String firstname;
	private String sex;
	private String age;
	private String sibsp;
	private String parch;
	private String ticket;
	private String fare;
	private String cabin;
	private String embarked;
	private String maidenname;

	/*
	 * Setter and Getter methods sets and gets the respective data of Person
	 * Class
	 */
	public String getSurvival() {
		return survival;
	}

	public void setSurvival(String survival) {
		this.survival = survival;
	}

	public String getPcClass() {
		return pcClass;
	}

	public void setPcClass(String pcClass) {
		this.pcClass = pcClass;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSibsp() {
		return sibsp;
	}

	public void setSibsp(String sibsp) {
		this.sibsp = sibsp;
	}

	public String getParch() {
		return parch;
	}

	public void setParch(String parch) {
		this.parch = parch;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getFare() {
		return fare;
	}

	public void setFare(String fare) {
		this.fare = fare;
	}

	public String getCabin() {
		return cabin;
	}

	public void setCabin(String cabin) {
		this.cabin = cabin;
	}

	public String getEmbarked() {
		return embarked;
	}

	public void setEmbarked(String embarked) {
		this.embarked = embarked;
	}

	public String getMaidenname() {
		return maidenname;
	}

	public void setMaidenname(String maidenname) {
		this.maidenname = maidenname;
	}

	/* Person Constructor */
	public Person(String survival, String pcClass, String lastname, String firstname, String sex, String age,
			String sibsp, String parch, String ticket, String fare, String cabin, String embarked, String maidenname) {
		this.survival = survival;
		this.pcClass = pcClass;
		this.lastname = lastname;
		this.firstname = firstname;
		this.sex = sex;
		this.age = age;
		this.sibsp = sibsp;
		this.parch = parch;
		this.ticket = ticket;
		this.fare = fare;
		this.cabin = cabin;
		this.embarked = embarked;
		this.maidenname = maidenname;
	}

	/*
	 * Comparable interface is used for sorting the lists according to the
	 * lastname
	 */
	public int compareTo(Person o) {
		int lname = this.getLastname().compareTo(o.getLastname());
		return lname != 0 ? lname : this.getLastname().compareTo(o.getLastname());
	}
}
