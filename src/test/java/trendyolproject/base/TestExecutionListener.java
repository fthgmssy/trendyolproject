package trendyolproject.base;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestExecutionListener implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult){
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment
    public byte[] saveFailureScreenShot(WebDriver driver){
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }
    @Attachment(value ="{0}", type ="text/plain")
    public static String saveTextLog(String message){

        return message;

    }
    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Allure.addAttachment("screenShot", new ByteArrayInputStream(((TakesScreenshot)iTestResult.getTestContext().getAttribute("WebDriver")).getScreenshotAs(OutputType.BYTES)));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Allure.addAttachment("screenShot", new ByteArrayInputStream(((TakesScreenshot)iTestResult.getTestContext().getAttribute("WebDriver")).getScreenshotAs(OutputType.BYTES)));

        Object webDriverAttribute = iTestResult.getTestContext().getAttribute("WebDriver");
        saveFailureScreenShot((WebDriver) webDriverAttribute);

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

        Allure.addAttachment("screenShot", new ByteArrayInputStream(((TakesScreenshot)iTestResult.getTestContext().getAttribute("WebDriver")).getScreenshotAs(OutputType.BYTES)));

        Object webDriverAttribute = iTestResult.getTestContext().getAttribute("WebDriver");
        saveFailureScreenShot((WebDriver) webDriverAttribute);
    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }
}


