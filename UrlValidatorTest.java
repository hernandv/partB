/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import junit.framework.TestCase;


/**
 * Performs Validation Test for url validations.
 *
 * @version $Revision: 1128446 $ $Date: 2011-05-27 13:29:27 -0700 (Fri, 27 May 2011) $
 */
public class UrlValidatorTest extends TestCase {
   private boolean printStatus = false;
   private boolean printIndex = false;//print index that indicates current scheme,host,port,path, query test were using.
   String validAuthority = "www.google.com";//Ones we know should be valid
   String validScheme = "http://";
   String validPort = ":22";
   String validPath = "/test1";
   String validQuery = "?action=view";

   public UrlValidatorTest(String testName) {
      super(testName);
   }
  
   public void testManualTest()
   {
	   System.out.println("Manual Tests: ");
	   UrlValidator urlVal = new UrlValidator();
	   //Should all return true
	   assertTrue(urlVal.isValid("http://www.amazon.com"));
	   assertTrue(urlVal.isValid("http://www.amazon.org:22"));
	   assertTrue(urlVal.isValid("http://adasd.amazon.com/test1"));
	   assertTrue(urlVal.isValid("http://www.amazon.zw")); 
	 //Lucky catch.  
	 //Line 446 UrlValidator.java  return !QUERY_PATTERN.matcher(query).matches(); 
	 //should be return QUERY_PATTERN.matcher(query).matches(); 
	 //
	 assertTrue(urlVal.isValid("http://www.amazon.gov?action=view"));
   }
   
   ////////////////////////bad query validator makes all these fail.  Comment out + validQuery in for loops to check for other errors
   public void testYourFirstPartition()
   {
	   System.out.println("Testing Scheme Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   String[] validSchemeArr = new String[3];
	   String[] invalidSchemeArr = new String[10];
	   //Check to make sure the pieces we aren't testing are valid.  If false exit tests
	   assertTrue(urlVal.isValidAuthority(validAuthority + validPort));
	   assertTrue(urlVal.isValidAuthority(validAuthority));
	   assertTrue(urlVal.isValidPath(validPath));
	   assertTrue(urlVal.isValidQuery(validQuery));

	   validSchemeArr[0] = "http://";//Known valid schemes
	   validSchemeArr[1] = "ftp://";
	   validSchemeArr[2] = "https://";
	
	   //Check known valid Schemes
	   for(int i=0; i < 3; i++){
		   String urlTry = validSchemeArr[i] + validAuthority + validPort + validPath;
		   assertTrue(urlTry, urlVal.isValid(urlTry));
	   }
	   //Invalid schmes
	   invalidSchemeArr[0] = "https";
	   invalidSchemeArr[1] = "data://";
	   invalidSchemeArr[2] = "zzzzz://";
	   invalidSchemeArr[3] = "ftp:/";
	   invalidSchemeArr[4] = "ftp";
	   invalidSchemeArr[5] = "ftp:://";
	   invalidSchemeArr[6] = "ftp:";
	   invalidSchemeArr[7] = "https:///";
	   invalidSchemeArr[8] = "https:$/";
	   invalidSchemeArr[9] = "https//:";
		   
	   for(int i = 0; i < 10; i++){//Same as last for loop but using invalid scheme and checking to see if ends up being true
		   assertFalse(urlVal.isValid(invalidSchemeArr[i] + validAuthority + validPort + validPath + validQuery));
	   }
   }
   
      public void testYourSecondPartition(){
	   System.out.println("Testing Authority Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   
	   String[] invalidAuthorityArr = new String[10];
	   String[] validAuthorityArr = new String[10];
	   
	   assertTrue(urlVal.isValidScheme("http"));

	   assertTrue(urlVal.isValidAuthority(validAuthority + validPort));

	   assertTrue(urlVal.isValidPath(validPath));

	   //assertTrue(urlVal.isValidQuery(validQuery));

	   invalidAuthorityArr[0] = "256.256.256.256";
	   invalidAuthorityArr[1] = "1.2.3.4.5";
	   invalidAuthorityArr[2] = ".1.2.3.4";
	   invalidAuthorityArr[3] = "go.a1a";
	   invalidAuthorityArr[4] = "go.1aa";
	   invalidAuthorityArr[5] = ".aaa";
	   invalidAuthorityArr[6] = "aaa";
	   invalidAuthorityArr[7] = "aaa.";
	   invalidAuthorityArr[8] = "1.2.3";
	   invalidAuthorityArr[9] = "";
	   
	   for(int i = 0; i < 10; i++){
		   String urlTry = validScheme + invalidAuthorityArr[i] + validPort + validPath + validQuery;
		   System.out.println(urlTry);
		   assertFalse(urlVal.isValid(urlTry));
	   }
		      
	   validAuthorityArr[0] = "www.google.com";
	   validAuthorityArr[1] = "google.com";
	   validAuthorityArr[2] = "google.org";
	   validAuthorityArr[3] = "255.com";
	   validAuthorityArr[4] = "google.gov";
	   validAuthorityArr[5] = "google.edu";
		   
	   for(int i = 0; i < 6; i++){
		   String urlTry = validScheme + validAuthorityArr[i] + validPort + validPath;
		   assertTrue(urlTry, urlVal.isValid(urlTry));
	   }
	   
   }
   
   public void testYourThirdPartition(){
	   System.out.println("Testing Port Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   String urlTry;
	   assertTrue(urlVal.isValidScheme("http"));

	   assertTrue(urlVal.isValidAuthority(validAuthority));

	   assertTrue(urlVal.isValidPath(validPath));

	   assertTrue(urlVal.isValidQuery(validQuery));
	   
	   String[] invalidPortArr = {
	   ":123456",
	   ":655365",
	   ":1b3",
	   ":b21",
	   ":.11111",
	   ":ljljl",
	   ":-1",
	   ":-200",
	   ":-b.",
	   ":1234567",
	   ":-0"};
		   
	   for(int i = 0; i < invalidPortArr.length; i++){
		   urlTry = validScheme + "://" + validAuthority + invalidPortArr[i] + validPath;
		   assertFalse(urlTry, urlVal.isValid(urlTry));
	   }
	   
	   String[] validPortArr = {
	   ":22",
	   ":65535",
	   ":0",
	   ":65534",
	   ":1",
	   ":12",
	   ":123",
	   ":1234",
	   ":12345",
	   ":11111",
	   };
	   for(int i = 0; i < validPortArr.length; i++){
		   urlTry = validScheme + "://" + validAuthority + validPortArr[i] + validPath;
		   assertTrue(urlTry, urlVal.isValid(urlTry));
	   }   
   }
   
   public void testYourFourthPartition(){
	   String urlTry;
	   System.out.println("Testing Path Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   
	   String[] invalidPathArr = new String[10];
	   String[] validPathArr = new String[10];
	   
	   assertTrue(validScheme, urlVal.isValidScheme(validScheme));

	   assertTrue(validAuthority, urlVal.isValidAuthority(validAuthority));

	   assertTrue(validAuthority+validPort, urlVal.isValidAuthority(validAuthority + validPort));
	   assertTrue(validQuery, urlVal.isValidQuery(validQuery));

	   invalidPathArr[0] = "/..";
	   invalidPathArr[1] = "/../";
	   invalidPathArr[2] = "/..//file";
	   invalidPathArr[3] = "/test1//file";
	   invalidPathArr[4] = "///////";
	   invalidPathArr[5] = "abc";
	   invalidPathArr[6] = "_$";
	   invalidPathArr[7] = "/^";
	   
	   for(int i = 0; i < 8; i++){
		   urlTry = validScheme + "://" + validAuthority + validPort + invalidPathArr[i];
		   assertFalse(urlTry, urlVal.isValid(urlTry));
	   }
		      
	   validPathArr[0] = "/test1";
	   validPathArr[1] = "/t123";
	   validPathArr[2] = "/$23";
	   validPathArr[3] = "/test1/";
	   validPathArr[4] = "/test1/file";
	   validPathArr[5] = "/java/java_object_classes";
	   validPathArr[6] = "/courses/1555028/assignments/6594488";
	   validPathArr[7] = "/wiki/Uniform_Resource_Identifier";
	   validPathArr[8] = "/search";
	   validPathArr[9] = "/r/cscareerquestions";
	   
	   for(int i = 0; i < 10; i++){
		   urlTry = validScheme + "://" + validAuthority + validPort + validPathArr[i] + validQuery;
		   assertTrue(urlTry, urlVal.isValid(urlTry));
	   }   
   }
   
   public void testYourFithPartition(){
	   String urlTry;
	   System.out.println("Testing Query Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   
	   assertTrue(validScheme, urlVal.isValidScheme(validScheme));

	   assertTrue(validAuthority, urlVal.isValidAuthority(validAuthority));

	   assertTrue(validAuthority + validPort, urlVal.isValidAuthority(validAuthority + validPort));

	   assertTrue(validPath, urlVal.isValidPath(validPath));

	   String[] invalidQueryArr = {
	   "?!!!!@#$#$%^%^&%*&()..",
	   "?/../",
	   "?/..//file",
	   "?/test1//file",
	   "?///////",
	   "?456/@",
	   "?abc",
	   "?_$",
	   "?/_#",
	   "?/^"
	   };
	   for(int i = 0; i < invalidQueryArr.length; i++){
		   urlTry = validScheme + "://" + validAuthority + validPort + validPath + invalidQueryArr[i];
		   assertFalse(urlTry + Integer.toString(i), urlVal.isValid(urlTry));
	   }

	   String[] validQueryArr = {
	   "?action=view",
	   "?action=edit&mode=up",
	   "?newwindow=1&q=url+query",
	   "?module_item_id=16435218",
	   "?some_action=Some_thiNG",
	   "?1111=22222",
	   "?royals=world_series_champs",
	   "?ideas=NoNe",
	   "?last_ONE=false",
	   "?LAST_one=true",
	   };
	   for(int i = 0; i < validQueryArr.length; i++){
		   urlTry = validScheme + "://" + validAuthority + validPort + validPath + validQueryArr[i];
		   assertTrue(urlTry, urlVal.isValid(urlTry));
	   } 
   }
   
   public void testIsValid()
   {
	   for(int i = 0;i<10000;i++)
	   {
		   
	   }
   }
   
   public void testAnyOtherUnitTest()
   {
	   
   }
   /**
    * Create set of tests by taking the testUrlXXX arrays and
    * running through all possible permutations of their combinations.
    *
    * @param testObjects Used to create a url.
    */
  
}
