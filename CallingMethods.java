import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CallingMethods {
	/* This method Extracts data from titanic dataset */
	public ArrayList<Person> readAndSplit(ArrayList<Person> tiinfo) throws FileNotFoundException {
		boolean hdr = true;
		String embar = "";
		String mname = "";
		ArrayList<String[]> line = new ArrayList<String[]>();
		File file = new File("titanic.csv");
		Scanner inputfile = new Scanner(file);
		while (inputfile.hasNext()) {
			line.add(inputfile.nextLine().split(","));
		}
		inputfile.close();
		for (String[] ln : line) {
			if (hdr) {
				hdr = false;
			} else {
				String b = ln[1];
				String pc = ln[2];
				String lname = ln[3].replace('"', ' ').trim();
				String fname = ln[4].replace('"', ' ').trim();
				String s = ln[5];
				String age = ln[6];
				String sib = ln[7];
				String pa = ln[8];
				String tic = ln[9];
				String fa = ln[10];
				String cab = ln[11];
				if (ln[0].equals("62") || ln[0].equals("830")) {
					embar = "";
				} else {
					embar = ln[12];
				}
				if (ln[5].equals("male")) {
					mname = "";
				} else {
					/*
					 * This methods extracts the last name from the maiden name
					 */
					mname = extractMaidenname(ln[4]);
				}
				Person passenger = new Person(b, pc, lname, fname, s, age, sib, pa, tic, fa, cab, embar, mname);
				tiinfo.add(passenger);
			}
		}
		Collections.sort(tiinfo);
		return tiinfo;
	}

	/*
	 * This method filters the people who does not have any
	 * sib/sip(siblings/spouse) and par/ch(parents/ children)
	 */
	public ArrayList<Family> collectFamilyInfo(ArrayList<Person> tiinfo) {
		ArrayList<Family> faminfo = new ArrayList<Family>();
		for (Person p : tiinfo) {
			if (!(p.getSibsp().equals("0")) || !(p.getParch().equals("0"))) {
				Family f = new Family(1, p);
				faminfo.add(f);
			}
		}
		return faminfo;
	}

	/*
	 * Comparing ticket number, fare and last name from the filtered list got by
	 * method collectFamilyInfo with the complete list
	 **/
	public ArrayList<Family> compareFamilyInfo(ArrayList<Person> tiinfo, ArrayList<Family> fam,
			ArrayList<Family> fam2) {
		for (Family f1 : fam) {
			int count = 0;
			if ((fam2.isEmpty()) || !(duplicateCheck(fam2, f1))) {
				for (Person p1 : tiinfo) {
					if (f1.getFlist().getLastname().equals(p1.getLastname())
							&& (f1.getFlist().getTicket().equals(p1.getTicket())
									&& (f1.getFlist().getFare().equals(p1.getFare())))) {
						count++;
						Family f2 = new Family(1, p1);
						fam2.add(f2);
					}
				}
			}
			if (count == 1) {
				fam2.remove(fam2.size() - 1);
			}
		}
		return fam2;
	}

	/*
	 * Filtering Individual and Family list - maiden name filtering,adding
	 * invalid entries to Individual Person List
	 */
	public void filterIndFamily(ArrayList<Family> fam1, ArrayList<Family> fam, ArrayList<Person> tiinfo) {
		ArrayList<Person> perinfo = new ArrayList<Person>();
		ArrayList<Family> famliyfinallist = new ArrayList<Family>();
		for (Person p2 : tiinfo) {
			if (p2.getParch().equals("0") && p2.getSibsp().equals("0")) {
				/*
				 * Adding only Individual Person records who doesnt have sib/sp
				 * or par/ch count
				 */
				perinfo.add(p2);/* 537 */
			}
		}
		for (Family ff2 : fam) {
			/*
			 * Considering the fact that maiden name is only given to the
			 * females
			 */
			if (ff2.getFlist().getSex().equals("female") && !(ff2.getFlist().getMaidenname().equals(""))) {
				/*
				 * This method compares the maiden name and adds the valid
				 * families to the Family list fam1
				 */
				isequalMaiden(fam, ff2, fam1);
			}
		}
		/*
		 * The entries which has sib/sp or par/ch count but no other family
		 * member is found, such entries(101) are added to the Individual
		 * List(perinfo list), Final Individual Person Info List contains 638
		 * Individual Person Records
		 **/
		filterInvalidFamilies(fam, fam1, perinfo);
		for (Family fmlist : fam1) {
			famliyfinallist.add(fmlist);
		}
		int count = 0, record = 0;
		int inc = 0;
		double familycount = 0;
		for (int i = 0; i < fam1.size(); i++) {
			count = 0;
			for (int j = i; j < fam1.size(); j++) {
				/* Comparing Ticket number to filter the familes */
				if (famliyfinallist.get(i).getFlist().getTicket().equals(fam1.get(j).getFlist().getTicket())) {
					count++;
					inc++;
					record++;
					if (record == fam1.size()) {
						for (int m = i; m < record; m++) {
							/* Total members added to family list is 253 */
							famliyfinallist.get(m).setMembers(count);
						}
						familycount++;
					}
				} else {

					for (int m = i; m < record; m++) {
						famliyfinallist.get(m).setMembers(count);
					}
					/* Final Family count is 100 */
					familycount++;
					i = inc - 1;
					break;
				}
			}
		}
		/*
		 * This method calculates average fare for people travel as families
		 * versus average ticket price for people travel individually
		 */
		averageTicketPersonAndFamily(perinfo, famliyfinallist);
		/*
		 * This method calculates the average age of people that survived versus
		 * people that don’t
		 */
		CalculateAverageAge(tiinfo);
		/*
		 * This method calculates average sex of people that survived versus
		 * people that don’t
		 */
		CalculateAverageSex(tiinfo);
		/*
		 * This method calculates the fare of people that survived versus people
		 * that don’t
		 */
		averageFareSurNonSur(perinfo, famliyfinallist);
		/*
		 * This method calculates the average Survival rate of members within
		 * families
		 */
		averageRateSurWithinFamilies(famliyfinallist, familycount);
		/*
		 * This method calculates the average survival rates of people coming
		 * from different port of embarkation
		 */
		CalculateAveSurRatePortsEmbark(tiinfo);
		/*
		 * This method calculates the average fare for survival versus
		 * non-survival for people embarking at different ports
		 */
		averageFareSurNonSurEmbarkPorts(perinfo, famliyfinallist);
	}

	/*
	 * This method calculates the average Survival rate of members within
	 * families
	 */
	private void averageRateSurWithinFamilies(ArrayList<Family> famliyfinallist, double famcount) {
		double survivalcount = 0;
		for (int i = 0; i < famliyfinallist.size(); i++) {
			if (famliyfinallist.get(i).getFlist().getSurvival().equals("1")) {
				survivalcount++;
			}
		}
		/* This gives the total Survival rate from the family List */
		double totalsurvivalrate = (survivalcount / famliyfinallist.size());
		System.out.println("\nThe average Survival rate of members within families is "
				+ String.format("%.2f", (totalsurvivalrate / famcount) * 100) + "%");
	}

	/*
	 * This method calculates the average fare for survival versus non-survival
	 * for people embarking at different ports
	 */
	public void averageFareSurNonSurEmbarkPorts(ArrayList<Person> perinfo, ArrayList<Family> famliyfinallist) {
		ArrayList<Family> completelist = new ArrayList<Family>();
		double scount_c = 0, scount_q = 0, scount_s = 0;
		double ncount_c = 0, ncount_q = 0, ncount_s = 0;
		double cticket = 0, sticket = 0, qticket = 0;
		double ncticket = 0, nsticket = 0, nqticket = 0;
		completelist.addAll(famliyfinallist);
		for (Person p : perinfo) {
			/* Adding member as 1 for Individual Person */
			Family f = new Family(1, p);
			/*
			 * Completelist contains both Person and Family ArrayList (638 + 253
			 * = 891(total entries from titanic.csv))
			 */
			completelist.add(f);
		}
		for (int i = 0; i < completelist.size(); i++) {
			String port = completelist.get(i).getFlist().getEmbarked();
			String sur = completelist.get(i).getFlist().getSurvival();
			double mem = completelist.get(i).getMembers();
			double tktfare = (Double.parseDouble(completelist.get(i).getFlist().getFare())) / mem;
			if (sur.equals("1")) {
				switch (port) {
				case "C":
					scount_c++;
					cticket += tktfare;
					break;
				case "Q":
					scount_q++;
					qticket += tktfare;
					break;
				case "S":
					scount_s++;
					sticket += tktfare;
					break;
				default:
					break;
				}
			} else {
				switch (port) {
				case "C":
					ncount_c++;
					ncticket += tktfare;
					break;
				case "Q":
					ncount_q++;
					nqticket += tktfare;
					break;
				case "S":
					ncount_s++;
					nsticket += tktfare;
					break;
				default:
					break;
				}
			}
		}
		System.out.println("\nThe average fare for survival for people embarking at port C is $"
				+ String.format("%.2f", (cticket) / (scount_c)));
		System.out.println("The average fare for survival for people embarking at port Q is $"
				+ String.format("%.2f", (qticket) / (scount_q)));
		System.out.println("The average fare for survival for people embarking at port S is $"
				+ String.format("%.2f", (sticket) / (scount_s)));
		System.out.println("The average fare for non-survival for people embarking at port C is $"
				+ String.format("%.2f", (ncticket) / (ncount_c)));
		System.out.println("The average fare for non-survival for people embarking at port Q is $"
				+ String.format("%.2f", (nqticket) / (ncount_q)));
		System.out.println("The average fare for non-survival for people embarking at port S is $"
				+ String.format("%.2f", (nsticket) / (ncount_s)));
	}

	/*
	 * This method calculates the fare of people that survived versus people
	 * that don’t
	 */
	public void averageFareSurNonSur(ArrayList<Person> perinfo, ArrayList<Family> famliyfinallist) {
		ArrayList<Family> completelist = new ArrayList<Family>();
		double smem = 0, nmem = 0;
		double stktprice = 0, ntktprice = 0, surfare = 0, nsurfare = 0;
		double scount = 0, ncount = 0;
		completelist.addAll(famliyfinallist);
		for (Person p : perinfo) {
			Family f = new Family(1, p);
			completelist.add(f);
		}
		for (int i = 0; i < completelist.size(); i++) {
			if (completelist.get(i).getFlist().getSurvival().equals("1")) {
				smem = completelist.get(i).getMembers();
				stktprice = (Double.parseDouble(completelist.get(i).getFlist().getFare())) / smem;
				scount++;
				surfare = surfare + stktprice;
			} else {
				nmem = completelist.get(i).getMembers();
				ntktprice = (Double.parseDouble(completelist.get(i).getFlist().getFare())) / nmem;
				ncount++;
				nsurfare = nsurfare + ntktprice;
			}
		}
		System.out.println(
				"\nThe average fare of people who survived is $" + String.format("%.2f", (surfare) / (scount)));
		System.out.println(
				"The average fare of people who did not survive is $" + String.format("%.2f", (nsurfare) / (ncount)));
	}

	/*
	 * This method calculates average fare for people travel as families versus
	 * average ticket price for people travel individually
	 */
	public void averageTicketPersonAndFamily(ArrayList<Person> perinfo, ArrayList<Family> famliyfinallist) {
		ArrayList<Double> familyFare = new ArrayList<Double>();
		double mem = 0;
		double tktprice = 0;
		double finaltktprice = 0;
		double familysum = 0;
		double indsum = 0;
		for (int i = 0; i < famliyfinallist.size(); i++) {
			mem = famliyfinallist.get(i).getMembers();
			tktprice = Double.parseDouble(famliyfinallist.get(i).getFlist().getFare());
			finaltktprice = tktprice / mem;
			familyFare.add(finaltktprice);
		}
		for (Double sum : familyFare) {
			familysum = familysum + sum;
		}
		System.out.println("The average fare of people who travelled as Families is $"
				+ String.format("%.2f", (familysum / (famliyfinallist.size()))));
		for (int j = 0; j < perinfo.size(); j++) {
			indsum = indsum + Double.parseDouble(perinfo.get(j).getFare());
		}
		System.out.println("The average fare of people who travelled as Individuals is $"
				+ String.format("%.2f", (indsum / (perinfo.size()))));
	}

	/*
	 * This method calculates the average survival rates of people coming from
	 * different port of embarkation
	 */
	public void CalculateAveSurRatePortsEmbark(ArrayList<Person> tiinfo) {
		double ccount = 0, scount = 0, qcount = 0;
		double s_ccount = 0, s_scount = 0, s_qcount = 0;
		for (Person embark : tiinfo) {
			if (embark.getEmbarked().equals("C")) {
				ccount++;
			} else if (embark.getEmbarked().equals("Q")) {
				qcount++;
			} else if (embark.getEmbarked().equals("S")) {
				scount++;
			}
		}
		for (Person port : tiinfo) {
			if ((port.getSurvival().equals("1")) && (port.getEmbarked().equals("C"))) {
				s_ccount++;
			} else if ((port.getSurvival().equals("1")) && (port.getEmbarked().equals("Q"))) {
				s_qcount++;
			} else if ((port.getSurvival().equals("1")) && (port.getEmbarked().equals("S"))) {
				s_scount++;
			}
		}
		System.out.println("\nThe average survival rate of people coming from port Cherbourg "
				+ String.format("%.2f", (s_ccount / ccount) * 100) + "%");
		System.out.println("The average survival rate of people coming from port Queenstown "
				+ String.format("%.2f", (s_qcount / qcount) * 100) + "%");
		System.out.println("The average survival rate of people coming from port Southampton "
				+ String.format("%.2f", (s_scount / scount) * 100) + "%");
	}

	/*
	 * This method calculates average sex of people that survived versus people
	 * that don’t
	 */
	public void CalculateAverageSex(ArrayList<Person> tiinfo) {
		double total_malecount = 0, total_femalecount = 0, s_malecount = 0, s_femalecount = 0, n_malecount = 0,
				n_femalecount = 0;
		for (Person pp : tiinfo) {
			if (pp.getSex().equals("male")) {
				total_malecount++;
			} else {
				total_femalecount++;
			}
		}
		for (Person psex : tiinfo) {
			if ((psex.getSurvival().equals("1")) && (psex.getSex().equals("male"))) {
				s_malecount++;
			} else if ((psex.getSurvival().equals("1")) && (psex.getSex().equals("female"))) {
				s_femalecount++;
			} else if ((psex.getSurvival().equals("0")) && (psex.getSex().equals("male"))) {
				n_malecount++;
			} else if ((psex.getSurvival().equals("0")) && (psex.getSex().equals("female"))) {
				n_femalecount++;
			}
		}
		System.out.println("\nThe average survival rate of male is "
				+ String.format("%.2f", (s_malecount / total_malecount) * 100) + "%");
		System.out.println("The average non survival rate of male is "
				+ String.format("%.2f", (n_malecount / total_malecount) * 100) + "%");
		System.out.println("The average survival rate of female is "
				+ String.format("%.2f", (s_femalecount / total_femalecount) * 100) + "%");
		System.out.println("The average non survival rate of female is "
				+ String.format("%.2f", (n_femalecount / total_femalecount) * 100) + "%");
	}

	/*
	 * This method calculates the average age of people that survived versus
	 * people that don’t
	 */
	public void CalculateAverageAge(ArrayList<Person> tiinfo) {
		int surcount = 0;
		int notsurcount = 0;
		double sum1 = 0;
		double sum2 = 0;
		for (Person pa : tiinfo) {
			if ((pa.getSurvival().equals("1")) && (!pa.getAge().equals(""))) {
				surcount++;
				sum1 = sum1 + Double.parseDouble(pa.getAge());
			}
			if ((pa.getSurvival().equals("0")) && (!pa.getAge().equals(""))) {
				notsurcount++;
				sum2 = sum2 + Double.parseDouble(pa.getAge());
			}
		}
		System.out
				.println("\nThe average age of the people who survived is " + String.format("%.2f", (sum1 / surcount)));
		System.out.println(
				"The average age of the people who didn't survive is " + String.format("%.2f", (sum2 / notsurcount)));
	}

	/* This method extracts the maiden name and adds to the Main Person List */
	public String extractMaidenname(String str) {
		String output = "";
		if (str.contains("(") && str.contains(")")) {
			int j = str.indexOf('(');
			int k = str.indexOf(')');
			str = str.substring(j, k);
		} else {
			return output;
		}
		String[] names = str.split(" ");
		if (names.length != 1) {
			output = names[names.length - 1];
		} else {
			return output;
		}
		return output;
	}

	/*
	 * This method filters the Persons who have siblings/spouse/parents/children
	 * mentioned but does not match with any lastname
	 */
	public void filterInvalidFamilies(ArrayList<Family> fam, ArrayList<Family> fam1, ArrayList<Person> perinfo) {
		for (Family fm : fam) {

			if (!(fam1.contains(fm))) {
				perinfo.add(fm.getFlist());
			}
		}
	}

	/*
	 * This method compares the maiden name and adds the valid families to the
	 * Family list
	 */
	public boolean isequalMaiden(ArrayList<Family> fam, Family ff2, ArrayList<Family> fam1) {
		for (Family fmeq : fam) {
			if (fmeq.getFlist().getLastname().equals(ff2.getFlist().getMaidenname())
					&& fmeq.getFlist().getTicket().equals(ff2.getFlist().getTicket())
					&& fmeq.getFlist().getFare().equals(ff2.getFlist().getFare())
					&& fmeq.getFlist().getFare().equals(ff2.getFlist().getFare())) {
				fam1.add(fmeq);
				fam1.add(ff2);
				return true;
			}
		}

		return false;
	}

	/*
	 * This method compares firstname and lastname to skip the already added
	 * record to the family list
	 */
	public boolean duplicateCheck(ArrayList<Family> fam1, Family f1) {
		for (Family fequal : fam1) {
			if ((fequal.getFlist().getFirstname().equals(f1.getFlist().getFirstname()))
					&& (fequal.getFlist().getLastname().equals(f1.getFlist().getLastname())))
				return true;
		}
		return false;
	}
}
