package sample.app
import sample.core.DomainConversions._
import org.junit._
import sample.utils.VPath._
import sample.utils.VEvent._
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Configurable
import javax.annotation.Resource
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import org.springframework.beans.factory.annotation.Autowired
import com.vaadin.Application
import org.springframework.test.annotation.DirtiesContext
import org.springframework.mock.web.MockServletContext
import org.springframework.web.context.ContextLoader
import org.springframework.mock.web.MockPageContext
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpSession
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import com.vaadin.ui.Button
import com.vaadin.ui.TextField

class TestApp {

  var app: Application = null

  @Before
  def beforeTest() {
    app = TestUtils.setupApp()
  }

  @Test
  def testLogin() {
    //    app \\ "loginForm" ! VLoginEvent("TestUser", "TestPassword")
    //    app \\ Caption(MSG.LOGIN_REGISTER) ! VClickEvent()
    //    (app \\ (Value(MSG.REGISTER_USERNAME) + 1): TextField).setValue("TestUser")
    //    (app \\ (Value(MSG.REGISTER_PASSWORD) + 1): TextField).setValue("TestPassword")
    //    app \\ Caption(MSG.REGISTER_BUTTON) ! VClickEvent()
    //    app \\ "loginForm" ! VLoginEvent("TestUser", "TestPassword")
    //    app \\ Caption(MSG.MENU_LOGOUT) ! VClickEvent()
  }
}
