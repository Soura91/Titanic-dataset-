
/*This program simulates the titanic dataset and analyze different statistics such as age, survival, fare etc*/
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TitanicDemo {
	public static void main(String[] args) throws FileNotFoundException {
		/*
		 * ArrayList tiinfo stores all the data from titanic
		 * dataset(titanic.csv)
		 */
		ArrayList<Person> tiinfo = new ArrayList<Person>();
		/*
		 * an ArrayList fam has the extracted data from tiinfo with sib/sp or
		 * par/ch count
		 */
		ArrayList<Family> fam = new ArrayList<Family>();
		/* an ArrayList fam1 has only family data */
		ArrayList<Family> fam1 = new ArrayList<Family>();
		/*
		 * Created an instance of class CallingMethods which has all methods
		 * called by main method
		 */
		CallingMethods cm = new CallingMethods();
		/* Extracting data from titanic dataset */
		tiinfo = cm.readAndSplit(tiinfo);
		/*
		 * method to calculate family details by removing the entries which has
		 * sib/sp or par/ch count to 0
		 */
		fam = cm.collectFamilyInfo(tiinfo);
		/*
		 * Below two methods filters the invalid entries in the list fam and
		 * gives the final Individual Person List and Family List Separately
		 */
		/*
		 * Assumptions for Families 1. The people who have same ticket number is
		 * considered to be in 1 family 2. The maiden name should match with the
		 * lastname along with the ticket and price 3. The entries which has
		 * sib/sp or par/ch count but has only 1 record are not part of Family,
		 * such entries are moved to Individual Person List
		 **/
		fam1 = cm.compareFamilyInfo(tiinfo, fam, fam1);
		cm.filterIndFamily(fam1, fam, tiinfo);
	}
}
