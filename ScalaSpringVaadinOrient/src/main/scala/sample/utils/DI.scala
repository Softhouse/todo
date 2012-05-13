/**
 * Copyright (c) 2012, Mikael Svahn, Softhouse Consulting AB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package sample.utils
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.stereotype.Component
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
import org.springframework.context.annotation.AnnotationConfigUtils
import org.springframework.beans.factory.wiring.BeanConfigurerSupport

/**
 * @author Mikael Svahn
 *
 */
trait DI {
  SpringContextManager.inject(this)
}

@Component
class SpringContextManager extends ApplicationListener[ContextRefreshedEvent] {

  def onApplicationEvent(event: ContextRefreshedEvent) {
    SpringContextManager.beanConfigurer = event.getApplicationContext().getBean("beanConfigurer").asInstanceOf[BeanConfigurerSupport]
  }
}

object SpringContextManager {
  var beanConfigurer: BeanConfigurerSupport = null

  def inject(obj: AnyRef) {
    beanConfigurer.configureBean(obj);
  }
}