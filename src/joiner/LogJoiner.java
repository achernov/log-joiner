package joiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LogJoiner {
	
	ArrayList<String> paths;
	int nFiles;
	
	//----------------------------------------------------------
	
	public LogJoiner() {
		paths = new ArrayList<String>();
	}
	
	//----------------------------------------------------------
	
	public void addFile( String path ) {
		paths.add( path );
	}
	
	//----------------------------------------------------------
	
	public void joinFiles() throws IOException {
		
		nFiles = paths.size();
		
		@SuppressWarnings("unchecked")
		HashMap<String,String> tables[] = (HashMap<String,String>[])new HashMap[nFiles];
		
		String[] times = null;
		
		for( int k=0; k<nFiles; k++ ) {
			ArrayList<String> lines = loadFile( paths.get(k) );
			
			tables[k] = linesToMap( lines );
			
			if( k == 0 ) {
				times = timesFromLines( lines );
			}
		}
		
		for( int k=0; k<times.length; k++ ) {
			joinLine( times[k], tables );
		}
		
	}

	//----------------------------------------------------------
	
	private void joinLine( String time, HashMap<String, String>[] tables ) {
		
		int nTables = tables.length;
		String lines[][] = new String[nTables][];
		
		for( int k=0; k<nTables; k++ ) {
			String line = tables[k].get( time );
			if( line == null ) {
				return;
			}
			lines[k] = line.split("\t");
		}
		
		double total = 0;
		double bad = 0;
		
		for( int k=0; k<nTables; k++ ) {
			double curTotal = Double.parseDouble( lines[k][8] );
			double curErr = Double.parseDouble( lines[k][7] );
			total += curTotal;
			bad += curErr*curTotal;
		}
		
		double err = bad/total;
		double good = total-bad;
		
		System.out.println( time + "\t" + 
							formatErr(err) + "\t" + 
							format2s(total) + "\t" + 
							format2s(good) );
	}

	//----------------------------------------------------------
	
	private String[] timesFromLines(ArrayList<String> lines) {
		
		int nLines = lines.size();
		String times[] = new String[nLines];
		
		for( int k=0; k<nLines; k++ ) {
			String s = lines.get(k);
			String fields[] = s.split("\t");
			times[k] = fields[0];
		}
		
		return times;
	}
	
	//----------------------------------------------------------

	private HashMap<String, String> linesToMap(ArrayList<String> lines) {
		
		int nLines = lines.size();
		HashMap<String, String> table = new HashMap<String, String>();
		
		for( int k=0; k<nLines; k++ ) {
			String s = lines.get(k);
			String fields[] = s.split("\t");
			table.put( fields[0], s );
		}
		
		return table;
	}

	//----------------------------------------------------------
	
	private ArrayList<String> loadFile(String path) throws IOException {
		FileInputStream inputStream = new FileInputStream(new File(path));
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(isr);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		String line;
		line=reader.readLine();
		while( (line=reader.readLine()) != null ) {
			lines.add( line );
		}
		
		reader.close();
		isr.close();
		inputStream.close();
		
		return lines;
	}
	
	
	//----------------------------------------------------------
	// simple and dirty hack ¹1 ;)
	
	public static String formatErr(double err) {
		
		if( err <= 0.9 ) {
			return format2s(err);
		}
		
		if( err >= 1.0 ) {
			
			return ""+((int)Math.round(err*100))/100.0;
		}
		else {
			int p=0;
			double u=err; 
			double v=(1-err); 
			while( v<1 ) { 
				u*=10; v*=10; p++; 
			}
				
			int u1 = (int)Math.round(u*10);
			String s = "0."+u1;
			
			while( s.endsWith("0") ) {
				s = s.substring(0,s.length()-1);
			}
			
			return s;
		}
	}
	
	//----------------------------------------------------------
	// simple and dirty hack ¹2 ;)
	
	private static String format2s(double value) {

		//value = (float)0.00023456789;
		
		String s = "";
		
		if( value < 1e-9 ) {
			return "0.0";
		}
		
		if( value >= 1.0 ) {
			
			s = ""+((int)Math.round(value*100))/100.0;
		}
		else {
			int p=0;
			double v=value; 
			while( v<1 ) { 
				v*=10; p++; 
			}
				
			s = "0.0000000000".substring(0,p+1);
			double v1 = Math.round(v*10);
			String s1 = ""+v1/100;
			s += s1.substring(2); 
		}
				
		return s;
	}

	//----------------------------------------------------------

}
