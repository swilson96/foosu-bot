package fusu.oddschecker;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TeamOdds {

	private static Pattern FRACTIONAL_ODDS_REGEX = Pattern.compile("\\(?(\\d+)/(\\d+)\\)?");
	private static Pattern WHOLE_ODDS_REGEX = Pattern.compile("\\(?(\\d+)\\)?");
	
	/**
	 * Sort by likelihood of winning, then alphabetically.
	 */
	public static Comparator<TeamOdds> TEAM_COMPARITOR = new Comparator<TeamOdds>() {
		public int compare(TeamOdds o1, TeamOdds o2) {
			int oddsCheck = Double.compare(o1.odds, o2.odds);
			if (oddsCheck == 0) {
				return o1.name.compareTo(o2.name);
			}
			return oddsCheck;
		}
	};
	
	private final String name;
	private final double odds;
	
	public TeamOdds(WebElement oddsElement) {
		name = oddsElement.findElement(By.xpath("span[@class='fixtures-bet-name']")).getText();
		
		String oddsAsString = oddsElement.findElement(By.xpath("span[@class='odds']")).getText();
		odds = parseOddsString(oddsAsString);
	}
	
	private double parseOddsString(String oddsAsString) {
		Matcher matcher = FRACTIONAL_ODDS_REGEX.matcher(oddsAsString);
		if (matcher.matches()) {
			return Double.valueOf(matcher.group(1)) / Double.valueOf(matcher.group(2));
		}
		
		matcher = WHOLE_ODDS_REGEX.matcher(oddsAsString);
		if (matcher.matches()) {
			return Double.valueOf(matcher.group(1));
		}
		
		// Slight temporal coupling here by assuming "name" is already set.
		throw new IllegalArgumentException("Error matching odds for " + name + ", don't understand these odds: \"" + oddsAsString + "\"");
	}

	public String getName() {
		return name;
	}
	
	public double getOdds() {
		return odds;
	}
}

