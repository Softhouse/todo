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

import com.vaadin.ui.CustomTable
import com.vaadin.ui.CustomTable.ColumnGenerator

/**
 * @author mis
 *
 */
trait SmartTable {

  def addGeneratedColumn(id: Any, generatedColumn: ColumnGenerator)

  def addGeneratedColumn(id: Any, generate: Any => AnyRef) {
    addGeneratedColumn(id, new ColumnGenerator {
      override def generateCell(customTable: CustomTable, itemId: Any, columnId: Any): java.lang.Object = {
        generate(customTable.getContainerDataSource.getItem(itemId).getItemProperty(columnId).getValue())
      }
    })
  }
}