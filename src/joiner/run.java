package joiner;
import java.io.IOException;
import java.nio.charset.Charset;

//IllegalArgumentException 
public class run {
	//----------------------------------------------------------
	
	static String usage = "\r\n\r\nОбъединение логов, полученных с помощью Analyze2.jar.\r\n"
		+"\r\n"
		+"Использование:\r\n"
		+"Java -jar joiner.jar  dir  file1 ..... fileN\r\n"
		+"\r\n"
		+"Параметры:\r\n"
		+"\r\n"
		+"dir - директория, где лежат входные файлы\r\n"
		+"file1... - имена входных файлов\r\n"
		+"\r\n"
		+"Пример использования:\r\n"
		+"Java -jar joiner.jar  D:\\Azure\\logs memo1 memo2 memo3 memo5\r\n";
	
	//----------------------------------------------------------
	
	public static void main( String[] args ) {
		String dir = args[0];
		
		LogJoiner joiner = new LogJoiner();
		
		for( int k=1; k<args.length; k++ ) {
			joiner.addFile( dir+"/"+args[k] );
		}
		
		try {
			joiner.joinFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//----------------------------------------------------------
	
	public static void printRussian( String s ) {
		try {
			System.out.write( s.getBytes(Charset.forName("cp866")) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//----------------------------------------------------------
}
