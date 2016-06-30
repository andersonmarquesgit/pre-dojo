package br.com.predojo.logLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LogLoader {

	private static Logger log = Logger.getLogger(LogLoader.class);

	public static File loadLogFile(String dir) {
		File logFile = null;
		log.info("Carregando arquivo de log...");
		logFile = new File(ClassLoader.getSystemResource(dir).getFile());
		
		if(!logFile.exists()) {
			log.error("Arquivo de log inexistente!");
		}
		
		log.info("Carregamento concluído com sucesso!");
		return logFile;
	}
	
	public static List<String> readContentLogFile(File file) {
		List<String> linhasDoArquivo = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while (bufferedReader.ready()) {
				linhasDoArquivo.add(bufferedReader.readLine());
			}

			fileReader.close();
			bufferedReader.close();

		} catch (IOException e) {
			log.error(e.getMessage(), e);;
		}
		
		return linhasDoArquivo;
	}
}
