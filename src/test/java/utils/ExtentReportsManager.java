package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportsManager {
    private static ExtentReports extent;
    private static ExtentTest test;

    public synchronized static ExtentReports createInstance() {
        if (extent == null) {
            String reportName = "ExtentReport.html";
            //ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/target/" + reportName);
            ExtentSparkReporter spark = new ExtentSparkReporter("target/restbooker.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }

    public synchronized static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }
}