# Titanic-dataset-
A java program to build and study titanic dataset.
For this project, you are going to build a Java program to store and study the Titanic dataset. 
The Titanic dataset is a comma separated text file (.csv). The first line of the file is the header line, containing the following fields: Survival, Pclass, Name, Sex, Age, Sibsp, Parch, Ticket, Fare, Cabin, and Embarked.

Meanings of each field are shown below:

Survival - Survival (0 = No; 1 = Yes). Not included in test.csv file.
Pclass - Passenger Class (1 = 1st; 2 = 2nd; 3 = 3rd)
Name - Name
Sex - Sex
Age - Age
Sibsp - Number of Siblings/Spouses Aboard
Parch - Number of Parents/Children Aboard
Ticket - Ticket Number
Fare - Passenger Fare
Cabin - Cabin
Embarked - Port of Embarkation (C = Cherbourg; Q = Queenstown; S = Southampton)

You are to perform the following tasks:

1.	Read the file and load the data such that:
    a.	Each person is imported into an instance of a Person class, which contains information about all fields above. The Name field must be broken into First Name (includes title) and Last Name. All fields must be private and can only be accessed via an accessor method. 
    b.	All Person instances are part of an ArrayList, sorted by instances’ LastName field.
    c.	A second ArrayList, consist of instances of class Family, is created. Class Family has a field representing the number of family members and an ArrayList of type Person containing references to the Person instances that belong to this family. 
2.	Using the data structures from (1), answer the following questions:
    a.	What is the average fare for people travel as families versus average ticket price for people travel individually?
    b.	What is the average age/sex/fare of people that survived versus people that don’t?
    c.	What is the average survival rate of members within families? 
    d.	What are the average survival rates of people coming from different port of embarkation? What are the average fare for survival versus non-survival for people embarking at different ports?
