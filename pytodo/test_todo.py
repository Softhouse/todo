import unittest
import todo


class TestTodo(unittest.TestCase):

    def test_parser(self):
        text = """* DONE A todo\n Contents\n* TODO Another todo\n Stuff here"""
        todos = list(todo.todo_parser(text))
        self.assertEqual(todos, [('A todo', True, None, 'Contents'), ('Another todo', False, None, 'Stuff here')])

        self.assertEqual(todos[0], todo.Todo('A todo', True, None, 'Contents'))


    def test_coder(self):
        todos = (todo.Todo('one', False, None, 'Stuff1'),
                 todo.Todo('two', True, None, 'Stuff2'))
        text = todo.todo_coder(todos)
        self.assertEqual(text, "* TODO one\n Stuff1\n* DONE two\n Stuff2")
        

    def test_writeread(self):
        todos = (todo.Todo('one', False, None, 'Stuff1'),
                 todo.Todo('two', True, None, 'Stuff2'))

        todo.save_todo("test.org", todos)
        rt = todo.load_todo("test.org")
        self.assertEqual(tuple(rt), todos)


    def test_parse_raw(self):
        raw = "A thing \n more text"
        self.assertEqual(todo.cmdline_parse(raw), todo.Todo('A thing', False, None, 'more text'))



unittest.main()
