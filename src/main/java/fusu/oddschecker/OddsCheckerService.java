package fusu.oddschecker;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OddsCheckerService {

	private static String ODDS_URL = "http://www.oddschecker.com/football/english/premier-league";

	private WebDriver driver;

	public OddsCheckerService(WebDriver driver) {
		this.driver = driver;
	}

	public List<String> teamsInOrderOfWinningOdds() {

		driver.get(ODDS_URL);

		List<WebElement> allOdds = driver.findElements(By.xpath("//p[span]"));

		SortedSet<TeamOdds> orderedOdds = new TreeSet<TeamOdds>(
				TeamOdds.TEAM_COMPARITOR);

		for (WebElement team : allOdds) {
			try {
				TeamOdds odds = new TeamOdds(team);

				// Sometimes there is more than one game per team listed, we
				// stick with the first
				if (!odds.getName().equals("Draw")
						&& !setContainsTeamAlready(orderedOdds, odds.getName())) {
					orderedOdds.add(odds);
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		List<String> results = new ArrayList<String>();

		for (TeamOdds team : orderedOdds) {
			System.out.println(team.getName() + ": " + team.getOdds());
			results.add(team.getName());
		}

		return results;
	}

	private boolean setContainsTeamAlready(Iterable<TeamOdds> odds, String name) {
		if (name == null) {
			return true;
		}
		for (TeamOdds candidate : odds) {
			if (name.equals(candidate.getName())) {
				return true;
			}
		}
		return false;
	}
}
