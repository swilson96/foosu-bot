package fusu.bot;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import fusu.bot.fusu.FooSuPlayer;
import fusu.oddschecker.OddsCheckerService;

public class App {
	/**
	 * Note: the team names happen to be identical in both the odds site and the
	 * FooSu site, otherwise we could translate them from one to the other.
	 */
	public static void main(String[] args) {
		// The Firefox driver supports javascript, for the FooSu site
		WebDriver driver = new FirefoxDriver();

		try {
			OddsCheckerService oddsChecker = new OddsCheckerService(driver);
			List<String> sortedTeams = oddsChecker.teamsInOrderOfWinningOdds();

			FooSuPlayer player = new FooSuPlayer(driver);
			player.selectBestTeamInAllGames(sortedTeams);
		} finally {
			driver.close();
		}
	}
}
