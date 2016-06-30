package br.com.predojo.main;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import br.com.predojo.entity.Match;
import br.com.predojo.entity.Player;
import br.com.predojo.logLoader.LogLoader;

public class RankingGenerator {
	
	private static Logger log = Logger.getLogger(RankingGenerator.class);
	
	private static final String LOG_FILE = "logFile.log"; 
	private static final String MATCH_STARTED = "has started"; 
	private static final String MATCH_ENDED = "has ended"; 
	private static final String NEW_MATCH = "New match"; 
	private static final String KILLED = "killed";
	private static final String USING = "using";
	private static final String WORLD = "<WORLD>";
	private static final String BY = "by";
	
	private File logFile;
	private List<String> rowsLogFile;
	private Map<String, Player> playerMap = new HashMap<>();
	
	private SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); 
	private Match match;
	
	public void initRankingGenerator() {
		logFile = LogLoader.loadLogFile(LOG_FILE);
		log.info("Lendo conteúdo do arquivo de log...");
		rowsLogFile = LogLoader.readContentLogFile(logFile);
		try {
			readRow(rowsLogFile);
		} catch (ParseException e) {
			log.info("Erro durante leitura das linhas do arquivo de log!", e);
		}
	}
	
	private void readRow(List<String> rowsLogFile) throws ParseException {
		for (String row : rowsLogFile) {
			String stringDateTime = (String) row.subSequence(0, 19);
			Date dateTime = dtFormat.parse(stringDateTime);
			
			if(row.contains(MATCH_STARTED) && row.contains(NEW_MATCH)) {
				match = new Match(dateTime);
				String matchNumber = "";
				for (int i = row.indexOf(NEW_MATCH)+10; i < row.indexOf(MATCH_STARTED)-1; i++) {
					matchNumber += row.charAt(i);
				}
				match.setMatchNumber(matchNumber);
				
			}else if(row.contains(KILLED)) {
				this.proccessMurderAndDeath(row, dateTime);
			}else if (row.contains(MATCH_ENDED)) {
				match.setFinishMatch(dateTime);
			}
		}
		
		log.info("*********** Ranking da partida Nº " + match.getMatchNumber() + " ***********");
		
		Map<String,Player> sortedNewPlayerMap = playerMap.entrySet().stream().sorted((e2,e1)->
        e1.getValue().getAmountMurder().compareTo(e2.getValue().getAmountMurder()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
		
		for (Map.Entry<String, Player> entry : sortedNewPlayerMap.entrySet()) {
			String awardDeath = "";
			String awardMurder = "";
			String weaponUded = "";
			
			if(entry.getValue().getAmountDeath() == 0) {
				awardDeath = " **Award Death**";
			}
			
			if(entry.getValue().getAmountMurder() >= 5) {
				long timeFirstMurder = entry.getValue().getFirstMurder().getTime();
				long timeLastMurder = entry.getValue().getLastMurder().getTime();
				long difference = timeLastMurder - timeFirstMurder;
				long differenceMinutes = difference/1000/60;
				
				if(differenceMinutes <= 1) {
					awardMurder = " **Award Murder**";
				}
			}
			
			if(entry.getValue().getAmountMurder() >= 1) {
				int maxWeaponValueInMap = (Collections.max(entry.getValue().getWeaponOfChoiceMap().values()));
				
				for (Entry<String, Integer> entryWeapon : entry.getValue().getWeaponOfChoiceMap().entrySet()) {
		            if (entryWeapon.getValue() == maxWeaponValueInMap) {
		            	weaponUded = ", Arma mais usada: " + entryWeapon.getKey();
		            }
		        }
			}
			
			log.info("*********** Jogador: " + entry.getValue().getName() 
			+ ", Assassinatos: " + entry.getValue().getAmountMurder() + awardMurder
			+ ", Mortes: " + entry.getValue().getAmountDeath() + awardDeath
			+ weaponUded
			+ ";");
		}
		
		log.info("*********** Fim da partida Nº " + match.getMatchNumber() + " ***********");
	}

	private void proccessMurderAndDeath(String row, Date dateTime) {
		String markBeginning = " - ";
		String killer = "";
		String killed = "";
		String weapon = "";
		
		int indexMarkBeginning = row.indexOf(markBeginning)+3;
		int indexMarkEndKiller = row.indexOf(KILLED)-1;
		int indexMarkBeginningKilled = row.indexOf(KILLED)+7;
		int indexMarkEndKilled = row.indexOf(USING)-1;
		int indexMarkBeginningUsing = row.indexOf(USING)+6;
		
		for (int i = indexMarkBeginning; i < indexMarkEndKiller; i++) {
			killer += row.charAt(i);
		}
		
		if(killer.equals(WORLD)) {
			indexMarkEndKilled = row.indexOf(BY)-1;
		}
		
		for (int i = indexMarkBeginningKilled; i < indexMarkEndKilled; i++) {
			killed += row.charAt(i);
		}
		
		if(row.contains(USING)) {
			for (int i = indexMarkBeginningUsing; i < row.length(); i++) {
				weapon += row.charAt(i);
			}
		}
		
		if(!playerMap.containsKey(killer)) {
			this.instanceKillerPlayer(killer, dateTime, weapon);
		}else{
			playerMap.get(killer).setAmountMurder(playerMap.get(killer).getAmountMurder() + 1);
			playerMap.get(killer).setLastMurder(dateTime);
			
			if(playerMap.get(killer).getWeaponOfChoiceMap().containsKey(weapon)){
				playerMap.get(killer).getWeaponOfChoiceMap().replace(weapon, playerMap.get(killer).getWeaponOfChoiceMap().get(weapon) + 1);
			}else{
				playerMap.get(killer).getWeaponOfChoiceMap().put(weapon, 1);
			}
		}
		
		if(!playerMap.containsKey(killed)) {
			this.instanceKilledPlayer(killed, dateTime);
		}else{
			playerMap.get(killed).setWhenDeath(dateTime);
			playerMap.get(killed).setAmountDeath(playerMap.get(killed).getAmountDeath() + 1);
		}
		
	}

	private void instanceKilledPlayer(String killed, Date dateTime) {
		Player killedPlayer = new Player();
		killedPlayer.setName(killed);
		killedPlayer.setAmountDeath(1);
		killedPlayer.setWhenDeath(dateTime);

		playerMap.put(killed, killedPlayer);
	}
	
	private void instanceKillerPlayer(String killer, Date dateTime, String weapon) {
		if(!killer.equals(WORLD)) {
			Player killerPlayer = new Player();
			killerPlayer.setName(killer);
			killerPlayer.setAmountMurder(1);
			killerPlayer.setFirstMurder(dateTime);
			killerPlayer.getWeaponOfChoiceMap().put(weapon, 1);
			
			playerMap.put(killer, killerPlayer);
		}
	}
}
