package runningOnCloud;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.gson.JsonObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BasicRunOnLamda {

	public static void main(String[] args) throws UnsupportedEncodingException {

		JsonObject capabilities = new JsonObject();
		JsonObject lamdaTestOptions = new JsonObject();
		
		String user = "bharath.cra";
		String accessKey = "cBp3EJjUPL36p6l0RWKKey7IGRJPFciGwExAbk89FPY56D4Uc9";
		
		// Browsers allowed: `Chrome`, `MicrosoftEdge`, `pw-chromium`, `pw-firefox` and `pw-webkit`
		
		capabilities.addProperty("browsername", "pw-firefox");
		capabilities.addProperty("browserVersion", "latest");
		
		lamdaTestOptions.addProperty("platform", "macOS Monterey");
		lamdaTestOptions.addProperty("user", user);
		lamdaTestOptions.addProperty("accessKey", accessKey);
		lamdaTestOptions.addProperty("name", "Playwright Demo on LamdaTest");
		lamdaTestOptions.addProperty("build", "PlayWright Demo on LamdaTest Build 1");
		
		capabilities.add("LT:Options", lamdaTestOptions);
		
		Playwright playwright = Playwright.create();
		BrowserType chromium = playwright.chromium();
		
		String caps = URLEncoder.encode(capabilities.toString(),"utf-8");
		String cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + caps;
		
		Browser browser = chromium.connect(cdpUrl);
//		Browser browser = chromium.launch(new LaunchOptions().setHeadless(false));
		Page page = browser.newPage();
		
			page.navigate("https://letcode.in/edit");
			page.getByPlaceholder("Enter first & last name").fill("Bharath CA");
			Locator byLabel = page.locator("#join");
			byLabel.fill(" boy");
			byLabel.press("Tab");
			System.out.println(page.locator("#getMe").textContent());
			page.locator("#clearMe").clear();
			if(page.locator("#noEdit").isDisabled()) {
				setTestStatus("passed", "Input field is disabled",page);
			} else {
				setTestStatus("failed", "Input field is not disabled",page);
			}
			System.out.println(page.locator("#dontwrite").isEnabled());
	
			page.close();
			browser.close();
			playwright.close();
			
	}
	
	public static void setTestStatus(String status, String remark, Page page) {
		page.evaluate("_ => {}",
				"lambdatest_action: { \"action\": \"setTestStatus\", \"arguments\": { \"status\": \"" + status
						+ "\", \"remark\": \"" + remark + "\"}}");
	}

}
