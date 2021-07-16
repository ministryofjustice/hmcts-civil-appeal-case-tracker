package com.calander.util;



import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;

public class GetObject2 {
   public static void main(String args[]) throws IOException{
	   GetObject2 gettobj=new GetObject2();
	  gettobj.getReaderobj();
   }
    public Reader getReaderobj() throws IOException {
      	
        Regions clientRegion = Regions.EU_WEST_2;
        String bucketName = "case-tracker";
        String key1 = "CASE_TRACKER.CSV";
        String key="data.csv";
        Reader reader=null;
        
        S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;
        try {
            
        	//AmazonS3 s3Client = new AmazonS3Client(new AWSStaticCredentialsProvider(awsCreds));        
        	//S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        	///System.out.println("Downloading an object");
        	//InputStream objectData = object.getObjectContent();
        	// Process the objectData stream.
        	//objectData.close();
        	  AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion).build();
                   // .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                  //  .build();

            // Get an object and print its contents.
           System.out.println("Downloading an object");
           ObjectListing objectListing = s3Client.listObjects(bucketName);
           for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
               System.out.println(os.getKey());
           }
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key));
           System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
            System.out.println("Content: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            //final InputStreamReader streamReader = new InputStreamReader(fullObject.getObjectContent(), StandardCharsets.UTF_8);
            reader = displayTextInputStream(fullObject.getObjectContent());
            //fullObject.getObjectContent().abort();
            //fullObject.close();
           
           return reader;
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        } finally {
            // To ensure that the network connection doesn't remain open, close any open input streams.
            if (fullObject != null) {
                fullObject.close();
            }
            if (objectPortion != null) {
                objectPortion.close();
            }
            if (headerOverrideObject != null) {
                headerOverrideObject.close();
            }
        }
		return reader;
    }

    private static Reader displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        
	 BufferedReader reader=null;
	 BufferedWriter writer=null;
	String line;
	try {
			reader = new BufferedReader(new InputStreamReader(input));
		System.out.println("writting to file");
			 writer = new BufferedWriter(new FileWriter(new File("opt/data.csv")));
			 System.out.println("writting to file2");
		    while ((line = reader.readLine()) != null) {
		        //doSomethingWith(line);
		        writer.write(line);
		        // must do this: .readLine() will have stripped line endings
		        writer.newLine();
		    }
	  writer.close();
	  System.out.println("writting to file doneeeeeeeeeeeeeeeeeeeeeeeeeeee");
	}
       // BufferedReader reader1 = new BufferedReader(new InputStreamReader(input));
        catch(Exception e)
        {
		//writer.close();
		e.printStackTrace();
        }
        return reader;
    }
}


