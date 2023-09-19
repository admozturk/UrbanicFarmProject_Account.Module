package Pages;

import Utilities.Driver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {
    public BasePage() {
        PageFactory.initElements(Driver.getDriver(),this);
    }
    private AccountPage accountPage;

    public AccountPage getAccountPage() {
        if (accountPage==null){
            accountPage = new AccountPage();
        }
        return accountPage;
    }
}
