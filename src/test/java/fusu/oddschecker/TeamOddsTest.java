package fusu.oddschecker;

import static org.mockito.Mockito.when;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@RunWith(MockitoJUnitRunner.class)
public class TeamOddsTest {
	
	@Mock WebElement rootElement;
	@Mock WebElement nameElement;
	@Mock WebElement oddsElement;
	
	@Before
	public void setup() {
		when(rootElement.findElement(By.xpath("span[@class='fixtures-bet-name']"))).thenReturn(nameElement);
		when(rootElement.findElement(By.xpath("span[@class='odds']"))).thenReturn(oddsElement);
		
		
	}
	
	@Test
	public void testFractionalOddsRegex() {
		when(nameElement.getText()).thenReturn("Everton");
		when(oddsElement.getText()).thenReturn("(11/15)");
		
		TeamOdds odds = new TeamOdds(rootElement);
		Assert.assertEquals("Everton", odds.getName());
		Assert.assertEquals(11.0/15, odds.getOdds());
	}
	
	@Test
	public void testWholeNumberOddsRegex() {
		when(nameElement.getText()).thenReturn("Man Utd");
		when(oddsElement.getText()).thenReturn("(2)");
		
		TeamOdds odds = new TeamOdds(rootElement);
		Assert.assertEquals("Man Utd", odds.getName());
		Assert.assertEquals(2.0, odds.getOdds());
	}
}
