package fusu.bot.fusu;

import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FooSuPlayer {
	
	private static String FOOSU_URL = "http://www.footballsuicide.com/";
	
	private WebDriver driver;
	
	public FooSuPlayer(WebDriver driver) {
		this.driver = driver;
	}
	
	public void selectBestTeamInAllGames(List<String> sortedTeams) {
		login();

		List<WebElement> games = driver.findElements(By.xpath("//table[contains(concat(' ', normalize-space(@class), ' '), ' leaguelist ')]"));
		
		for (WebElement game : games) {
			String gameName = game.findElement(By.xpath("tbody/tr/td[@class='center title']/span[@class='title']")).getText();
						
			game.click();
			waitForJavascriptToLoad();
			
			String currentGame = driver.findElement(By.xpath("//span[@class='heading2']")).getText();
			if (!currentGame.equals(gameName)) {
				System.err.println("Uh oh, selected " + currentGame + " instead of " + gameName + "! Maybe try waiting longer for the crazy javascript?");
				System.err.println("Skipping " + gameName);
				continue;
			}
			
			System.out.println("Selected game: " + gameName);
			
			boolean picked = false;
			for (String team : sortedTeams) {
				List<WebElement> links = driver.findElements(By.xpath("//a[@data-teamname='" + team + "']"));
				if (links.isEmpty()) {
					System.out.println("Couldn't pick " + team + " for game " + gameName + ", moving on to try the next team");
					continue;
				}
				links.get(0).click();
				waitForJavascriptToLoad();
				picked = true;
				System.out.println("Picked " + team + " for game " + gameName);
				break;
			}
			
			if (!picked) {
				// This could be because we are just out of the game.
				// It would be possible to detect that earlier and handle it better.
				System.err.println("Uh oh, couldn't pick a team for game " + gameName);
			}
		}
	}
	
	private void login() {
		// We need to go there before we can set any cookies.
		driver.get(FOOSU_URL);
		
		Calendar later = Calendar.getInstance();
		later.set(2015, 1, 1);
		Cookie privateCookie = new Cookie("FooSuPrivateGuid", "729d6e63-f6e0-4aeb-bbea-a6a6d88c5e77", "www.footballsuicide.com", "/", later.getTime());
		Cookie publicCookie = new Cookie("FooSuPublicGuid", "380ce70c-0606-4679-ba9e-8e46b36b4305", "www.footballsuicide.com", "/", later.getTime());
		driver.manage().addCookie(privateCookie);
		driver.manage().addCookie(publicCookie);
		
		// reload with auth cookies
		driver.get(FOOSU_URL);
		
		waitForJavascriptToLoad();
	}

	private void waitForJavascriptToLoad() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// NOP
		}
	}
}
