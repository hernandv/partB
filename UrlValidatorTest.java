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
   String validScheme = "http";
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
	   assertFalse(urlVal.isValid("http://www.amazon.zw")); 
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
	   if(urlVal.isValidAuthority(validAuthority) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com . Canceling scheme tests");
	   }
	   else if(urlVal.isValidAuthority(validAuthority + validPort) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com:22 . Canceling scheme tests");
	   }
	   else if(urlVal.isValidPath(validPath) == false){
		   System.out.println("Error in isValidPath code.  isValidPath returns false on /test1 . Canceling scheme tests");
	   }
	   else if(urlVal.isValidQuery(validQuery) == false){
		   System.out.println("Error in isValidQuery code.  isValidQuery returns false on ?action=view . Canceling scheme tests");
	   }
	   else{//If all are valid begin test
		   validSchemeArr[0] = "http://";//Known valid schemes
		   validSchemeArr[1] = "ftp://";
		   validSchemeArr[2] = "https://";
	
		   for(int i=0; i < 3; i++){//Check each valid scheme combined with the other vaild parts.  If it returns false we have a bug
			   if(urlVal.isValid(validSchemeArr[i] + validAuthority + validPort + validPath + validQuery) == false){
				   System.out.println("Scheme: " + validSchemeArr[i] + validAuthority + validPort + validPath + validQuery + " failed.");
			   }
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
			   if(urlVal.isValid(invalidSchemeArr[i] + validAuthority + validPort + validPath + validQuery) == true){
				   System.out.println("Scheme " + invalidSchemeArr[i] + validAuthority + validPort + validPath + validQuery + " failed.");
			   }
		   }
	   }
   }
   
   public void testYourSecondPartition(){
	   System.out.println("Testing Authority Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   
	   String[] invalidAuthorityArr = new String[10];
	   String[] validAuthorityArr = new String[10];
	   
	   if(urlVal.isValidScheme(validScheme) == false){
		   System.out.println("Error in isValidScheme code.  isValidScheme returns false on http:// . Canceling authority tests");
	   }
	   else if(urlVal.isValidAuthority(validAuthority + validPort) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com:22 . Canceling authority tests");
	   }
	   else if(urlVal.isValidPath(validPath) == false){
		   System.out.println("Error in isValidPath code.  isValidPath returns false on /test1 . Canceling authority tests");
	   }
	   else if(urlVal.isValidQuery(validQuery) == false){
		   System.out.println("Error in isValidQuery code.  isValidQuery returns false on ?action=view . Canceling authority tests");
	   }
	   else{
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
			   if(urlVal.isValid(validScheme + invalidAuthorityArr[i] + validPort + validPath + validQuery) == true){
				   System.out.println("Authority: " + validScheme + invalidAuthorityArr[i] + validPort + validPath + validQuery + " failed.");
			   }
		   }
		      
		   validAuthorityArr[0] = "www.google.com";
		   validAuthorityArr[1] = "google.com";
		   validAuthorityArr[2] = "google.org";
		   validAuthorityArr[3] = "255.com";
		   validAuthorityArr[4] = "google.gov";
		   validAuthorityArr[5] = "google.edu";
		   validAuthorityArr[6] = "aaa";
		   validAuthorityArr[7] = "aaa.";
		   validAuthorityArr[8] = "1.2.3";
		   validAuthorityArr[9] = "";
		   
		   for(int i = 0; i < 10; i++){
			   if(urlVal.isValid(validScheme + validAuthorityArr[i] + validPort + validPath + validQuery) == false){
				   System.out.println("Authority: " + validScheme + validAuthorityArr[i] + validPort + validPath + validQuery + " failed.");
			   }
		   }
	   }
   }
   
   public void testYourThirdPartition(){
	   System.out.println("Testing Port Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   
	   String[] invalidPortArr = new String[10];
	   String[] validPortArr = new String[10];
	   
	   if(urlVal.isValidScheme(validScheme) == false){
		   System.out.println("Error in isValidScheme code.  isValidScheme returns false on http:// . Canceling port tests");
	   }
	   else if(urlVal.isValidAuthority(validAuthority) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com . Canceling port tests");
	   }
	   else if(urlVal.isValidPath(validPath) == false){
		   System.out.println("Error in isValidPath code.  isValidPath returns false on /test1 . Canceling port tests");
	   }
	   else if(urlVal.isValidQuery(validQuery) == false){
		   System.out.println("Error in isValidQuery code.  isValidQuery returns false on ?action=view . Canceling port tests");
	   }
	   else{
		   invalidPortArr[0] = ":123456";
		   invalidPortArr[1] = ":1b3";
		   invalidPortArr[2] = ":b21";
		   invalidPortArr[3] = ":.11111";
		   invalidPortArr[4] = ":ljljl";
		   invalidPortArr[5] = ":-1";
		   invalidPortArr[6] = ":-200";
		   invalidPortArr[7] = ":-b.";
		   invalidPortArr[8] = ":1234567";
		   invalidPortArr[9] = ":-0";
		   
		   for(int i = 0; i < 10; i++){
			   if(urlVal.isValid(validScheme + validAuthority + invalidPortArr[i] + validPath + validQuery) == true){
				   System.out.println("Port: " + validScheme + validAuthority + invalidPortArr[i] + validPath + validQuery + " failed.");
			   }
		   }
		      
		   validPortArr[0] = ":22";
		   validPortArr[1] = ":65535";
		   validPortArr[2] = ":0";
		   validPortArr[3] = ":65636";
		   validPortArr[4] = ":1";
		   validPortArr[5] = ":12";
		   validPortArr[6] = ":123";
		   validPortArr[7] = ":1234";
		   validPortArr[8] = ":12345";
		   validPortArr[9] = ":11111";
		   
		   for(int i = 0; i < 10; i++){
			   if(urlVal.isValid(validScheme + validAuthority + validPortArr[i] + validPath + validQuery) == false){
				   System.out.println("Port: " + validScheme + validAuthority + validPortArr + validPath + validQuery + " failed.");
			   }
		   }
	   }
   }
   
   public void testYourFourthPartition(){
	   System.out.println("Testing Path Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   
	   String[] invalidPathArr = new String[10];
	   String[] validPathArr = new String[10];
	   
	   if(urlVal.isValidScheme(validScheme) == false){
		   System.out.println("Error in isValidScheme code.  isValidScheme returns false on http:// . Canceling port tests");
	   }
	   else if(urlVal.isValidAuthority(validAuthority) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com . Canceling port tests");
	   }
	   else if(urlVal.isValidAuthority(validAuthority + validPort) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com:22 . Canceling authority tests");
	   }
	   else if(urlVal.isValidQuery(validQuery) == false){
		   System.out.println("Error in isValidQuery code.  isValidQuery returns false on ?action=view . Canceling port tests");
	   }
	   else{
		   invalidPathArr[0] = "/..";
		   invalidPathArr[1] = "/../";
		   invalidPathArr[2] = "/..//file";
		   invalidPathArr[3] = "/test1//file";
		   invalidPathArr[4] = "///////";
		   invalidPathArr[5] = "456";
		   invalidPathArr[6] = "abc";
		   invalidPathArr[7] = "_$";
		   invalidPathArr[8] = "/_#";
		   invalidPathArr[9] = "/^";
		   
		   for(int i = 0; i < 10; i++){
			   if(urlVal.isValid(validScheme + validAuthority + validPort + invalidPathArr[i] + validQuery) == true){
				   System.out.println("Path: " + validScheme + validAuthority + validPort + invalidPathArr[i] + validQuery + " failed.");
			   }
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
			   if(urlVal.isValid(validScheme + validAuthority + validPort + validPathArr[i] + validQuery) == false){
				   System.out.println("Port: " + validScheme + validAuthority + validPort + validPathArr[i] + validQuery + " failed.");
			   }
		   }
	   }
   }
   
   public void testYourFithPartition(){
	   System.out.println("Testing Query Partition:");
	   UrlValidator urlVal = new UrlValidator();
	   
	   String[] invalidQueryArr = new String[10];
	   String[] validQueryArr = new String[10];
	   
	   if(urlVal.isValidScheme(validScheme) == false){
		   System.out.println("Error in isValidScheme code.  isValidScheme returns false on http:// . Canceling port tests");
	   }
	   else if(urlVal.isValidAuthority(validAuthority) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com . Canceling port tests");
	   }
	   else if(urlVal.isValidAuthority(validAuthority + validPort) == false){
		   System.out.println("Error in isValidAuthority code.  isValidAuthority returns false on www.google.com:22 . Canceling authority tests");
	   }
	   else if(urlVal.isValidPath(validPath) == false){
		   System.out.println("Error in isValidPath code.  isValidPath returns false on /test1 . Canceling port tests");
	   }
	   else{
		  /* invalidQueryArr[0] = "/..";
		   invalidQueryArr[1] = "/../";
		   invalidQueryArr[2] = "/..//file";
		   invalidQueryArr[3] = "/test1//file";
		   invalidQueryArr[4] = "///////";
		   invalidQueryArr[5] = "456";
		   invalidQueryArr[6] = "abc";
		   invalidQueryArr[7] = "_$";
		   invalidQueryArr[8] = "/_#";
		   invalidQueryArr[9] = "/^";
		   
		   for(int i = 0; i < 10; i++){
			   if(urlVal.isValid(validScheme + validAuthority + validPort + validPath + validQuery) == true){
				   System.out.println("Port: " + validScheme + validAuthority + validPort + validPath + invalidQueryArr[i] + " failed.");
			   }
		   }
		     */
		   validQueryArr[0] = "?action=view";
		   validQueryArr[1] = "?action=edit&mode=up";
		   validQueryArr[2] = "?newwindow=1&q=url+query";
		   validQueryArr[3] = "?module_item_id=16435218";
		   validQueryArr[4] = "?some_action=Some_thiNG";
		   validQueryArr[5] = "?1111=22222";
		   validQueryArr[6] = "?royals=world_series_champs";
		   validQueryArr[7] = "?ideas=NoNe";
		   validQueryArr[8] = "?last_ONE=false";
		   validQueryArr[9] = "?LAST_one=true";
		   
		   for(int i = 0; i < 10; i++){
			   if(urlVal.isValid(validScheme + "://" + validAuthority + validPort + validPath + validQuery) == false){
				   System.out.println("Query: " + validScheme + "://" + validAuthority + validPort + validPath + validQueryArr[i] + " failed.");
			   }
		   }
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
