package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({UserQueueExtension.class, BrowserExtension.class})
public class QueueTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void testWithEmptyUser1(@UserType(empty = true) StaticUser user) throws InterruptedException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openProfile()
        .checkProfileIsDisplayed();
    System.out.println(user);
  }

  @Test
  void testWithEmptyUser2(@UserType(empty = true) StaticUser user) throws InterruptedException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openProfile()
        .checkProfileIsDisplayed();
    System.out.println(user);
  }

  @Test
  void testWithEmptyUser3(@UserType(empty = false) StaticUser user) throws InterruptedException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openProfile()
        .checkProfileIsDisplayed();
    System.out.println(user);
  }

  @Test
  void testWithEmptyUser4(@UserType(empty = false) StaticUser user) throws InterruptedException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openProfile()
        .checkProfileIsDisplayed();
    System.out.println(user);
  }

  @Test
  void testWithEmptyUser5(@UserType(empty = false) StaticUser user) throws InterruptedException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.password())
        .openProfile()
        .checkProfileIsDisplayed();
    System.out.println(user);
  }

//  @Test
//  void testWithEmptyUser1(@UserType(empty = true) StaticUser user) throws InterruptedException {
//    Thread.sleep(1000);
//    System.out.println(user);
//  }

//  @Test
//  void testWithEmptyUser2(@UserType(empty = true) StaticUser user) throws InterruptedException {
//    Thread.sleep(1000);
//    System.out.println(user);
//  }

//  @Test
//  void testWithEmptyUser3(@UserType(empty = false) StaticUser user) throws InterruptedException {
//    Thread.sleep(1000);
//    System.out.println(user);
//  }
//
//  @Test
//  void testWithEmptyUser4(@UserType(empty = false) StaticUser user) throws InterruptedException {
//    Thread.sleep(1000);
//    System.out.println(user);
//  }
//
//  @Test
//  void testWithEmptyUser5(@UserType(empty = false) StaticUser user) throws InterruptedException {
//    Thread.sleep(1000);
//    System.out.println(user);
//  }

}
