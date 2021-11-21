package com.example.app.controllers;

import java.nio.file.Paths;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.responses.GreetingResponse;

@RestController
public class GreetingController {
	
	protected static WebDriver driver;
	protected static WebDriverWait wait;

	@GetMapping(value="/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GreetingResponse> greetingGet() {
		
		setUp();
		driver.get("https://www.google.com");
		String title = driver.getTitle();
		close();
		
		boolean isOk = ("Google".equals(title)) ? true : false;
		
		GreetingResponse response = new GreetingResponse(HttpStatus.OK, "Is Ok: " + isOk);
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@PostMapping(value="/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GreetingResponse> greetingPost() {
		
		GreetingResponse response = new GreetingResponse(HttpStatus.OK, "POST: Hello World!");
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	private static void setupDriver() {
		String driverPath="agent//chromedriver";
	    System.setProperty("webdriver.chrome.driver", driverPath);
	    
		ChromeOptions options = getChromeOptions();
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, 20);
	}

	private static ChromeOptions getChromeOptions() {
		HashMap<String, Object> chromePrefs = new HashMap<>();
//		chromePrefs.put("download.default_directory", Paths.get("src","test","resources", "temporaryDownloads").toAbsolutePath().toString());
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		options.addArguments("ignore-certificate-errors");
		options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
		return options;
	}
	
	public void setUp() {
		if (driver == null) {
			setupDriver();
		}
	}
	
	public static void close() {
		if(driver != null) {
			driver.quit();
		}
	}	
	
}