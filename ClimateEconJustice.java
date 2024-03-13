package climate;

import java.util.ArrayList;



public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    */ 
    public StateNode getFirstState () {
        
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     */
    public void createLinkedStructure ( String inputFile ) {
        
        
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

    String[] values = inputLine.split(",");
  
    String StateName = values[2];
  
    StateNode current = firstState;
        while (current != null) {
            if (current.getName().equals(StateName)) {
            // State already exists, do nothing
            return; 
        }
    current = current.getNext(); 
     }

        // State not found, add new StateNode 
    StateNode newNode = new StateNode();
    newNode.setName(StateName);
  
        if (firstState == null) {
            firstState = newNode;
    }         else {
        // Add to end of list
        current = firstState;
        while (current.getNext() != null) {
        current = current.getNext();
    }
    current.setNext(newNode);
  }

    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel(String line) {

        String[] values = line.split(",");
      
        String CountyName = values[1];
        String StateName = values[2];
      
        StateNode currentState = firstState;
      
        while (currentState != null) {
          if (currentState.getName().equals(StateName)) {
            // Found state
            
            CountyNode currentCounty = currentState.getDown();
            
            while (currentCounty != null) {
              if (currentCounty.getName().equals(CountyName)) {
                // County already exists
                return;
              }
              currentCounty = currentCounty.getNext();  
            }
      
            // County not found, add new county
            CountyNode newCounty = new CountyNode();
            newCounty.setName(CountyName);
      
            if (currentState.getDown() == null) {
              currentState.setDown(newCounty);
            } else {
              // Add to end of county list
              currentCounty = currentState.getDown();
              while (currentCounty.getNext() != null) {
                currentCounty = currentCounty.getNext();
              }
              currentCounty.setNext(newCounty);  
            }
      
            return;
          }
      
          currentState = currentState.getNext();
        }
      }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel(String line) {
  
        // Parse CSV line
        String[] values = line.split(",");
      
        String CommunityName = values[0];
        String CountyName = values[1];
        String StateName = values[2];
      
        // Create Data object
        Data Data = new Data();
        Data.prcntAfricanAmerican = Double.parseDouble(values[3]);
        Data.prcntNative = Double.parseDouble(values[4]);  
        Data.prcntAsian = Double.parseDouble(values[5]);
        Data.prcntWhite = Double.parseDouble(values[8]);
        Data.prcntHispanic = Double.parseDouble(values[9]);
        Data.disadvantaged = values[19];
        Data.PMlevel = Double.parseDouble(values[49]);
        Data.chanceOfFlood = Double.parseDouble(values[37]);
        Data.prcntPovertyLine = Double.parseDouble(values[121]);
      
        // Find state
        StateNode StateNode = firstState;
        while (StateNode != null) {
          if (StateNode.getName().equals(StateName)) {
            break; 
          }
          StateNode = StateNode.getNext();
        }
      
        // Find county in state
        CountyNode CountyNode = StateNode.getDown(); 
        while (CountyNode != null) {
          if (CountyNode.getName().equals(CountyName)) {
            break;
          }
          CountyNode = CountyNode.getNext(); 
        }
      
        // Create CommunityNode
        CommunityNode node = new CommunityNode();
        node.setName(CommunityName);
        node.setInfo(Data); // Link Data object
      
        // Add node to community list
        if (CountyNode.getDown() == null) {
          CountyNode.setDown(node);
        } else {
          CommunityNode temp = CountyNode.getDown();
          while (temp.getNext() != null) {
            temp = temp.getNext();
          }
          temp.setNext(node);
        }
      
      }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities(double userPrcntage, String race) {
     int count = 0;

     StateNode currentState = firstState;
     
     while (currentState != null) {
        
      CountyNode currentCounty = currentState.getDown();
        while (currentCounty != null) {
          CommunityNode currentCommunity = currentCounty.getDown();
          while (currentCommunity != null) {
            double racePercentage = 0;
            if (race.equals("African American")){
              racePercentage = currentCommunity.getInfo().getPrcntAfricanAmerican();
              racePercentage = racePercentage * 100;
            }
            if (race.equals("Native American")){
              racePercentage = currentCommunity.getInfo().getPrcntNative();
              racePercentage = racePercentage * 100;
            }
            if (race.equals("Hispanic American")){
              racePercentage = currentCommunity.getInfo().getPrcntHispanic();
              racePercentage = racePercentage * 100;
            }
            if (race.equals("Asian American")){
              racePercentage = currentCommunity.getInfo().getPrcntAsian();
              racePercentage = racePercentage * 100;
            }
            if (race.equals("White American")){
              racePercentage = currentCommunity.getInfo().getPrcntWhite();
              racePercentage = racePercentage * 100;
            }

            if (currentCommunity.getInfo().getAdvantageStatus().equals("True") && racePercentage >= userPrcntage){
                 count++; 
            }
            currentCommunity = currentCommunity.getNext();
          }
          currentCounty = currentCounty.getNext();
        }
        currentState = currentState.getNext();
      }
      return count;
  }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities(double userPrcntage, String race) {
      int count = 0;
      StateNode currentState = firstState;
     
      while (currentState != null) {
         
       CountyNode currentCounty = currentState.getDown();
         while (currentCounty != null) {
           CommunityNode currentCommunity = currentCounty.getDown();
           while (currentCommunity != null) {
             double racePercentage = 0;
             if (race.equals("African American")){
               racePercentage = currentCommunity.getInfo().getPrcntAfricanAmerican();
               racePercentage = racePercentage * 100;
             }

             if (race.equals("Native American")){
               racePercentage = currentCommunity.getInfo().getPrcntNative();
               racePercentage = racePercentage * 100;
             }
             if (race.equals("Hispanic American")){
               racePercentage = currentCommunity.getInfo().getPrcntHispanic();
               racePercentage = racePercentage * 100;
             }
             if (race.equals("Asian American")){
               racePercentage = currentCommunity.getInfo().getPrcntAsian();
               racePercentage = racePercentage * 100;
             }
             if (race.equals("White American")){
               racePercentage = currentCommunity.getInfo().getPrcntWhite();
               racePercentage = racePercentage * 100;
             }
 
             if (currentCommunity.getInfo().getAdvantageStatus().equals("False") && racePercentage >= userPrcntage){
                  count++; 
             }
             currentCommunity = currentCommunity.getNext();
           }
           currentCounty = currentCounty.getNext();
         }
         currentState = currentState.getNext();
       }
       return count;
   }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels(double PMlevel) {
      ArrayList<StateNode> states = new ArrayList<>();
      StateNode stateNode = firstState;
      while (stateNode != null) {
          boolean stateAdded = false;
          CountyNode countyNode = stateNode.getDown();
          while (countyNode != null && !stateAdded) {
              CommunityNode communityNode = countyNode.getDown();
              while (communityNode != null && !stateAdded) {
                  Data data = communityNode.getInfo();
                  if (data.PMlevel >= PMlevel) {
                      states.add(stateNode);
                      stateAdded = true;
                  }
                  communityNode = communityNode.getNext();
              }
              countyNode = countyNode.getNext();
          }
          stateNode = stateNode.getNext();
      }
      return states;
  }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {
      int count = 0;
      StateNode stateNode = firstState;
      while (stateNode != null) {
          CountyNode countyNode = stateNode.getDown();
          while (countyNode != null) {
              CommunityNode communityNode = countyNode.getDown();
              while (communityNode != null) {
                  Data data = communityNode.getInfo();
                  double chancePercentage = data.chanceOfFlood;
                  if (chancePercentage >= userPercntage) {
                      count++;
                  }
                  communityNode = communityNode.getNext();
              }
              countyNode = countyNode.getNext();
          }
          stateNode = stateNode.getNext();
      }
      return count;
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities(String stateName) {
      ArrayList<CommunityNode> lowestIncomeCommunities = new ArrayList<>();
      StateNode stateNode = firstState;
      while (stateNode != null) {
          if (stateNode.getName().equals(stateName)) {
              CountyNode countyNode = stateNode.getDown();
              while (countyNode != null) {
                  CommunityNode communityNode = countyNode.getDown();
                  while (communityNode != null) {
                      double income = communityNode.getInfo().getPercentPovertyLine();
                      if (lowestIncomeCommunities.size() < 10) {
                          lowestIncomeCommunities.add(communityNode);
                      } else {
                          int minIncomeIndex = 0;
                          double minIncome = lowestIncomeCommunities.get(0).getInfo().getPercentPovertyLine();
                          for (int i = 1; i < 10; i++) {
                              double currentIncome = lowestIncomeCommunities.get(i).getInfo().getPercentPovertyLine();
                              if (currentIncome < minIncome) {
                                  minIncome = currentIncome;
                                  minIncomeIndex = i;
                              }
                          }
                          if (income > minIncome) {
                              lowestIncomeCommunities.set(minIncomeIndex, communityNode);
                          }
                      }
                      communityNode = communityNode.getNext();
                  }
                  countyNode = countyNode.getNext();
              }
          }
          stateNode = stateNode.getNext();
      }
      return lowestIncomeCommunities;
  }
}
