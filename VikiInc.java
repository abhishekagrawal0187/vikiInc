import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * VikiInc.java calls a URl and gets the response
 * it appends the page number and gets the response till it encounters a specific flag
 * it also counts the number of true and false hd tags and prints it.
 * @author abhishek.agrawal
 *
 */


public class VikiInc {

	
	final String URL = "http://api.viki.io/v4/videos.json?app=100250a&per_page=10&page=";
    
	int pageNumber = 1;
    
	int numberofFalseFlags = 0;
	
	int numberofTrueFlags = 0;
    
	public int getNumberofTrueFlags() {
		return numberofTrueFlags;
	}

	public void setNumberofTrueFlags(int numberofTrueFlags) {
		this.numberofTrueFlags = numberofTrueFlags;
	}

	public int getNumberofFalseFlags() {
		return numberofFalseFlags;
	}

	public void setNumberofFalseFlags(int numberofFalseFlags) {
		this.numberofFalseFlags = numberofFalseFlags;
	}


    public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int counter) {
		pageNumber = counter;
		
	}
	public String getURL() {
		return URL;
	}
	
	/*
	 * Reads inputStream of jsonResponse formats it and returns it.
	 * @throws: IOException
	 */
	public String readAll(Reader jsonResponse) throws IOException {
	    StringBuilder jsonResponseString = new StringBuilder();
	    int cp;
	    while ((cp = jsonResponse.read()) != -1) {
	      jsonResponseString.append((char) cp);
	    }
	    return jsonResponseString.toString();
	  }

	/**
	 * reads JSON response from a specific URL string
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	  public  JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream jsonResponseInputStream = new URL(url).openStream();
	    try {
	      BufferedReader jsonBufferedReader = new BufferedReader(new InputStreamReader(jsonResponseInputStream, Charset.forName("UTF-8")));
	      String jsonText = this.readAll(jsonBufferedReader);
	      JSONObject jsonObject = new JSONObject(jsonText);
	      return jsonObject;
	    } finally {
	      jsonResponseInputStream.close();
	    }
	  }
	
	
	  /**
	   * checks number of true and false hd flag tags in a jsonResponse
	   * @param jsonResponse
	   * @throws JSONException
	   */
		private void checkHdFlags(JSONObject jsonResponse) throws JSONException {
			// TODO Auto-generated method stub
			JSONArray jArray3 = jsonResponse.getJSONArray("response");
		    for(int i = 0; i < jArray3 .length(); i++)
		    {
		       JSONObject object3 = jArray3.getJSONObject(i);
		       JSONObject object4 = object3.getJSONObject("flags");
		       Boolean value = object4.getBoolean("hd");
		       if(value)
		    	   this.numberofTrueFlags++;
		       else
		    	   this.numberofFalseFlags++;
		      
		    }
		    
		}
		
		/**
		 * reads jsonResponse and sets the number of true and false hd flags.
		 * @throws IOException
		 * @throws JSONException
		 */
		private void readJsonResponseAndGetNumberofFlags() throws IOException, JSONException {
			StringBuilder newUrl = new StringBuilder(this.getURL());
			int counter = this.getPageNumber();
			newUrl.append(Integer.toString(counter++));
			this.setPageNumber(counter);
			JSONObject jsonResponse = this.readJsonFromUrl(newUrl.toString());
			while((boolean) jsonResponse.get("more")) {
				this.checkHdFlags(jsonResponse);
				newUrl = new StringBuilder(this.getURL());
			    counter = this.getPageNumber();
			    newUrl.append(counter++);
			    this.setPageNumber(counter);
			    jsonResponse = this.readJsonFromUrl(newUrl.toString());
			    
			}
			
		}


	public static void main(String[] args) throws IOException, JSONException {
		VikiInc vikiInc = new VikiInc();
		vikiInc.readJsonResponseAndGetNumberofFlags();
	    System.out.println("total number of hd flags set to true are " + vikiInc.numberofTrueFlags );
		System.out.println("total number of hd flags set to false are " + vikiInc.getNumberofFalseFlags());
	}

}
